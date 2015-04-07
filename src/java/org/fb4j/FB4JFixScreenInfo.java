/*
 * The compilation of software known as FB4J is distributed under the
 * following terms:
 * 
 * Copyright (c) 2013 Christopher Friedt. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED.  IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 */

package org.fb4j;

import java.util.*;

import com.sun.jna.*;

public class FB4JFixScreenInfo extends Structure {

	public static class ByReference extends FB4JFixScreenInfo implements Structure.ByReference {}

	public byte[] id = new byte[16];
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
	public short[] reserved = new short[2];

	@Override
	public String toString() {
		return super.toString() +
			"id:" + new String( id ) +
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
