package woko.persistence;

public interface TransactionCallback {

    void execute() throws Exception;

}
