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
package io.seata.common.util;

import io.seata.common.XID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The type Class utils test.
 *
 * @author wang.liang
 */
public class ClassUtilsTest {

    @Test
    public void test_hasFastjson() {
        assertThat(ClassUtils.hasFastjson()).isFalse();
    }

    @Test
    public void test_hasJackson() {
        assertThat(ClassUtils.hasJackson()).isFalse();
    }

    @Test
    public void test_getDefaultClassLoader() {
        assertThat(ClassUtils.getDefaultClassLoader()).isNotNull();
    }

    @Test
    public void test_forName() throws ClassNotFoundException {
        Assertions.assertThrows(ClassNotFoundException.class, () -> {
            ClassUtils.forName("com.aaa.bbb.Aaaa", null);
        });

        Class<?> clazz = ClassUtils.forName("io.seata.common.XID", this.getClass().getClassLoader());
        assertThat(clazz).isEqualTo(XID.class);
    }

    @Test
    public void test_isPresent() {
        assertThat(ClassUtils.isPresent("com.aaa.bbb.Aaaa", this.getClass().getClassLoader())).isFalse();
        assertThat(ClassUtils.isPresent("io.seata.common.XID")).isTrue();
    }
}