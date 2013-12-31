package org.fb4j;

import java.nio.*;

public class FB4JVarScreenInfo {
	
	private static final int XRES = 0, YRES = 1, XRES_VIRTUAL = 2,
		YRES_VIRTUAL = 3, XOFFSET = 4, YOFFSET = 5, BITS_PER_PIXEL = 6,
		// pixel format
		GRAYSCALE = 7,
		RED_OFFSET = 8, RED_LENGTH = 9,
		GREEN_OFFSET = 10, GREEN_LENGTH = 11,
		BLUE_OFFSET = 12, BLUE_LENGTH = 13,
		TRANSP_OFFSET = 14, TRANSP_LENGTH = 15;
	private static int offsets[];
	
	private ByteBuffer bb;
	
	private native void initInstance();
	private native void finiInstance();
	
	FB4JVarScreenInfo() {
		initInstance();
		if ( ByteOrder.nativeOrder() != bb.order() )
			bb.order(ByteOrder.nativeOrder());
	}
	
	public int getXres() {
		int x = bb.getInt(offsets[XRES]);
		return x;
	}
	public int getYres() {
		return bb.getInt(offsets[YRES]);
	}
	public int getXresVirtual() {
		return bb.getInt(offsets[XRES_VIRTUAL]);
	}	
	public int getYresVirtual() {
		return bb.getInt(offsets[YRES_VIRTUAL]);
	}	
	public int getXoffset() {
		return bb.getInt(offsets[XOFFSET]);
	}	
	public int getYoffset() {
		return bb.getInt(offsets[YOFFSET]);
	}	
	public int getBitsPerPixel() {
		return bb.getInt(offsets[BITS_PER_PIXEL]);
	}	
	public int getGrayscale() {
		return bb.getInt(offsets[GRAYSCALE]);
	}	
	public int getRedOffset() {
		return bb.getInt(offsets[RED_OFFSET]);
	}	
	public int getRedLength() {
		return bb.getInt(offsets[RED_LENGTH]);
	}	
	public int getGreenOffset() {
		return bb.getInt(offsets[GREEN_OFFSET]);
	}	
	public int getGreenLength() {
		return bb.getInt(offsets[GREEN_LENGTH]);
	}	
	public int getBlueOffset() {
		return bb.getInt(offsets[BLUE_OFFSET]);
	}	
	public int getBlueLength() {
		return bb.getInt(offsets[BLUE_LENGTH]);
	}	
	public int getTranspOffset() {
		return bb.getInt(offsets[TRANSP_OFFSET]);
	}	
	public int getTranspLength() {
		return bb.getInt(offsets[TRANSP_LENGTH]);
	}	
	public void setXres(int x) {
		bb.putInt(offsets[XRES],x);
	}
	public void setYres(int x) {
		bb.putInt(offsets[YRES],x);
	}
	public void setXresVirtual(int x) {
		bb.putInt(offsets[XRES_VIRTUAL],x);
	}
	public void setYresVirtual(int x) {
		bb.putInt(offsets[YRES_VIRTUAL],x);
	}
	public void setXoffset(int x) {
		bb.putInt(offsets[XOFFSET],x);
	}
	public void setYoffset(int x) {
		bb.putInt(offsets[YOFFSET],x);
	}
	public void setBitsPerPixel(int x) {
		bb.putInt(offsets[BITS_PER_PIXEL],x);
	}
	public void setGrayscale(int x) {
		bb.putInt(offsets[GRAYSCALE],x);
	}
	public void setRedOffset(int x) {
		bb.putInt(offsets[RED_OFFSET],x);
	}
	public void setRedLength(int x) {
		bb.putInt(offsets[RED_LENGTH],x);
	}
	public void setGreenOffset(int x) {
		bb.putInt(offsets[GREEN_OFFSET],x);
	}
	public void setGreenLength(int x) {
		bb.putInt(offsets[GREEN_LENGTH],x);
	}
	public void setBlueOffset(int x) {
		bb.putInt(offsets[BLUE_OFFSET],x);
	}
	public void setBlueLength(int x) {
		bb.putInt(offsets[BLUE_LENGTH],x);
	}
	public void setTranspOffset(int x) {
		bb.putInt(offsets[TRANSP_OFFSET],x);
	}
	public void setTranspLength(int x) {
		bb.putInt(offsets[TRANSP_LENGTH],x);
	}
	
	@Override
	public String toString() {
		return super.toString() +
			",xres:" + getXres() +
			",yres:" + getYres() +
			",xres_virtual:" + getXresVirtual() +
			",yres_virtual:" + getYresVirtual() +
			",xoffset:" + getXoffset() +
			",yoffset:" + getYoffset() +
			",bits_per_pixel:" + getBitsPerPixel() +
			",grayscale:" + getGrayscale() +
			",red.offset:" + getRedOffset() +
			",red.length:" + getRedLength() +
			",green.offset:" + getGreenOffset() +
			",green.length:" + getGreenLength() +
			",blue.offset:" + getBlueOffset() +
			",blue.length:" + getBlueLength() +
			",transp.offset:" + getTranspOffset() +
			",transp.length:" + getTranspLength() +
			"";
	}
	
//	@Override
//	public void finalize() {
//		finiInstance();
//	}

	static {
		System.loadLibrary("fb4j");
	}
	
	/*
	struct fb_var_screeninfo {
		__u32 xres;			// visible resolution		
		__u32 yres;
		__u32 xres_virtual;		// virtual resolution		
		__u32 yres_virtual;
		__u32 xoffset;			// offset from virtual to visible 
		__u32 yoffset;			// resolution			
	
		__u32 bits_per_pixel;		// guess what			
		__u32 grayscale;		// != 0 Graylevels instead of colors 
	
		struct fb_bitfield red;		// bitfield in fb mem if true color, 
		struct fb_bitfield green;	// else only length is significant 
		struct fb_bitfield blue;
		struct fb_bitfield transp;	// transparency				
	
		__u32 nonstd;			// != 0 Non standard pixel format 
	
		__u32 activate;			// see FB_ACTIVATE_*		
	
		__u32 height;			// height of picture in mm    
		__u32 width;			// width of picture in mm     
	
		__u32 accel_flags;		// (OBSOLETE) see fb_info.flags 
	
		// Timing: All values in pixclocks, except pixclock (of course) 
		__u32 pixclock;			// pixel clock in ps (pico seconds) 
		__u32 left_margin;		// time from sync to picture	
		__u32 right_margin;		// time from picture to sync	
		__u32 upper_margin;		// time from sync to picture	
		__u32 lower_margin;
		__u32 hsync_len;		// length of horizontal sync	
		__u32 vsync_len;		// length of vertical sync	
		__u32 sync;			// see FB_SYNC_*		
		__u32 vmode;			// see FB_VMODE_*		
		__u32 rotate;			// angle we rotate counter clockwise 
		__u32 reserved[5];		// Reserved for future compatibility 
	};
	*/
}
