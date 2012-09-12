package woko.hibernate;

import javax.validation.MessageInterpolator;
import java.util.Locale;

public class ClientLocaleMessageInterpolator implements MessageInterpolator {
  private final MessageInterpolator delegate;
 
  public ClientLocaleMessageInterpolator(MessageInterpolator delegate) {
    this.delegate = delegate;
  }
 
  @Override
  public String interpolate(String message, Context context) {
    return this.interpolate(message, context, HibernateValidatorInterceptor.getThreadLocale());
  }
 
  @Override
  public String interpolate(String message, Context context, Locale locale)
  {
    return delegate.interpolate(message, context, locale);
  }
}