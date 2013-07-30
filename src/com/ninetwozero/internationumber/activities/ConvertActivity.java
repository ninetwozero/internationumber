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
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.ninetwozero.internationumber.R;
import com.ninetwozero.internationumber.abstractions.AbstractFragmentActivity;
import com.ninetwozero.internationumber.adapters.ViewPagerAdapter;
import com.ninetwozero.internationumber.callbacks.MenuCallback;
import com.ninetwozero.internationumber.datatypes.Country;
import com.ninetwozero.internationumber.fragments.convert.ContactListFragment;
import com.ninetwozero.internationumber.fragments.convert.ConvertFragment;
import com.ninetwozero.internationumber.fragments.convert.CountryListFragment;
import com.ninetwozero.internationumber.widgets.NoSwipeViewPager;


public class ConvertActivity extends AbstractFragmentActivity implements MenuCallback {	
	public static final String TAG = "ConvertActivity";
	private NoSwipeViewPager mViewPager;
	private Country mCountry;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_convert);
		setup();
	}

	private void setup() {
		getSupportActionBar().setHomeButtonEnabled(true);
		setupViewPager();
	}

	private void setupViewPager() {
		final List<Fragment> fragments = new ArrayList<Fragment>();
		fragments.add(CountryListFragment.newInstance());
		fragments.add(ContactListFragment.newInstance());
		fragments.add(ConvertFragment.newInstance());
		
		mViewPager = (NoSwipeViewPager) findViewById(R.id.viewpager);
		mViewPager.setAdapter( new ViewPagerAdapter(getSupportFragmentManager(), fragments) );
		mViewPager.setAllowSwiping(false);
		mViewPager.setOffscreenPageLimit(2);		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.activity_convert, menu);
		return true;
	}
	
	public boolean onPrepareOptionsMenu(Menu menu) {
		int maxIndex = 2;
		if( mViewPager.getCurrentItem() == maxIndex ) {
			menu.findItem(R.id.menu_next).setVisible(false);
			menu.findItem(R.id.menu_finish).setVisible(true);
		} else {
			menu.findItem(R.id.menu_next).setVisible(true);
			menu.findItem(R.id.menu_finish).setVisible(false);
		}
		return true;
	}

	@Override
	protected void onMenuPressHome(int position) {
		if( position == 0 ) {
			finish();
			return;
		}
		mViewPager.setCurrentItem(position-1, true);
	}

	private void onMenuPressNext(int position) {
		if( position == 0 ) {
			handleMoveToContacts(position);
		} else if( position == 1 ) {
			handleMoveToFinish(position);
		}
	}
	
	private void handleMoveToContacts(int position) {
		final ViewPagerAdapter adapter = (ViewPagerAdapter) mViewPager.getAdapter();
		final CountryListFragment initFragment = (CountryListFragment) adapter.getItem(0);
		final ContactListFragment contactFragment = (ContactListFragment) adapter.getItem(1);
		
		mCountry = initFragment.getCountry();
		if( mCountry == null ) {
			Toast.makeText(this, R.string.msg_country_not_selected, Toast.LENGTH_SHORT).show();
			return;
		}
		contactFragment.setCountry(mCountry);
		mViewPager.setCurrentItem(position+1, true);
	}
	
	private void handleMoveToFinish(int position) {
		final ViewPagerAdapter adapter = (ViewPagerAdapter) mViewPager.getAdapter();
		final ContactListFragment contactFragment = (ContactListFragment) adapter.getItem(1);
		final ConvertFragment finishFragment = (ConvertFragment) adapter.getItem(2);
		
		final long[] idArray = contactFragment.getSelectedIdArray();
		if( idArray.length == 0 ) {
			Toast.makeText(this, R.string.msg_contact_not_selected, Toast.LENGTH_SHORT).show();
			return;
		}
		finishFragment.setCountry(mCountry);
		finishFragment.setContactIdArrayAndLoad(idArray);
		mViewPager.setCurrentItem(position+1, true);
	}
	
	private void onMenuPressFinish() {
		finish();
	}

	private void onMenuPressBackup() {
		startActivity(new Intent(this, BackupActivity.class));
	}
	
	private void updateHomeButton() {
		if( mViewPager.getCurrentItem() == 0 ) {
			getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		} else {
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		}
		supportInvalidateOptionsMenu();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if( keyCode == KeyEvent.KEYCODE_BACK ){
			int currentItem = mViewPager.getCurrentItem();
			if( currentItem == 0 ) {
				finish();
			} else {
				mViewPager.setCurrentItem(--currentItem, true);
			}
			updateHomeButton();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onMenuClick(int id) {
		int position = mViewPager.getCurrentItem();
		switch( id ) {
			case android.R.id.home:
				onMenuPressHome(position);
				break;
			
			case R.id.menu_next:
				onMenuPressNext(position);
				break;
				
			case R.id.menu_finish:
				onMenuPressFinish();
				break;
				
			case R.id.menu_backup:
				onMenuPressBackup();
				break;
			
			default:
				return super.onMenuClick(id);
		}
		updateHomeButton();
		return true;
	}
	
	@Override
	public void onActivityResult(int requestCode, int responseCode, Intent data) {
		ViewPagerAdapter adapter = (ViewPagerAdapter) mViewPager.getAdapter();
		ConvertFragment finishFragment = (ConvertFragment) adapter.getItem(2);
		finishFragment.onServiceFinished(data.getIntExtra("numberOfConversions", 0));
	}
}