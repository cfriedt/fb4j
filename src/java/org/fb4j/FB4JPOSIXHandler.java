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

import java.io.File;
import java.io.InputStream;
import java.io.PrintStream;

import org.jruby.ext.posix.POSIX.ERRORS;
import org.jruby.ext.posix.POSIXHandler;

class FB4JPOSIXHandler implements POSIXHandler {

    public void error(ERRORS error, String extraData) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void unimplementedError(String methodName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void warn(WARNING_ID id, String message, Object... data) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isVerbose() {
        return false;
    }

    public File getCurrentWorkingDirectory() {
        return new File("/tmp");
    }

    public String[] getEnv() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public InputStream getInputStream() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public PrintStream getOutputStream() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int getPID() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public PrintStream getErrorStream() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
