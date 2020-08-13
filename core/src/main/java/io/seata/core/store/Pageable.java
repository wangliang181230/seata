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
package io.seata.core.store;

/**
 * @author wang.liang
 */
public interface Pageable {

    /**
     * Gets page index
     *
     * @return the page index
     */
    int getPageIndex();

    /**
     * Sets page index
     *
     * @param pageIndex the page index
     */
    void setPageIndex(int pageIndex);

    /**
     * Gets page size
     *
     * @return the page size
     */
    int getPageSize();

    /**
     * Sets page size
     *
     * @param pageSize the page size
     */
    void setPageSize(int pageSize);
}