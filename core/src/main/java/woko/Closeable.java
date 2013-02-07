package woko;

/**
 * Interface for IOC components that can be closed.
 *
 * @see woko.ioc.WokoIocContainer
 */
public interface Closeable {

    /**
     * Close this component and release all resources
     */
    void close();

}
