package woko.util

class Link {

  String href
  String text

  static Link newInstance(String text, String href) {
    return new Link([href:href, text:text])
  }

}
