package woko.hbcompass.tagcloud

import org.compass.core.Compass
import org.compass.core.CompassTransaction
import org.compass.core.CompassSession
import org.compass.core.lucene.engine.LuceneSearchEngineInternalSearch
import org.compass.core.lucene.util.LuceneHelper
import org.apache.lucene.index.IndexReader
import org.apache.lucene.index.TermEnum

class CompassCloud {

  protected int computeCategory(int freq) {
      if (freq==1) {
          return 1
      }
      if (freq < 5) {
          return 2
      }
      if (freq < 10) {
          return 3
      }
      if (freq < 100) {
          return 4
      }
      return 5
  }

  Collection<CompassCloudElem> getCloud(Compass compass) {
    def res = []
    CompassSession session = compass.openSession()
    CompassTransaction tx = session.beginTransaction()
    try {
      LuceneSearchEngineInternalSearch is = LuceneHelper.getLuceneInternalSearch(session)
      IndexReader reader = is.getReader()
      if (reader==null) {
          return []
      }
      TermEnum terms = reader.terms()
      def alreadyProcessed = []
      while (terms.next()) {
        String text = terms.term().text().trim()
        if (!alreadyProcessed.contains(text)) {
            String field = terms.term().field()
            if (field.startsWith('$') || field.equals("alias")) {
              continue
            }
//        println "field : $field"
            int freq = terms.docFreq()
//        println "freq : $freq"

//        println "text : $text"
            res << new CompassCloudElem([term: text, freq:freq, categ:computeCategory(freq)])

            alreadyProcessed << text
        }
      }
      tx.commit()
    } catch(Exception e) {
      tx.rollback()
      throw e
    }
    return res
  }

}
