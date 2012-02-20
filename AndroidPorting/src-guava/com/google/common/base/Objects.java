/*
 * Copyright (C) 2007 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.common.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.VisibleForTesting;

/**
 * Helper functions that can operate on any {@code Object}.
 * 
 * @author Laurence Gonsalves
 * @since 2010.01.04 <b>stable</b> (imported from Google Collections Library)
 */
@GwtCompatible
public final class Objects {
    private Objects() {
    }

    /**
     * Determines whether two possibly-null objects are equal. Returns:
     * 
     * <ul>
     * <li>{@code true} if {@code a} and {@code b} are both null.
     * <li>{@code true} if {@code a} and {@code b} are both non-null and they
     * are equal according to {@link Object#equals(Object)}.
     * <li>{@code false} in all other situations.
     * </ul>
     * 
     * <p>
     * This assumes that any non-null objects passed to this function conform to
     * the {@code equals()} contract.
     */
    public static boolean equal(@Nullable Object a, @Nullable Object b) {
        return a == b || (a != null && a.equals(b));
    }

    /**
     * Generates a hash code for multiple values. The hash code is generated by
     * calling {@link Arrays#hashCode(Object[])}.
     * 
     * <p>
     * This is useful for implementing {@link Object#hashCode()}. For example,
     * in an object that has three properties, {@code x}, {@code y}, and
     * {@code z}, one could write:
     * 
     * <pre>
     * public int hashCode() {
     *     return Objects.hashCode(getX(), getY(), getZ());
     * }
     * </pre>
     * 
     * <b>Warning</b>: When a single object is supplied, the returned hash code
     * does not equal the hash code of that object.
     */
    public static int hashCode(Object... objects) {
        return Arrays.hashCode(objects);
    }

    /**
     * Creates an instance of {@link ToStringHelper}.
     * 
     * <p>
     * This is helpful for implementing {@link Object#toString()}. For example,
     * in an object that contains two member variables, {@code x}, and {@code y}
     * , one could write:
     * 
     * <pre>
     * <tt>
     *   public class ClassName {
     *     public String toString() {
     *       return Objects.toStringHelper(this)
     *           .add("x", x)
     *           .add("y", y)
     *           .toString();
     *     }
     *   }</tt>
     * </pre>
     * 
     * Assuming the values of {@code x} and {@code y} are 1 and 2, this code
     * snippet returns the string <tt>"ClassName{x=1, y=2}"</tt>.
     * 
     * @since 2010.01.04 <b>tentative</b>
     */
    public static ToStringHelper toStringHelper(Object object) {
        return new ToStringHelper(object);
    }

    /**
     * Support class for {@link Objects#toStringHelper}.
     * 
     * @author Jason Lee
     * @since 2010.01.04 <b>tentative</b>
     */
    public static class ToStringHelper {
        private final List<String> fieldString = new ArrayList<String>();
        private final Object instance;

        /**
         * Use {@link Objects#toStringHelper(Object)} to create an instance.
         */
        private ToStringHelper(Object instance) {
            this.instance = Preconditions.checkNotNull(instance);
        }

        /**
         * Adds a name/value pair to the formatted output in {@code name=value}
         * format. If {@code value} is {@code null}, the string {@code "null"}
         * is used.
         */
        public ToStringHelper add(String name, @Nullable Object value) {
            return addValue(Preconditions.checkNotNull(name) + "=" + value);
        }

        /**
         * Adds a value to the formatted output in {@code value} format.
         * <p/>
         * 
         * It is strongly encouraged to use {@link #add(String, Object)} instead
         * and give value a readable name.
         */
        public ToStringHelper addValue(@Nullable Object value) {
            fieldString.add(String.valueOf(value));
            return this;
        }

        private static final Joiner JOINER = Joiner.on(", ");

        /**
         * Returns the formatted string.
         */
        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder(100).append(
                    simpleName(instance.getClass())).append('{');
            return JOINER.appendTo(builder, fieldString).append('}').toString();
        }

        /**
         * {@link Class#getSimpleName()} is not GWT compatible yet, so we
         * provide our own implementation.
         */
        @VisibleForTesting
        static String simpleName(Class<?> clazz) {
            String name = clazz.getName();

            // we want the name of the inner class all by its lonesome
            int start = name.lastIndexOf('$');

            // if this isn't an inner class, just find the start of the
            // top level class name.
            if (start == -1) {
                start = name.lastIndexOf('.');
            }
            return name.substring(start + 1);
        }
    }
}
