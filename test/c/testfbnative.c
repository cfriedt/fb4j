#include <stdio.h>

#include <stdbool.h>

#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>

#include <unistd.h>

#include <sys/mman.h>

#include <string.h>
#include <errno.h>

#include <signal.h>

#include <math.h>

#include <sys/time.h>

#include <linux/fb.h>

#define D(fmt,args...) printf("%d: " fmt "\n", __LINE__, ##args)
#define absf(x) ((x)<0.0f ? -(x) : (x))

enum {
	white  = 0xffffff,
	red    = 0xff0000,
	green  = 0x00ff00,
	blue   = 0x0000ff,
	yellow = 0xffff00,
};

struct ball {
	int r, x, y, dx, dy, c;
};

struct ctx {
	int fd;
	int *map;
	unsigned maplen;
	struct ball ball[4];
	struct fb_var_screeninfo vinfo;
	struct fb_fix_screeninfo finfo;
	bool should_exit;
};

static struct ctx *_ctx;

typedef void (*sighandler_t)(int);

static void handler(int sig) {
	if ( SIGINT == sig ) {
		printf("\ncaught signal\n");
		_ctx->should_exit = true;
	}
}

static int init(struct ctx *ctx) {
	int r;

	D("opening /dev/fb0");
	ctx->fd = open("/dev/fb0", O_RDWR);
	if ( -1 == ctx->fd ) {
		r = errno;
		perror("open(/dev/fb0)");
		goto out;
	}
	D("opened as %d", ctx->fd);

	D("calling ioctl(FBIOGET_VSCREENINFO)");
	if ( -1 == ioctl(ctx->fd, FBIOGET_VSCREENINFO, &ctx->vinfo) ) {
		r = errno;
		perror("ioctl(FBIOGET_VSCREENINFO)");
		goto out;
	}

	ctx->vinfo.xres_virtual = ctx->vinfo.xres;
	ctx->vinfo.yres_virtual = 2*ctx->vinfo.yres;

	D("calling ioctl(FBIOPUT_VSCREENINFO)");
	if ( -1 == ioctl(ctx->fd, FBIOPUT_VSCREENINFO, &ctx->vinfo) ) {
		r = errno;
		perror("ioctl(FBIOPUT_VSCREENINFO)");
		goto out;
	}

	ctx->maplen = ctx->vinfo.xres_virtual * ctx->vinfo.yres_virtual * ctx->vinfo.bits_per_pixel / 8;

	D("calling mmap");
	ctx->map = mmap(NULL, ctx->maplen, PROT_READ|PROT_WRITE, MAP_SHARED, ctx->fd, 0);
	if ( MAP_FAILED == ctx->map ) {
		r = errno;
		perror("mmap");
		goto out;
	}
	D("mmaped to %p", ctx->map);

	D("calling signal()");
	if ( SIG_ERR == signal(SIGINT,handler) ) {
		perror("signal");
		r = EINVAL;
		goto out;
	}

	r = 0;

out:
	return r;
}

static void fini(struct ctx *ctx) {
	if ( MAP_FAILED != ctx->map ) {
		D("calling munmap()");
		munmap(ctx->map, ctx->maplen);
		ctx->map = MAP_FAILED;
	}
	if ( -1 != ctx->fd ) {
		D("calling close()");
		close(ctx->fd);
		ctx->fd = -1;
	}
}

static void ball_init( struct ctx *ctx ) {
	int i;

	ctx->ball[0].c = red;
	ctx->ball[1].c = green;
	ctx->ball[2].c = blue;
	ctx->ball[3].c = yellow;

	for(i=0; i<4; i++) {
		ctx->ball[i].r = ctx->vinfo.yres/10;
		ctx->ball[i].x = (i+1) * ctx->vinfo.xres / 5;
		ctx->ball[i].y = (i+1) * ctx->vinfo.yres / 5;
		ctx->ball[i].dx = 5;
		ctx->ball[i].dy = 7;
	}
}

static void ball_update(struct ctx *ctx) {
	int i;
	for(i=0; i<4; i++) {
		int
			dx = ctx->ball[i].dx,
			dy = ctx->ball[i].dy,
			x  = ctx->ball[i].x,
			y  = ctx->ball[i].y,
			r  = ctx->ball[i].r,
			w  = ctx->vinfo.xres,
			h  = ctx->vinfo.yres;
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
		ctx->ball[i].dx = dx;
		ctx->ball[i].dy = dy;
		ctx->ball[i].x += dx;
		ctx->ball[i].y += dy;
		if ( ctx->ball[i].x < 0 || ctx->ball[i].x >= w || ctx->ball[i].y < 0 || ctx->ball[i].y >= h ) {
			D("%d %d", ctx->ball[i].x, ctx->ball[i].y);
		}
	}
}

static void ball_draw( struct ctx *ctx, int yoffs ) {
	int i, row, col;
	int x, y, w, h, r;
	int *pixel = ctx->map;

	w = ctx->vinfo.xres;
	h = ctx->vinfo.yres;

	for(i = 0; i < 4; i++) {
		x = ctx->ball[i].x;
		y = ctx->ball[i].y;
		r = ctx->ball[i].r;
		for( row=y-r; row<y+r; row++) {
			for( col=x-r; col<x+r; col++) {
				float
					xx = (float)x,
					yy = (float)y,
					colf = (float)col,
					rowf = (float)row,
					rr = (float)
					sqrtf(
						powf(absf(colf - xx), 2.0f) +
						powf(absf(rowf - yy), 2.0f)
					);
				if ( (int)rr < r ) {
					pixel[(yoffs+row)*w + col] = ctx->ball[i].c;
				}
			}
		}
	}
}

static void loop( struct ctx *ctx) {
	int i, yoffs, w = ctx->vinfo.xres, h = ctx->vinfo.yres;
	int *pixel;
	int frames;
	struct timeval tv1 = {}, tv2 = {};

	int blank[w*h];

	for(i=0; i<w*h; i++) {
		blank[i] = white;
	}

	ball_init(ctx);

	gettimeofday(&tv1,NULL);
	for( frames=0, yoffs=h; !ctx->should_exit; yoffs += h, yoffs %= ctx->vinfo.yres_virtual ) {

		pixel = &ctx->map[yoffs*w];
		ctx->vinfo.yoffset = yoffs;

		memcpy(pixel,blank,ctx->maplen/2);

		ball_update(ctx);
		ball_draw(ctx, yoffs);

		ioctl(ctx->fd, FBIOPAN_DISPLAY, &ctx->vinfo);

		frames++;
		gettimeofday(&tv2, NULL);
		if ( tv2.tv_sec - tv1.tv_sec  >= 5 ) {
			printf("fps: %f\n", (float)frames / (float)(tv2.tv_sec - tv1.tv_sec) );
			tv1.tv_sec = tv2.tv_sec;
			frames = 0;
		}
	}
}

int main() {
	int r = 0;
	struct ctx ctx = { .fd = 1, .map = MAP_FAILED, };
	_ctx = &ctx;

	D("calling init");
	r = init(&ctx);
	if ( 0 != r ) {
		goto do_cleanup;
	}

	loop(&ctx);

do_cleanup:
	fini(&ctx);

	return r;
}
