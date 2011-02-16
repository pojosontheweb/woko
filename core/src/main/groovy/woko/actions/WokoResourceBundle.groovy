package woko.actions

class WokoResourceBundle extends ResourceBundle {

  private Locale locale

  public WokoResourceBundle(Locale locale) {
    this.locale = locale
  }

  @Override
  public Enumeration<String> getKeys() {
    return null
  }

  @Override
  protected Object handleGetObject(String fullKey) {
      return getResult(locale, "WokoResources", fullKey)
  }

  // Just returns null if the bundle or the key is not found,
  // instead of throwing an exception.
  private String getResult(Locale loc, String name, String key) {
    String result = null
    ResourceBundle bundle = ResourceBundle.getBundle(name, loc)
    if (bundle != null) {
      try { result = bundle.getString(key) }
      catch (MissingResourceException exc) { }
    }
    return result
  }
}

