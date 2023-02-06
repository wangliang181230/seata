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
package io.seata.config;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import io.seata.common.executor.Cacheable;
import io.seata.common.executor.Cleanable;
import io.seata.common.util.CollectionUtils;
import io.seata.common.util.ConvertUtils;
import io.seata.common.util.ObjectUtils;
import io.seata.common.util.StringUtils;
import io.seata.config.source.ConfigSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The type Cacheable configuration.
 *
 * @author wang.liang
 */
public class CacheableConfiguration implements ConfigurationWrapper, Cacheable {

    private static final Logger LOGGER = LoggerFactory.getLogger(CacheableConfiguration.class);

    public static final String NAME_PREFIX = "cacheable:";


    /**
     * The origin configuration.
     */
    private final Configuration origin;

    /**
     * The cache map.
     */
    private final Map<String, ConfigCache> configCacheMap = new ConcurrentHashMap<>();


    private CacheableConfiguration(Configuration origin) {
        Objects.requireNonNull(origin, "The 'origin' configuration must not be null.");
        this.origin = origin;
    }


    /**
     * Wrap the origin configuration.
     *
     * @param origin the origin configuration
     * @return the cacheable configuration
     */
    public static CacheableConfiguration wrap(Configuration origin) {
        if (origin instanceof CacheableConfiguration) {
            return (CacheableConfiguration)origin;
        }

        return new CacheableConfiguration(origin);
    }


    //region # Override ConfigurationWrapper

    @Nonnull
    @Override
    public Configuration getOrigin() {
        return origin;
    }

    //region # Override ConfigurationWrapper


    //region # Override Configuration

    @Nonnull
    @Override
    public String getNamePrefix() {
        return NAME_PREFIX;
    }

    /**
     * Override to use cache
     */
    @Override
    public <T> T getConfig(String dataId, T defaultValue, long timeoutMills, Class<T> dataType) {
        if (StringUtils.isBlank(dataId)) {
            return null;
        }

        // Get config from cache
        ConfigCache cache = CollectionUtils.computeIfAbsent(configCacheMap, dataId, key -> {
            // Get config from sources
            ConfigInfo config = origin.getConfigFromSources(dataId, timeoutMills);
            // Wrap config, also when config is null or blank
            return ConfigCache.fromConfigInfo(dataId, config, dataType);
        });

        T value = cache.getConfig();
        if (value != null) {
            // If the same config is obtained from cache with different types multiple times,
            // The dataType will be inconsistent with the class of the value.
            if (!dataType.isAssignableFrom(value.getClass())) {
                LOGGER.warn("The dataType '{}' used to get config '{}' is different from type '{}' in the cache." +
                                " Recommended to use the same type multiple times.",
                        dataType.getName(), dataId, value.getClass().getName());

                // Convert to the targetType
                value = ConvertUtils.convert(value, dataType);
            }

            if (ObjectUtils.isNotBlank(value)) {
                return value;
            }
        }

        // Return default value
        if (defaultValue != null) {
            LOGGER.debug("Config '{}' not found, returned defaultValue '{}' of type [{}] from parameter by configuration '{}'.",
                    dataId, defaultValue, defaultValue.getClass().getName(), this.getName());

            return defaultValue;
        }

        // May be null or blank.
        return value;
    }

    //region ## Override ConfigSourceManager

    @Override
    public void afterAddingSource(ConfigSource newSource) {
        origin.afterAddingSource(newSource);

        // clean cache
        this.clean();
    }

    //endregion ## Override ConfigSourceManager

    //endregion # Override Configuration


    //region # Override Cacheable, Cleanable

    @Nullable
    @Override
    public ConfigCache getCache(String dataId) {
        return this.configCacheMap.get(dataId);
    }

    @Nullable
    @Override
    public ConfigCache removeCache(String dataId) {
        return this.configCacheMap.remove(dataId);
    }

    @Override
    public boolean containsCacheKey(String dataId) {
        return this.configCacheMap.containsKey(dataId);
    }

    @Override
    public void clean() {
        if (origin instanceof Cleanable) {
            ((Cleanable)origin).clean();
        }
        this.cleanCaches();
    }

    public void cleanCaches() {
        this.configCacheMap.clear();
    }

    @Nullable
    public Object getCacheValue(String dataId) {
        ConfigCache configCache = this.getCache(dataId);
        return configCache != null ? configCache.getValue() : null;
    }

    //endregion # Override Cacheable, Cleanable
}
