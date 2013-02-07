package woko.persistence;

/**
 * Interface for transactional <code>ObjectStore</code> implementations.
 * Uses a callback mechanism to execute the code inside a databse transaction.
 */
public interface TransactionalStore {

    /**
     * Executes callback inside a transaction and return the result
     * @param callback the callback to be executed
     * @param <RES> the type of the result returned by the callback
     * @return the result of callback execution
     */
    <RES> RES doInTransactionWithResult(TransactionCallbackWithResult<RES> callback);

    /**
     * Execute passed callback inside a transaction.
     * @param callback the callback to be executed
     */
    void doInTransaction(TransactionCallback callback);

}
