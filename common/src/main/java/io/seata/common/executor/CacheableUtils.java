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
 * The interface Cacheable utils.
 *
 * @author wang.liang
 */
public class CacheableUtils {

    /**
     * Get the cache from the obj if Cacheable,
     *
     * @param obj the obj
     * @param key the cache key
     * @param <T> the cache type
     */
    @Nullable
    @SuppressWarnings("all")
    public static <T> T getCache(Object obj, String key) {
        if (obj instanceof Cacheable) {
            return (T)((Cacheable)obj).getCache(key);
        }

        if (obj instanceof Wrapper) {
            Cacheable cacheable = ((Wrapper)obj).unwrap(Cacheable.class);
            if (cacheable != null) {
                return (T)cacheable.getCache(key);
            }
        }

        return null;
    }

    /**
     * Contains cache.
     *
     * @param obj the obj
     * @param key the cache key
     * @return the boolean
     */
    public static boolean containsCacheKey(Object obj, String key) {
        if (obj instanceof Cacheable) {
            return ((Cacheable)obj).containsCacheKey(key);
        }

        if (obj instanceof Wrapper) {
            Cacheable cacheable = ((Wrapper)obj).unwrap(Cacheable.class);
            if (cacheable != null) {
                return cacheable.containsCacheKey(key);
            }
        }

        return false;
    }


    /**
     * Remove cache by key.
     *
     * @param key the cache key
     * @return the removed cache or null
     */
    @Nullable
    public static Object removeCache(Object obj, String key) {
        if (obj instanceof Cacheable) {
            return ((Cacheable)obj).removeCache(key);
        }

        if (obj instanceof Wrapper) {
            Cacheable cacheable = ((Wrapper)obj).unwrap(Cacheable.class);
            if (cacheable != null) {
                return cacheable.removeCache(key);
            }
        }

        return null;
    }

    /**
     * Clean the obj
     *
     * @param obj the obj
     */
    public static void cleanCaches(Object obj) {
        if (obj instanceof Cacheable) {
            ((Cacheable)obj).clean();
            return;
        }

        if (obj instanceof Wrapper) {
            Cacheable cacheable = ((Wrapper)obj).unwrap(Cacheable.class);
            if (cacheable != null) {
                cacheable.clean();
            }
        }
    }
}
