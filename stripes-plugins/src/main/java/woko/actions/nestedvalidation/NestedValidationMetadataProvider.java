package woko.actions.nestedvalidation;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.controller.ExecutionContext;
import net.sourceforge.stripes.controller.Interceptor;
import net.sourceforge.stripes.controller.Intercepts;
import net.sourceforge.stripes.controller.LifecycleStage;
import net.sourceforge.stripes.exception.StripesRuntimeException;
import net.sourceforge.stripes.util.Log;
import net.sourceforge.stripes.util.ReflectUtil;
import net.sourceforge.stripes.validation.DefaultValidationMetadataProvider;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidateNestedProperties;
import net.sourceforge.stripes.validation.ValidationMetadata;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

@Intercepts({LifecycleStage.BindingAndValidation})
public class NestedValidationMetadataProvider extends DefaultValidationMetadataProvider implements Interceptor {

  private static final Log logger = Log.getInstance(NestedValidationMetadataProvider.class);
  private static ThreadLocal<ActionBean> actionBeanHolder = new ThreadLocal<ActionBean>();

  private volatile int threadDelta = 0;

  @Override
  public Map<String, ValidationMetadata> getValidationMetadata(Class<?> aClass) {
    Map<String, ValidationMetadata> original = super.getValidationMetadata(aClass);
    Map<String, ValidationMetadata> all = new TreeMap<String, ValidationMetadata>();
    all.putAll(original);
    ActionBean actionBean = actionBeanHolder.get();
    if (actionBean == null) {
      logger.debug("Unable to find ActionBean for current thread, using static validation only");
    } else {
      logger.debug("Computing dynamic validation metadata for " + actionBean);
      Map<String, ValidationMetadata> dynamicMetadata = computeBeanMetadata(actionBean);
      // append "facet" prefix to the property name
      for (String key : dynamicMetadata.keySet()) {
        ValidationMetadata validationMetadata = dynamicMetadata.get(key);
        all.put(key, validationMetadata);
        logger.debug("  * added validation metadata : " + key + "=" + validationMetadata);
      }
    }
    return all;
  }

