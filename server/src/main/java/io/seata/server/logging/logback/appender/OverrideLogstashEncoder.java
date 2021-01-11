/*
 *  Copyright 1999-2019 Seata.io Group.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package io.seata.server.logging.logback.appender;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import ch.qos.logback.classic.spi.ILoggingEvent;
import net.logstash.logback.LogstashFormatter;
import net.logstash.logback.composite.CompositeJsonFormatter;
import net.logstash.logback.composite.JsonProvider;
import net.logstash.logback.composite.JsonProviders;
import net.logstash.logback.composite.LogstashVersionJsonProvider;
import net.logstash.logback.encoder.LogstashEncoder;

/**
 * @author wang.liang
 */
public class OverrideLogstashEncoder extends LogstashEncoder {

    @Override
    protected CompositeJsonFormatter<ILoggingEvent> createFormatter() {
        LogstashFormatter formatter = new LogstashFormatter(this);

        //region There are some useless data that are not sent to logstash, Do exclude providers.

        // The excludedProviderClasses
        Set<Class<?>> excludedProviderClasses = new HashSet<>();
        // The `@version` provider
        excludedProviderClasses.add(LogstashVersionJsonProvider.class);
        // Do exclude providers
        this.doExcludeProviders(formatter, excludedProviderClasses);

        //endregion

        return formatter;
    }

    /**
     * Do exclude providers
     *
     * @param formatter               the formatter
     * @param excludedProviderClasses the excluded provider classes
     */
    private void doExcludeProviders(LogstashFormatter formatter, Set<Class<?>> excludedProviderClasses) {
        JsonProviders<?> providers = formatter.getProviders();
        for (JsonProvider<?> provider : new ArrayList<>(providers.getProviders())) {
            if (excludedProviderClasses.contains(provider.getClass())) {
                providers.removeProvider((JsonProvider) provider);
            }
        }
    }
}