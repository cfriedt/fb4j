package org.fb4j;

import java.io.*;
import java.nio.*;
import java.nio.channels.*;

public class FB4JFrameBuffer {
	
	static boolean debug = true;
	
	String fn = "/dev/fb0";
	
	RandomAccessFile raf;
	FileDescriptor fd;
	FileChannel fc;
	MappedByteBuffer mbb;
	FB4JFixScreenInfo finfo;
	FB4JVarScreenInfo vinfo;
	
	public FB4JFrameBuffer() {
		try {
			raf = new RandomAccessFile(fn, "rw");
			fc = raf.getChannel();
			fd = raf.getFD();
			vinfo = getVarScreenInfoNative(fd);
			finfo = getFixScreenInfoNative(fd);
			mbb = fc.map(FileChannel.MapMode.READ_WRITE,0,mapLength());
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	private int mapLength() {
		return vinfo.getBitsPerPixel()/8 * vinfo.getXresVirtual() * vinfo.getYresVirtual(); 
	}
	
	public synchronized FB4JVarScreenInfo getVarScreenInfo()  throws IOException {
		if ( null == vinfo ) {
			vinfo = getVarScreenInfoNative(fd);
		}
		return vinfo;
	}
	
	public synchronized void putVarScreenInfo(FB4JVarScreenInfo info) throws IOException {
		if ( null != vinfo ) {
			putVarScreenInfoNative(fd, info);
			vinfo = info;
			finfo = getFixScreenInfoNative(fd);
			mbb = fc.map(FileChannel.MapMode.READ_WRITE,0,mapLength());
		}
	}

	public synchronized FB4JFixScreenInfo getFixScreenInfo() {
		finfo = getFixScreenInfoNative(fd);
		return finfo;
	}
	
	public synchronized ByteBuffer asByteBuffer() {
		return (ByteBuffer)mbb;
	}

	public synchronized void flip() {
		panDisplayNative(fd,vinfo);
	}
	
	public synchronized void blank() {
		blankNative(fd);
	}
	
	public void force() {
		mbb.force();
	}
	
	private static native FB4JVarScreenInfo getVarScreenInfoNative(FileDescriptor fd);
	private static native void              putVarScreenInfoNative(FileDescriptor fd, FB4JVarScreenInfo vinfo);
	private static native FB4JFixScreenInfo getFixScreenInfoNative(FileDescriptor fd);
	private static native void              panDisplayNative(FileDescriptor fd, FB4JVarScreenInfo vinfo);
	private static native void              blankNative(FileDescriptor fd);
	private static native void              waitForVSyncNative(FileDescriptor fd);
	protected static native void            init();
	
	static {
		System.loadLibrary("fb4j");
		init();
	}
}