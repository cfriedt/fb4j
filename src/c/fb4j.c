#include <stdio.h>
#include <errno.h>
#include <string.h>
#include <stddef.h>
#include <stdlib.h>
#include <pthread.h>

#include <sys/ioctl.h>
#include <stdint.h>

#include <linux/fb.h>

#include "org_fb4j_FB4JFrameBuffer.h"
#include "org_fb4j_FB4JVarScreenInfo.h"
#include "org_fb4j_FB4JFixScreenInfo.h"

static void FB4JFixScreenInfo_initClass(JNIEnv *env);
static void FB4JVarScreenInfo_initClass(JNIEnv *env);

#define ARRAY_SIZE(x) (sizeof(x)/sizeof((x)[0]))

#define THROW_EXCEPTION(e,s) \
{ \
	(*e)->ThrowNew(env,IOException_class,s); \
}

#ifdef DEBUG
#define METHOD_NAME(x) errno = 0; const char *methodName = CLASSNAME "." #x; printf("%s\n", methodName)
#else
#define METHOD_NAME(x) errno = 0
#endif

static jclass IOException_class = NULL;

static jclass FileDescriptor_class = NULL;
static jfieldID FileDescriptor_fd_fieldID = NULL;

static jclass FB4JVarScreenInfo_class = NULL;
static jmethodID FB4JVarScreenInfo_ctor_methodID = NULL;
static jfieldID FB4JVarScreenInfo_bb_fieldID = NULL;
static jfieldID FB4JVarScreenInfo_offsets_fieldID = NULL;

static jclass FB4JFixScreenInfo_class = NULL;
static jmethodID FB4JFixScreenInfo_ctor_methodID = NULL;
static jfieldID FB4JFixScreenInfo_bb_fieldID = NULL;
static jfieldID FB4JFixScreenInfo_offsets_fieldID = NULL;

enum {
	FIND_CLASS,
	FIND_FIELDID,
	FIND_METHODID,
};

#ifdef DEBUG
static jclass findClass(JNIEnv *env, const char *n) {
	jclass r = (*env)->FindClass(env,n);
	printf("%s class '%s'\n", r ? "found" : "failed to find",n);
	return r;
}

static jfieldID getFieldID(JNIEnv *env, jclass clazz, const char *n, const char *s) {
	jfieldID r = (*env)->GetFieldID(env,clazz,n,s);
	printf("%s field '%s'\n", r ? "found" : "failed to find",n);
	return r;
}

static jfieldID getStaticFieldID(JNIEnv *env, jclass clazz, const char *n, const char *s) {
	jfieldID r = (*env)->GetStaticFieldID(env,clazz,n,s);
	printf("%s static field '%s'\n", r ? "found" : "failed to find",n);
	return r;
}

static jmethodID getMethodID(JNIEnv *env, jclass clazz, const char *n, const char *s) {
	jmethodID r = (*env)->GetMethodID(env,clazz,n,s);
	printf("%s method '%s'\n", r ? "found" : "failed to find",n);
	return r;
}
#else
#define findClass(e,n) (*e)->FindClass(e,n)
#define getFieldID(e,c,n,s) (*e)->GetFieldID(e,c,n,s)
#define getStaticFieldID(e,c,n,s) (*e)->GetStaticFieldID(e,c,n,s)
#define getMethodID(e,c,n,s) (*env)->GetMethodID(e,c,n,s)
#endif

/*
 * org.fb4j.FB4JFrameBuffer
 */
#undef CLASSNAME
#define CLASSNAME "FB4JFrameBuffer"

