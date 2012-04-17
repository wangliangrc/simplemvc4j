/*
 * Copyright (c) 2007 David Crawshaw <david@zentus.com>
 * 
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
 * ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
 * OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package org.sqlite;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

/** This class provides a thin JNI layer over the SQLite3 C API. */
final class NativeDB extends DB {
    static boolean load() {
        if (loaded != null)
            return loaded == Boolean.TRUE;

        String libpath = System.getProperty("org.sqlite.lib.path");
        String libname = System.getProperty("org.sqlite.lib.name");
        if (libname == null)
            libname = System.mapLibraryName("sqlitejdbc");

        // look for a pre-installed library
        try {
            if (libpath == null)
                // XXX 修改本地共享库名称
                System.loadLibrary("db");
            else
                System.load(new File(libpath, libname).getAbsolutePath());

            loaded = Boolean.TRUE;
            return true;
        } catch (UnsatisfiedLinkError e) {
        } // fall through

        // guess what a bundled library would be called
        String osname = System.getProperty("os.name").toLowerCase();
        String osarch = System.getProperty("os.arch");
        if (osname.startsWith("mac os")) {
            osname = "mac";
            osarch = "universal";
        }
        if (osname.startsWith("windows"))
            osname = "win";
        if (osname.startsWith("sunos"))
            osname = "solaris";
        if (osarch.startsWith("i") && osarch.endsWith("86"))
            osarch = "x86";
        libname = osname + '-' + osarch + ".lib";

        // try a bundled library
        try {
            ClassLoader cl = NativeDB.class.getClassLoader();
            InputStream in = cl.getResourceAsStream(libname);
            if (in == null)
                throw new Exception("libname: " + libname + " not found");
            File tmplib = File.createTempFile("libsqlitejdbc-", ".lib");
            tmplib.deleteOnExit();
            OutputStream out = new FileOutputStream(tmplib);
            byte[] buf = new byte[1024];
            for (int len; (len = in.read(buf)) != -1;)
                out.write(buf, 0, len);
            in.close();
            out.close();

            System.load(tmplib.getAbsolutePath());

            loaded = Boolean.TRUE;
            return true;
        } catch (Exception e) {
        }

        loaded = Boolean.FALSE;
        return false;
    }

    static void throwex(String msg) throws SQLException {
        throw new SQLException(msg);
    }

    @Override
    protected native synchronized int finalize(long stmt);

    // native synchronized void exec(String sql) throws SQLException;
    @Override
    protected native synchronized long prepare(String sql) throws SQLException;

    // WRAPPER FUNCTIONS ////////////////////////////////////////////

    @Override
    protected native synchronized int reset(long stmt);

    @Override
    protected native synchronized int step(long stmt);

    @Override
    native synchronized int bind_blob(long stmt, int pos, byte[] v);

    @Override
    native synchronized int bind_double(long stmt, int pos, double v);

    @Override
    native synchronized int bind_int(long stmt, int pos, int v);

    @Override
    native synchronized int bind_long(long stmt, int pos, long v);

    @Override
    native synchronized int bind_null(long stmt, int pos);

    @Override
    native synchronized int bind_parameter_count(long stmt);

    @Override
    native synchronized int bind_text(long stmt, int pos, String v);

    @Override
    native synchronized void busy_timeout(int ms);

    @Override
    native synchronized int changes();

    @Override
    native synchronized int clear_bindings(long stmt);

    @Override
    native synchronized byte[] column_blob(long stmt, int col);

    @Override
    native synchronized int column_count(long stmt);

    @Override
    native synchronized String column_decltype(long stmt, int col);

    @Override
    native synchronized double column_double(long stmt, int col);

    @Override
    native synchronized int column_int(long stmt, int col);

    @Override
    native synchronized long column_long(long stmt, int col);

    /**
     * Provides metadata for the columns of a statement. Returns: res[col][0] =
     * true if column constrained NOT NULL res[col][1] = true if column is part
     * of the primary key res[col][2] = true if column is auto-increment
     */
    @Override
    native synchronized boolean[][] column_metadata(long stmt);

    @Override
    native synchronized String column_name(long stmt, int col);

    @Override
    native synchronized String column_table_name(long stmt, int col);

    @Override
    native synchronized String column_text(long stmt, int col);

    @Override
    native synchronized int column_type(long stmt, int col);

    @Override
    native synchronized int create_function(String name, Function func);

    @Override
    native synchronized int destroy_function(String name);

    @Override
    native synchronized String errmsg();

    @Override
    native synchronized void free_functions();

    @Override
    native synchronized void interrupt();

    @Override
    native synchronized String libversion();

    @Override
    native synchronized void result_blob(long context, byte[] val);

    @Override
    native synchronized void result_double(long context, double val);

    @Override
    native synchronized void result_error(long context, String err);

    @Override
    native synchronized void result_int(long context, int val);

    @Override
    native synchronized void result_long(long context, long val);

    @Override
    native synchronized void result_null(long context);

    @Override
    native synchronized void result_text(long context, String val);

    @Override
    native synchronized int shared_cache(boolean enable);

    @Override
    native synchronized byte[] value_blob(Function f, int arg);

    @Override
    native synchronized int value_bytes(Function f, int arg);

    @Override
    native synchronized double value_double(Function f, int arg);

    @Override
    native synchronized int value_int(Function f, int arg);

    @Override
    native synchronized long value_long(Function f, int arg);

    @Override
    native synchronized String value_text(Function f, int arg);

    @Override
    native synchronized int value_type(Function f, int arg);

    /** SQLite connection handle. */
    long pointer = 0;

    private static Boolean loaded = null;

    @Override
    protected native synchronized void _close() throws SQLException;

    // COMPOUND FUNCTIONS (for optimisation) /////////////////////////

    @Override
    protected native synchronized void _open(String file) throws SQLException;

    /** linked list of all instanced UDFDatas */
    private long udfdatalist = 0;
}
