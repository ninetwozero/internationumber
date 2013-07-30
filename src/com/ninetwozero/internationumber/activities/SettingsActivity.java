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

package com.ninetwozero.internationumber.activities;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.MenuItem;
import com.ninetwozero.internationumber.R;
import com.ninetwozero.internationumber.preference.CountryListPreference;

@SuppressWarnings("deprecation")
public class SettingsActivity extends SherlockPreferenceActivity {	
	public static final String TAG = "SettingsActivity";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setup();
	}

	private void setup() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		addPreferencesFromResource(R.xml.preferences_global);
		setupCountryListPreference();
	}
	
	private void setupCountryListPreference() {
		final CountryListPreference listPreference = (CountryListPreference) getPreferenceManager().findPreference("selected_country");
		listPreference.populate();
		listPreference.setOnPreferenceChangeListener(
			new OnPreferenceChangeListener() {
				@Override
				public boolean onPreferenceChange(Preference preference, Object newValue) {
					final String locale = String.valueOf(newValue);
					listPreference.updateSummary(locale);
					listPreference.updateStorage(locale);
					return true;
				}
			}
		);
		listPreference.setSummary(listPreference.getEntry());
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		switch( menuItem.getItemId() ) {
			case android.R.id.home:
				onMenuPressHome();
				return true;
			case R.id.menu_about:
				onMenuPressAbout();
				return true;
			case R.id.menu_help:
				onMenuPressHelp();
				return true;
			default:
				return false;
		}
	}

	private void onMenuPressAbout() {
		startActivity( new Intent(this, AboutActivity.class) );		
	}

	private void onMenuPressHelp() {
		startActivity( new Intent(this, HelpActivity.class) );
	}
	
	protected void onMenuPressHome() {
		finish();
	}
}