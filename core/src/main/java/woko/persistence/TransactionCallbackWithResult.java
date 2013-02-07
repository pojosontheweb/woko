package woko.persistence;

/**
 * Interface for transaction callbacks that return a result
 * @param <RES> the type of the result
 */
public interface TransactionCallbackWithResult<RES> {

    /**
     * Execute callback and return the result
     * @return the result of the callback execution
     * @throws Exception if an error occurs while executing
     */
    RES execute() throws Exception;

}
