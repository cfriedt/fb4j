package org.fb4j;

import com.sun.jna.*;

public class FB4JVarScreenInfo extends Structure {

	public static class ByReference extends FB4JVarScreenInfo implements Structure.ByReference {}

	public int xres;
	public int yres;
	public int xres_virtual;
	public int yres_virtual;
	public int xoffset;
	public int yoffset;

	public int bits_per_pixel;
	public int grayscale;

	public FB4JBitField red;
	public FB4JBitField green;
	public FB4JBitField blue;
	public FB4JBitField transp;

	public int nonstd;

	public int activate;

	public int height;
	public int width;

	public int accel_flags;

	public int pixclock;
	public int left_margin;
	public int right_margin;
	public int upper_margin;
	public int lower_margin;
	public int hsync_len;
	public int vsync_len;
	public int sync;
	public int vmode;
	public int rotate;
	public int colorspace;

	public int[] reserved; // __u32 reserved[4];

	@Override
	public String toString() {
		return super.toString() +
			"xres:" + xres +
			", " +
			"yres:" + yres +
			", " +
			"xres_virtual:" + xres_virtual +
			", " +
			"yres_virtual:" + yres_virtual +
			", " +
			"xoffset:" + xoffset +
			", " +
			"yoffset:" + yoffset +
			", " +
			"bits_per_pixel:" + bits_per_pixel +
			", " +
			"grayscale:" + grayscale +
			", " +
			"red:" + red +
			", " +
			"green:" + green +
			", " +
			"blue:" + blue +
			", " +
			"transp:" + transp +
			"";
	}
}
