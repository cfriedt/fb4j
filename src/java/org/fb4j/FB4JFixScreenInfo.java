package org.fb4j;

import java.nio.*;

public class FB4JFixScreenInfo {

	private static final int ID = 0, SMEM_LEN = 1, TYPE = 2, XPANSTEP = 3,
			YPANSTEP = 4, YWRAPSTEP = 5, LINE_LENGTH = 6;
	private static int offsets[];
	
	private ByteBuffer bb;
	
	private native void initInstance();
	private native void finiInstance();
	
	protected FB4JFixScreenInfo() {
		initInstance();
		if ( ByteOrder.nativeOrder() != bb.order() )
			bb.order(ByteOrder.nativeOrder());
	}
	
	public String getID() {
		byte[] b = new byte[16];
		bb.get(b, offsets[ID], b.length);
		return new String(b);
	}
	public int getSmemLen() {
		return bb.getInt(offsets[SMEM_LEN]);
	}
	public int getType() {
		return bb.getInt(offsets[TYPE]);
	}
	public int getXpanStep() {
		return bb.getShort(offsets[XPANSTEP]);
	}
	public int getYpanStep() {
		return bb.getShort(offsets[YPANSTEP]);
	}
	public int getYwrapStep() {
		return bb.getShort(offsets[YWRAPSTEP]);
	}
	public int getLineLength() {
		return bb.getInt(offsets[LINE_LENGTH]);
	}
	
	@Override
	public String toString() {
		return super.toString() +
			",id:" + getID() +
			",smem_len:" + getSmemLen() +
			",type:" + getType() +
			",xpanstep:" + getXpanStep() +
			",ypanstep:" + getYpanStep() +
			",ywrapstep:" + getYwrapStep() +
			",line_length:" + getLineLength();
	}
	
//	@Override
//	public void finalize() {
//		finiInstance();
//	}

	static {
		System.loadLibrary("fb4j");
	}
	
	/*
	struct fb_fix_screeninfo {
        char id[16];                    // identification string eg "TT Builtin" 
        unsigned long smem_start;       // Start of frame buffer mem 
                                        // (physical address) 
        __u32 smem_len;                 // Length of frame buffer mem 
        __u32 type;                     // see FB_TYPE_*                
        __u32 type_aux;                 // Interleave for interleaved Planes 
        __u32 visual;                   // see FB_VISUAL_*               
        __u16 xpanstep;                 // zero if no hardware panning  
        __u16 ypanstep;                 // zero if no hardware panning  
        __u16 ywrapstep;                // zero if no hardware ywrap    
        __u32 line_length;              // length of a line in bytes    
        unsigned long mmio_start;       // Start of Memory Mapped I/O   
                                        // (physical address) 
        __u32 mmio_len;                 // Length of Memory Mapped I/O  
        __u32 accel;                    // Indicate to driver which     
                                        //  specific chip/card we have  
        __u16 reserved[3];              // Reserved for future compatibility 
	};
	*/
}
