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

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.actionbarsherlock.view.Menu;
import com.ninetwozero.internationumber.R;
import com.ninetwozero.internationumber.abstractions.AbstractFragmentActivity;
import com.ninetwozero.internationumber.fragments.BackupListFragment;


public class BackupActivity extends AbstractFragmentActivity {	
	public static final String TAG = "BackupActivity";
	private BackupListFragment mFragment;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_single_fragment);
		setup(savedInstanceState);
	}

	private void setup(Bundle state) {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		mFragment = BackupListFragment.newInstance();
		if (state == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.wrap, mFragment).commit();
        }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.activity_backup, menu);
		return true;
	}

	public boolean onMenuClick(int id) {
		if( id == R.id.menu_restore ) {
			onMenuPressRestore();
			return true;
		}
		return super.onMenuClick(id);
	}
	
	protected void onMenuPressHome(int position) {
		finish();
	}
	
	private void onMenuPressRestore() {
		Log.i(TAG, "Restoring some backups...");
		mFragment.initRestore();
	}
}