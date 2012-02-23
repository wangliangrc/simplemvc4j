package com.clark.mutable;

import java.io.Serializable;

/**
 * A mutable <code>boolean</code> wrapper.
 * 
 * @see Boolean
 * @since 2.2
 * @author Apache Software Foundation
 * @version $Id: MutableBoolean.java 916081 2010-02-25 01:28:13Z niallp $
 */
public class MutableBoolean implements Mutable<Boolean>, Serializable,
        Comparable<MutableBoolean> {

    /**
     * Required for serialization support.
     * 
     * @see java.io.Serializable
     */
    private static final long serialVersionUID = -4830728138360036487L;

    /** The mutable value. */
    private boolean value;

    /**
     * Constructs a new MutableBoolean with the default value of false.
     */
    public MutableBoolean() {
        super();
    }

    /**
     * Constructs a new MutableBoolean with the specified value.
     * 
     * @param value
     *            the initial value to store
     */
    public MutableBoolean(boolean value) {
        super();
        this.value = value;
    }

    /**
     * Constructs a new MutableBoolean with the specified value.
     * 
     * @param value
     *            the initial value to store, not null
     * @throws NullPointerException
     *             if the object is null
     */
    public MutableBoolean(Boolean value) {
        super();
        this.value = value.booleanValue();
    }

    // -----------------------------------------------------------------------
    /**
     * Gets the value as a Boolean instance.
     * 
     * @return the value as a Boolean, never null
     */
    public Boolean getValue() {
        return Boolean.valueOf(this.value);
    }

    /**
     * Sets the value.
     * 
     * @param value
     *            the value to set
     */
    public void setValue(boolean value) {
        this.value = value;
    }

    /**
     * Sets the value from any Boolean instance.
     * 
     * @param value
     *            the value to set, not null
     * @throws NullPointerException
     *             if the object is null
     */
    public void setValue(Boolean value) {
        this.value = value.booleanValue();
    }

    // -----------------------------------------------------------------------
    /**
     * Checks if the current value is <code>true</code>.
     * 
     * @return <code>true</code> if the current value is <code>true</code>
     * @since 2.5
     */
    public boolean isTrue() {
        return value == true;
    }

    /**
     * Checks if the current value is <code>false</code>.
     * 
     * @return <code>true</code> if the current value is <code>false</code>
     * @since 2.5
     */
    public boolean isFalse() {
        return value == false;
    }

    // -----------------------------------------------------------------------
    /**
     * Returns the value of this MutableBoolean as a boolean.
     * 
     * @return the boolean value represented by this object.
     */
    public boolean booleanValue() {
        return value;
    }

    // -----------------------------------------------------------------------
    /**
     * Gets this mutable as an instance of Boolean.
     * 
     * @return a Boolean instance containing the value from this mutable, never
     *         null
     * @since 2.5
     */
    public Boolean toBoolean() {
        return Boolean.valueOf(booleanValue());
    }

    // -----------------------------------------------------------------------
    /**
     * Compares this object to the specified object. The result is
     * <code>true</code> if and only if the argument is not <code>null</code>
     * and is an <code>MutableBoolean</code> object that contains the same
     * <code>boolean</code> value as this object.
     * 
     * @param obj
     *            the object to compare with, null returns false
     * @return <code>true</code> if the objects are the same; <code>false</code>
     *         otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MutableBoolean) {
            return value == ((MutableBoolean) obj).booleanValue();
        }
        return false;
    }

    /**
     * Returns a suitable hash code for this mutable.
     * 
     * @return the hash code returned by <code>Boolean.TRUE</code> or
     *         <code>Boolean.FALSE</code>
     */
    @Override
    public int hashCode() {
        return value ? Boolean.TRUE.hashCode() : Boolean.FALSE.hashCode();
    }

    // -----------------------------------------------------------------------
    /**
     * Compares this mutable to another in ascending order.
     * 
     * @param other
     *            the other mutable to compare to, not null
     * @return negative if this is less, zero if equal, positive if greater
     *         where false is less than true
     */
    public int compareTo(MutableBoolean other) {
        boolean anotherVal = other.value;
        return value == anotherVal ? 0 : (value ? 1 : -1);
    }

    // -----------------------------------------------------------------------
    /**
     * Returns the String value of this mutable.
     * 
     * @return the mutable value as a string
     */
    @Override
    public String toString() {
        return String.valueOf(value);
    }

}
