package com.clark.ifk;

public class Type {
    private Object val;
    public static final Type NULL = new Type(null);

    Type(Object val) {
        this.val = val;
    }

    public boolean toBool() {
        if (val == null) {
            return false;
        }
        return ((Boolean) val).booleanValue();
    }

    public byte toByte() {
        if (val == null) {
            return 0;
        }
        return ((Byte) val).byteValue();
    }

    public char toChar() {
        if (val == null) {
            return 0;
        }
        return ((Character) val).charValue();
    }

    public short toShort() {
        if (val == null) {
            return 0;
        }
        return ((Short) val).shortValue();
    }

    public int toInt() {
        if (val == null) {
            return 0;
        }
        return ((Integer) val).intValue();
    }

    public long toLong() {
        if (val == null) {
            return 0;
        }
        return ((Long) val).longValue();
    }

    public float toFloat() {
        if (val == null) {
            return 0;
        }
        return ((Float) val).floatValue();
    }

    public double toDouble() {
        if (val == null) {
            return 0;
        }
        return ((Double) val).doubleValue();
    }

    public boolean[] toBoolArray() {
        if (val == null) {
            return new boolean[0];
        }
        return (boolean[]) val;
    }

    public byte[] toByteArray() {
        if (val == null) {
            return new byte[0];
        }
        return (byte[]) val;
    }

    public char[] toCharArray() {
        if (val == null) {
            return new char[0];
        }
        return (char[]) val;
    }

    public short[] toShortArray() {
        if (val == null) {
            return new short[0];
        }
        return (short[]) val;
    }

    public int[] toIntArray() {
        if (val == null) {
            return new int[0];
        }
        return (int[]) val;
    }

    public long[] toLongArray() {
        if (val == null) {
            return new long[0];
        }
        return (long[]) val;
    }

    public float[] toFloatArray() {
        if (val == null) {
            return new float[0];
        }
        return (float[]) val;
    }

    public double[] toDoubleArray() {
        if (val == null) {
            return new double[0];
        }
        return (double[]) val;
    }

    public Object toObject() {
        return val;
    }

    @SuppressWarnings("unchecked")
    public <V> V to(Class<V> c) {
        if (val == null) {
            return null;
        }
        return (V) val;
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
