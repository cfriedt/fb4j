package org.fb4j;

import java.io.*;

import org.jruby.ext.posix.*;
import org.jruby.ext.posix.POSIX.ERRORS;

final class FB4JPOSIXHandler implements POSIXHandler {

	public FB4JPOSIXHandler() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void error( ERRORS error, String extraData ) {
		// TODO Auto-generated method stub

	}

	@Override
	public void unimplementedError( String methodName ) {
		// TODO Auto-generated method stub

	}

	@Override
	public void warn( WARNING_ID id, String message, Object... data ) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isVerbose() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public File getCurrentWorkingDirectory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getEnv() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream getInputStream() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PrintStream getOutputStream() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getPID() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public PrintStream getErrorStream() {
		// TODO Auto-generated method stub
		return null;
	}

}
