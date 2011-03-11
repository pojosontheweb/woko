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