JNIEXPORT void JNICALL Java_org_fb4j_FB4JFrameBuffer_init
  (JNIEnv *env, jclass clazz)
{
	METHOD_NAME(init);

//	static pthread_mutex_t mutex = PTHREAD_MUTEX_INITIALIZER;
	static int initialized = 0;

	//pthread_mutex_lock(&mutex);

	if ( initialized )
		goto out;

	if (
		! (
			(IOException_class = findClass(env,"java.io.IOException")) &&

			(FileDescriptor_class = findClass(env,"java.io.FileDescriptor")) &&
			(FileDescriptor_fd_fieldID = getFieldID(env, FileDescriptor_class, "fd", "I")) &&

			(FB4JVarScreenInfo_class = findClass(env,"org.fb4j.FB4JVarScreenInfo")) &&
			(FB4JVarScreenInfo_ctor_methodID = getMethodID(env, FB4JVarScreenInfo_class, "<init>", "()V")) &&
			(FB4JVarScreenInfo_bb_fieldID = getFieldID(env, FB4JVarScreenInfo_class, "bb", "Ljava/nio/ByteBuffer;")) &&
			(FB4JVarScreenInfo_offsets_fieldID = getStaticFieldID(env, FB4JVarScreenInfo_class, "offsets", "[I")) &&

			(FB4JFixScreenInfo_class = findClass(env,"org.fb4j.FB4JFixScreenInfo")) &&
			(FB4JFixScreenInfo_ctor_methodID = getMethodID(env, FB4JFixScreenInfo_class, "<init>", "()V")) &&
			(FB4JFixScreenInfo_bb_fieldID = getFieldID(env, FB4JFixScreenInfo_class, "bb", "Ljava/nio/ByteBuffer;")) &&
			(FB4JFixScreenInfo_offsets_fieldID = getStaticFieldID(env, FB4JFixScreenInfo_class, "offsets", "[I"))
		)
	) {
		errno = ENOENT;
		goto out;
	}

	FB4JVarScreenInfo_initClass(env);
	FB4JFixScreenInfo_initClass(env);

	initialized = 1;

out:
//	pthread_mutex_unlock(&mutex);

	if ( errno ) {
		THROW_EXCEPTION(env,strerror(errno));
	}
}

JNIEXPORT jobject JNICALL
Java_org_fb4j_FB4JFrameBuffer_getVarScreenInfoNative
(JNIEnv *env, jclass clazz, jobject FileDescriptor_obj)
{
	METHOD_NAME(getVarScreenInfoNative);

	jobject FB4JVarScreenInfo_obj = NULL;
	struct fb_var_screeninfo *vinfo;
	jobject FB4JVarScreenInfo_bb_obj;
	int fd = -1;

	errno = 0;

	if (
		(    0  > (fd = (*env)->GetIntField(env, FileDescriptor_obj, FileDescriptor_fd_fieldID))) ||
		( NULL == (FB4JVarScreenInfo_obj = (*env)->NewObject(env,FB4JVarScreenInfo_class,FB4JVarScreenInfo_ctor_methodID))) ||
		( NULL == (FB4JVarScreenInfo_bb_obj = (*env)->GetObjectField(env,FB4JVarScreenInfo_obj,FB4JVarScreenInfo_bb_fieldID))) ||
		( NULL == (vinfo = (*env)->GetDirectBufferAddress(env, FB4JVarScreenInfo_bb_obj))) ||
		(   -1 == ioctl(fd,FBIOGET_VSCREENINFO,vinfo))
	) {
		errno = errno ? errno : EIO;
		goto out;
	}


out:
	if ( errno ) {
		THROW_EXCEPTION(env,strerror(errno));
	}
	return FB4JVarScreenInfo_obj;
}

JNIEXPORT void JNICALL
Java_org_fb4j_FB4JFrameBuffer_putVarScreenInfoNative
(JNIEnv *env, jclass clazz, jobject FileDescriptor_obj, jobject FB4JVarScreenInfo_obj)
{

	METHOD_NAME(putVarScreenInfoNative);

	int fd;
	struct fb_var_screeninfo *vinfo;
	jobject FB4JVarScreenInfo_bb_obj;

	if (
		(    0  > (fd = (*env)->GetIntField(env, FileDescriptor_obj, FileDescriptor_fd_fieldID))) ||
		( NULL == (FB4JVarScreenInfo_bb_obj = (*env)->GetObjectField(env,FB4JVarScreenInfo_obj,FB4JVarScreenInfo_bb_fieldID))) ||
		( NULL == (vinfo = (*env)->GetDirectBufferAddress(env, FB4JVarScreenInfo_bb_obj))) ||
		(   -1 == ioctl(fd,FBIOPUT_VSCREENINFO,vinfo))
	) {
		errno = errno ? errno : EIO;
		goto out;
	}

out:
	if ( errno ) {
		THROW_EXCEPTION(env, strerror(errno));
	}
}

