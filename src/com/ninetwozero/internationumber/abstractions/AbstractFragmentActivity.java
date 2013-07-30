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

package com.ninetwozero.internationumber.abstractions;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.ninetwozero.internationumber.R;
import com.ninetwozero.internationumber.activities.AboutActivity;
import com.ninetwozero.internationumber.activities.DonateActivity;
import com.ninetwozero.internationumber.activities.HelpActivity;
import com.ninetwozero.internationumber.activities.SettingsActivity;
import com.ninetwozero.internationumber.callbacks.MenuCallback;
import com.ninetwozero.internationumber.database.entities.CountryDataConverter;
import com.ninetwozero.internationumber.datatypes.Country;
import com.ninetwozero.internationumber.services.AutoConvertService;

abstract public class AbstractFragmentActivity extends SherlockFragmentActivity implements MenuCallback {
	private static final String TAG = "AbstractFragmentActivity";
	private static final String SP_RUNNING = "is_running";
	private static final String SP_RUN_SERVICE = "run_service";
	
	private static SharedPreferences mSharedPreferences;
	private Toast mToast;
	
	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
	}
	
	private void setupService() {
		if( mSharedPreferences.getBoolean(SP_RUN_SERVICE, false) != mSharedPreferences.getBoolean(SP_RUNNING, false) ) {
			final AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
			final boolean shouldStartService = mSharedPreferences.getBoolean(SP_RUN_SERVICE, false);
			final Country country = new CountryDataConverter().fromPreferences(mSharedPreferences);	
			final Intent intent = AutoConvertService.getIntent(getApplicationContext()).putExtra(
				AutoConvertService.INTENT_COUNTRY, 
				country
			);
			final PendingIntent pendingIntentOperation = AutoConvertService.getPendingIntent(getApplicationContext(), intent);
			
			if( country.getCode() == 0 ) {
				updateSharedPreferences(false, false);
				showToast(getString(R.string.msg_service_disabled_nocountry));
				Log.i(TAG, "Service is disabled");
			} else if( shouldStartService ) {
				alarmManager.setInexactRepeating(
					AlarmManager.RTC, 
					AlarmManager.INTERVAL_HOUR, 
					AlarmManager.INTERVAL_DAY, 
					pendingIntentOperation
				);	
				updateSharedPreferences(true, true);
				Log.i(TAG, "Service is started");
			} else {
				alarmManager.cancel(pendingIntentOperation);
				updateSharedPreferences(false, false);
				Log.i(TAG, "Service is stopped");
			}
		}
	}
	
	private void updateSharedPreferences(boolean isRunning, boolean shouldStart) {
		final SharedPreferences.Editor spEditor = mSharedPreferences.edit();
		spEditor.putBoolean(SP_RUNNING, isRunning);
		spEditor.putBoolean(SP_RUN_SERVICE, shouldStart);
		spEditor.commit();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		setupService();
	}

	@Override
	final public boolean onOptionsItemSelected(MenuItem item) {
		final int id = item.getItemId();
		final boolean status = onMenuClick(id);
		return status ? status : this.onMenuClick(id);
	}
	
	public boolean onMenuClick(int id) {
		switch( id ) {
			case android.R.id.home:
				onMenuPressHome(0);
				return true;
			case R.id.menu_about:
				onMenuPressAbout();
				return true;
			case R.id.menu_settings:
				onMenuPressSettings();
				return true;
			case R.id.menu_donate:
				onMenuPressDonate();
				return true;
			case R.id.menu_help:
				onMenuPressHelp();
				return true;
			default:
				return false;
		}
	}
	
	abstract protected void onMenuPressHome(int position);
	
	protected void onMenuPressAbout() {
		startActivity(new Intent(this, AboutActivity.class));
	}

	private void onMenuPressDonate() {
		startActivity(new Intent(this, DonateActivity.class));
	}
	
	private void onMenuPressSettings() {
		startActivity(new Intent(this, SettingsActivity.class));
	}
	
	protected void onMenuPressHelp() {
		startActivity(new Intent(this, HelpActivity.class));
	}  

	@Override
	public void onSaveInstanceState(Bundle out) {
		super.onSaveInstanceState(out);
	}
	
	@Override
	public void onRestoreInstanceState(Bundle in) {
		super.onRestoreInstanceState(in);
	}
	
	protected void showToast(String text) {
		if( mToast != null ) {
			mToast.cancel();
		}
		mToast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
		mToast.show();
	}
}