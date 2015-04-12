/*
 * The compilation of software known as FB4J is distributed under the
 * following terms:
 * 
 * Copyright (c) 2013 Christopher Friedt. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED.  IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 */

package org.fb4j;

import java.util.*;

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

	public int[] reserved = new int[4];


	@Override
	protected List getFieldOrder() {
		return Arrays.asList( new String[] { "xres", "yres", "xres_virtual", "yres_virtual", "xoffset", "yoffset", "bits_per_pixel", "grayscale", "red", "green", "blue", "transp", "nonstd", "activate", "height", "width", "accel_flags", "pixclock", "left_margin", "right_margin", "upper_margin", "lower_margin", "hsync_len", "vsync_len", "sync", "vmode", "rotate", "colorspace", "reserved", } );
	}

	@Override
	public String toString() {
		return
			getClass().getName() + "@" + getClass().getName() + '@' + Integer.toHexString(hashCode()) + ", " +
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
