/*
 * Copyright © 2013-2021, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.jaeger.internal;

import org.seedstack.jaeger.JaegerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;

/**
 * Module for Jaeger Tracer.
 */
public class JaegerModule extends AbstractModule {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(JaegerModule.class);

    /** The jaeger config. */
    private JaegerConfig jaegerConfig;

    /**
     * @param jaegerConfig
     */
    JaegerModule(JaegerConfig jaegerConfig) {

        this.jaegerConfig = jaegerConfig;

    }

    /**
     * {@inheritDoc}
     * 
     * @see com.google.inject.AbstractModule#configure()
     */
    @Override
    protected void configure() {
        LOGGER.info("Bind the TypeListener for Tracer");
        bindListener(Matchers.any(), new TracerTypeListener(jaegerConfig));

    }

}
