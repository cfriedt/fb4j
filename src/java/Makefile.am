_fb4j_java_srcs = \
  org/fb4j/FB4JFrameBuffer.java \
  org/fb4j/FB4JFixScreenInfo.java \
  org/fb4j/FB4JVarScreenInfo.java \
  org/fb4j/FB4JInfo.java


fb4jdir = $(datadir)/fb4j/lib
fb4j_JAVA = $(_fb4j_java_srcs)
fb4j_DATA = $(top_builddir)/fb4j.jar

BUILT_SOURCES = org/fb4j/FB4JInfo.java

org/fb4j/FB4JInfo.java: $(top_srcdir)/COPYING $(top_srcdir)/AUTHORS
	echo "package org.fb4j;" > $@
	echo 'public class FB4JInfo {' >> $@
	echo 'public static final String version = "$(VERSION)";' >> $@
	echo 'public static final String[] author = new String[] {' >> $@
	cat $(top_srcdir)/AUTHORS | sed -e 's|\(.*\)|"\1",|g' >> $@
	echo '};' >> $@
	echo 'public static final String license = ' >> $@
	cat $(top_srcdir)/COPYING | sed -e 's|\(.*\)|"\1\\n"\ \+|g' >> $@
	echo '"";' >> $@
	echo '}' >> $@

$(top_builddir)/org/fb4j/FB4JInfo.class: org/fb4j/FB4JInfo.java
	mkdir -p $(top_builddir)/org/fb4j
	cd $(top_srcdir)/src/java && \
	  javac -d $(top_builddir)/ $<

$(top_builddir)/fb4j.jar: classfb4j.stamp $(top_builddir)/org/fb4j/FB4JInfo.class
	cd $(top_builddir) && \
	  jar cf fb4j.jar `find org -name '*.class'`

clean-local:
	rm -Rf \
	  $(BUILT_SOURCES) \
	  $(top_builddir)/fb4j.jar \
	  $(top_builddir)/org/