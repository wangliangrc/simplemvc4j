package com.clark.ifk;

public class Type {
    private Object val;

    Type(Object val) {
        this.val = val;
    }

    public boolean toBool() {
        return ((Boolean) val).booleanValue();
    }

    public byte toByte() {
        return ((Byte) val).byteValue();
    }

    public char toChar() {
        return ((Character) val).charValue();
    }

    public short toShort() {
        return ((Short) val).shortValue();
    }

    public int toInt() {
        return ((Integer) val).intValue();
    }

    public long toLong() {
        return ((Long) val).longValue();
    }

    public float toFloat() {
        return ((Float) val).floatValue();
    }

    public double toDouble() {
        return ((Double) val).doubleValue();
    }

    public boolean[] toBoolArray() {
        return (boolean[]) val;
    }

    public byte[] toByteArray() {
        return (byte[]) val;
    }

    public char[] toCharArray() {
        return (char[]) val;
    }

    public short[] toShortArray() {
        return (short[]) val;
    }

    public int[] toIntArray() {
        return (int[]) val;
    }

    public long[] toLongArray() {
        return (long[]) val;
    }

    public float[] toFloatArray() {
        return (float[]) val;
    }

    public double[] toDoubleArray() {
        return (double[]) val;
    }

    public Object toObject() {
        return val;
    }

    @SuppressWarnings("unchecked")
    public <V> V to(Class<V> c) {
        return (V) val;
    }

    @SuppressWarnings("unchecked")
    public <V> V[] toArray(Class<V> c) {
        return (V[]) val;
    }
}
