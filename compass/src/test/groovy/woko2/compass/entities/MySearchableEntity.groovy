package woko2.compass.entities

import org.compass.annotations.Searchable
import org.compass.annotations.SearchableId
import org.compass.annotations.SearchableProperty

@Searchable
class MySearchableEntity {

  @SearchableId
  Long id

  @SearchableProperty
  String prop1

  @SearchableProperty
  String prop2

}
