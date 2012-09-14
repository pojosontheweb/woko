package woko.ioc;

public interface WokoIocContainer {

    static final String ObjectStore = "ObjectStore";
    static final String UserManager = "UserManager";
    static final String FacetDescriptorManager = "FacetDescriptorManager";
    static final String UsernameResolutionStrategy = "UsernameResolutionStrategy";

    <T> T getComponent(String name);

    void close();

}