JNIEXPORT jobject JNICALL
Java_org_fb4j_FB4JFrameBuffer_getFixScreenInfoNative
(JNIEnv *env, jclass clazz, jobject FileDescriptor_obj)
{
	METHOD_NAME(getFixScreenInfoNative);

	jobject FB4JFixScreenInfo_obj = NULL;
	struct fb_fox_screeninfo *vinfo;
	jobject FB4JFixScreenInfo_bb_obj;
	int fd = -1;

	if (
		(    0  > (fd = (*env)->GetIntField(env, FileDescriptor_obj, FileDescriptor_fd_fieldID))) ||
		( NULL == (FB4JFixScreenInfo_obj = (*env)->NewObject(env,FB4JFixScreenInfo_class,FB4JFixScreenInfo_ctor_methodID))) ||
		( NULL == (FB4JFixScreenInfo_bb_obj = (*env)->GetObjectField(env,FB4JFixScreenInfo_obj,FB4JFixScreenInfo_bb_fieldID))) ||
		( NULL == (vinfo = (*env)->GetDirectBufferAddress(env, FB4JFixScreenInfo_bb_obj))) ||
		(   -1 == ioctl(fd,FBIOGET_FSCREENINFO,vinfo))
	) {
		errno = errno ? errno : EIO;
		goto out;
	}

out:
	if ( errno ) {
		THROW_EXCEPTION(env,strerror(errno));
	}
	return FB4JFixScreenInfo_obj;
}

JNIEXPORT void JNICALL Java_org_fb4j_FB4JFrameBuffer_panDisplayNative
(JNIEnv *env, jclass clazz, jobject FileDescriptor_obj, jobject FB4JVarScreenInfo_obj)
{
	int fd = -1;

	METHOD_NAME(panDisplayNative);

	struct fb_var_screeninfo *vinfo;
	jobject FB4JVarScreenInfo_bb_obj;

	if (
		(    0  > (fd = (*env)->GetIntField(env, FileDescriptor_obj, FileDescriptor_fd_fieldID))) ||
		( NULL == (FB4JVarScreenInfo_bb_obj = (*env)->GetObjectField(env,FB4JVarScreenInfo_obj,FB4JVarScreenInfo_bb_fieldID))) ||
		( NULL == (vinfo = (*env)->GetDirectBufferAddress(env, FB4JVarScreenInfo_bb_obj))) ||
		(   -1 == ioctl(fd,FBIOPAN_DISPLAY,vinfo))
	) {
		errno = errno ? errno : EIO;
		goto out;
	}

out:
	if ( errno ) {
		THROW_EXCEPTION(env, strerror(errno));
	}
}

JNIEXPORT void JNICALL Java_org_fb4j_FB4JFrameBuffer_blankNative
(JNIEnv *env, jclass clazz, jobject FileDescriptor_obj)
{
	int fd = -1, ret;

	METHOD_NAME(blankNative);

	if (
		(  0  > (fd = (*env)->GetIntField(env, FileDescriptor_obj, FileDescriptor_fd_fieldID))) ||
		( -1 == (ret = ioctl(fd,FBIOBLANK)))
	) {
		errno = errno ? errno : EIO;
		goto out;
	}

out:
	if ( errno ) {
		THROW_EXCEPTION(env, strerror(errno));
	}
}

JNIEXPORT void JNICALL Java_org_fb4j_FB4JFrameBuffer_waitForVSyncNative
(JNIEnv *env, jclass clazz, jobject FileDescriptor_obj)
{
	int fd = -1, ret;
	uint32_t x;

	METHOD_NAME(waitForVSyncNative);

	if (
		(  0  > (fd = (*env)->GetIntField(env, FileDescriptor_obj, FileDescriptor_fd_fieldID))) ||
		( -1 == (ret = ioctl(fd,FBIO_WAITFORVSYNC,&x)))
	) {
		errno = errno ? errno : EIO;
		goto out;
	}

out:
	if ( errno ) {
		THROW_EXCEPTION(env, strerror(errno));
	}
}

/*
 * org.fb4j.FB4JVarScreenInfo
 */

#undef CLASSNAME
#define CLASSNAME "FB4JVarScreenInfo"

