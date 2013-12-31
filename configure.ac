AC_PREREQ(2.59)
AC_INIT(fb4j, 0.1)
AC_PROG_CC

AC_CANONICAL_SYSTEM

AM_INIT_AUTOMAKE

LT_INIT

AC_PROG_LIBTOOL
dnl AC_PROG_TRY_JAVAC
dnl AC_PROG_TRY_JAVAH
dnl AC_PROG_TRY_JAR

case $host_os in
  *linux*)
  JNI_CFLAGS="-I${JDK_HOME}/include -I${JDK_HOME}/include/linux"
  ;;
  *)
  AC_MSG_ERROR([unsupported host os $host_os])
  ;;
esac
AC_SUBST(JNI_CFLAGS)

dnl automake currently sets this in a broken way 
CLASSPATH_ENV=""
AC_SUBST(CLASSPATH_ENV)

AC_CONFIG_FILES(
    Makefile
    src/Makefile
    src/java/Makefile
    src/native/Makefile
    src/test/Makefile
)
AC_OUTPUT
