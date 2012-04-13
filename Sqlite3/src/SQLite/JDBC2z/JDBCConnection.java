package SQLite.JDBC2z;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Properties;

public class JDBCConnection implements java.sql.Connection, SQLite.BusyHandler {

    /**
     * Open database.
     */
    protected DatabaseX db;

    /**
     * Database URL.
     */
    protected String url;

    /**
     * Character encoding.
     */
    protected String enc;

    /**
     * SQLite 3 VFS to use.
     */
    protected String vfs;

    /**
     * Autocommit flag, true means autocommit.
     */
    protected boolean autocommit = true;

    /**
     * In-transaction flag. Can be true only when autocommit false.
     */
    protected boolean intrans = false;

    /**
     * Timeout for Database.exec()
     */
    protected int timeout = 1000000;

    /**
     * Use double/julian date representation.
     */
    protected boolean useJulian = false;

    /**
     * File name of database.
     */
    private String dbfile = null;

    /**
     * Reference to meta data or null.
     */
    private JDBCDatabaseMetaData meta = null;

    /**
     * Base time value for timeout handling.
     */
    private long t0;

    /**
     * Database in readonly mode.
     */
    private boolean readonly = false;

    /**
     * Transaction isolation mode.
     */
    private int trmode = TRANSACTION_SERIALIZABLE;

    /**
     * 
     * @param db
     * @param count
     * @return
     */
    private boolean busy0(DatabaseX db, int count) {
        if (count <= 1) {
            t0 = System.currentTimeMillis();
        }
        if (db != null) {
            long t1 = System.currentTimeMillis();
            if (t1 - t0 > timeout) {
                return false;
            }
            db.wait(100);
            return true;
        }
        return false;
    }

    @Override
    public boolean busy(String table, int count) {
        return busy0(db, count);
    }

