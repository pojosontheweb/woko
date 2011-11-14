package test

import javax.persistence.Entity
import javax.persistence.Id
import org.compass.annotations.SearchableProperty
import org.compass.annotations.SearchableId
import org.compass.annotations.Searchable
import javax.validation.constraints.NotNull

@Entity
@Searchable
class MyBook {

  @NotNull
  @SearchableProperty
  String name

  @Id
  @SearchableId
  String id

  @SearchableProperty
  int nbPages

  @SearchableProperty
  Date creationTime = new Date()

}