  protected Map<String, ValidationMetadata> computeBeanMetadata(Object object) {
    Map<String, ValidationMetadata> meta = new HashMap<String, ValidationMetadata>();
    Set<String> seen = new HashSet<String>();
    try {
      for (Class<?> clazz = object.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
        List<PropertyDescriptor> pds = new ArrayList<PropertyDescriptor>(
            Arrays.asList(ReflectUtil.getPropertyDescriptors(clazz)));

        // Also look at public fields
        Field[] publicFields = clazz.getFields();
        for (Field field : publicFields) {
          pds.add(new PropertyDescriptor(field.getName(), null, null));
        }

        for (PropertyDescriptor pd : pds) {
          String propertyName = pd.getName();
          Method accessor = pd.getReadMethod();
          Method mutator = pd.getWriteMethod();
          Field field = null;
          try {
            field = clazz.getDeclaredField(propertyName);
          } catch (NoSuchFieldException e) {
          }

          boolean onAccessor = accessor != null
              && Modifier.isPublic(accessor.getModifiers())
              && accessor.getDeclaringClass().equals(clazz)
              && accessor.isAnnotationPresent(ValidateNestedProperties.class);
          boolean onMutator = mutator != null
              && Modifier.isPublic(mutator.getModifiers())
              && mutator.getDeclaringClass().equals(clazz)
              && mutator.isAnnotationPresent(ValidateNestedProperties.class);
          boolean onField = field != null
              && !Modifier.isStatic(field.getModifiers())
              && field.getDeclaringClass().equals(clazz)
              && field.isAnnotationPresent(ValidateNestedProperties.class);

          // I don't think George Boole would like this ...
          int count = 0;
          if (onAccessor) ++count;
          if (onMutator) ++count;
          if (onField) ++count;

          // must be 0 or 1
          if (count > 1) {
            StringBuilder buf = new StringBuilder(
                "There are conflicting @ValidateNestedProperties annotations in ")
                .append(clazz)
                .append(". The following elements are improperly annotated for the '")
                .append(propertyName)
                .append("' property:\n");
            if (onAccessor) {
              buf.append("--> Getter method ").append(accessor.getName()).append(
                  " is annotated with @ValidateNestedProperties\n");
            }
            if (onMutator) {
              buf.append("--> Setter method ").append(mutator.getName()).append(
                  " is annotated with @ValidateNestedProperties\n");
            }
            if (onField) {
              buf.append("--> Field ").append(field.getName()).append(
                  " is annotated with @ValidateNestedProperties\n");
            }
            throw new StripesRuntimeException(buf.toString());
          }

          // after the conflict check, stop processing fields we've already seen
          if (seen.contains(propertyName))
            continue;

          // get the @Validate and/or @ValidateNestedProperties
          ValidateNestedProperties nested;
          if (onAccessor) {
            nested = accessor.getAnnotation(ValidateNestedProperties.class);
            seen.add(propertyName);
          } else if (onMutator) {
            nested = mutator.getAnnotation(ValidateNestedProperties.class);
            seen.add(propertyName);
          } else if (onField) {
            nested = field.getAnnotation(ValidateNestedProperties.class);
            seen.add(propertyName);
          } else {
            nested = null;
          }

          // add all sub-properties referenced in @ValidateNestedProperties
          if (nested != null) {
            Validate[] validates = nested.value();
            if (validates != null && validates.length==0) {
              // found empty @ValidateNestedProperties : grab the first level metadata using
              // default metadata provider, and recurse...
              // we use the run time type if available
              Class<?> propertyType = ReflectUtil.resolvePropertyType(pd);
              if (accessor!=null) {
                Object value = accessor.invoke(object);
                if (value!=null) {
                  propertyType = value.getClass();
                }
              }
              Map<String,ValidationMetadata> firstLevel = super.getValidationMetadata(propertyType);
              for (String s : firstLevel.keySet()) {
                ValidationMetadata vm = firstLevel.get(s);
                meta.put(propertyName + "." + s, vm);
              }
            }
          }
        }
      }
    } catch (RuntimeException e) {
      logger.error(e, "Failure checking @Validate annotations ", getClass().getName());
      throw e;
    } catch (Exception e) {
      logger.error(e, "Failure checking @Validate annotations ", getClass().getName());
      StripesRuntimeException sre = new StripesRuntimeException(e.getMessage(), e);
      sre.setStackTrace(e.getStackTrace());
      throw sre;
    }

    // Print out a pretty debug message showing what validations got configured
    StringBuilder builder = new StringBuilder(128);
    for (Map.Entry<String, ValidationMetadata> entry : meta.entrySet()) {
      if (builder.length() > 0) {
        builder.append(", ");
      }
      builder.append(entry.getKey());
      builder.append("->");
      builder.append(entry.getValue());
    }
    logger.debug("Loaded @ValidateNestedProperties validations for ", object, ": ", builder
        .length() > 0 ? builder : "<none>");

    return Collections.unmodifiableMap(meta);
  }

  void setCurrentAction(ActionBean actionBean) {
    actionBeanHolder.set(actionBean);
  }

  void clearCurrentAction() {
    actionBeanHolder.remove();
  }

  public Resolution intercept(ExecutionContext executionContext) throws Exception {
    ActionBean actionBean = executionContext.getActionBean();
    // bind the action to thread local before binding and validation
    try {
      setCurrentAction(actionBean);
      threadDelta++;
      logger.debug("Assigning ActionBean to local thread : ", actionBean, ", request=",
          executionContext.getActionBeanContext().getRequest(), ", delta=", threadDelta);
      return executionContext.proceed();
    } finally {
      clearCurrentAction();
      threadDelta--;
      logger.debug("Removing ActionBean from local thread : " + actionBean + ", request=" +
          executionContext.getActionBeanContext().getRequest() + ", delta=" + threadDelta);
    }
  }

}
