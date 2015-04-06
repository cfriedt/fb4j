#include <stdio.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>
#include <sys/mman.h>
#include <linux/fb.h>
#include <string.h>

#include "TestFBJNI.h"

struct ctx {
	int fd;
	int *map;
	unsigned maplen;
	struct fb_var_screeninfo vinfo;
	struct fb_fix_screeninfo finfo;
};

static struct ctx ctx = { .fd = -1, .map = MAP_FAILED };

JNIEXPORT void JNICALL Java_TestFBJNI_init(JNIEnv *e, jclass clazz) {

	ctx.fd = open("/dev/fb0", O_RDWR);
	if ( -1 == ctx.fd ) {
		goto out;
	}

	if ( -1 == ioctl(ctx.fd, FBIOGET_VSCREENINFO, &ctx.vinfo) ) {
		goto out;
	}

	ctx.vinfo.xres_virtual = ctx.vinfo.xres;
	ctx.vinfo.yres_virtual = 2*ctx.vinfo.yres;

	if ( -1 == ioctl(ctx.fd, FBIOPUT_VSCREENINFO, &ctx.vinfo) ) {
		goto out;
	}

	ctx.maplen = ctx.vinfo.xres_virtual * ctx.vinfo.yres_virtual * ctx.vinfo.bits_per_pixel / 8;

	ctx.map = mmap(NULL, ctx.maplen, PROT_READ|PROT_WRITE, MAP_SHARED, ctx.fd, 0);
	if ( MAP_FAILED == ctx.map ) {
		goto out;
	}
out:
	return;
}

JNIEXPORT jint JNICALL Java_TestFBJNI_getWidth (JNIEnv *e, jclass clazz) {
	return ctx.vinfo.xres;
}

JNIEXPORT jint JNICALL Java_TestFBJNI_getHeight (JNIEnv *e, jclass clazz) {
	return ctx.vinfo.yres;
}

JNIEXPORT void JNICALL Java_TestFBJNI_paint (JNIEnv *e, jclass clazz, jintArray screen) {
	jboolean isCopy;
	jint *scr = (*e)->GetIntArrayElements(e,screen,&isCopy);
	ctx.vinfo.yoffset += ctx.vinfo.yres;
	ctx.vinfo.yoffset %= ctx.vinfo.yres_virtual;
	memcpy(&ctx.map[ctx.vinfo.yoffset*ctx.vinfo.xres], scr, ctx.vinfo.xres*ctx.vinfo.yres * 4);
	ioctl(ctx.fd, FBIOPAN_DISPLAY, &ctx.vinfo);
	(*e)->ReleaseIntArrayElements(e,screen,scr,JNI_ABORT);
}
