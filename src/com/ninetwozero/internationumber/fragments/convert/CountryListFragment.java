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

package com.ninetwozero.internationumber.fragments.convert;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.ninetwozero.internationumber.R;
import com.ninetwozero.internationumber.abstractions.AbstractListFragment;
import com.ninetwozero.internationumber.adapters.CountryListAdapter;
import com.ninetwozero.internationumber.callbacks.MenuCallback;
import com.ninetwozero.internationumber.database.entities.CountryDataConverter;
import com.ninetwozero.internationumber.datatypes.Country;

public class CountryListFragment extends AbstractListFragment implements LoaderCallbacks<Cursor> {
	private static final int LOADER_INIT = 0;
	public static final String TAG = "Convert.ContactListFragment";
	
	private CountryListAdapter mAdapter;
	private Country mSelectedCountry;
	
	public static CountryListFragment newInstance() { 
		final CountryListFragment fragment = new CountryListFragment();
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
		super.onCreateView(inflater, parent, savedInstanceState);
		final View view = inflater.inflate(R.layout.fragment_default_list, parent, false);
		setupViews(view);
		load();
		return view;
	}
	
	private void setupViews(final View view) {
		final TextView textView = (TextView) view.findViewById(R.id.heading);
		final CheckBox checkBox = (CheckBox) view.findViewById(R.id.toggle_selection);
		final TextView textViewEmpty = (TextView) view.findViewById(android.R.id.empty);

		textView.setText(R.string.text_info_country_header);
		textViewEmpty.setText(R.string.msg_no_countries);
		checkBox.setChecked(true);
	}
	
	private void setupListView() {
		final ListView listView = getListView();
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		listView.setFastScrollEnabled(true);
		listView.setAdapter(mAdapter);
	}
	
	private void setupAdapter(Cursor cursor) {
		if( mAdapter == null ) {
			mAdapter = new CountryListAdapter(getSherlockActivity(), getSherlockActivity().getLayoutInflater(), cursor);
		} else {
			mAdapter.changeCursor(cursor);
		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {			
		Log.i(TAG, "Loading countries from the DB.");
		return new CursorLoader(
			getSherlockActivity(), 
			CountryDataConverter.URI,
			null, 
			null,
			null, 
			null
		);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		Log.i(TAG, "Done loading countries from the DB.");
		setupAdapter(data);
		setupListView();
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
	    mAdapter.changeCursor(null);
	}
	
	@Override
	public void onListItemClick(ListView listView, View view, int position, long id) {
		mSelectedCountry = (Country) view.getTag();
		((MenuCallback) getSherlockActivity()).onMenuClick(R.id.menu_next);
	}
	
	public void load() {
		getLoaderManager().initLoader(LOADER_INIT, new Bundle(), this);
	}
	
	public Country getCountry() {
		return mSelectedCountry;
	}
}