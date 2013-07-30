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

package com.ninetwozero.internationumber.database;

import android.content.UriMatcher;

import com.ninetwozero.internationumber.Internationumber;

public class UriFactory {
	private UriFactory() {};
	
    interface URI_CODES {
    	static final int COUNTRIES = 0;
    	static final int BACKUP = 1;
    }

    interface URI_PATH {
    	static final String COUNTRIES = "countries";
    	static final String BACKUP = "backup";
    }

    public static final UriMatcher URI_MATCHER;
    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(Internationumber.AUTHORITY, URI_PATH.COUNTRIES + "/",URI_CODES.COUNTRIES);
        URI_MATCHER.addURI(Internationumber.AUTHORITY, URI_PATH.BACKUP + "/",URI_CODES.BACKUP);
    }
}