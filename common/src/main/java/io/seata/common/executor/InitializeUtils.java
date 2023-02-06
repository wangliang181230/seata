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

/**
 * The interface Initialize.
 *
 * @author wang.liang
 */
public class InitializeUtils {

    /**
     * Init the object.
     */
    public static void init(Object obj) {
        if (obj instanceof Initialize) {
            ((Initialize)obj).init();
            return;
        }

        if (obj instanceof Wrapper) {
            Initialize initialize = ((Wrapper)obj).unwrap(Initialize.class);
            if (initialize != null) {
                initialize.init();
            }
        }
    }

    /**
     * Whether the object is initialized.
     *
     * @return the boolean
     */
    public static boolean isInitialized(Object obj) {
        if (obj instanceof Initialize) {
            return ((Initialize)obj).isInitialized();
        }

        if (obj instanceof Wrapper) {
            Initialize initialize = ((Wrapper)obj).unwrap(Initialize.class);
            if (initialize != null) {
                return initialize.isInitialized();
            }
        }

        return true;
    }

    public static boolean isInitialize(Object obj) {
        if (obj instanceof Initialize) {
            return true;
        }

        if (obj instanceof Wrapper) {
            Initialize initialize = ((Wrapper)obj).unwrap(Initialize.class);
            if (initialize != null) {
                return true;
            }
        }

        return false;
    }

}
