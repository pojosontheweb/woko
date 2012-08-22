/*
 * Copyright 2001-2012 Remi Vankeisbelck
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

package woko.actions;

import java.lang.reflect.Type;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.StrictBinding;
import net.sourceforge.stripes.controller.ParameterName;
import net.sourceforge.stripes.controller.StripesFilter;
import net.sourceforge.stripes.exception.StripesRuntimeException;
import net.sourceforge.stripes.util.Log;
import net.sourceforge.stripes.util.bean.NodeEvaluation;
import net.sourceforge.stripes.util.bean.PropertyExpressionEvaluation;
import net.sourceforge.stripes.validation.ValidationMetadataProvider;
import woko.Woko;
import woko.facets.ResolutionFacet;
import woko.facets.WokoFacetContext;

import javax.servlet.http.HttpServletRequest;

@StrictBinding(defaultPolicy = StrictBinding.Policy.ALLOW)
public class WokoFacetBindingPolicyManager {

    /** The regular expression that a property name must match */
    private static final String PROPERTY_REGEX = "\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*";

    /** The compiled form of {@link #PROPERTY_REGEX} */
    private static final Pattern PROPERTY_PATTERN = Pattern.compile(PROPERTY_REGEX);

    /** Log */
    private static final Log log = Log.getInstance(WokoFacetBindingPolicyManager.class);

    /** Cached instances */
    private static final Map<Class<?>, WokoFacetBindingPolicyManager> instances = new HashMap<Class<?>, WokoFacetBindingPolicyManager>();


    public static WokoFacetBindingPolicyManager getInstance(Class<?> facetClass) {
        ValidationMetadataProvider vmp = StripesFilter.getConfiguration().getValidationMetadataProvider();
        return getInstance(facetClass, vmp);
    }

    public static WokoFacetBindingPolicyManager getInstance(Class<?> facetClass, ValidationMetadataProvider vmp) {
        if (instances.containsKey(facetClass))
            return instances.get(facetClass);

        WokoFacetBindingPolicyManager instance = new WokoFacetBindingPolicyManager(facetClass, vmp);
        instances.put(facetClass, instance);
        return instance;
    }

    /** The default policy to honor, in case of conflicts */
    private StrictBinding.Policy defaultPolicy;

    /** The regular expression that allowed properties must match */
    private Pattern allowPattern;

    /** The regular expression that denied properties must match */
    private Pattern denyPattern;

    /** The regular expression that matches properties with {@literal @Validate} */
    private Pattern validatePattern;

    private ValidationMetadataProvider vmp = null;

    protected WokoFacetBindingPolicyManager(Class<?> facetClass, ValidationMetadataProvider vmp) {
        this.vmp = vmp;
        try {
            log.debug("Creating ", getClass().getName(), " for ", facetClass,
                    " with default policy ", defaultPolicy);

            // process the annotation
            StrictBinding annotation = getAnnotation(facetClass);
            if (annotation != null) {
                // set default policy
                this.defaultPolicy = annotation.defaultPolicy();

                // construct the allow pattern
                this.allowPattern = globToPattern(annotation.allow());

                // construct the deny pattern
                this.denyPattern = globToPattern(annotation.deny());

                // construct the validated properties pattern
                this.validatePattern = globToPattern(getValidatedProperties(WokoActionBean.class));
            }
        }
        catch (Exception e) {
            log.error(e, "%%% Failure instantiating ", getClass().getName());
            StripesRuntimeException sre = new StripesRuntimeException(e.getMessage(), e);
            sre.setStackTrace(e.getStackTrace());
            throw sre;
        }
    }

    public static final Collection<Class<?>> UNBINDABLE_CLASSES = makeUnbindableClasses();

    private static Collection<Class<?>> makeUnbindableClasses() {
        HashSet<Class<?>> res = new HashSet<Class<?>>();
        res.add(ActionBeanContext.class);
        res.add(HttpServletRequest.class);
        res.add(Woko.class);
        res.add(WokoFacetContext.class);
        return Collections.unmodifiableCollection(res);
    }

    /**
     * Indicates if binding is allowed for the given expression.
     *
     * @param eval a property expression that has been evaluated against an {@link net.sourceforge.stripes.action.ActionBean}
     * @return true if binding is allowed; false if not
     */
    public boolean isBindingAllowed(PropertyExpressionEvaluation eval) {
        NodeEvaluation e = eval.getRootNode();
        // can't bind to the unbindable classes : there should be
        // no unbindable class on the path
        while (e!=null) {
            Type valueType = e.getValueType();
            if (valueType instanceof Class<?>) {
                Class<?> t = (Class<?>)valueType;
                for (Class<?> unbindableClass : UNBINDABLE_CLASSES) {
                    if (unbindableClass.isAssignableFrom(t)) {
                        return false;
                    }
                }
            }
            e = e.getNext();
        }

        // check parameter name against access lists
        String paramName = new ParameterName(eval.getExpression().getSource()).getStrippedName();
        return isBindingAllowed(paramName);
    }

    public boolean isBindingAllowed(String paramName) {
        boolean deny = denyPattern != null && denyPattern.matcher(paramName).matches();
        boolean allow = (allowPattern != null && allowPattern.matcher(paramName).matches())
                || (validatePattern != null && validatePattern.matcher(paramName).matches());

        /*
         * if path appears on neither or both lists ( i.e. !(allow ^ deny) ) and default policy is
         * to deny access, then fail
         */
        if (defaultPolicy == StrictBinding.Policy.DENY && !(allow ^ deny))
            return false;

        /*
         * regardless of default policy, if it's in the deny list but not in the allow list, then
         * fail
         */
        if (!allow && deny)
            return false;

        // any other conditions pass the test
        return true;

    }

    /**
     * Get the {@link StrictBinding} annotation for a class, checking all its superclasses if
     * necessary. If no annotation is found, then one will be returned whose default policy is to
     * allow binding to all properties.
     *
     * @param beanType the class to get the {@link StrictBinding} annotation for
     * @return An annotation. This method never returns null.
     */
    protected StrictBinding getAnnotation(Class<?> beanType) {
        StrictBinding annotation;
        do {
            annotation = beanType.getAnnotation(StrictBinding.class);
        } while (annotation == null && (beanType = beanType.getSuperclass()) != null);
        if (annotation == null) {
            annotation = getClass().getAnnotation(StrictBinding.class);
        }
        return annotation;
    }

    protected String[] getValidatedProperties(Class<?> beanClass) {
        Set<String> properties = vmp!=null ? vmp.getValidationMetadata(beanClass).keySet() : Collections.<String>emptySet();
        return new ArrayList<String>(properties).toArray(new String[properties.size()]);
    }

    /**
     * Get the default policy.
     *
     * @return the policy
     */
    public StrictBinding.Policy getDefaultPolicy() {
        return defaultPolicy;
    }

    /**
     * Converts a glob to a regex {@link Pattern}.
     *
     * @param globArray an array of property name globs, each of which may be a comma separated list
     *            of globs
     * @return the pattern
     */
    protected Pattern globToPattern(String... globArray) {
        if (globArray == null || globArray.length == 0)
            return null;

        // things are much easier if we convert to a single list
        List<String> globs = new ArrayList<String>();
        for (String glob : globArray) {
            String[] subs = glob.split("(\\s*,\\s*)+");
            for (String sub : subs) {
                globs.add(sub);
            }
        }

        List<String> subs = new ArrayList<String>();
        StringBuilder buf = new StringBuilder();
        for (String glob : globs) {
            buf.setLength(0);
            String[] properties = glob.split("\\.");
            for (int i = 0; i < properties.length; i++) {
                String property = properties[i];
                if ("*".equals(property)) {
                    buf.append(PROPERTY_REGEX);
                }
                else if ("**".equals(property)) {
                    buf.append(PROPERTY_REGEX).append("(\\.").append(PROPERTY_REGEX).append(")*");
                }
                else if (property.length() > 0) {
                    Matcher matcher = PROPERTY_PATTERN.matcher(property);
                    if (matcher.matches()) {
                        buf.append(property);
                    }
                    else {
                        log.warn("Invalid property name: " + property);
                        return null;
                    }
                }

                // add a literal dot after all but the last
                if (i < properties.length - 1)
                    buf.append("\\.");
            }

            // add to the list of subs
            if (buf.length() != 0)
                subs.add(buf.toString());
        }

        // join subs together with pipes and compile
        buf.setLength(0);
        for (String sub : subs) {
            buf.append(sub).append('|');
        }
        if (buf.length() > 0)
            buf.setLength(buf.length() - 1);
        log.debug("Translated globs ", Arrays.toString(globArray), " to regex ", buf);

        // return null if pattern is empty
        if (buf.length() == 0)
            return null;
        else
            return Pattern.compile(buf.toString());
    }
}

