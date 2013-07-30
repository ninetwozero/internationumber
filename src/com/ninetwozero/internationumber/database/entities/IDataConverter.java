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

package com.ninetwozero.internationumber.database.entities;

import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;

public interface IDataConverter <T extends Object> {
	public T fromCursor(Cursor c);
	public ContentValues toContentValues(T o);
	public ContentValues[] toContentValueArray(List<T> o);
}
