package webtests.async

import woko.async.hibernate.HbJobDetails

import javax.persistence.Entity

@Entity
class MyJobDetails extends HbJobDetails {

    int current = 0

}
