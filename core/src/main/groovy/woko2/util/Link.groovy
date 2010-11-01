package woko2.util

class Link {

  String href
  String text

  static Link newInstance(String text, String href) {
    return new Link([href:href, text:text])
  }

}
