/*
 * Copyright © 2013-2021, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.jaeger.internal;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.seedstack.jaeger.JaegerConfig;
import org.seedstack.jaeger.ServiceName;
import org.seedstack.seed.SeedException;
import org.seedstack.shed.reflect.ReflectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.inject.MembersInjector;

import io.nuun.kernel.api.plugin.PluginException;
import io.opentracing.Tracer;

/**
 * MemberInjector for Jaeger Tracer
 */
class TracerMemberInjector<T> implements MembersInjector<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(TracerMemberInjector.class);
    private final Field field;
    private final JaegerConfig jaegerConfig;
    private final ServiceName annotation;
    Tracer tracer;
    Map<String, Tracer> tracersMap = new HashMap<>();

    TracerMemberInjector(Field field, JaegerConfig jaegerConfig) {
        this.field = field;
        this.jaegerConfig = jaegerConfig;
        this.annotation = field.getAnnotation(ServiceName.class);

    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.inject.MembersInjector#injectMembers(java.lang.Object)
     */
    @Override
    public void injectMembers(T instance) {

        // Pre verification
        if (Strings.isNullOrEmpty(annotation.value())) {
            LOGGER.error("Value for annotation {} on field {} can not be empty.", annotation.annotationType(), field.toGenericString());
            throw new PluginException("Value for annotation %s on field %s can not be empty.", annotation.annotationType(), field.toGenericString());
        }

        try {

            String serviceName = annotation.value();
            if (tracersMap.containsKey(serviceName)) {

                tracer = tracersMap.get(serviceName);
            }

            else {
                tracer = JaegerInternal.getTracer(serviceName, jaegerConfig);
                tracersMap.put(serviceName, tracer);
            }

            ReflectUtils.setValue(ReflectUtils.makeAccessible(field), instance, tracer);
        } catch (RuntimeException e) {
            throw SeedException.wrap(e, JaegerErrorCode.ERROR_LOADING);
        }
    }

}
