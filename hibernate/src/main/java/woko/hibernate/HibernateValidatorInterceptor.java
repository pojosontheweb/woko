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

package woko.hibernate;

import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.config.ConfigurableComponent;
import net.sourceforge.stripes.config.Configuration;
import net.sourceforge.stripes.controller.ExecutionContext;
import net.sourceforge.stripes.controller.Intercepts;
import net.sourceforge.stripes.controller.LifecycleStage;
import woko.util.WLogger;

import javax.validation.MessageInterpolator;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Locale;

@Intercepts({LifecycleStage.RequestInit, LifecycleStage.RequestComplete})
public class HibernateValidatorInterceptor implements net.sourceforge.stripes.controller.Interceptor, ConfigurableComponent {

    private static final WLogger log = WLogger.getLogger(HibernateValidatorInterceptor.class);

    private static ThreadLocal<Locale> THREAD_LOCALE = new ThreadLocal<Locale>();

    private static Validator VALIDATOR = null;

    @Override
    public void init(Configuration configuration) throws Exception {
        javax.validation.Configuration config = Validation.byDefaultProvider().configure();
        MessageInterpolator mi = new ClientLocaleMessageInterpolator(config.getDefaultMessageInterpolator());
        config = config.messageInterpolator( mi );
        VALIDATOR = config.buildValidatorFactory().getValidator();
    }

    public static Validator getValidator() {
        return VALIDATOR;
    }

    public static Locale getThreadLocale() {
        return THREAD_LOCALE.get();
    }

    public Resolution intercept(ExecutionContext context) throws Exception {
        LifecycleStage stage = context.getLifecycleStage();
        if (stage == LifecycleStage.RequestInit) {
            Locale locale = context.getActionBeanContext().getLocale();
            if (locale==null) {
                locale = Locale.getDefault();
                log.warn("Unable to obtain locale from ActionBeanContext, using default locale : " + locale);
            }
            THREAD_LOCALE.set(locale);

        } else if (stage.equals(LifecycleStage.RequestComplete)) {
            THREAD_LOCALE.remove();
        }
        return context.proceed();
    }

}
