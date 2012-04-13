package SQLite;

/**
 * Class wrapping an SQLite backup object.
 */

public class Backup {

    /**
     * Internal handle for the native SQLite API.
     */
    protected long handle = 0;

    /**
     * Finish a backup.
     */
    protected void finish() throws SQLite.SQLiteException {
        synchronized (this) {
            _finalize();
        }
    }

    /**
     * Destructor for object.
     */
    protected void finalize() {
        synchronized (this) {
            try {
                _finalize();
            } catch (SQLite.SQLiteException e) {
            }
        }
    }

    protected native void _finalize() throws SQLite.SQLiteException;

    /**
     * Perform a backup step.
     * 
     * @param n
     *            number of pages to backup
     * @return true when backup completed
     */
    public boolean step(int n) throws SQLite.SQLiteException {
        synchronized (this) {
            return _step(n);
        }
    }

    private native boolean _step(int n) throws SQLite.SQLiteException;

    /**
     * Perform the backup in one step.
     */
    public void backup() throws SQLite.SQLiteException {
        synchronized (this) {
            _step(-1);
        }
    }

    /**
     * Return number of remaining pages to be backed up.
     */
    public int remaining() throws SQLite.SQLiteException {
        synchronized (this) {
            return _remaining();
        }
    }

    private native int _remaining() throws SQLite.SQLiteException;

    /**
     * Return the total number of pages in the backup source database.
     */
    public int pagecount() throws SQLite.SQLiteException {
        synchronized (this) {
            return _pagecount();
        }
    }

    private native int _pagecount() throws SQLite.SQLiteException;

    /**
     * Internal native initializer.
     */
    private static native void internal_init();

    static {
        internal_init();
    }
}
