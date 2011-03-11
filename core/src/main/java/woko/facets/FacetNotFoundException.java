package woko.facets;

public class FacetNotFoundException extends RuntimeException {

  public FacetNotFoundException(String facetName, String className, String key, String username) {
    super("Facet not found, facetName=" + facetName +
            ", className=" + className +
            ", key=" + key +
            ", username=" + username);
  }

  public FacetNotFoundException(String facetName, Object targetObject, Class targetObjectClass, String username) {
    super("Facet not found, facetName=" + facetName +
            ", targetObject=" + targetObject +
            ", targetObjectClass=" + targetObjectClass +
            ", username=" + username);
  }

}