    protected boolean busy3(DatabaseX db, int count) {
        if (count <= 1) {
            t0 = System.currentTimeMillis();
        }
        if (db != null) {
            long t1 = System.currentTimeMillis();
            if (t1 - t0 > timeout) {
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * 打开数据库。<br />
     * PRAGMA short_column_names = off;<br />
     * PRAGMA full_column_names = on;<br />
     * PRAGMA empty_result_callbacks = on;<br />
     * PRAGMA show_datatypes = on;
     * <p>
     * 如果数据库 busy（等待时间超过 timeout 设置） 则打开失败，返回 null。
     * 
     * @param readonly
     * @return
     * @throws SQLException
     */
    private DatabaseX open(boolean readonly) throws SQLException {
        DatabaseX dbx = null;
        try {
            dbx = new DatabaseX();
            dbx.open(
                    dbfile,
                    readonly ? SQLite.Constants.SQLITE_OPEN_READONLY
                            : (SQLite.Constants.SQLITE_OPEN_READWRITE | SQLite.Constants.SQLITE_OPEN_CREATE),
                    vfs);
            dbx.set_encoding(enc);
        } catch (SQLite.Exception e) {
            throw new SQLException(e);
        }
        int loop = 0;
        while (true) {
            try {
                dbx.exec("PRAGMA short_column_names = off;", null);
                dbx.exec("PRAGMA full_column_names = on;", null);
                dbx.exec("PRAGMA empty_result_callbacks = on;", null);
                if (SQLite.Database.version().compareTo("2.6.0") >= 0) {
                    dbx.exec("PRAGMA show_datatypes = on;", null);
                }
            } catch (SQLite.Exception e) {
                if (dbx.last_error() != SQLite.Constants.SQLITE_BUSY
                        || !busy0(dbx, ++loop)) {
                    try {
                        dbx.close();
                    } catch (SQLite.Exception ee) {
                    }
                    throw new SQLException(e);
                }
                continue;
            }
            break;
        }
        return dbx;
    }

    /**
     * 打开一个数据库连接。
     * 
     * @param url
     *            如果以“sqlite:/”或者“jdbc:sqlite:/”开头，则截取后面部分为数据库文件路径；否则抛出
     *            SQLException 异常。
     * @param enc
     *            表示字符集
     * @param pwd
     *            打开数据库文件所需要的密钥。如果密钥不对则抛出 SQLException 异常。
     * @param drep
     *            如果以大写或者小写字母 j 开头则表示使用双/朱力安日期表示时间。
     * @param vfs
     *            SQLite 3 将要使用的 VFS 描述
     * @throws SQLException
     *             url格式不对；密钥不对；数据库文件打开失败等原因。
     */
    public JDBCConnection(String url, String enc, String pwd, String drep,
            String vfs, boolean readonly) throws SQLException {
        if (url.startsWith("sqlite:/")) {
            dbfile = url.substring(8);
        } else if (url.startsWith("jdbc:sqlite:/")) {
            dbfile = url.substring(13);
        } else {
            throw new SQLException("unsupported url");
        }
        this.url = url;
        this.enc = enc;
        this.vfs = vfs;
        try {
            db = open(this.readonly = readonly);
            try {
                if (pwd != null && pwd.length() > 0) {
                    db.key(pwd);
                }
            } catch (SQLite.Exception se) {
                throw new SQLException("error while setting key", se);
            }
            db.busy_handler(this);
        } catch (SQLException e) {
            if (db != null) {
                try {
                    db.close();
                } catch (SQLite.Exception ee) {
                    ee.printStackTrace();
                }
            }
            throw e;
        }
        useJulian = drep != null
                && (drep.startsWith("j") || drep.startsWith("J"));
    }

    /* non-standard */
    public SQLite.Database getSQLiteDatabase() {
        return db;
    }

    @Override
    public Statement createStatement() {
        JDBCStatement s = new JDBCStatement(this);
        return s;
    }

    /**
     * @param resultSetType
     *            目前支持的类型有 {@link ResultSet}.TYPE_FORWARD_ONLY；{@link ResultSet}
     *            .TYPE_SCROLL_INSENSITIVE；{@link ResultSet}
     *            .TYPE_SCROLL_SENSITIVE
     * @param resultSetConcurrency
     *            目前支持的类型有 {@link ResultSet}.CONCUR_READ_ONLY 和
     *            {@link ResultSet}.CONCUR_UPDATABLE
     */
    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency)
            throws SQLException {
        if (resultSetType != ResultSet.TYPE_FORWARD_ONLY
                && resultSetType != ResultSet.TYPE_SCROLL_INSENSITIVE
                && resultSetType != ResultSet.TYPE_SCROLL_SENSITIVE) {
            throw new SQLFeatureNotSupportedException(
                    "unsupported result set type");
        }
        if (resultSetConcurrency != ResultSet.CONCUR_READ_ONLY
                && resultSetConcurrency != ResultSet.CONCUR_UPDATABLE) {
            throw new SQLFeatureNotSupportedException(
                    "unsupported result set concurrency");
        }
        JDBCStatement s = new JDBCStatement(this);
        return s;
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        if (meta == null) {
            meta = new JDBCDatabaseMetaData(this);
        }
        return meta;
    }

    @Override
    public void close() throws SQLException {
        try {
            rollback();
        } catch (SQLException e) {
            /* ignored */
        }
        intrans = false;
        if (db != null) {
            try {
                db.close();
                db = null;
            } catch (SQLite.Exception e) {
                throw new SQLException(e);
            }
        }
    }

    @Override
    public boolean isClosed() throws SQLException {
        return db == null;
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        return readonly;
    }

    /**
     * 不支持
     */
    @Override
    public void clearWarnings() throws SQLException {
    }

    @Override
    public void commit() throws SQLException {
        if (db == null) {
            throw new SQLException("stale connection");
        }
        if (!intrans) {
            return;
        }
        try {
            db.exec("COMMIT", null);
            intrans = false;
        } catch (SQLite.Exception e) {
            throw new SQLException(e);
        }
    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        return autocommit;
    }

    /**
     * 不支持
     */
    @Override
    public String getCatalog() throws SQLException {
        return null;
    }

    @Override
    public int getTransactionIsolation() throws SQLException {
        return trmode;
    }

    /**
     * 不支持
     */
    @Override
    public SQLWarning getWarnings() throws SQLException {
        return null;
    }

    /**
     * 不支持
     */
    @Override
    public String nativeSQL(String sql) throws SQLException {
        throw new SQLException("not supported");
    }

    /**
     * 不支持
     */
    @Override
    public CallableStatement prepareCall(String sql) throws SQLException {
        throw new SQLException("not supported");
    }

    /**
     * 不支持
     */
    @Override
    public CallableStatement prepareCall(String sql, int x, int y)
            throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        JDBCPreparedStatement s = new JDBCPreparedStatement(this, sql);
        return s;
    }

    /**
     * @param sql
     * @param resultSetType
     *            目前支持的类型有 {@link ResultSet}.TYPE_FORWARD_ONLY；{@link ResultSet}
     *            .TYPE_SCROLL_INSENSITIVE；{@link ResultSet}
     *            .TYPE_SCROLL_SENSITIVE
     * @param resultSetConcurrency
     *            目前支持的类型有 {@link ResultSet}.CONCUR_READ_ONLY 和
     *            {@link ResultSet}.CONCUR_UPDATABLE
     */
    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType,
            int resultSetConcurrency) throws SQLException {
        if (resultSetType != ResultSet.TYPE_FORWARD_ONLY
                && resultSetType != ResultSet.TYPE_SCROLL_INSENSITIVE
                && resultSetType != ResultSet.TYPE_SCROLL_SENSITIVE) {
            throw new SQLFeatureNotSupportedException(
                    "unsupported result set type");
        }
        if (resultSetConcurrency != ResultSet.CONCUR_READ_ONLY
                && resultSetConcurrency != ResultSet.CONCUR_UPDATABLE) {
            throw new SQLFeatureNotSupportedException(
                    "unsupported result set concurrency");
        }
        JDBCPreparedStatement s = new JDBCPreparedStatement(this, sql);
        return s;
    }

