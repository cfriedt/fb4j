import org.fb4j.*;

public class TestFB4J {
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
			//System.out.println("ball: " + x + ", " + y);
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

		System.out.println("FB4J v" + FB4JInfo.version);
		System.out.println("\nBrought to you by:\n");
		for( String s: FB4JInfo.author ) {
			System.out.println("\t" + s);
		}
		System.out.println("\n\nTerms of Use:\n\n" + FB4JInfo.license);
		
		Thread.sleep(2000);
		
		fb = new FB4JFrameBuffer();
		vinfo = fb.getVarScreenInfo();
		vinfo.setXresVirtual(vinfo.getXres());
		vinfo.setYresVirtual(2*vinfo.getYres());
		fb.putVarScreenInfo(vinfo);
		finfo = fb.getFixScreenInfo();

		System.out.println();

		System.out.print("vinfo:\n\t");
		System.out.println( ("" + vinfo).replaceAll(",", "\n\t") );
		System.out.println();

		System.out.print("finfo:\n\t");
		System.out.println( ("" + finfo).replaceAll(",", "\n\t") );
		System.out.println();

		final int w=vinfo.getXres(), h=vinfo.getYres(), hmax = vinfo.getYresVirtual();

		pixel = fb.asByteBuffer().asIntBuffer().array();
		
		Ball redball = new Ball(Math.min(w,h)/10, w/5, h/5, 5, 7, w, h);
		Ball greenball = new Ball(Math.min(w,h)/10, w/5*2, h/5*2, -7, 5, w, h);
		Ball blueball = new Ball(Math.min(w,h)/10, w/5*3, h/5*3, 5, -7, w, h);
		Ball yellowball = new Ball(Math.min(w,h)/10, w/5*4, h/5*4, 5, -7, w, h);
		int yoffs, frames;
		long ms, new_ms;
		
		for(frames=0, ms = System.currentTimeMillis(), yoffs=h;; yoffs += h, yoffs %= hmax) {
			// draw a black screen (can be done faster)
			for(int i=yoffs*w; i<(yoffs+h)*w; i++) {
				pixel[i] = white;
			}
			redball.update();
			redball.draw(yoffs, red);
			greenball.update();
			greenball.draw(yoffs, green);
			blueball.update();
			blueball.draw(yoffs, blue);
			yellowball.update();
			yellowball.draw(yoffs, yellow);
			vinfo.setYoffset(yoffs);
			fb.flip();
			//Thread.sleep(1000);
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
