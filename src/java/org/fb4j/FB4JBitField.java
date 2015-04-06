package org.fb4j;

import java.util.*;

import com.sun.jna.*;

public class FB4JBitField extends Structure {

	public static class ByReference extends FB4JBitField implements Structure.ByValue {};

	public int offset;
	public int length;
	public int msb_right;

	@Override
	public String toString() {
		return super.toString() +
			"offset: " + offset +
			", " +
			"length: " + length +
			", " +
			"msb_right: " + msb_right +
			"";
	}

	@Override
	protected List getFieldOrder() {
		return Arrays.asList( new String[] { "offset", "length", "msb_right", } );
	}
}
