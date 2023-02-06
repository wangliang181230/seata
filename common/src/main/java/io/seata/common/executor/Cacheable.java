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
package io.seata.common.executor;

import javax.annotation.Nullable;

/**
 * The interface Cacheable.
 *
 * @author wang.liang
 */
public interface Cacheable extends Cleanable {

    /**
     * Get cache by key
     *
     * @param key the cache key
     * @return the cache or null
     */
    @Nullable
    Object getCache(String key);

    /**
     * Contains cache key.
     *
     * @param key the cache key
     * @return the boolean
     */
    boolean containsCacheKey(String key);

    /**
     * Remove cache by key.
     *
     * @param key the cache key
     * @return the removed cache or null
     */
    @Nullable
    Object removeCache(String key);
}

