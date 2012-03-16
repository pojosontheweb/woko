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

import net.sourceforge.stripes.util.ReflectUtil;
import woko.Woko;
import woko.facets.builtin.RenderPropertyValue;
import woko.facets.builtin.WokoFacets;

import javax.servlet.http.HttpServletRequest;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

public class Util {

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
        if (owningObject==null) {
            return false;
        }
        Class<?> clazz = owningObject.getClass();
        return getPropertyType(clazz, propertyName) != null;
    }
}