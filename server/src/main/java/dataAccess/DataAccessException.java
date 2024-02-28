package dataAccess;

/**
 * Indicates there was an error connecting to the database
 */
public class DataAccessException extends Exception{
    public int getStatus() {
        return status;
    }

    int status;
    public DataAccessException(String message, int status) {
        super(message);
        this.status = status;
    }
}
