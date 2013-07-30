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

package com.ninetwozero.internationumber;

import android.app.Application;
import android.content.Context;

public class Internationumber extends Application {
    public static final String NAME = "app.db";
    public static final String AUTHORITY = "com.ninetwozero.internationumber.provider";
    public static final String DOMAIN = "internationumber.com";
    
    private static Internationumber mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static Context getContext() {
        return mInstance;
    }
}
