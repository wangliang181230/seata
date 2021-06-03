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

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Reflection tools
 *
 * @author zhangsen
 */
public final class ReflectionUtil {

    private ReflectionUtil() {
    }

    /**
     * The constant MAX_NEST_DEPTH.
     */
    public static final int MAX_NEST_DEPTH = 20;

    /**
     * The constant MAX_RETRY_COUNT.
     */
    public static final int MAX_RETRY_COUNT = 10;

    /**
     * The EMPTY_FIELD_ARRAY
     */
    public static final Field[] EMPTY_FIELD_ARRAY = new Field[0];

    /**
     * The cache CLASS_FIELDS_CACHE
     */
    private static final Map<Class<?>, Field[]> CLASS_FIELDS_CACHE = new ConcurrentHashMap<>();

    /**
     * Gets class by name.
     *
     * @param className the class name
     * @return the class by name
     * @throws ClassNotFoundException the class not found exception
     */
    public static Class<?> getClassByName(String className) throws ClassNotFoundException {
        return Class.forName(className, true, Thread.currentThread().getContextClassLoader());
    }

    /**
     * get Field
     *
     * @param clazz     the class
     * @param fieldName the field name
     * @return the field
     * @throws NoSuchFieldException the no such field exception
     * @throws SecurityException    the security exception
     */
    public static Field getField(Class<?> clazz, String fieldName) throws NoSuchFieldException, SecurityException {
        Class<?> cl = clazz;
        int i = 0;
        while (cl != null && cl != Object.class && !cl.isInterface() && i < MAX_NEST_DEPTH) {
            try {
                return cl.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                i++;
                cl = cl.getSuperclass();
            }
        }
        throw new NoSuchFieldException("field not found. class: " + clazz + ", field: " + fieldName);
    }

    /**
     * get field value
     *
     * @param target the target
     * @param field  the field of the target
     * @return field value
     * @throws IllegalAccessException   the illegal access exception
     * @throws IllegalArgumentException the illegal argument exception
     * @throws SecurityException        the security exception
     */
    public static Object getFieldValue(Object target, Field field)
            throws IllegalAccessException, IllegalArgumentException, SecurityException {
        IllegalAccessException ex = null;
        int i = 0;
        while (i < MAX_RETRY_COUNT) {
            try {
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                return field.get(target);
            } catch (IllegalAccessException e) {
                // Avoid other threads executing `field.setAccessible(false)`
                i++;
                ex = e;
            }
        }
        throw ex;
    }

    /**
     * get field value
     *
     * @param target    the target
     * @param fieldName the field name
     * @return field value
     * @throws NoSuchFieldException     the no such field exception
     * @throws IllegalAccessException   the illegal access exception
     * @throws IllegalArgumentException the illegal argument exception
     * @throws SecurityException        the security exception
     */
    public static Object getFieldValue(Object target, String fieldName)
            throws NoSuchFieldException, IllegalAccessException, IllegalArgumentException, SecurityException {
        Field field = getField(target.getClass(), fieldName);
        return getFieldValue(target, field);
    }

    /**
     * set field value
     *
     * @param target     the target
     * @param field      the field of the target
     * @param fieldValue the field value
     * @throws IllegalAccessException   the illegal access exception
     * @throws IllegalArgumentException the illegal argument exception
     * @throws SecurityException        the security exception
     */
    public static void setFieldValue(Object target, Field field, Object fieldValue)
            throws IllegalAccessException, IllegalArgumentException, SecurityException {
        IllegalAccessException ex = null;
        int i = 0;
        while (i < MAX_RETRY_COUNT) {
            try {
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                field.set(target, fieldValue);
            } catch (IllegalAccessException e) {
                // Avoid other threads executing `field.setAccessible(false)`
                i++;
                ex = e;
            }
        }
        throw ex;
    }

    /**
     * get field value
     *
     * @param target     the target
     * @param fieldName  the field name
     * @param fieldValue the field value
     * @throws NoSuchFieldException     the no such field exception
     * @throws IllegalAccessException   the illegal access exception
     * @throws IllegalArgumentException the illegal argument exception
     * @throws SecurityException        the security exception
     */
    public static void setFieldValue(Object target, String fieldName, Object fieldValue)
            throws NoSuchFieldException, IllegalAccessException, IllegalArgumentException, SecurityException {
        Field field = getField(target.getClass(), fieldName);
        setFieldValue(target, field, fieldValue);
    }

