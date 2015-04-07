package org.fb4j;

import java.io.*;
import java.nio.*;
import java.nio.channels.*;

import org.jruby.ext.posix.*;

public class FB4JFrameBuffer {

	static final POSIXHandler handler;
	static final POSIX posix;

	static boolean debug = true;

	static String fn = "/dev/fb0";

	RandomAccessFile raf;
	FileDescriptor fd;
	FileChannel fc;
	MappedByteBuffer mbb;
	FB4JFixScreenInfo finfo;
	FB4JVarScreenInfo vinfo;

	public FB4JFrameBuffer( String fn ) {
		try {
			raf = new RandomAccessFile(fn, "rw");
			fc = raf.getChannel();
			fd = raf.getFD();
			vinfo = getVarScreenInfo();
			finfo = getFixScreenInfo();
			mbb = fc.map(FileChannel.MapMode.READ_WRITE,0,mapLength());
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	public FB4JFrameBuffer() {
		this( fn );
	}

	private int mapLength() {
		return vinfo.bits_per_pixel / Byte.SIZE * vinfo.xres_virtual * vinfo.yres_virtual;
	}

	public synchronized FB4JVarScreenInfo getVarScreenInfo()
	throws IOException
	{
		final int FBIOGET_VSCREENINFO = 0x4600;
		if ( null == vinfo ) {
			vinfo = new FB4JVarScreenInfo.ByReference();
			int r = posix.ioctl( fd, FBIOGET_VSCREENINFO, vinfo );
			if ( -1 == r ) {
				int errno = posix.errno();
				throw new IOException( "errno: " + errno );
			}
		}
		return vinfo;
	}

	public synchronized void putVarScreenInfo(FB4JVarScreenInfo info)
	throws IOException
	{
		final int FBIOPUT_VSCREENINFO = 0x4601;
		if ( null != vinfo ) {
			int r = posix.ioctl( fd, FBIOPUT_VSCREENINFO, vinfo );
			if ( -1 == r ) {
				int errno = posix.errno();
				throw new IOException( "errno: " + errno );
			}
			vinfo = info;
			finfo = getFixScreenInfo();
			mbb = fc.map(FileChannel.MapMode.READ_WRITE,0,mapLength());
		}
	}

	public synchronized FB4JFixScreenInfo getFixScreenInfo()
	throws IOException
	{
		final int FBIOGET_FSCREENINFO = 0x4602;
		finfo = new FB4JFixScreenInfo.ByReference();
		int r = posix.ioctl( fd, FBIOGET_FSCREENINFO, finfo );
		if ( -1 == r ) {
			int errno = posix.errno();
			throw new IOException( "errno: " + errno );
		}
		return finfo;
	}

	public synchronized ByteBuffer asByteBuffer() {
		return (ByteBuffer)mbb;
	}

	public synchronized void flip()
	throws IOException
	{
		final int FBIOPAN_DISPLAY = 0x4606;
		int r = posix.ioctl( fd, FBIOPAN_DISPLAY, vinfo );
		if ( -1 == r ) {
			int errno = posix.errno();
			throw new IOException( "errno: " + errno );
		}
	}

	public synchronized void blank()
	throws IOException
	{
		final int FBIOBLANK = 0x4611;
		int r = posix.ioctl( fd, FBIOBLANK );
		if ( -1 == r ) {
			int errno = posix.errno();
			throw new IOException( "errno: " + errno );
		}
	}

	public void force() {
		mbb.force();
	}

	static {
		handler = new FB4JPOSIXHandler();
		posix = POSIXFactory.getPOSIX( handler, true );
	}
}
