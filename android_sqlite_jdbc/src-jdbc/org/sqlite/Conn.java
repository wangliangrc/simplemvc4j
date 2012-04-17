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
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;

class Conn implements Connection {
    public Conn(String url, String filename) throws SQLException {
        boolean ro = false;

        // check the path to the file exists
        if (!":memory:".equals(filename)) {
            File file = new File(filename).getAbsoluteFile();
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) {
                for (File up = parent; up != null && !up.exists();) {
                    parent = up;
                    up = up.getParentFile();
                }
                throw new SQLException("path to '" + filename + "': '" + parent
                        + "' does not exist");
            }

            // check write access if file does not exist
            try {
                // The extra check to exists() is necessary as createNewFile()
                // does not follow the JavaDoc when used on read-only shares.
                if (!file.exists() && file.createNewFile())
                    file.delete();
            } catch (Exception e) {
                throw new SQLException("opening db: '" + filename + "': "
                        + e.getMessage());
            }
            filename = file.getAbsolutePath();
            if (file.exists())
                ro = !file.canWrite();
        }

        readOnly = ro;

        // TODO: library variable to explicitly control load type
        // attempt to use the Native library first
        try {
            Class<?> nativedb = Class.forName("org.sqlite.NativeDB");
            if (((Boolean) nativedb.getDeclaredMethod("load").invoke(null))
                    .booleanValue())
                db = (DB) nativedb.newInstance();
        } catch (Exception e) {
        } // fall through to nested library

        // load nested library
        if (db == null) {
            try {
                db = (DB) Class.forName("org.sqlite.NestedDB").newInstance();
            } catch (Exception e) {
                throw new SQLException("no SQLite library found");
            }
        }

