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

import javax.annotation.Nonnull;

import io.seata.config.source.ConfigSourceManagerWrapper;

/**
 * The interface ConfigurationWrapper.
 *
 * @author wang.liang
 */
public interface ConfigurationWrapper extends Configuration, ConfigSourceManagerWrapper {

    /**
     * Get origin configuration
     *
     * @return the origin configuration
     */
    @Nonnull
    @Override
    Configuration getOrigin();

    /**
     * Get name prefix.
     *
     * @return the name prefix
     */
    String getNamePrefix();


    @Nonnull
    @Override
    default String getName() {
        return getNamePrefix() + getOrigin().getName();
    }
}
