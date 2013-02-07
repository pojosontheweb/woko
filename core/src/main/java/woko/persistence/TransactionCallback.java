package woko.persistence;

/**
 * Interface for transaction callbacks.
 */
public interface TransactionCallback {

    /**
     * Called inside a transaction
     * @throws Exception if an error occurs while excuting
     */
    void execute() throws Exception;

}