    @Override
    public void rollback() throws SQLException {
        if (db == null) {
            throw new SQLException("stale connection");
        }
        if (!intrans) {
            return;
        }
        try {
            db.exec("ROLLBACK", null);
            intrans = false;
        } catch (SQLite.Exception e) {
            throw new SQLException(e);
        }
    }

    /**
     * 设置前事务自动回滚
     * 
     * @param ac
     */
    @Override
    public void setAutoCommit(boolean ac) throws SQLException {
        if (ac && intrans && db != null) {
            try {
                db.exec("ROLLBACK", null);
            } catch (SQLite.Exception e) {
                throw new SQLException(e);
            } finally {
                intrans = false;
            }
        }
        autocommit = ac;
    }

    /**
     * 不支持
     */
    @Override
    public void setCatalog(String catalog) throws SQLException {
    }

    /**
     * 如果 autocomment 为 false 调用该方法将抛出 SQLException 异常
     * 
     * @param ro
     */
    @Override
    public void setReadOnly(boolean ro) throws SQLException {
        if (intrans) {
            throw new SQLException("incomplete transaction");
        }
        if (ro != readonly) {
            DatabaseX dbx = null;
            try {
                dbx = open(ro);
                db.close();
                db = dbx;
                dbx = null;
                readonly = ro;
            } catch (SQLException e) {
                throw e;
            } catch (SQLite.Exception ee) {
                if (dbx != null) {
                    try {
                        dbx.close();
                    } catch (SQLite.Exception eee) {
                    }
                }
                throw new SQLException(ee);
            }
        }
    }

    /**
     * 可以设置 TransactionIsolation 的前提是数据库的 sharedCache 为 true。
     * 
     * @param level
     *            可选值为 {@link Connection}.TRANSACTION_READ_UNCOMMITTED 或者
     *            {@link Connection}.TRANSACTION_SERIALIZABLE（默认）。<br />
     *            {@link Connection}.TRANSACTION_READ_UNCOMMITTED 意味着执行 PRAGMA
     *            read_uncommitted = on; <br />
     *            {@link Connection}.TRANSACTION_SERIALIZABLE 意味着 PRAGMA
     *            read_uncommitted = off;
     */
    @Override
    public void setTransactionIsolation(int level) throws SQLException {
        if (db.is3() && SQLite.JDBC.sharedCache) {
            String flag = null;
            if (level == TRANSACTION_READ_UNCOMMITTED
                    && trmode != TRANSACTION_READ_UNCOMMITTED) {
                flag = "on";
            } else if (level == TRANSACTION_SERIALIZABLE
                    && trmode != TRANSACTION_SERIALIZABLE) {
                flag = "off";
            }
            if (flag != null) {
                try {
                    db.exec("PRAGMA read_uncommitted = " + flag + ";", null);
                    trmode = level;
                } catch (java.lang.Exception e) {
                }
            }
        }
        if (level != trmode) {
            throw new SQLException("not supported");
        }
    }

    /**
     * 不支持
     */
    @Override
    public java.util.Map<String, Class<?>> getTypeMap() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * 不支持
     */
    @SuppressWarnings("rawtypes")
    @Override
    public void setTypeMap(java.util.Map map) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * 返回 {@link ResultSet}.HOLD_CURSORS_OVER_COMMIT
     */
    @Override
    public int getHoldability() throws SQLException {
        return ResultSet.HOLD_CURSORS_OVER_COMMIT;
    }

    /**
     * 只能设置 {@link ResultSet}.HOLD_CURSORS_OVER_COMMIT 否则抛出
     * SQLFeatureNotSupportedException 异常
     */
    @Override
    public void setHoldability(int holdability) throws SQLException {
        if (holdability == ResultSet.HOLD_CURSORS_OVER_COMMIT) {
            return;
        }
        throw new SQLFeatureNotSupportedException("unsupported holdability");
    }

    /**
     * 不支持
     */
    @Override
    public Savepoint setSavepoint() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * 不支持
     */
    @Override
    public Savepoint setSavepoint(String name) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * 不支持
     */
    @Override
    public void rollback(Savepoint x) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * 不支持
     */
    @Override
    public void releaseSavepoint(Savepoint x) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * @param resultSetType
     * @param resultSetConcurrency
     * @param resultSetHoldability
     *            如果不是 {@link ResultSet}.HOLD_CURSORS_OVER_COMMIT 则抛出
     *            SQLFeatureNotSupportedException 异常
     */
    @Override
    public Statement createStatement(int resultSetType,
            int resultSetConcurrency, int resultSetHoldability)
            throws SQLException {
        if (resultSetHoldability != ResultSet.HOLD_CURSORS_OVER_COMMIT) {
            throw new SQLFeatureNotSupportedException("unsupported holdability");
        }
        return createStatement(resultSetType, resultSetConcurrency);
    }

