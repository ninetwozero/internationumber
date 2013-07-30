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

package com.ninetwozero.internationumber.preference;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.ListPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;

import com.ninetwozero.internationumber.database.entities.CountryDataConverter;
import com.ninetwozero.internationumber.datatypes.Country;

public final class CountryListPreference extends ListPreference {
	public static final String TAG = "CountryListPreference";
	
	private Map<String, String> mData = new HashMap<String, String>();
	private Map<String, Country> mCountries = new HashMap<String, Country>();
	
	public CountryListPreference(Context context) {
		super(context);
	}	
	
	public CountryListPreference(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
	}

	public void populate() {
		CountryDataConverter dataConverter = new CountryDataConverter();
		Cursor cursor = getContext().getContentResolver().query(
			CountryDataConverter.URI, 
			CountryDataConverter.Columns.getAll(),
			null,
			null, 
			CountryDataConverter.Columns.NAME + " ASC"
		);
		
		if( cursor.getCount() > 0 ) {
			mData.clear();
			mCountries.clear();
			
			mData = new LinkedHashMap<String, String>();
			mCountries = new HashMap<String, Country>();
			
			while(cursor.moveToNext()) {
				Country country = dataConverter.fromCursor(cursor);
				mCountries.put(country.getLocale(), country);
				mData.put(country.getLocale(), country.getName());
			}
		}
		
		setEntries(mData.values().toArray(new String[]{}));
		setEntryValues(mData.keySet().toArray(new String[]{}));
		cursor.close();
	}

	public void updateSummary(String key) {
		setSummary(mData.get(key));
	}	
	
	public Country getCountryObject(String locale) {
		return mCountries.get(locale);
	}

	public void updateStorage(String locale) {
		Log.i(TAG, "Saving the country selection...");
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
		SharedPreferences.Editor spEditor = preferences.edit();
		Country country = mCountries.get(locale);
		
		spEditor.putString("country_name", country.getName());
		spEditor.putInt("country_code", country.getCode());
		spEditor.putString("country_prefixes", country.getPrefixString());
		spEditor.commit();
	}
}