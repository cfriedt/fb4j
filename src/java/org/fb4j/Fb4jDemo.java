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

public class Fb4jDemo {
	static FB4JFrameBuffer fb;
	static FB4JVarScreenInfo vinfo;
	static FB4JFixScreenInfo finfo;
	static int[] pixel;

	static final int black = 0;
	static final int white = 0xffffff;
	static final int teal = 0x008080;
	static final int orange = 0xffa500;

	static final int red = 0xff0000;
	static final int green = 0x00ff00;
	static final int blue = 0x0000ff;
	static final int yellow = 0xffff00;

	static class Ball {
		final int r, w, h;
		int x, y;
		int dx, dy;
		Ball(int radius, int xpos, int ypos, int vx, int vy, int width, int height) {
			r = radius;
			w = width;
			h = height;
			x = xpos;
			y = ypos;
			dx = vx;
			dy = vy;
		}
		void update() {
			if ( dx > 0 ) {
				if ( x + dx + r >= w ) {
					dx = -dx;
				}
			} else {
				if ( x + dx - r < 0 ) {
					dx = -dx;
				}
			}
			if ( dy > 0 ) {
				if ( y + dy + r >= h ) {
					dy = -dy;
				}
			} else {
				if ( y + dy - r < 0 ) {
					dy = -dy;
				}
			}
			x += dx;
			y += dy;
		}
		void draw(int yoffs, int color) {
			int row, col;
			for( row=y-r; row<y+r; row++) {
				for( col=x-r; col<x+r; col++) {
					float
						xx = (float)x,
						yy = (float)y,
						colf = (float)col,
						rowf = (float)row,
						rr = (float)
					Math.sqrt(
						Math.pow(Math.abs(colf - xx), 2.0f) +
						Math.pow(Math.abs(rowf - yy), 2.0f)
					);
					if ( (int)rr < r ) {
						pixel[(yoffs+row)*w + col] = color;
					}
				}
			}
		}
	}

	static void func() throws Throwable {

		fb = new FB4JFrameBuffer();
		vinfo = fb.getVarScreenInfo();

		vinfo.bits_per_pixel = 32;
		vinfo.xres_virtual = vinfo.xres;
		vinfo.yres_virtual = 2 * vinfo.yres;

		fb.putVarScreenInfo(vinfo);
		finfo = fb.getFixScreenInfo();

		System.out.print("vinfo:\n\t");
		System.out.println( ("" + vinfo).replaceAll(", ", "\n\t") );
		System.out.println();

		System.out.print("finfo:\n\t");
		System.out.println( ("" + finfo).replaceAll(", ", "\n\t") );
		System.out.println();

		final int w=vinfo.xres, h=vinfo.yres, hmax = vinfo.yres_virtual;

		pixel = fb.asByteBuffer().asIntBuffer().array();

		Ball redball = new Ball(Math.min(w,h)/10, w/5, h/5, 5, 7, w, h);
		Ball greenball = new Ball(Math.min(w,h)/10, w/5*2, h/5*2, -7, 5, w, h);
		Ball blueball = new Ball(Math.min(w,h)/10, w/5*3, h/5*3, 5, -7, w, h);
		Ball yellowball = new Ball(Math.min(w,h)/10, w/5*4, h/5*4, 5, -7, w, h);
		int yoffs, frames;
		long ms, new_ms;

		int[] blank = new int[w*h];
		for(int i=0; i<blank.length; i++) {
			blank[i] = white;
		}

		for(frames=0, ms = System.currentTimeMillis(), yoffs=h;; yoffs += h, yoffs %= hmax) {
			// draw a blank screen
			System.arraycopy(blank,0,pixel,yoffs*w,blank.length);

			redball.update();
			redball.draw(yoffs, red);
			greenball.update();
			greenball.draw(yoffs, green);
			blueball.update();
			blueball.draw(yoffs, blue);
			yellowball.update();
			yellowball.draw(yoffs, yellow);
			vinfo.yoffset = yoffs;
			fb.flip();

			frames++;
			new_ms = System.currentTimeMillis();
			if ( new_ms - ms  >= 5000 ) {
				ms = new_ms;
				System.out.println("fps:" + (float)frames / 5.0f );
				frames = 0;
			}
		}
	}

	public static void main(String[] arg) {
		try {
			func();
		} catch(Throwable t) {
			t.printStackTrace();
		}
	}
}
