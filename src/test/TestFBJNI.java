public class TestFBJNI {
	static final int black = 0;
	static final int white = 0xffffff;
	static final int teal = 0x008080;
	static final int orange = 0xffa500;
	
	static final int red = 0xff0000;
	static final int green = 0x00ff00;
	static final int blue = 0x0000ff;
	static final int yellow = 0xffff00;
	
	int pixel[], w, h;
	
	private class Ball {
		final int r;
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
		void draw(int color) {
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
						pixel[row*w + col] = color;
					}
				}				
			}
		}
	}
	
	void func() {

		System.out.println();

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
		
		for(frames=0, ms = System.currentTimeMillis();true;) {
			// draw a blank screen
			System.arraycopy(blank,0,pixel,0,blank.length);

			redball.update();
			redball.draw(red);
			greenball.update();
			greenball.draw(green);
			blueball.update();
			blueball.draw(blue);
			yellowball.update();
			yellowball.draw(yellow);
			paint(pixel);
			
			frames++;
			new_ms = System.currentTimeMillis();
			if ( new_ms - ms  >= 5000 ) {
				ms = new_ms;
				System.out.println("fps:" + (float)frames / 5.0f );
				frames = 0;
			}
		}
	}
	public TestFBJNI() {
		w = getWidth();
		h = getHeight();
		pixel = new int[w*h];
	}
	private static native void init();
	private static native void fini();
	private static native int getWidth();
	private static native int getHeight();
	private static native void paint(int[] screen);

	public static void main(String[] arg) {
		TestFBJNI t = new TestFBJNI();
		t.func();
	}
	
	static {
		System.loadLibrary("TestFBJNI");
		init();
	}
}
