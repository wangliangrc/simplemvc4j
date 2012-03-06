package com.clark.ifk.v2;

public class Type {
    private Object val;

    public static final Type NULL = new Type(null);

    Type(Object argument) {
        super();
        this.val = argument;
    }

    public boolean toBoolValue() {
        if (val == null) {
            return false;
        }
        return ((Boolean) val).booleanValue();
    }

    public byte toByteValue() {
        if (val == null) {
            return 0;
        }
        return ((Byte) val).byteValue();
    }

    public char toCharValue() {
        if (val == null) {
            return 0;
        }
        return ((Character) val).charValue();
    }

    public short toShortValue() {
        if (val == null) {
            return 0;
        }
        return ((Short) val).shortValue();
    }

    public int toIntValue() {
        if (val == null) {
            return 0;
        }
        return ((Integer) val).intValue();
    }

    public long toLongValue() {
        if (val == null) {
            return 0;
        }
        return ((Long) val).longValue();
    }

    public float toFloatValue() {
        if (val == null) {
            return 0;
        }
        return ((Float) val).floatValue();
    }

    public double toDoubleValue() {
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

    public Object toObjectValue() {
        return val;
    }

    public String toStringValue() {
        if (val == null) {
            return "null";
        } else {
            return val.toString();
        }
    }

    @SuppressWarnings("unchecked")
    public <V> V toValue(Class<V> c) {
        if (val == null) {
            return null;
        }
        return (V) val;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Argument [val=").append(val).append("]");
        return builder.toString();
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

}
