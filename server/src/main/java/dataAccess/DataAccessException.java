package dataAccess;

/**
 * Indicates there was an error connecting to the database
 */
public class DataAccessException extends Exception{
    public int getStatus() {
        return status;
    }

    final int status;
    public DataAccessException(String message, Integer status) {
        super(message);
        this.status = status;
    }
}
