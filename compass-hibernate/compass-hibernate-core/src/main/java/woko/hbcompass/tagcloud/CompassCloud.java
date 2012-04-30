/*
 * Copyright 2001-2012 Remi Vankeisbelck
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

package woko.hbcompass.tagcloud;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.TermEnum;
import org.compass.core.Compass;
import org.compass.core.CompassSession;
import org.compass.core.CompassTransaction;
import org.compass.core.lucene.engine.LuceneSearchEngineInternalSearch;
import org.compass.core.lucene.util.LuceneHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CompassCloud {

  protected int computeCategory(int freq) {
    if (freq == 1) {
      return 1;
    }
    if (freq < 5) {
      return 2;
    }
    if (freq < 10) {
      return 3;
    }
    if (freq < 100) {
      return 4;
    }
    return 5;
  }

  public Collection<CompassCloudElem> getCloud(Compass compass) {
    List<CompassCloudElem> res = new ArrayList<CompassCloudElem>();
    CompassSession session = compass.openSession();
    CompassTransaction tx = session.beginTransaction();
    try {
      LuceneSearchEngineInternalSearch is = LuceneHelper.getLuceneInternalSearch(session);
      IndexReader reader = is.getReader();
      if (reader == null) {
        return Collections.emptySet();
      }
      TermEnum terms = reader.terms();
      List<String> alreadyProcessed = new ArrayList<String>();
      while (terms.next()) {
        String text = terms.term().text().trim();
        if (!alreadyProcessed.contains(text)) {
          String field = terms.term().field();
          if (field.startsWith("$") || field.equals("alias")) {
            continue;
          }
          int freq = terms.docFreq();
          CompassCloudElem cce = new CompassCloudElem(text, freq, computeCategory(freq));
          res.add(cce);
          alreadyProcessed.add(text);
        }
      }
      tx.commit();
    } catch (Exception e) {
      tx.rollback();
      throw new RuntimeException(e);
    }
    return res;
  }

}
