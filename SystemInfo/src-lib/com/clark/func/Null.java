package com.clark.func;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class Null implements Serializable, Parcelable {

    private static final long serialVersionUID = 4157873317800864560L;

    public static final Creator<Null> CREATOR = new Creator<Null>() {

        @Override
        public Null createFromParcel(Parcel source) {
            return new Null(source);
        }

        @Override
        public Null[] newArray(int size) {
            return new Null[size];
        }

    };

    public Null() {
    }

    private Null(Parcel source) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