static void FB4JVarScreenInfo_initClass(JNIEnv *env)
{
	METHOD_NAME(initClass);

	static const jint off[] = {
		[org_fb4j_FB4JVarScreenInfo_XRES]           = offsetof(struct fb_var_screeninfo,xres),
		[org_fb4j_FB4JVarScreenInfo_YRES]           = offsetof(struct fb_var_screeninfo,yres),
		[org_fb4j_FB4JVarScreenInfo_XRES_VIRTUAL]   = offsetof(struct fb_var_screeninfo,xres_virtual),
		[org_fb4j_FB4JVarScreenInfo_YRES_VIRTUAL]   = offsetof(struct fb_var_screeninfo,yres_virtual),
		[org_fb4j_FB4JVarScreenInfo_XOFFSET]        = offsetof(struct fb_var_screeninfo,xoffset),
		[org_fb4j_FB4JVarScreenInfo_YOFFSET]        = offsetof(struct fb_var_screeninfo,yoffset),
		[org_fb4j_FB4JVarScreenInfo_BITS_PER_PIXEL] = offsetof(struct fb_var_screeninfo,bits_per_pixel),
		[org_fb4j_FB4JVarScreenInfo_GRAYSCALE]      = offsetof(struct fb_var_screeninfo,grayscale),
		[org_fb4j_FB4JVarScreenInfo_RED_OFFSET]     = offsetof(struct fb_var_screeninfo,red.offset),
		[org_fb4j_FB4JVarScreenInfo_RED_LENGTH]     = offsetof(struct fb_var_screeninfo,red.length),
		[org_fb4j_FB4JVarScreenInfo_GREEN_OFFSET]   = offsetof(struct fb_var_screeninfo,green.offset),
		[org_fb4j_FB4JVarScreenInfo_GREEN_LENGTH]   = offsetof(struct fb_var_screeninfo,green.length),
		[org_fb4j_FB4JVarScreenInfo_BLUE_OFFSET]    = offsetof(struct fb_var_screeninfo,blue.offset),
		[org_fb4j_FB4JVarScreenInfo_BLUE_LENGTH]    = offsetof(struct fb_var_screeninfo,blue.length),
		[org_fb4j_FB4JVarScreenInfo_TRANSP_OFFSET]  = offsetof(struct fb_var_screeninfo,transp.offset),
		[org_fb4j_FB4JVarScreenInfo_TRANSP_LENGTH]  = offsetof(struct fb_var_screeninfo,transp.length),
	};

	jintArray FB4JVarScreenInfo_offsets_obj;

	if (
		(NULL == (FB4JVarScreenInfo_offsets_obj = (*env)->NewIntArray(env,ARRAY_SIZE(off))))
	) {
		errno = errno ? errno : EIO;
		goto out;
	}

	(*env)->SetStaticObjectField(env,FB4JVarScreenInfo_class,FB4JVarScreenInfo_offsets_fieldID,FB4JVarScreenInfo_offsets_obj);
	(*env)->SetIntArrayRegion(env,FB4JVarScreenInfo_offsets_obj,0,ARRAY_SIZE(off),off);

out:
	if ( errno ) {
		THROW_EXCEPTION(env, strerror(errno));
	}
}

JNIEXPORT void JNICALL Java_org_fb4j_FB4JVarScreenInfo_initInstance
  (JNIEnv *env, jobject FB4JVarScreenInfo_obj)
{
	METHOD_NAME(initInstance);

	jobject FB4JVarScreenInfo_bb_obj;
	void *address;

	if (
		( NULL == (address = malloc(sizeof(struct fb_var_screeninfo)))) ||
		( NULL == (FB4JVarScreenInfo_bb_obj = (*env)->NewDirectByteBuffer(env,address,sizeof(struct fb_var_screeninfo))))
	) {
		errno = errno ? errno : EIO;
		goto out;
	}

	(*env)->SetObjectField(env, FB4JVarScreenInfo_obj, FB4JVarScreenInfo_bb_fieldID, FB4JVarScreenInfo_bb_obj);

out:
	if ( errno ) {
		THROW_EXCEPTION(env, strerror(errno));
	}
}

JNIEXPORT void JNICALL Java_org_fb4j_FB4JVarScreenInfo_finiInstance
  (JNIEnv *env, jobject FB4JVarScreenInfo_obj)
{
	METHOD_NAME(finiInstance);

	jobject FB4JVarScreenInfo_bb_obj;
	void *address = NULL;

	if (
		( NULL == (FB4JVarScreenInfo_bb_obj = (*env)->NewDirectByteBuffer(env,address,sizeof(struct fb_var_screeninfo)))) ||
		( NULL == (address = (*env)->GetDirectBufferAddress(env, FB4JVarScreenInfo_bb_obj)))
	) {
		errno = errno ? errno : EIO;
		goto out;
	}

	free(address);

out:
	if ( errno ) {
		THROW_EXCEPTION(env, strerror(errno));
	}
}

