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
package io.seata.config.source;

import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;

import io.seata.common.executor.Wrapper;

/**
 * The interface ConfigurationWrapper.
 *
 * @author wang.liang
 */
public interface ConfigSourceManagerWrapper extends ConfigSourceManager, Wrapper {

    /**
     * Get origin config source manager
     *
     * @return the target config source manager
     */
    @Nonnull
    ConfigSourceManager getOrigin();


    @Override
    default ConfigSource getMainSource() {
        return getOrigin().getMainSource();
    }

    @Override
    default void setMainSource(ConfigSource mainSource) {
        getOrigin().setMainSource(mainSource);
    }

    @Nonnull
    @Override
    default List<ConfigSource> getSources() {
        return getOrigin().getSources();
    }

    @Nonnull
    @Override
    default Map<String, ConfigSource> getSourceMap() {
        return getOrigin().getSourceMap();
    }
}
