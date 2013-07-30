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

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.ninetwozero.internationumber.R;
import com.ninetwozero.internationumber.abstractions.AbstractFragmentActivity;
import com.ninetwozero.internationumber.adapters.ActionBarSpinnerAdapter;
import com.ninetwozero.internationumber.fragments.donate.DonateListFragment;


public class DonateActivity extends AbstractFragmentActivity {	
	private static final String URL_PAYPAL = "https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=KMWU9GFS6EHFA";
	public static final String TAG = "DonateActivity";

	private ActionBarSpinnerAdapter mAdapter;
	private DonateListFragment mFragment;
	private SparseArray<Uri> mIntents;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_single_fragment);
		setup(savedInstanceState);
	}

	private void setup(final Bundle savedInstanceState) {
		setupSpinner();
		setupIntents();
		setupActionBar();
		setupActivity(savedInstanceState);
	}

	private void setupSpinner() {
		final List<Integer> resources = new ArrayList<Integer>();
		resources.add(R.string.label_donate_gplay);
		resources.add(R.string.label_donate_paypal);
		mAdapter = new ActionBarSpinnerAdapter(this, getLayoutInflater(), resources);
	}
	
	private void setupIntents() {
		mIntents = new SparseArray<Uri>();
		mIntents.put(1, Uri.parse(URL_PAYPAL));
	}

	private void setupActionBar() {
		final ActionBar actionbar = getSupportActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		actionbar.setTitle("");
		actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		actionbar.setListNavigationCallbacks( 
			mAdapter,
		    new OnNavigationListener() {
		        @Override
		        public boolean onNavigationItemSelected(int position, long id){		            
		        	if( mIntents.get(position) != null ){
		        		startActivity(new Intent(Intent.ACTION_VIEW, mIntents.get(position)));
		        		actionbar.setSelectedNavigationItem(0);
		        	}
		        	return true;
		        }
		    } 
		);
	}

	private void setupActivity(Bundle state) {
		mFragment = DonateListFragment.newInstance();
		if (state == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.wrap, mFragment).commit();
        }
		
	}
	
	protected void onMenuPressHome(int position) {
		finish();
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if( requestCode == DonateListFragment.REQUEST_IAP ) {
			mFragment.onActivityResult(requestCode, resultCode, data);
		}
	}
}