/*
 * org.fb4j.FB4JFixScreenInfo
 */

#undef CLASSNAME
#define CLASSNAME "FB4JFixScreenInfo"

static void FB4JFixScreenInfo_initClass(JNIEnv *env)
{
	METHOD_NAME(initClass);

	static const jint off[] = {
		[org_fb4j_FB4JFixScreenInfo_ID]          = offsetof(struct fb_fix_screeninfo,id),
		[org_fb4j_FB4JFixScreenInfo_SMEM_LEN]    = offsetof(struct fb_fix_screeninfo,smem_len),
		[org_fb4j_FB4JFixScreenInfo_TYPE]        = offsetof(struct fb_fix_screeninfo,type),
		[org_fb4j_FB4JFixScreenInfo_XPANSTEP]    = offsetof(struct fb_fix_screeninfo,xpanstep),
		[org_fb4j_FB4JFixScreenInfo_YPANSTEP]    = offsetof(struct fb_fix_screeninfo,ypanstep),
		[org_fb4j_FB4JFixScreenInfo_YWRAPSTEP]   = offsetof(struct fb_fix_screeninfo,ywrapstep),
		[org_fb4j_FB4JFixScreenInfo_LINE_LENGTH] = offsetof(struct fb_fix_screeninfo,line_length),
	};

	jintArray FB4JFixScreenInfo_offsets_obj;

	if (
		(NULL == (FB4JFixScreenInfo_offsets_obj = (*env)->NewIntArray(env,ARRAY_SIZE(off))))
	) {
		errno = errno ? errno : EIO;
		goto out;
	}

	(*env)->SetIntArrayRegion(env,FB4JFixScreenInfo_offsets_obj,0,ARRAY_SIZE(off),off);
	(*env)->SetStaticObjectField(env,FB4JFixScreenInfo_class,FB4JFixScreenInfo_offsets_fieldID,FB4JFixScreenInfo_offsets_obj);

out:
	if ( errno ) {
		THROW_EXCEPTION(env, strerror(errno));
	}
}

JNIEXPORT void JNICALL Java_org_fb4j_FB4JFixScreenInfo_initInstance
  (JNIEnv *env, jobject FB4JFixScreenInfo_obj)
{
	METHOD_NAME(initInstance);

	jobject FB4JFixScreenInfo_bb_obj;
	void *address;

	if (
		( NULL == (address = malloc(sizeof(struct fb_fix_screeninfo)))) ||
		( NULL == (FB4JFixScreenInfo_bb_obj = (*env)->NewDirectByteBuffer(env,address,sizeof(struct fb_fix_screeninfo))))
	) {
		errno = errno ? errno : EIO;
		goto out;
	}
	(*env)->SetObjectField(env, FB4JFixScreenInfo_obj, FB4JFixScreenInfo_bb_fieldID, FB4JFixScreenInfo_bb_obj);

out:
	if ( errno ) {
		THROW_EXCEPTION(env, strerror(errno));
	}
}

JNIEXPORT void JNICALL Java_org_fb4j_FB4JFixScreenInfo_finiInstance
  (JNIEnv *env, jobject FB4JFixScreenInfo_obj)
{
	METHOD_NAME(finiInstance);

	jobject FB4JFixScreenInfo_bb_obj;
	void *address = NULL;

	if (
		( NULL == (FB4JFixScreenInfo_bb_obj = (*env)->NewDirectByteBuffer(env,address,sizeof(struct fb_var_screeninfo)))) ||
		( NULL == (address = (*env)->GetDirectBufferAddress(env, FB4JFixScreenInfo_bb_obj)))
	) {
		errno = errno ? errno : EIO;
		goto out;
	}

	free(address);

out:
	if ( errno ) {
		THROW_EXCEPTION(env, strerror(errno));
	}
}

#include "version.inc"

void org_fb4j_printversion() {
	printf("%s\n", _version);
}

#include "author.inc"

void org_fb4j_printauthor() {
	int i;
	for(i=0; _author[i]; i++) {
		printf("%s\n", _author[i]);
	}
}

#include "license.inc"

void org_fb4j_printlicense() {
	printf("%s\n", _license);
}

