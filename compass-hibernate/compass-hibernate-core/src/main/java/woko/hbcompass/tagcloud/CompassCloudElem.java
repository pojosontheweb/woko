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

package woko.hbcompass.tagcloud;

public class CompassCloudElem {

  private final String term;
  private final int freq;
  private final int categ;

  public CompassCloudElem(String term, int freq, int categ) {
    this.term = term;
    this.freq = freq;
    this.categ = categ;
  }

  public String getTerm() {
    return term;
  }

  public int getFreq() {
    return freq;
  }

  public int getCateg() {
    return categ;
  }
}
