package woko.facets.builtin;

import woko.persistence.ResultIterator;

public interface ResultFacet {

  ResultIterator getResults();

  Integer getPage();

  Integer getResultsPerPage();
  
}