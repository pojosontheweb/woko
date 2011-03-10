package woko.facets

class FacetNotFoundException extends RuntimeException {

  FacetNotFoundException(String facetName, String className, String key, String username) {
    super((String)'Facet not found, facetName=' + facetName +
            ', className=' + className +
            ', key=' + key +
            ', username=' + username)
  }

  FacetNotFoundException(String facetName, Object targetObject, Class targetObjectClass, String username) {
    super((String)'Facet not found, facetName=' + facetName +
            ', targetObject=' + targetObject +
            ', targetObjectClass=' + targetObjectClass +
            ', username=' + username)
  }

}
