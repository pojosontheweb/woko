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

    /**
     * Return the current transaction if any.
     * @return the current transaction if any, <code>null</code> is no tx is open
     */
    StoreTransaction getCurrentTransaction();

    /**
     * Create and return a new, active transaction. This should fire begin transaction in
     * the underlying database system.
     * If a transaction is already running, then it should simply return this one
     * and do no additional work.
     * @return a new transaction if there was no tx open, the current tx otherwise
     */
    StoreTransaction beginTransaction();
}
