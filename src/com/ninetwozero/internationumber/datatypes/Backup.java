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

public class Backup {
	private final int mId;
	private final int mContactId;
	private final String mName;
	private final String mPreviousNumber;
	private final String mCurrentNumber;
	
	public Backup(int id, String name, String previous, String current, int cId) {
		mId = id;
		mName = name;
		mPreviousNumber = previous;
		mCurrentNumber = current;
		mContactId = cId;
	}

	public int getId() {
		return mId;
	}
	
	public int getContactId() {
		return mContactId;
	}

	public String getName() {
		return mName;
	}
	
	public String getPreviousNumber() {
		return mPreviousNumber;
	}
	
	public String getCurrentNumber() {
		return mCurrentNumber;
	}
}
