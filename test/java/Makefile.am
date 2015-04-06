_fb4j_unit_tests = \
  org/fb4j/Fb4jTest.java \
  org/fb4j/Fb4jJniTest.java

noinst_JAVA = $(_fb4j_test_src)

BUILT_SOURCES = org/fb4j/AllUnitTests.java

org/fb4j/AllUnitTests.java:
	mkdir -p $(dir $@)
	echo "package org.fb4j;" > $@
	echo "" >> $@
	echo 'import org.junit.runner.*;' >> $@
	echo 'import org.junit.runners.*;' >> $@
	echo "import org.junit.runners.Suite.*;" >> $@
	echo "" >> $@
	echo "@SuiteClasses({" >> $@
	echo "$(notdir $(_fb4j_unit_tests:.java=.class))" | sed -e "s| |,|g" >> $@
	echo '})' >> $@
	echo 'public class AllUnitTests {' >> $@
	echo '}' >> $@

org/fb4j/AllUnitTests.class: org/fb4j/AllUnitTests.java
	mkdir -p $(dir $@)
	@CLASSPATH_ENV@ $(JAVAC) -d $(JAVAROOT) $(AM_JAVACFLAGS) $<
          
clean-local:
	rm -Rf \
	  $(BUILT_SOURCES) \
	  $(top_builddir)/org/