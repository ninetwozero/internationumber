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
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.view.ActionMode;
import com.ninetwozero.internationumber.R;
import com.ninetwozero.internationumber.abstractions.AbstractListFragment;
import com.ninetwozero.internationumber.adapters.ContactListAdapter;
import com.ninetwozero.internationumber.datatypes.Country;
import com.ninetwozero.internationumber.misc.ContactActionWrapper;
import com.ninetwozero.internationumber.utils.ContactUtils;


public class ContactListFragment extends AbstractListFragment implements LoaderCallbacks<Cursor>, ContactActionWrapper.Callback {
	private static final int CONTACTS_LOADER = 1;
	public static final String TAG = "Convert.ContactListFragment";
	
	private ActionMode mActionMode = null;
	private ContactListAdapter mAdapter;
	private CheckBox mCheckAll;
	private Country mCountry;
	private TextView mSubHeading;
	
	public static ContactListFragment newInstance() { 
		final ContactListFragment fragment = new ContactListFragment();
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
		final View view = inflater.inflate(R.layout.fragment_default_list, parent, false);
		setupViews(view);
		load();
		return view;
	}
	
	private void setupViews(View view) {
		final TextView headerTextView = (TextView) view.findViewById(R.id.heading);
		final TextView textView = (TextView) view.findViewById(android.R.id.empty);
		mSubHeading = (TextView) view.findViewById(R.id.subheading);
		
		headerTextView.setText(R.string.text_info_contacts_header);
		textView.setText(R.string.msg_no_contacts);
		
		mCheckAll = (CheckBox) view.findViewById(R.id.toggle_selection);
		mCheckAll.setVisibility(View.VISIBLE);
		mCheckAll.setOnCheckedChangeListener(
			new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton checkbox, boolean isChecked) {
					selectAllRows(isChecked);					
				}
			}
		);
	}
	
	private void setupListView(Cursor cursor) {
		final ListView listView = getListView();
		setupAdapter(cursor);

		if( getListView().getAdapter() == null ) {
			listView.setAdapter(mAdapter);
			listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
			listView.setFastScrollEnabled(true);
			listView.setOnItemLongClickListener(
				new AdapterView.OnItemLongClickListener() {
				    public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {
				      handleLongClick(listView, position);
				      return true;
				    }
				}
			);
		}
	}
	
	private void setupAdapter(Cursor cursor) {
		if( mAdapter == null ) {
			if( mCountry == null ) {
				mAdapter = new ContactListAdapter(getSherlockActivity(), getSherlockActivity().getLayoutInflater(), cursor);
			} else {
				mAdapter = new ContactListAdapter(getSherlockActivity(), getSherlockActivity().getLayoutInflater(), cursor, mCountry);
			}
		} else {
			mAdapter.changeCursor(cursor);
		}
	}
	
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		Log.i(TAG, "Getting the contacts...");
		return ContactUtils.getLoader(getSherlockActivity());
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		try {
			setupListView(data);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		mAdapter.changeCursor(null);
	}
	
	public void load() {
		getLoaderManager().initLoader(CONTACTS_LOADER, new Bundle(), this);
	}
	
	public void setCountry(Country country) {
		mCountry = country;
		mAdapter.setCountry(mCountry);
		setupCountryText();
	}

	private void setupCountryText() {
		if( mCountry == null ) {
			mSubHeading.setVisibility(View.GONE);
			mSubHeading.setText("");
		} else {
			mSubHeading.setVisibility(View.VISIBLE);
			mSubHeading.setText(String.format(getString(R.string.info_text_current_country), mCountry.getName()));	
		}
	}

	@Override
	public void onListItemClick(ListView listView, View view, int position, long id) {
		//startActionMode();
	}
	
	private void handleLongClick(ListView listView, int position) {
		startActionMode();
		listView.setItemChecked(position, !listView.isItemChecked(position));
	}
	
	public void checkTheList(boolean check) {
		mCheckAll.setChecked(check);
	}

	private void startActionMode() {
		if( mActionMode == null ) {
			mActionMode = getSherlockActivity().startActionMode(new ContactActionWrapper(this));
		}
	}

	@Override
	public void closeActionMode() {
		mActionMode = null;
	}	
}