    /**
     * get method
     *
     * @param clazz      the class
     * @param methodName the method name
     * @return the method
     * @throws NoSuchMethodException the no such method exception
     * @throws SecurityException     the security exception
     */
    public static Method getMethod(Class<?> clazz, String methodName) throws NoSuchMethodException, SecurityException {
        Class<?> cl = clazz;
        int i = 0;
        while (cl != null && i < MAX_NEST_DEPTH) {
            try {
                return cl.getDeclaredMethod(methodName);
            } catch (NoSuchMethodException e) {
                i++;
                cl = cl.getSuperclass();
            }
        }
        throw new NoSuchMethodException("method not found. class: " + clazz + ", method: " + methodName);
    }

    /**
     * get method
     *
     * @param clazz          the class
     * @param methodName     the method name
     * @param parameterTypes the parameter types
     * @return the method
     * @throws NoSuchMethodException the no such method exception
     * @throws SecurityException     the security exception
     */
    public static Method getMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) throws NoSuchMethodException, SecurityException {
        Class<?> cl = clazz;
        int i = 0;
        while (cl != null && i < MAX_NEST_DEPTH) {
            try {
                return cl.getDeclaredMethod(methodName, parameterTypes);
            } catch (NoSuchMethodException e) {
                i++;
                cl = cl.getSuperclass();
            }
        }
        throw new NoSuchMethodException("method not found. class: " + clazz + ", method: " + methodName);
    }

    /**
     * invoke Method
     *
     * @param target the target
     * @param method the method
     * @return object
     * @throws IllegalAccessException   the illegal access exception
     * @throws IllegalArgumentException the illegal argument exception
     * @throws SecurityException        the security exception
     */
    public static Object invokeMethod(Object target, Method method)
            throws IllegalAccessException, IllegalArgumentException, SecurityException, InvocationTargetException {
        Exception ex = null;
        int i = 0;
        while (i < MAX_RETRY_COUNT) {
            try {
                if (!method.isAccessible()) {
                    method.setAccessible(true);
                }
                return method.invoke(target);
            } catch (IllegalAccessException | InvocationTargetException e) {
                // Avoid other threads executing `field.setAccessible(false)`
                i++;
                ex = e;
            }
        }
        if (ex instanceof InvocationTargetException) {
            throw (InvocationTargetException)ex;
        } else {
            throw (IllegalAccessException)ex;
        }
    }

    /**
     * invoke Method
     *
     * @param target     the target
     * @param methodName the method name
     * @return object
     * @throws NoSuchMethodException    the no such method exception
     * @throws IllegalArgumentException the illegal argument exception
     * @throws SecurityException        the security exception
     */
    public static Object invokeMethod(Object target, String methodName)
            throws NoSuchMethodException, InvocationTargetException, IllegalArgumentException, SecurityException, IllegalAccessException {
        Method method = getMethod(target.getClass(), methodName);
        return invokeMethod(target,method);
    }

    /**
     * invoke Method
     *
     * @param target the target
     * @param method the method
     * @param args   the args
     * @return object
     * @throws InvocationTargetException the invocation target exception
     * @throws IllegalAccessException    the illegal access exception
     * @throws IllegalArgumentException  the illegal argument exception
     * @throws SecurityException         the security exception
     */
    public static Object invokeMethod(Object target, Method method, Object... args)
            throws InvocationTargetException, IllegalAccessException, IllegalArgumentException, SecurityException {
        Exception ex = null;
        int i = 0;
        while (i < MAX_RETRY_COUNT) {
            try {
                if (!method.isAccessible()) {
                    method.setAccessible(true);
                }
                return method.invoke(target, args);
            } catch (IllegalAccessException | InvocationTargetException e) {
                // Avoid other threads executing `field.setAccessible(false)`
                i++;
                ex = e;
            }
        }
        if (ex instanceof InvocationTargetException) {
            throw (InvocationTargetException)ex;
        } else {
            throw (IllegalAccessException)ex;
        }
    }

    /**
     * invoke Method
     *
     * @param target         the target
     * @param methodName     the method name
     * @param parameterTypes the parameter types
     * @param args           the args
     * @return object
     * @throws NoSuchMethodException     the no such method exception
     * @throws InvocationTargetException the invocation target exception
     * @throws IllegalArgumentException  the illegal argument exception
     * @throws SecurityException         the security exception
     */
    public static Object invokeMethod(Object target, String methodName, Class<?>[] parameterTypes, Object... args)
            throws NoSuchMethodException, InvocationTargetException, IllegalArgumentException, SecurityException, IllegalAccessException {
        Method method = getMethod(target.getClass(), methodName, parameterTypes);
        return invokeMethod(target, method, args);
    }

    public static Object invokeMethod(Object target, String methodName, Class<?> parameterType, Object arg)
            throws InvocationTargetException, NoSuchMethodException, IllegalArgumentException, SecurityException, IllegalAccessException {
        return invokeMethod(target, methodName, new Class<?>[]{parameterType}, arg);
    }

    /**
     * invoke static Method
     *
     * @param targetClass    the target class
     * @param methodName     the method name
     * @param parameterTypes the parameter types
     * @param args           the args
     * @return object
     * @throws NoSuchMethodException    the no such method exception
     * @throws SecurityException        the security exception
     * @throws IllegalArgumentException the illegal argument exception
     */
    public static Object invokeStaticMethod(Class<?> targetClass, String methodName, Class<?>[] parameterTypes,
                                            Object... args)
            throws NoSuchMethodException, SecurityException, IllegalArgumentException {
        int i = 0;
        while ((i++) < MAX_NEST_DEPTH && targetClass != null) {
            try {
                Method m = targetClass.getMethod(methodName, parameterTypes);
                return m.invoke(null, args);
            } catch (Exception e) {
                targetClass = targetClass.getSuperclass();
            }
        }
        throw new NoSuchMethodException("class:" + targetClass + ", methodName:" + methodName);
    }

    /**
     * get all interface of the clazz
     *
     * @param clazz the clazz
     * @return set
     */
    public static Set<Class<?>> getInterfaces(Class<?> clazz) {
        if (clazz.isInterface()) {
            return Collections.singleton(clazz);
        }
        Set<Class<?>> interfaces = new LinkedHashSet<>();
        while (clazz != null) {
            Class<?>[] ifcs = clazz.getInterfaces();
            for (Class<?> ifc : ifcs) {
                interfaces.addAll(getInterfaces(ifc));
            }
            clazz = clazz.getSuperclass();
        }
        return interfaces;
    }

    public static void modifyStaticFinalField(Class<?> cla, String modifyFieldName, Object newValue)
            throws NoSuchFieldException, IllegalAccessException {
        Field field = cla.getDeclaredField(modifyFieldName);
        field.setAccessible(true);
        Field modifiers = field.getClass().getDeclaredField("modifiers");
        modifiers.setAccessible(true);
        modifiers.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        field.set(cla, newValue);
    }

    /**
     * Gets all fields.
     *
     * @param targetClazz the target class
     */
    public static Field[] getAllFields(Class<?> targetClazz) {
        if (targetClazz == Object.class || targetClazz.isInterface()) {
            return EMPTY_FIELD_ARRAY;
        }

        // get from the cache
        Field[] fields = CLASS_FIELDS_CACHE.get(targetClazz);
        if (fields != null) {
            return fields;
        }

        // load current class declared fields
        fields = targetClazz.getDeclaredFields();
        LinkedList<Field> fieldList = new LinkedList<>(Arrays.asList(fields));

        // remove the static or synthetic fields
        fieldList.removeIf(f -> Modifier.isStatic(f.getModifiers()) || f.isSynthetic());

        // load super class all fields, and add to the field list
        Field[] superFields = getAllFields(targetClazz.getSuperclass());
        if (CollectionUtils.isNotEmpty(superFields)) {
            fieldList.addAll(Arrays.asList(superFields));
        }

        // list to array
        Field[] resultFields;
        if (!fieldList.isEmpty()) {
            resultFields = fieldList.toArray(new Field[0]);
        } else {
            // reuse the EMPTY_FIELD_ARRAY
            resultFields = EMPTY_FIELD_ARRAY;
        }

        // set cache
        CLASS_FIELDS_CACHE.put(targetClazz, resultFields);

        return resultFields;
    }
}
