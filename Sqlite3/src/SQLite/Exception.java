package SQLite;

/**
 * Class for SQLite related exceptions.
 */
public class Exception extends java.lang.Exception {

    private static final long serialVersionUID = 4663177360263756125L;

    /**
     * Construct a new SQLite exception.
     * 
     * @param string
     *            error message
     */
    public Exception(String string) {
        super(string);
    }
}
