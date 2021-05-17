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
package io.seata.common.exception;

import java.sql.SQLException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The test for {@link FrameworkException}
 *
 * @author Otis.z
 */
class FrameworkExceptionTest {

    private Message message = new Message();

    /**
     * Test get errcode.
     */
    @Test
    void testGetErrcode() {
        FrameworkException throwable = Assertions.assertThrows(FrameworkException.class, () -> {
            message.print4();
        });
        assertThat(throwable).hasMessage(FrameworkErrorCode.UnknownAppError.getErrMessage());
        assertThat(throwable.getErrcode()).isEqualTo(FrameworkErrorCode.UnknownAppError);
    }

    /**
     * Test nested exception.
     */
    @Test
    void testNestedException() {
        Throwable throwable = Assertions.assertThrows(FrameworkException.class, () -> {
            message.print();
        });
        assertThat(throwable).hasMessage("");

        FrameworkException ex = new FrameworkException();
        FrameworkException ex2 = FrameworkException.nestedException(ex);
        Assertions.assertSame(ex, ex2);
    }

    /**
     * Test nested exception 1.
     */
    @Test
    void testNestedException1() {
        Throwable throwable = Assertions.assertThrows(FrameworkException.class, () -> {
            message.print1();
        });
        assertThat(throwable).hasMessage("nestedException");
    }

    /**
     * Test nested exception 2.
     */
    @Test
    void testNestedException2() {
        Throwable throwable = Assertions.assertThrows(SQLException.class, () -> {
            message.print2();
        });
        assertThat(throwable).hasMessageContaining("Message");
    }

    /**
     * Test nested exception 3.
     */
    @Test
    void testNestedException3() {
        Throwable throwable = Assertions.assertThrows(SQLException.class, () -> {
            message.print3();
        });
        assertThat(throwable).hasMessageContaining("Message");
    }

    /**
     * Test nested exception 5.
     */
    @Test
    void testNestedException5() {
        Throwable throwable = Assertions.assertThrows(FrameworkException.class, () -> {
            message.print5();
        });
        assertThat(throwable).hasMessage(FrameworkErrorCode.ExceptionCaught.getErrMessage());
    }

    /**
     * Test nested exception 6.
     */
    @Test
    void testNestedException6() {
        Throwable throwable = Assertions.assertThrows(FrameworkException.class, () -> {
            message.print6();
        });
        assertThat(throwable).hasMessage("frameworkException");
    }

    /**
     * Test nested exception 7.
     */
    @Test
    void testNestedException7() {
        Throwable throwable = Assertions.assertThrows(FrameworkException.class, () -> {
            message.print7();
        });
        assertThat(throwable).hasMessage("frameworkException");
    }

    /**
     * Test nested exception 8.
     */
    @Test
    void testNestedException8() {
        Throwable throwable = Assertions.assertThrows(FrameworkException.class, () -> {
            message.print8();
        });
        assertThat(throwable).hasMessage("throw");
    }

    /**
     * Test nested exception 9.
     */
    @Test
    void testNestedException9() {
        Throwable throwable = Assertions.assertThrows(FrameworkException.class, () -> {
            message.print9();
        });
        assertThat(throwable).hasMessage("frameworkExceptionMsg");
    }

    /**
     * Test nested sql exception.
     */
    @Test
    void testNestedSQLException() {
        SQLException ex = new SQLException();
        SQLException ex2 = FrameworkException.nestedSQLException(ex);
        Assertions.assertSame(ex, ex2);
    }

    private static void exceptionAsserts(FrameworkException exception, String expectMessage) {
        if (expectMessage == null) {
            expectMessage = FrameworkErrorCode.UnknownAppError.getErrMessage();
        }
        assertThat(exception).isInstanceOf(FrameworkException.class).hasMessage(expectMessage);
        assertThat(exception.getErrcode()).isEqualTo(FrameworkErrorCode.UnknownAppError);
    }

}
