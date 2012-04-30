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

package woko.util;

import net.sourceforge.stripes.controller.StripesFilter;
import net.sourceforge.stripes.localization.LocalizationUtility;
import net.sourceforge.stripes.util.ReflectUtil;
import woko.Woko;
import woko.facets.builtin.RenderPropertyValue;
import woko.facets.builtin.WokoFacets;

import javax.servlet.http.HttpServletRequest;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

public class Util {

    private static final WLogger logger = WLogger.getLogger(Util.class);

    public static void assertArg(String name, Object val) {
        if (val == null) {
            throw new IllegalArgumentException("The argument " + name + " cannot be null");
        }
    }

    public static List<String> getPropertyNames(Object obj, List<String> exclusions) {
        // use stripes reflect utils
        PropertyDescriptor[] descriptors = ReflectUtil.getPropertyDescriptors(obj.getClass());
        List<String> res = new ArrayList<String>();
        for (PropertyDescriptor pd : descriptors) {
            String name = pd.getName();
            if (!exclusions.contains(name)) {
                res.add(name);
            }
        }
        return res;
    }

    public static Object getPropertyValue(Object obj, String propertyName) {
        assertArg("obj", obj);
        try {
            PropertyDescriptor pd = ReflectUtil.getPropertyDescriptor(obj.getClass(), propertyName);
            if (pd != null) {
                Method readMethod = pd.getReadMethod();
                if (readMethod != null) {
                    return readMethod.invoke(obj);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error getting property " + propertyName + " on object " + obj + " ! Make sure your renderProperties facet doesn't return incorrect property names.");
        }
        return null;
    }

    public static String firstCharLowerCase(String s) {
        return Character.toLowerCase(s.charAt(0)) + s.substring(1, s.length());
    }

    public static String computePropertyPath(Stack<String> propChain) {
        StringBuilder sb = new StringBuilder();
        for (Iterator<String> it = propChain.iterator(); it.hasNext(); ) {
            sb.append(it.next());
            if (it.hasNext()) {
                sb.append(".");
            }
        }
        return sb.toString();
    }

    public static Class<?> getPropertyType(Class owningObjectClass, String propertyName) {
        PropertyDescriptor pd = ReflectUtil.getPropertyDescriptor(owningObjectClass, propertyName);
        if (pd == null) {
            return null;
        }
        return pd.getPropertyType();
    }

    private static RenderPropertyValue getRenderFacet(
            String facetName,
            Woko woko,
            HttpServletRequest request,
            Object owningObject,
            String propertyName,
            Object propertyValue,
            boolean throwIfNotFound) {
        RenderPropertyValue renderPropertyValue = (RenderPropertyValue) woko.getFacet(facetName + "_" + propertyName, request, owningObject);
        if (renderPropertyValue == null) {
            Class<?> pClass = propertyValue != null ? propertyValue.getClass() : Util.getPropertyType(owningObject.getClass(), propertyName);
            renderPropertyValue =
                    (RenderPropertyValue) woko.getFacet(facetName, request, propertyValue, pClass, throwIfNotFound);
        } else {
            request.setAttribute(facetName, renderPropertyValue);
        }
        if (renderPropertyValue != null) {
            renderPropertyValue.setPropertyValue(propertyValue);
            renderPropertyValue.setOwningObject(owningObject);
            renderPropertyValue.setPropertyName(propertyName);
        }
        return renderPropertyValue;
    }

    public static RenderPropertyValue getRenderPropValueFacet(Woko woko, HttpServletRequest request, Object owningObject, String propertyName, Object propertyValue) {
        return getRenderFacet(WokoFacets.renderPropertyValue, woko, request, owningObject, propertyName, propertyValue, true);
    }

    public static RenderPropertyValue getRenderPropValueEditFacet(Woko woko, HttpServletRequest request, Object owningObject, String propertyName, Object propertyValue) {
        String fName = WokoFacets.renderPropertyValueEdit;
        RenderPropertyValue renderPropertyValue = getRenderFacet(fName, woko, request, owningObject, propertyName, propertyValue, false);
        if (renderPropertyValue == null) {
            renderPropertyValue = getRenderFacet(WokoFacets.renderPropertyValue, woko, request, owningObject, propertyName, propertyValue, true);
        }
        renderPropertyValue.setPropertyValue(propertyValue);
        renderPropertyValue.setOwningObject(owningObject);
        renderPropertyValue.setPropertyName(propertyName);
        request.setAttribute(fName, renderPropertyValue);
        return renderPropertyValue;
    }

    public static boolean hasProperty(Object owningObject, String propertyName) {
        if (owningObject == null) {
            return false;
        }
        Class<?> clazz = owningObject.getClass();
        return getPropertyType(clazz, propertyName) != null;
    }

    public static Field[] getFields(Class<?> clazz) {
        ArrayList<Field> fields = new ArrayList<Field>();
        while (clazz != null) {
            Field[] fds = clazz.getDeclaredFields();
            fields.addAll(Arrays.asList(fds));
            clazz = clazz.getSuperclass();
        }
        Field[] result = new Field[fields.size()];
        result = fields.toArray(result);
        return result;
    }

    /**
     * Return the field for passed class and field name
     */
    public static Field getField(Class<?> clazz, String name) {
        Field[] fields = getFields(clazz);
        Field f = null;
        for (int i = 0; (i < fields.length && f == null); i++) {
            if (fields[i].getName().equals(name))
                f = fields[i];
        }
        return f;
    }

    public static Type[] getPropertyGenericTypes(Class<?> clazz, String propName) {
        PropertyDescriptor pd = ReflectUtil.getPropertyDescriptor(clazz, propName);
        if (pd==null) {
            throw new IllegalStateException("The property '" + propName + "' of class '" + clazz.getName() + "'" +
                " doesn't exist");
        }

        // use read method in order to grab the generic return type
        Method meth = pd.getReadMethod();
        if (meth==null) {
            throw new IllegalStateException("The property '" + propName + "' of class '" + clazz.getName() + "'" +
                " has no read method");
        }

        Type retType = meth.getGenericReturnType();
        if (retType instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType)retType;
            return pt.getActualTypeArguments();
        }
        return new Type[0];
    }

    public static String getMessage(Locale locale, String key) {
        ResourceBundle b =  StripesFilter.getConfiguration().getLocalizationBundleFactory().getFormFieldBundle(locale);
        try {
            return b.getString(key);
        } catch(Exception e) {
            logger.warn("Key '" + key + "' not found in bundle(s) for locale '" + locale + "'");
            return "???" + key + "???";
        }
    }

}