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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.seata.common.exception.NotSupportYetException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The bean utils
 *
 * @author wangzhongxiang
 */
public class BeanUtils {

    protected static final Logger LOGGER = LoggerFactory.getLogger(BeanUtils.class);

    /**
     * object to string
     *
     * @param o the object
     * @return the string
     * @deprecated please use {@link StringUtils#toString(Object)}
     */
    @Deprecated
    public static String beanToString(Object o) {
        if (o == null) {
            return null;
        }

        Field[] fields = ReflectionUtil.getAllFields(o.getClass());
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (Field field : fields) {
            Object val = null;
            try {
                val = ReflectionUtil.getFieldValue(o, field.getName());
            } catch (IllegalAccessException | RuntimeException | NoSuchFieldException e) {
                LOGGER.warn("get field value failed, class: {}, field: {}", o.getClass(), field.getName(), e);
            }
            if (val != null) {
                sb.append(field.getName()).append("=").append(val).append(", ");
            }
        }
        if (sb.length() > 2) {
            sb.delete(sb.length() - 2, sb.length());
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * map to object
     *
     * @param map   the map
     * @param clazz the Object class
     * @return the object
     */
    public static Object mapToObject(Map<String, String> map, Class<?> clazz) {
        if (CollectionUtils.isEmpty(map)) {
            return null;
        }
        try {
            Object instance = clazz.newInstance();
            Field[] fields = instance.getClass().getDeclaredFields();
            for (Field field : fields) {
                int modifiers = field.getModifiers();
                if (Modifier.isStatic(modifiers) || Modifier.isFinal(modifiers)) {
                    continue;
                }
                boolean accessible = field.isAccessible();
                field.setAccessible(true);
                try {
                    Class<?> type = field.getType();
                    if (type == Date.class) {
                        if (!StringUtils.isEmpty(map.get(field.getName()))) {
                            field.set(instance, new Date(Long.parseLong(map.get(field.getName()))));
                        }
                    } else if (type == Long.class) {
                        if (!StringUtils.isEmpty(map.get(field.getName()))) {
                            field.set(instance, Long.valueOf(map.get(field.getName())));
                        }
                    } else if (type == Integer.class) {
                        if (!StringUtils.isEmpty(map.get(field.getName()))) {
                            field.set(instance, Integer.valueOf(map.get(field.getName())));
                        }
                    } else if (type == Double.class) {
                        if (!StringUtils.isEmpty(map.get(field.getName()))) {
                            field.set(instance, Double.valueOf(map.get(field.getName())));
                        }
                    } else if (type == String.class) {
                        if (!StringUtils.isEmpty(map.get(field.getName()))) {
                            field.set(instance, map.get(field.getName()));
                        }
                    }
                } finally {
                    field.setAccessible(accessible);
                }
            }
            return instance;
        } catch (IllegalAccessException | InstantiationException e) {
            throw new NotSupportYetException(
                    "map to " + clazz.toString() + " failed:" + e.getMessage(), e);
        }
    }


    /**
     * object to map
     *
     * @param object the object
     * @return the map
     */
    public static Map<String, String> objectToMap(Object object) {
        if (object == null) {
            return null;
        }
        Map<String, String> map = new HashMap<>(16);
        Field[] fields = object.getClass().getDeclaredFields();
        try {
            for (Field field : fields) {
                boolean accessible = field.isAccessible();
                field.setAccessible(true);
                try {
                    if (field.getType() == Date.class) {
                        Date date = (Date)field.get(object);
                        if (date != null) {
                            map.put(field.getName(), String.valueOf(date.getTime()));
                        }
                    } else {
                        Object fieldValue = ReflectionUtil.getFieldValue(object, field);
                        map.put(field.getName(),
                                fieldValue == null ? "" : fieldValue.toString());
                    }
                } finally {
                    field.setAccessible(accessible);
                }
            }
        } catch (IllegalAccessException e) {
            throw new NotSupportYetException(
                    "object " + object.getClass().toString() + " to map failed:" + e.getMessage());
        }
        return map;
    }

}
