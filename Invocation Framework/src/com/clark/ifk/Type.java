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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((val == null) ? 0 : val.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Type other = (Type) obj;
        if (val == null) {
            if (other.val != null)
                return false;
        } else if (!val.equals(other.val))
            return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Type [val=");
        builder.append(val);
        if (val != null) {
            builder.append(" (");
            builder.append(val.getClass().getCanonicalName());
            builder.append(")");
        }
        builder.append("]");
        return builder.toString();
    }

}
