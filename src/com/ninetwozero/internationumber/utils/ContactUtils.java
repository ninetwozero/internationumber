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

package com.ninetwozero.internationumber.utils;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.v4.content.CursorLoader;

import com.ninetwozero.internationumber.database.entities.ContactDataConverter;

final public class ContactUtils {
	public static CursorLoader getLoader(Context c) {
		return new CursorLoader(
			c, 
			ContactDataConverter.URI, 
			new String[] {
				ContactDataConverter.Columns.ID,
				ContactDataConverter.Columns.NAME,
				ContactDataConverter.Columns.NUMBER
			}, 
			getQueryString(),
			null, 
			ContactDataConverter.Columns.NAME + " ASC"
		);
	}
	
	public static String getQueryString() {
		final StringBuilder query = new StringBuilder()
		.append("LENGTH(").append(ContactDataConverter.Columns.NUMBER).append(") > 4")
		.append(" AND (")
			.append(" LENGTH(").append(ContactDataConverter.Columns.NUMBER).append(") > 6")
			.append(" AND ")
			.append(ContactDataConverter.Columns.NUMBER).append(" NOT LIKE '*%#'")
		.append(") AND ")
		.append(ContactDataConverter.Columns.NUMBER).append(" NOT LIKE '%+%'")
		.append(" AND ")
		.append(ContactsContract.Contacts.IN_VISIBLE_GROUP).append("=1");
		return query.toString();
	}
}
