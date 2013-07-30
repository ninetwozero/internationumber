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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;

import com.actionbarsherlock.app.ActionBar;
import com.ninetwozero.internationumber.R;
import com.ninetwozero.internationumber.abstractions.AbstractFragmentActivity;
import com.ninetwozero.internationumber.adapters.ViewPagerAdapter;
import com.ninetwozero.internationumber.fragments.about.AuthorInformationFragment;
import com.ninetwozero.internationumber.fragments.help.FAQListFragment;
import com.ninetwozero.internationumber.fragments.help.TipsAndTricksListFragment;

public class HelpActivity extends AbstractFragmentActivity implements ActionBar.TabListener {
	private ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
		setupLayout();
	}
	
	private void setupLayout() {		
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		List<Fragment> fragments = new ArrayList<Fragment>();
		fragments.add(TipsAndTricksListFragment.newInstance());
		fragments.add(FAQListFragment.newInstance());
		fragments.add(AuthorInformationFragment.newInstance());
		
		final ViewPagerAdapter adapter = new ViewPagerAdapter(
			getSupportFragmentManager(), 
			new String[] {
				getString(R.string.label_tipsntricks),
				getString(R.string.label_faq),
				getString(R.string.label_getintouch)
			},
			fragments
		); 
		
		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		mViewPager.setAdapter(adapter);
		mViewPager.setOffscreenPageLimit(2);
		mViewPager.setOnPageChangeListener(
			new ViewPager.SimpleOnPageChangeListener() {
				@Override
				public void onPageSelected(int position) {
					actionBar.setSelectedNavigationItem(position);
				}
			}
		);
		for (int i = 0; i < adapter.getCount(); i++) {	
			actionBar.addTab(
				actionBar.newTab().setText(adapter.getPageTitle(i)).setTabListener(this)
			);
		}
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
	}

	@Override
	protected void onMenuPressHome(int position) {
		finish();
	}
}
