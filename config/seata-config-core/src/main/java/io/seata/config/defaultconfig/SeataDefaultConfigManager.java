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
package io.seata.config.defaultconfig;

import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;

import io.seata.common.executor.Initialize;
import io.seata.common.loader.EnhancedServiceLoader;
import io.seata.config.AbstractConfiguration;
import io.seata.config.CacheableConfiguration;
import io.seata.config.Configuration;
import io.seata.config.ConfigurationWrapper;

/**
 * The type Seata default config manager.
 *
 * @author wang.liang
 */
public class SeataDefaultConfigManager extends AbstractConfiguration
        implements DefaultConfigManager, ConfigurationWrapper, Initialize {

    private static final String NAME_PREFIX = "seata:";


    @Nonnull
    private final CacheableConfiguration cacheable;


    public SeataDefaultConfigManager(CacheableConfiguration cacheable) {
        Objects.requireNonNull(cacheable, "The 'cacheable' configuration must not be null.");
        this.cacheable = cacheable;
    }


    @Nonnull
    @Override
    public Configuration getOrigin() {
        return cacheable;
    }

    @Override
    public String getNamePrefix() {
        return NAME_PREFIX;
    }

    /**
     * Override for load the DefaultConfigSource list in this method,
     * not the ConfigSource list in the method {@link super#loadSources()}.
     */
    @Override
    protected void loadSources() {
        // Avoid print logs repeatedly.
        super.disablePrintGetSuccessLog();

        // Load the DefaultConfigSourceProvider, and provide some DefaultConfigSource.
        List<DefaultConfigSourceProvider> providers = EnhancedServiceLoader.loadAll(DefaultConfigSourceProvider.class);
        for (DefaultConfigSourceProvider provider : providers) {
            provider.provide(this);
        }
    }
}