        this.url = url;
        db.open(this, filename);
        setTimeout(3000);
    }

    public Conn(String url, String filename, boolean sharedCache)
            throws SQLException {
        this(url, filename);
        db.shared_cache(sharedCache);
    }

    @Override
    public void clearWarnings() throws SQLException {
    }

    @Override
    public void close() throws SQLException {
        if (db == null)
            return;
        if (meta != null)
            meta.close();

        db.close();
        db = null;
    }

    @Override
    public void commit() throws SQLException {
        checkOpen();
        if (autoCommit)
            throw new SQLException("database in auto-commit mode");
        db.exec("commit;");
        db.exec("begin;");
    }

    @Override
    public Statement createStatement() throws SQLException {
        return createStatement(ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_READ_ONLY, ResultSet.CLOSE_CURSORS_AT_COMMIT);
    }

    @Override
    public Statement createStatement(int rsType, int rsConcurr)
            throws SQLException {
        return createStatement(rsType, rsConcurr,
                ResultSet.CLOSE_CURSORS_AT_COMMIT);
    }

    @Override
    public Statement createStatement(int rst, int rsc, int rsh)
            throws SQLException {
        checkCursor(rst, rsc, rsh);
        return new Stmt(this);
    }

    public Struct createStruct(String t, Object[] attr) throws SQLException {
        throw new SQLException("unsupported by SQLite");
    }

    @Override
    public void finalize() throws SQLException {
        close();
    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        checkOpen();
        return autoCommit;
    }

    @Override
    public String getCatalog() throws SQLException {
        checkOpen();
        return null;
    }

    @Override
    public int getHoldability() throws SQLException {
        checkOpen();
        return ResultSet.CLOSE_CURSORS_AT_COMMIT;
    }

    @Override
    public DatabaseMetaData getMetaData() {
        if (meta == null)
            meta = new MetaData(this);
        return meta;
    }

    @Override
    public int getTransactionIsolation() {
        return TRANSACTION_SERIALIZABLE;
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Map getTypeMap() throws SQLException {
        throw new SQLException("not yet implemented");
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return null;
    }

    @Override
    public boolean isClosed() throws SQLException {
        return db == null;
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        return readOnly;
    }

    @Override
    public String nativeSQL(String sql) {
        return sql;
    }

    @Override
    public CallableStatement prepareCall(String sql) throws SQLException {
        return prepareCall(sql, ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_READ_ONLY, ResultSet.CLOSE_CURSORS_AT_COMMIT);
    }

    @Override
    public CallableStatement prepareCall(String sql, int rst, int rsc)
            throws SQLException {
        return prepareCall(sql, rst, rsc, ResultSet.CLOSE_CURSORS_AT_COMMIT);
    }

    @Override
    public CallableStatement prepareCall(String sql, int rst, int rsc, int rsh)
            throws SQLException {
        throw new SQLException("SQLite does not support Stored Procedures");
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_READ_ONLY);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int autoC)
            throws SQLException {
        throw new SQLException("NYI");
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int rst, int rsc)
            throws SQLException {
        return prepareStatement(sql, rst, rsc,
                ResultSet.CLOSE_CURSORS_AT_COMMIT);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int rst, int rsc,
            int rsh) throws SQLException {
        checkCursor(rst, rsc, rsh);
        return new PrepStmt(this, sql);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int[] colInds)
            throws SQLException {
        throw new SQLException("NYI");
    }

    @Override
    public PreparedStatement prepareStatement(String sql, String[] colNames)
            throws SQLException {
        throw new SQLException("NYI");
    }

    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        throw new SQLException("unsupported by SQLite: savepoints");
    }

    @Override
    public void rollback() throws SQLException {
        checkOpen();
        if (autoCommit)
            throw new SQLException("database in auto-commit mode");
        db.exec("rollback;");
        db.exec("begin;");
    }

    @Override
    public void rollback(Savepoint savepoint) throws SQLException {
        throw new SQLException("unsupported by SQLite: savepoints");
    }

    @Override
    public void setAutoCommit(boolean ac) throws SQLException {
        checkOpen();
        if (autoCommit == ac)
            return;
        autoCommit = ac;
        db.exec(autoCommit ? "commit;" : "begin;");
    }

    @Override
    public void setCatalog(String catalog) throws SQLException {
        checkOpen();
    }

    @Override
    public void setHoldability(int h) throws SQLException {
        checkOpen();
        if (h != ResultSet.CLOSE_CURSORS_AT_COMMIT)
            throw new SQLException(
                    "SQLite only supports CLOSE_CURSORS_AT_COMMIT");
    }

    @Override
    public void setReadOnly(boolean ro) throws SQLException {
    }

    @Override
    public Savepoint setSavepoint() throws SQLException {
        throw new SQLException("unsupported by SQLite: savepoints");
    }

    @Override
    public Savepoint setSavepoint(String name) throws SQLException {
        throw new SQLException("unsupported by SQLite: savepoints");
    }

    @Override
    public void setTransactionIsolation(int level) throws SQLException {
        if (level != TRANSACTION_SERIALIZABLE)
            throw new SQLException(
                    "SQLite supports only TRANSACTION_SERIALIZABLE");
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void setTypeMap(Map map) throws SQLException {
        throw new SQLException("not yet implemented");
    }

    DB db() {
        return db;
    }

    /** Used to supply DatabaseMetaData.getDriverVersion(). */
    String getDriverVersion() {
        if (db != null) {
            String dbname = db.getClass().getName();
            if (dbname.indexOf("NestedDB") >= 0)
                return "pure";
            if (dbname.indexOf("NativeDB") >= 0)
                return "native";
        }
        return "unloaded";
    }

    int getTimeout() {
        return timeout;
    }

    String libversion() throws SQLException {
        return db.libversion();
    }

    void setTimeout(int ms) throws SQLException {
        timeout = ms;
        db.busy_timeout(ms);
    }

    String url() {
        return url;
    }

    private void checkCursor(int rst, int rsc, int rsh) throws SQLException {
        if (rst != ResultSet.TYPE_FORWARD_ONLY)
            throw new SQLException(
                    "SQLite only supports TYPE_FORWARD_ONLY cursors");
        if (rsc != ResultSet.CONCUR_READ_ONLY)
            throw new SQLException(
                    "SQLite only supports CONCUR_READ_ONLY cursors");
        if (rsh != ResultSet.CLOSE_CURSORS_AT_COMMIT)
            throw new SQLException(
                    "SQLite only supports closing cursors at commit");
    }

    private void checkOpen() throws SQLException {
        if (db == null)
            throw new SQLException("database connection closed");
    }

    private final String url;

    // UNUSED FUNCTIONS /////////////////////////////////////////////

    private final boolean readOnly;

    private DB db = null;

    private MetaData meta = null;

    private boolean autoCommit = true;

    private int timeout = 0;
}
