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

public class OpenSourceProject {
	private final  String mName;
	private final  String mAuthor;
	private final String mUrl;
	private final String mLicense;
	
	public OpenSourceProject(String name, String author, String url, String license) {
		mName = name;
		mAuthor = author;
		mUrl = url;
		mLicense = license;
	}

	public String getName() {
		return mName;
	}
	
	public String getAuthor() {
		return mAuthor;
	}
	
	public String getUrl() {
		return mUrl;
	}
	
	public String getLicense() {
		return mLicense;
	}
}
