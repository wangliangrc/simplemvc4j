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

package com.google.common.collect;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.RandomAccess;

import javax.annotation.Nullable;

import com.google.common.annotations.GwtCompatible;

/**
 * A high-performance, immutable, random-access {@code List} implementation.
 * Does not permit null elements.
 * 
 * <p>
 * Unlike {@link Collections#unmodifiableList}, which is a <i>view</i> of a
 * separate collection that can still change, an instance of
 * {@code ImmutableList} contains its own private data and will <i>never</i>
 * change. {@code ImmutableList} is convenient for {@code public static final}
 * lists ("constant lists") and also lets you easily make a "defensive copy" of
 * a list provided to your class by a caller.
 * 
 * <p>
 * <b>Note</b>: Although this class is not final, it cannot be subclassed as it
 * has no public or protected constructors. Thus, instances of this type are
 * guaranteed to be immutable.
 * 
 * @see ImmutableMap
 * @see ImmutableSet
 * @author Kevin Bourrillion
 * @since 2010.01.04 <b>stable</b> (imported from Google Collections Library)
 */
@GwtCompatible(serializable = true)
@SuppressWarnings({ "rawtypes", "unchecked" })
// we're overriding default serialization
public abstract class ImmutableList<E> extends ImmutableCollection<E> implements
        List<E>, RandomAccess {
    private static final long serialVersionUID = -6664612775086793983L;

    /**
     * Returns the empty immutable list. This set behaves and performs
     * comparably to {@link Collections#emptyList}, and is preferable mainly for
     * consistency and maintainability of your code.
     */
    // Casting to any type is safe because the list will never hold any
    // elements.
    public static <E> ImmutableList<E> of() {
        // BEGIN android-changed
        return (ImmutableList) EmptyImmutableList.INSTANCE;
        // END android-changed
    }

    /**
     * Returns an immutable list containing a single element. This list behaves
     * and performs comparably to {@link Collections#singleton}, but will not
     * accept a null element. It is preferable mainly for consistency and
     * maintainability of your code.
     * 
     * @throws NullPointerException
     *             if {@code element} is null
     */
    public static <E> ImmutableList<E> of(E element) {
        return new SingletonImmutableList<E>(element);
    }

    /**
     * Identical to {@link #of(Object[])}.
     * 
     * @throws NullPointerException
     *             if any element is null
     */
    public static <E> ImmutableList<E> of(E e1, E e2) {
        return new RegularImmutableList<E>(copyIntoArray(e1, e2));
    }

    /**
     * Identical to {@link #of(Object[])}.
     * 
     * @throws NullPointerException
     *             if any element is null
     */
    public static <E> ImmutableList<E> of(E e1, E e2, E e3) {
        return new RegularImmutableList<E>(copyIntoArray(e1, e2, e3));
    }

    /**
     * Identical to {@link #of(Object[])}.
     * 
     * @throws NullPointerException
     *             if any element is null
     */
    public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4) {
        return new RegularImmutableList<E>(copyIntoArray(e1, e2, e3, e4));
    }

    /**
     * Identical to {@link #of(Object[])}.
     * 
     * @throws NullPointerException
     *             if any element is null
     */
    public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5) {
        return new RegularImmutableList<E>(copyIntoArray(e1, e2, e3, e4, e5));
    }

    /**
     * Identical to {@link #of(Object[])}.
     * 
     * @throws NullPointerException
     *             if any element is null
     */
    public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6) {
        return new RegularImmutableList<E>(
                copyIntoArray(e1, e2, e3, e4, e5, e6));
    }

    /**
     * Identical to {@link #of(Object[])}.
     * 
     * @throws NullPointerException
     *             if any element is null
     */
    public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6,
            E e7) {
        return new RegularImmutableList<E>(copyIntoArray(e1, e2, e3, e4, e5,
                e6, e7));
    }

    /**
     * Identical to {@link #of(Object[])}.
     * 
     * @throws NullPointerException
     *             if any element is null
     */
    public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6,
            E e7, E e8) {
        return new RegularImmutableList<E>(copyIntoArray(e1, e2, e3, e4, e5,
                e6, e7, e8));
    }

    /**
     * Identical to {@link #of(Object[])}.
     * 
     * @throws NullPointerException
     *             if any element is null
     */
    public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6,
            E e7, E e8, E e9) {
        return new RegularImmutableList<E>(copyIntoArray(e1, e2, e3, e4, e5,
                e6, e7, e8, e9));
    }

    /**
     * Identical to {@link #of(Object[])}.
     * 
     * @throws NullPointerException
     *             if any element is null
     */
    public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6,
            E e7, E e8, E e9, E e10) {
        return new RegularImmutableList<E>(copyIntoArray(e1, e2, e3, e4, e5,
                e6, e7, e8, e9, e10));
    }

    /**
     * Identical to {@link #of(Object[])}.
     * 
     * @throws NullPointerException
     *             if any element is null
     */
    public static <E> ImmutableList<E> of(E e1, E e2, E e3, E e4, E e5, E e6,
            E e7, E e8, E e9, E e10, E e11) {
        return new RegularImmutableList<E>(copyIntoArray(e1, e2, e3, e4, e5,
                e6, e7, e8, e9, e10, e11));
    }

    // These go up to eleven. After that, you just get the varargs form, and
    // whatever warnings might come along with it. :(

    /**
     * Returns an immutable list containing the given elements, in order.
     * 
     * @throws NullPointerException
     *             if any of {@code elements} is null
     */
    public static <E> ImmutableList<E> of(E... elements) {
        checkNotNull(elements); // for GWT
        switch (elements.length) {
            case 0:
                return ImmutableList.of();
            case 1:
                return new SingletonImmutableList<E>(elements[0]);
            default:
                return new RegularImmutableList<E>(copyIntoArray(elements));
        }
    }

    /**
     * Returns an immutable list containing the given elements, in order. If
     * {@code elements} is a {@link Collection}, this method behaves exactly as
     * {@link #copyOf(Collection)}; otherwise, it behaves exactly as
     * {@code copyOf(elements.iterator()}.
     * 
     * @throws NullPointerException
     *             if any of {@code elements} is null
     */
    // bugs.sun.com/view_bug.do?bug_id=6558557
    public static <E> ImmutableList<E> copyOf(Iterable<? extends E> elements) {
        checkNotNull(elements);
        return (elements instanceof Collection) ? copyOf((Collection<? extends E>) elements)
                : copyOf(elements.iterator());
    }

    /**
     * Returns an immutable list containing the given elements, in order.
     * 
     * <p>
     * <b>Note:</b> Despite what the method name suggests, if {@code elements}
     * is an {@code ImmutableList}, no copy will actually be performed, and the
     * given list itself will be returned.
     * 
     * <p>
     * Note that if {@code list} is a {@code List<String>}, then
     * {@code ImmutableList.copyOf(list)} returns an
     * {@code ImmutableList<String>} containing each of the strings in
     * {@code list}, while ImmutableList.of(list)} returns an
     * {@code ImmutableList<List<String>>} containing one element (the given
     * list itself).
     * 
     * <p>
     * This method is safe to use even when {@code elements} is a synchronized
     * or concurrent collection that is currently being modified by another
     * thread.
     * 
     * @throws NullPointerException
     *             if any of {@code elements} is null
     * @since 2010.01.04 <b>stable</b> (Iterable overload existed previously)
     */
    public static <E> ImmutableList<E> copyOf(Collection<? extends E> elements) {
        checkNotNull(elements);
        // TODO: Once the ImmutableAsList and ImmutableSortedAsList are
        // GWT-compatible, return elements.asList() when elements is an
        // ImmutableCollection.
        if (elements instanceof ImmutableList) {
            /*
             * TODO: When given an ImmutableList that's a sublist, copy the
             * referenced portion of the array into a new array to save space?
             */
            // all supported methods are covariant
            ImmutableList<E> list = (ImmutableList<E>) elements;
            return list;
        }
        return copyFromCollection(elements);
    }

    /**
     * Returns an immutable list containing the given elements, in order.
     * 
     * @throws NullPointerException
     *             if any of {@code elements} is null
     */
    public static <E> ImmutableList<E> copyOf(Iterator<? extends E> elements) {
        return copyFromCollection(Lists.newArrayList(elements));
    }

    private static <E> ImmutableList<E> copyFromCollection(
            Collection<? extends E> collection) {
        Object[] elements = collection.toArray();
        switch (elements.length) {
            case 0:
                return of();
            case 1:
                // collection had only Es in it
                ImmutableList<E> list = new SingletonImmutableList<E>(
                        (E) elements[0]);
                return list;
            default:
                return new RegularImmutableList<E>(copyIntoArray(elements));
        }
    }

    ImmutableList() {
    }

    // This declaration is needed to make List.iterator() and
    // ImmutableCollection.iterator() consistent.
    @Override
    public abstract UnmodifiableIterator<E> iterator();

    // Mark these two methods with @Nullable

    public abstract int indexOf(@Nullable Object object);

    public abstract int lastIndexOf(@Nullable Object object);

    // constrain the return type to ImmutableList<E>

    /**
     * Returns an immutable list of the elements between the specified
     * {@code fromIndex}, inclusive, and {@code toIndex}, exclusive. (If
     * {@code fromIndex} and {@code toIndex} are equal, the empty immutable list
     * is returned.)
     */
    public abstract ImmutableList<E> subList(int fromIndex, int toIndex);

    /**
     * Guaranteed to throw an exception and leave the list unmodified.
     * 
     * @throws UnsupportedOperationException
     *             always
     */
    public final boolean addAll(int index, Collection<? extends E> newElements) {
        throw new UnsupportedOperationException();
    }

    /**
     * Guaranteed to throw an exception and leave the list unmodified.
     * 
     * @throws UnsupportedOperationException
     *             always
     */
    public final E set(int index, E element) {
        throw new UnsupportedOperationException();
    }

    /**
     * Guaranteed to throw an exception and leave the list unmodified.
     * 
     * @throws UnsupportedOperationException
     *             always
     */
    public final void add(int index, E element) {
        throw new UnsupportedOperationException();
    }

    /**
     * Guaranteed to throw an exception and leave the list unmodified.
     * 
     * @throws UnsupportedOperationException
     *             always
     */
    public final E remove(int index) {
        throw new UnsupportedOperationException();
    }

    private static Object[] copyIntoArray(Object... source) {
        Object[] array = new Object[source.length];
        int index = 0;
        for (Object element : source) {
            if (element == null) {
                throw new NullPointerException("at index " + index);
            }
            array[index++] = element;
        }
        return array;
    }

    /**
     * Returns this list instance.
     * 
     * @since 2010.01.04 <b>tentative</b>
     */
    @Override
    public ImmutableList<E> asList() {
        return this;
    }

    /*
     * Serializes ImmutableLists as their logical contents. This ensures that
     * implementation types do not leak into the serialized representation.
     */
    private static class SerializedForm implements Serializable {
        final Object[] elements;

        SerializedForm(Object[] elements) {
            this.elements = elements;
        }

        Object readResolve() {
            return of(elements);
        }

        private static final long serialVersionUID = 0;
    }

    private void readObject(ObjectInputStream stream)
            throws InvalidObjectException {
        throw new InvalidObjectException("Use SerializedForm");
    }

    @Override
    Object writeReplace() {
        return new SerializedForm(toArray());
    }

    /**
     * Returns a new builder. The generated builder is equivalent to the builder
     * created by the {@link Builder} constructor.
     */
    public static <E> Builder<E> builder() {
        return new Builder<E>();
    }

    /**
     * A builder for creating immutable list instances, especially
     * {@code public static final} lists ("constant lists").
     * 
     * <p>
     * Example:
     * 
     * <pre>
     * {
     *     &#064;code
     *     public static final ImmutableList&lt;Color&gt; GOOGLE_COLORS = new ImmutableList.Builder&lt;Color&gt;()
     *             .addAll(WEBSAFE_COLORS).add(new Color(0, 191, 255)).build();
     * }
     * </pre>
     * 
     * <p>
     * Builder instances can be reused - it is safe to call {@link #build}
     * multiple times to build multiple lists in series. Each new list contains
     * the one created before it.
     */
    public static final class Builder<E> extends ImmutableCollection.Builder<E> {
        private final ArrayList<E> contents = Lists.newArrayList();

        /**
         * Creates a new builder. The returned builder is equivalent to the
         * builder generated by {@link ImmutableList#builder}.
         */
        public Builder() {
        }

        /**
         * Adds {@code element} to the {@code ImmutableList}.
         * 
         * @param element
         *            the element to add
         * @return this {@code Builder} object
         * @throws NullPointerException
         *             if {@code element} is null
         */
        @Override
        public Builder<E> add(E element) {
            contents.add(checkNotNull(element));
            return this;
        }

        /**
         * Adds each element of {@code elements} to the {@code ImmutableList}.
         * 
         * @param elements
         *            the {@code Iterable} to add to the {@code ImmutableList}
         * @return this {@code Builder} object
         * @throws NullPointerException
         *             if {@code elements} is null or contains a null element
         */
        @Override
        public Builder<E> addAll(Iterable<? extends E> elements) {
            if (elements instanceof Collection) {
                Collection<?> collection = (Collection<?>) elements;
                contents.ensureCapacity(contents.size() + collection.size());
            }
            super.addAll(elements);
            return this;
        }

        /**
         * Adds each element of {@code elements} to the {@code ImmutableList}.
         * 
         * @param elements
         *            the {@code Iterable} to add to the {@code ImmutableList}
         * @return this {@code Builder} object
         * @throws NullPointerException
         *             if {@code elements} is null or contains a null element
         */
        @Override
        public Builder<E> add(E... elements) {
            checkNotNull(elements); // for GWT
            contents.ensureCapacity(contents.size() + elements.length);
            super.add(elements);
            return this;
        }

        /**
         * Adds each element of {@code elements} to the {@code ImmutableList}.
         * 
         * @param elements
         *            the {@code Iterable} to add to the {@code ImmutableList}
         * @return this {@code Builder} object
         * @throws NullPointerException
         *             if {@code elements} is null or contains a null element
         */
        @Override
        public Builder<E> addAll(Iterator<? extends E> elements) {
            super.addAll(elements);
            return this;
        }

        /**
         * Returns a newly-created {@code ImmutableList} based on the contents
         * of the {@code Builder}.
         */
        @Override
        public ImmutableList<E> build() {
            return copyOf(contents);
        }
    }
}
