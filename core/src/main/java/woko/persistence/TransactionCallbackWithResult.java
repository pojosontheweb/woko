package woko.persistence;

public interface TransactionCallbackWithResult<RES> {

    RES execute() throws Exception;

}
