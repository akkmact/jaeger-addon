/*
 * Copyright © 2013-2021, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
/*
 * Creation : 5 Jul 2021
 */
package org.seedstack.jaeger.fixture;

import io.jaegertracing.Configuration;

public class CustomJaeger {

    public Configuration defaultConfiguration() {

        Configuration config = new Configuration("CustomService");

        return config;

    }

}
