package woko.ioc;

public interface WokoIocContainer {

    <T> T getComponent(Class<T> componentClass);

}
