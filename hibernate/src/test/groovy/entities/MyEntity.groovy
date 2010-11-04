package entities

import javax.persistence.Entity
import javax.persistence.Id
import org.hibernate.search.annotations.Indexed
import org.hibernate.search.annotations.DocumentId
import org.hibernate.search.annotations.Field

@Entity
@Indexed
class MyEntity {

  @Id
  @DocumentId
  Long id

  @Field
  String name

}
