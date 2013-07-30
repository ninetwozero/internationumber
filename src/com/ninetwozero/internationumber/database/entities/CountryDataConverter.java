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
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

import com.ninetwozero.internationumber.Internationumber;
import com.ninetwozero.internationumber.datatypes.Country;

final public class CountryDataConverter implements IDataConverter<Country> {
	public static final Uri URI = Uri.parse("content://" + Internationumber.AUTHORITY + "/countries/");
	
	public Country fromCursor(Cursor c) {
		final String name = c.getString(c.getColumnIndex(Columns.NAME));
		final String locale = c.getString(c.getColumnIndex(Columns.LOCALE));
		final int code = c.getInt(c.getColumnIndex(Columns.CODE));
		final String prefixString = c.getString(c.getColumnIndex(Columns.PREFIX));
		
		final String[] prefixes = prefixString.split(Country.DELIMITER);
		return new Country(name, locale, code, prefixes);
	}
	
	/* TODO: Refactor constants */
	public Country fromPreferences(SharedPreferences preferences) {
		return new Country(
			preferences.getString("country_name", "N/A"),
			preferences.getString("selected_country", "N/A"),
			preferences.getInt("country_code", 0),
			preferences.getString("country_prefixes", "").split(Country.DELIMITER)
		);
	}
	
	public ContentValues toContentValues(Country country) {
		final ContentValues contentValues = new ContentValues();
		contentValues.put(Columns.NAME, country.getName());
		contentValues.put(Columns.LOCALE, country.getLocale());
		contentValues.put(Columns.CODE, country.getCode());
		contentValues.put(Columns.PREFIX, country.getPrefixString());
		return contentValues;
	}
	
	@Override
	public ContentValues[] toContentValueArray(List<Country> countrys) {
		final int numCountrys = countrys.size();
		final ContentValues[] contentValues = new ContentValues[numCountrys];
		for( int count = 0; count < numCountrys; count++ ) {	
			final Country country = countrys.get(count);
			contentValues[count] = new ContentValues();
			
			contentValues[count].put(Columns.NAME, country.getName());
			contentValues[count].put(Columns.LOCALE, country.getLocale());
			contentValues[count].put(Columns.CODE, country.getCode());
			contentValues[count].put(Columns.PREFIX, TextUtils.join(" ", country.getPrefixes()));
		}
		return contentValues;
	}
	
	public static class Columns {
		public static final String NAME = "name";
		public static final String LOCALE = "locale";
		public static final String CODE = "code";
		public static final String PREFIX = "prefixes";
		
		public static final String[] getAll() {
			return new String[] { NAME, LOCALE, CODE, PREFIX };
		}
	}
}
