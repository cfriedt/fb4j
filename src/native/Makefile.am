BUILT_SOURCES = \
  $(top_builddir)/org_fb4j_FB4JFrameBuffer.h \
  $(top_builddir)/org_fb4j_FB4JVarScreenInfo.h \
  $(top_builddir)/org_fb4j_FB4JFixScreenInfo.h \
  $(top_builddir)/version.inc \
  $(top_builddir)/author.inc \
  $(top_builddir)/license.inc

libdir = $(datadir)/fb4j/lib
lib_LTLIBRARIES = libfb4j.la
libfb4j_la_SOURCES = fb4j.c
libfb4j_la_CFLAGS = -std=c99 -I$(top_builddir) @JNI_CFLAGS@
libfb4j_la_LDFLAGS = -no-undefined
libfb4j_la_DEPENDENCIES = $(BUILT_SOURCES)

$(top_builddir)/version.inc:
	echo 'static const char *_version = "$(VERSION)";' > $@

$(top_builddir)/author.inc: $(top_srcdir)/AUTHORS
	echo 'static const char *_author[] = {' > $@
	cat $< | sed -e 's|\(.*\)|"\1",|g' >> $@
	echo 'NULL,' >> $@
	echo '};' >> $@

$(top_builddir)/license.inc: $(top_srcdir)/COPYING
	echo 'static const char *_license = ' > $@
	cat $< | sed -e 's|\(.*\)|"\1\\n"|g' >> $@
	echo ';' >> $@

$(top_builddir)/org_fb4j_FB4JFrameBuffer.h: $(top_builddir)/org/fb4j/FB4JFrameBuffer.class
	cd $(top_builddir)  && javah org.fb4j.FB4JFrameBuffer

$(top_builddir)/org_fb4j_FB4JVarScreenInfo.h: $(top_builddir)/org/fb4j/FB4JVarScreenInfo.class
	cd $(top_builddir)  && javah org.fb4j.FB4JVarScreenInfo

$(top_builddir)/org_fb4j_FB4JFixScreenInfo.h: $(top_builddir)/org/fb4j/FB4JFixScreenInfo.class
	cd $(top_builddir)  && javah org.fb4j.FB4JFixScreenInfo

clean-local:
	rm -f $(BUILT_SOURCES)