package woko.persistence;

/**
 * Interface for Store transactions. Allows to demarcate the tx in the store implementation
 * and to provide an abstraction for automatic tx handling.
 *
 * @see TransactionalStore
 * @see woko.actions.WokoTxInterceptor
 */
public interface StoreTransaction {

    /**
     * Return <code>true</code> if this transaction is active (needs commit or rollback), <code>false</code> otherwise
     * @return <code>true</code> if this tx is active
     */
    boolean isActive();

    /**
     * Commit this transaction
     */
    void commit();

    /**
     * Roll-back this transaction
     */
    void rollback();
}
