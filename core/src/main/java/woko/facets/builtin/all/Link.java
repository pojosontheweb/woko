package woko.facets.builtin.all;

public class Link {

  private final String href;
  private final String text;
  private String cssClass;

  public Link(String href, String text) {
    this.href = href;
    this.text = text;
  }

  public String getHref() {
    return href;
  }

  public String getText() {
    return text;
  }

  public Link setCssClass(String cssClass) {
    this.cssClass = cssClass;
    return this;
  }

  public String getCssClass() {
    return cssClass;
  }
}
