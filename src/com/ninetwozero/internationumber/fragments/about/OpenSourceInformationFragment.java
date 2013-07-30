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

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockListFragment;
import com.ninetwozero.internationumber.R;
import com.ninetwozero.internationumber.adapters.OpenSourceListAdapter;
import com.ninetwozero.internationumber.datatypes.OpenSourceProject;

public class OpenSourceInformationFragment extends SherlockListFragment {
	public static final String TAG = "OpenSourceInformationFragment";
	private List<OpenSourceProject> mProjects;
	private OpenSourceListAdapter mAdapter;
	
	public static OpenSourceInformationFragment newInstance() { 
		final OpenSourceInformationFragment fragment = new OpenSourceInformationFragment();
		fragment.setArguments(new Bundle());
		return fragment;
	} 
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setRetainInstance(true);
		setupProjects();
	}
	
	private void setupProjects() {
		mProjects = new ArrayList<OpenSourceProject>();
		mProjects.add(new OpenSourceProject("ActionBarSherlock", "Jake Wharton", "http://www.actionbarsherlock.com", "Apache2.0"));
		mProjects.add(new OpenSourceProject("SQLiteProvider", "Novoda", "https://github.com/novoda/SQLiteProvider", "Apache2.0"));
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_about_oss, parent, false);
		final ListView listView = (ListView) view.findViewById(android.R.id.list);
		final TextView textView = (TextView) view.findViewById(android.R.id.empty);
		
		mAdapter = new OpenSourceListAdapter(getSherlockActivity(), inflater, mProjects);
		listView.setAdapter(mAdapter);
		listView.setChoiceMode(ListView.CHOICE_MODE_NONE);
		textView.setText(getString(R.string.msg_no_projects));
		return view;
	}
	
	@Override
	public void onListItemClick(ListView listView, View view, int position, long id) {
		final OpenSourceProject project = mProjects.get(position);
		final Uri url = Uri.parse(project.getUrl());
		final Intent intent = new Intent(Intent.ACTION_VIEW).setData(url);
		
		Log.i(TAG, "Opening " + url.toString() + " via ACTION_VIEW.");
		startActivity(intent);
	}
}