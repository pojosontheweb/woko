package woko2.facets.builtin

import woko2.persistence.ResultIterator

public interface ResultFacet {

  ResultIterator getResults()

  Integer getPage()

  Integer getResultsPerPage()
  
}