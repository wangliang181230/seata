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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.seata.common.Constants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * The type String utils test.
 *
 * @author Otis.z
 * @author Geng Zhang
 */
public class StringUtilsTest {

    /**
     * Test is empty.
     */
    @Test
    public void testIsNullOrEmpty() {
        assertThat(StringUtils.isNullOrEmpty(null)).isTrue();
        assertThat(StringUtils.isNullOrEmpty("abc")).isFalse();
        assertThat(StringUtils.isNullOrEmpty("")).isTrue();
        assertThat(StringUtils.isNullOrEmpty(" ")).isFalse();
    }

    @Test
    public void testInputStream2String() throws IOException {
        assertNull(StringUtils.inputStream2String(null));
        String data = "abc\n"
                + ":\"klsdf\n"
                + "2ks,x:\".,-3sd˚ø≤ø¬≥";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data.getBytes(Constants.DEFAULT_CHARSET));
        assertThat(StringUtils.inputStream2String(inputStream)).isEqualTo(data);
    }

    @Test
    void inputStream2Bytes() {
        assertNull(StringUtils.inputStream2Bytes(null));
        String data = "abc\n"
                + ":\"klsdf\n"
                + "2ks,x:\".,-3sd˚ø≤ø¬≥";
        byte[] bs = data.getBytes(Constants.DEFAULT_CHARSET);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data.getBytes(Constants.DEFAULT_CHARSET));
        assertThat(StringUtils.inputStream2Bytes(inputStream)).isEqualTo(bs);
    }

    @Test
    void testEquals() {
        Assertions.assertTrue(StringUtils.equals("1", "1"));
        Assertions.assertFalse(StringUtils.equals("1", "2"));
        Assertions.assertFalse(StringUtils.equals(null, "1"));
        Assertions.assertFalse(StringUtils.equals("1", null));
        Assertions.assertFalse(StringUtils.equals("", null));
        Assertions.assertFalse(StringUtils.equals(null, ""));
    }

    @Test
    void testEqualsIgnoreCase() {
        Assertions.assertTrue(StringUtils.equalsIgnoreCase("a", "a"));
        Assertions.assertTrue(StringUtils.equalsIgnoreCase("a", "A"));
        Assertions.assertTrue(StringUtils.equalsIgnoreCase("A", "a"));
        Assertions.assertFalse(StringUtils.equalsIgnoreCase("1", "2"));
        Assertions.assertFalse(StringUtils.equalsIgnoreCase(null, "1"));
        Assertions.assertFalse(StringUtils.equalsIgnoreCase("1", null));
        Assertions.assertFalse(StringUtils.equalsIgnoreCase("", null));
        Assertions.assertFalse(StringUtils.equalsIgnoreCase(null, ""));
    }

    @Test
    void testToString() throws StackOverflowError {
        //null
        Assertions.assertEquals("null", StringUtils.toString(null));
        //string
        Assertions.assertEquals("aa", StringUtils.toString("aa"));
        //number
        Assertions.assertEquals("11", StringUtils.toString(11));
        //char
        Assertions.assertEquals("c", StringUtils.toString('c'));
        //boolean
        Assertions.assertEquals("true", StringUtils.toString(true));
        Assertions.assertEquals("false", StringUtils.toString(false));
        //date
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
        Assertions.assertEquals(sdf.format(date), StringUtils.toString(date));
        //list
        ArrayList<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        Assertions.assertEquals("[1,2]", StringUtils.toString(list));
        //map
        Map<String, Object> map = new HashMap<>();
        map.put("aaa", 111);
        map.put("bbb", CycleDependency.A);
        Assertions.assertEquals("{aaa->111,bbb->{s=a}}", StringUtils.toString(map));
        //object
        String str = StringUtils.toString(CycleDependency.B);
        Assertions.assertEquals("{s=b}", str);
    }

    @Test
    void testCycleDependency() throws StackOverflowError {
        StringUtils.toString(CycleDependency.A);
    }

    static class CycleDependency {
        public static final CycleDependency A = new CycleDependency("a");
        public static final CycleDependency B = new CycleDependency("b");

        private String s;

        private CycleDependency(String s) {
            this.s = s;
        }

        @Override
        public String toString() {
            return "{" +
                    "s='" + s + '\'' +
                    '}';
        }
    }
}
