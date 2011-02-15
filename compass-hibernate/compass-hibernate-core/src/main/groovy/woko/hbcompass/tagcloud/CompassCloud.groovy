package woko.hbcompass.tagcloud

import org.compass.core.Compass
import org.compass.core.CompassTransaction
import org.compass.core.CompassSession
import org.compass.core.lucene.engine.LuceneSearchEngineInternalSearch
import org.compass.core.lucene.util.LuceneHelper
import org.apache.lucene.index.IndexReader
import org.apache.lucene.index.TermEnum

class CompassCloud {

  Collection<CompassCloudElem> getCloud(Compass compass) {
    def res = []
    CompassSession session = compass.openSession()
    CompassTransaction tx = session.beginTransaction()
    try {
      LuceneSearchEngineInternalSearch is = LuceneHelper.getLuceneInternalSearch(session)
      IndexReader reader = is.getReader()
      TermEnum terms = reader.terms();
      while (terms.next()) {
        String field = terms.term().field();
        if (field.startsWith('$') || field.equals("alias")) {
          continue
        }
//        println "field : $field"
        int freq = terms.docFreq();
//        println "freq : $freq"
        String text = terms.term().text().trim();
//        println "text : $text"
        res << new CompassCloudElem([term: text, freq:freq])
      }
      tx.commit()
    } catch(Exception e) {
      tx.rollback()
      throw e
    }
    return res
  }

}
