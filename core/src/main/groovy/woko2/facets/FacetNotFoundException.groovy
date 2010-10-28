package woko2.facets

class FacetNotFoundException extends RuntimeException {

  FacetNotFoundException(String facetName, String className, String key, String username) {
    super('Facet not found, facetName=' + facetName +
            ', className=' + className +
            ', key=' + key +
            ', username=' + username)
  }
}
