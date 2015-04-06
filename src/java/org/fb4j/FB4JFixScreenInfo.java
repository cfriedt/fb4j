package org.fb4j;

import java.util.*;

import com.sun.jna.*;

public class FB4JFixScreenInfo extends Structure {

	public static class ByReference extends FB4JFixScreenInfo implements Structure.ByReference {}

	public String id; // char id[16]
	public long smem_start;

	public int smem_len;
	public int type;
	public int type_aux;
	public int visual;
	public short xpanstep;
	public short ypanstep;
	public short ywrapstep;
	public int line_length;
	public long mmio_start;

	public int mmio_len;
	public int accel;

	public short capabilities;
	public short[] reserved; // __u16 reserved[2];

	@Override
	public String toString() {
		return super.toString() +
			"id:" + id +
			", " +
			"smem_len:" + smem_len +
			", " +
			"type:" + type +
			", " +
			"xpanstep:" + xpanstep +
			", " +
			"ypanstep:" + ypanstep +
			", " +
			"ywrapstep:" + ywrapstep +
			", " +
			"line_length:" + line_length +
			"";
	}

	@Override
	protected List getFieldOrder() {
		return Arrays.asList( new String[] { "id", "smem_start", "smem_len", "type", "type_aux", "visual", "xpanstep", "ypanstep", "ywrapstep", "line_length", "mmio_start", "mmio_len", "accel", "capabilities", "reserved", } );
	}
}
