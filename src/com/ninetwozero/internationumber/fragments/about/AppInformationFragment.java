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

package com.ninetwozero.internationumber.fragments.about;

import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;
import com.ninetwozero.internationumber.R;

public class AppInformationFragment extends SherlockFragment {
	public static final String TAG = "AppInformationFragment";
	
	public static AppInformationFragment newInstance() { 
		final AppInformationFragment fragment = new AppInformationFragment();
		fragment.setArguments(new Bundle());
		return fragment;
	} 
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setRetainInstance(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_about_app, parent, false);
		String versionNumber = "Unknown";
		try {
			versionNumber = getSherlockActivity().getPackageManager().getPackageInfo(
				getSherlockActivity().getPackageName(), 0
			).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		((TextView) view.findViewById(R.id.version)).setText(versionNumber);
		return view;
	}
}