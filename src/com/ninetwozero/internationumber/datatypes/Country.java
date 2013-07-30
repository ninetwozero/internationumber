/*
	This file is part of Internationumber

    Internationumber is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Internationumber is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
 */

package com.ninetwozero.internationumber.datatypes;

import java.util.Arrays;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

final public class Country implements Parcelable {
	public static final String DELIMITER = ":";
	
	private final String mName;
	private final String mLocale;
	private final int mCode;
	private final String[] mPrefixes;
	
	public Country(Parcel in) {
		mName = in.readString();
		mLocale = in.readString();
		mCode = in.readInt();
		mPrefixes = in.createStringArray();
	}
	
	public Country(String name, String locale, int code, String[] prefixes) {
		mName = name;
		mLocale = locale;
		mCode = code;
		mPrefixes = prefixes.clone();
	}
	
	public String getName() {
		return mName;
	}
	
	public int getCode() {
		return mCode;
	}
	
	public String getLocale() {
		return mLocale;
	}

	public String[] getPrefixes() {
		return mPrefixes.clone();
	}

	public String getPrefixString() {
		return TextUtils.join(DELIMITER, mPrefixes);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(mName);
		out.writeString(mLocale);
		out.writeInt(mCode);
		out.writeStringArray(mPrefixes);
	}
	
	public static final Parcelable.Creator<Country> CREATOR = new Parcelable.Creator<Country>() {
        public Country createFromParcel(Parcel in) {
            return new Country(in);
        }
        public Country[] newArray(int size) {
            return new Country[size];
        }
    };

	@Override
	public String toString() {
		return "Country [mName=" + mName + ", mLocale=" + mLocale + ", mCode="
				+ mCode + ", mPrefixes=" + Arrays.toString(mPrefixes) + "]";
	}
}
