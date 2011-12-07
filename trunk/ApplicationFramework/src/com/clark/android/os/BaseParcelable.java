package com.clark.android.os;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class BaseParcelable<T extends Serializable> implements Parcelable {
    public static final Parcelable.Creator<BaseParcelable<?>> CREATOR = new Parcelable.Creator<BaseParcelable<?>>() {
        @SuppressWarnings({ "rawtypes" })
        public BaseParcelable<?> createFromParcel(Parcel in) {
            return new BaseParcelable(in);
        }

        public BaseParcelable<?>[] newArray(int size) {
            return new BaseParcelable<?>[size];
        }
    };

    public T prototype;

    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return prototype.toString();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(prototype);
    }

    public BaseParcelable(T prototype) {
        this.prototype = prototype;
    }

    @SuppressWarnings("unchecked")
    protected BaseParcelable(Parcel in) {
        prototype = (T) in.readSerializable();
    }

}
