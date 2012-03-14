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
      Map<String, ValidationMetadata> dynamicMetadata = computeBeanMetadata(actionBean, actionBean.getClass(), null);
      // append "facet" prefix to the property name
      for (String key : dynamicMetadata.keySet()) {
        ValidationMetadata validationMetadata = dynamicMetadata.get(key);
        all.put(key, validationMetadata);
        logger.debug("  * added validation metadata : " + key + "=" + validationMetadata);
      }
    }
    return all;
  }

  protected Map<String, ValidationMetadata> computeBeanMetadata(Object object, Class<?> objectClass, List<String> propertyPath) {
    List<String> pp = propertyPath;
    if (pp==null) {
      pp = new ArrayList<String>();
    }
    Map<String, ValidationMetadata> meta = new HashMap<String, ValidationMetadata>();
    Set<String> seen = new HashSet<String>();
    try {
      Class<?> initialClass = object!=null ? object.getClass() : objectClass;
      for (Class<?> clazz = initialClass; clazz != null; clazz = clazz.getSuperclass()) {
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
              StringBuilder propertyFullPath = new StringBuilder();
              boolean hasPath = false;
              for (Iterator<String> it = pp.iterator() ; it.hasNext() ; ) {
                String pathElem = it.next();
                propertyFullPath.append(pathElem);
                if (it.hasNext()) {
                  propertyFullPath.append(".");
                }
                hasPath = true;
              }
              String pfp = propertyFullPath.toString();
              if (hasPath) {
                pfp = pfp + "." + propertyName;
              } else {
                pfp = propertyName;
              }

              // found empty @ValidateNestedProperties : grab the first level metadata using
              // default metadata provider, and recurse...
              // we use the run time type if available
              Class<?> propertyType = ReflectUtil.resolvePropertyType(pd);
              Object propertyValue = null;
              if (object!=null && accessor!=null) {
                propertyValue = accessor.invoke(object);
                if (propertyValue!=null) {
                  propertyType = propertyValue.getClass();
                }
              }
              // grab first level metadata
              Map<String,ValidationMetadata> firstLevel = super.getValidationMetadata(propertyType);
              for (String s : firstLevel.keySet()) {
                ValidationMetadata vm = firstLevel.get(s);
                meta.put(pfp + "." + s, vm);
              }
              // recurse
              List<String> newPropertyPath = new ArrayList<String>(pp);
              newPropertyPath.add(propertyName);
              Map<String,ValidationMetadata> childMetadata = computeBeanMetadata(propertyValue, propertyType, newPropertyPath);
              meta.putAll(childMetadata);
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