    /**
     * @param sql
     * @param resultSetType
     * @param resultSetConcurrency
     * @param resultSetHoldability
     *            如果不是 {@link ResultSet}.HOLD_CURSORS_OVER_COMMIT 则抛出
     *            SQLFeatureNotSupportedException 异常
     */
    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType,
            int resultSetConcurrency, int resultSetHoldability)
            throws SQLException {
        if (resultSetHoldability != ResultSet.HOLD_CURSORS_OVER_COMMIT) {
            throw new SQLFeatureNotSupportedException("unsupported holdability");
        }
        return prepareStatement(sql, resultSetType, resultSetConcurrency);
    }

    /**
     * 不支持
     */
    @Override
    public CallableStatement prepareCall(String sql, int x, int y, int z)
            throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * @param sql
     * @param autokeys
     *            如果不为 {@link Statement}.NO_GENERATED_KEYS 则抛出
     *            SQLFeatureNotSupportedException 异常
     */
    @Override
    public PreparedStatement prepareStatement(String sql, int autokeys)
            throws SQLException {
        if (autokeys != Statement.NO_GENERATED_KEYS) {
            throw new SQLFeatureNotSupportedException(
                    "generated keys not supported");
        }
        return prepareStatement(sql);
    }

    /**
     * 不支持
     */
    @Override
    public PreparedStatement prepareStatement(String sql, int colIndexes[])
            throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * 不支持
     */
    @Override
    public PreparedStatement prepareStatement(String sql, String columns[])
            throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * 不支持
     */
    @Override
    public Clob createClob() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * 不支持
     */
    @Override
    public Blob createBlob() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * 不支持
     */
    @Override
    public NClob createNClob() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * 不支持
     */
    @Override
    public SQLXML createSQLXML() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * 永远返回 true
     */
    @Override
    public boolean isValid(int timeout) throws SQLException {
        return true;
    }

    /**
     * 不支持
     */
    @Override
    public void setClientInfo(String name, String value)
            throws SQLClientInfoException {
        throw new SQLClientInfoException();
    }

    /**
     * 不支持
     */
    @Override
    public void setClientInfo(Properties prop) throws SQLClientInfoException {
        throw new SQLClientInfoException();
    }

    /**
     * 不支持
     */
    @Override
    public String getClientInfo(String name) throws SQLException {
        throw new SQLException("unsupported");
    }

    /**
     * 不支持
     */
    @Override
    public Properties getClientInfo() throws SQLException {
        return new Properties();
    }

    /**
     * 不支持
     */
    @Override
    public Array createArrayOf(String type, Object[] elems) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * 不支持
     */
    @Override
    public Struct createStruct(String type, Object[] attrs) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    /**
     * 不支持
     */
    @Override
    public <T> T unwrap(java.lang.Class<T> iface) throws SQLException {
        throw new SQLException("unsupported");
    }

    /**
     * 永远返回 false
     */
    @Override
    public boolean isWrapperFor(java.lang.Class<?> iface) throws SQLException {
        return false;
    }

}

/**
 * 增加线程安全判断的 SQLite.Database 子类
 * 
 * @author clark
 * 
 */
class DatabaseX extends SQLite.Database {

    static Object lock = new Object();

    public DatabaseX() {
        super();
    }

    void wait(int ms) {
        try {
            synchronized (lock) {
                lock.wait(ms);
            }
        } catch (java.lang.Exception e) {
        }
    }

    @Override
    public void exec(String sql, SQLite.Callback cb) throws SQLite.Exception {
        super.exec(sql, cb);
        synchronized (lock) {
            lock.notifyAll();
        }
    }

    @Override
    public void exec(String sql, SQLite.Callback cb, String args[])
            throws SQLite.Exception {
        super.exec(sql, cb, args);
        synchronized (lock) {
            lock.notifyAll();
        }
    }

    @Override
    public SQLite.TableResult get_table(String sql, String args[])
            throws SQLite.Exception {
        SQLite.TableResult ret = super.get_table(sql, args);
        synchronized (lock) {
            lock.notifyAll();
        }
        return ret;
    }

    @Override
    public void get_table(String sql, String args[], SQLite.TableResult tbl)
            throws SQLite.Exception {
        super.get_table(sql, args, tbl);
        synchronized (lock) {
            lock.notifyAll();
        }
    }

}
