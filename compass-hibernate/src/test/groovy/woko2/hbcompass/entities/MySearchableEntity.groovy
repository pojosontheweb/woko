package woko2.hbcompass.entities

import org.compass.annotations.Searchable
import org.compass.annotations.SearchableId
import org.compass.annotations.SearchableProperty
import javax.persistence.Entity
import javax.persistence.Id

@Searchable
@Entity
class MySearchableEntity {

  @SearchableId
  @Id
  Long id

  @SearchableProperty
  String prop1

  @SearchableProperty
  String prop2

}
