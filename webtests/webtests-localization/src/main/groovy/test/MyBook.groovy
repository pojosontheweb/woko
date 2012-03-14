/*
 * Copyright 2001-2010 Remi Vankeisbelck
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package test

import javax.persistence.Entity
import javax.persistence.Id
import org.compass.annotations.SearchableProperty
import org.compass.annotations.SearchableId
import org.compass.annotations.Searchable
import javax.validation.constraints.NotNull
import javax.persistence.GenerationType
import javax.persistence.GeneratedValue
import javax.validation.constraints.Min

@Entity
@Searchable
class MyBook {

    @NotNull
    @SearchableProperty
    String name

    @Id
    @SearchableId
    String _id

    @SearchableProperty
    int nbPages

    @SearchableProperty
    Date creationTime = new Date()

    /*
    transient List<MyBook> listOfMe = [this, this, this]
    transient MyBook me = this
    transient List<String> listOfStrings = ["abd", "ddd"]

    transient OtherPojo otherPojo = new OtherPojo()
    transient List<OtherPojo> otherPojos = [new OtherPojo()]

    transient OtherPojo2 otherPojo2 = new OtherPojo2()
    transient List<OtherPojo2> otherPojos2 = [new OtherPojo2()]
    */

}
