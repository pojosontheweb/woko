package woko.persistence;

public interface TransactionalStore {

    <RES> RES doInTransactionWithResult(TransactionCallbackWithResult<RES> callback);

    void doInTransaction(TransactionCallback callback);

}
