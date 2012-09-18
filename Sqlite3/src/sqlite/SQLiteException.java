package sqlite;

/**
 * Class for SQLite related exceptions.
 */
public class SQLiteException extends java.lang.Exception {

    private static final long serialVersionUID = 4663177360263756125L;

    /**
     * Construct a new SQLite exception.
     * 
     * @param string
     *            error message
     */
    public SQLiteException(String string) {
        super(string);
    }

    SQLiteException() {
        super();
    }

    SQLiteException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    SQLiteException(Throwable throwable) {
        super(throwable);
    }

}
