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

package com.ninetwozero.internationumber.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;

import com.ninetwozero.internationumber.R;
import com.ninetwozero.internationumber.abstractions.AbstractCursorListAdapter;
import com.ninetwozero.internationumber.database.entities.ContactDataConverter;
import com.ninetwozero.internationumber.datatypes.Contact;
import com.ninetwozero.internationumber.datatypes.Country;
import com.ninetwozero.internationumber.utils.CountryUtils;
import com.ninetwozero.internationumber.widgets.CheckedRelativeLayout;

public class ContactListAdapter extends AbstractCursorListAdapter<Contact>  {
	public static final String TAG = "ContactListAdapter";
	private CountryUtils mCountryUtils;
	
	public ContactListAdapter(Context context, LayoutInflater layoutInflater, Cursor cursor) {
		super(context, layoutInflater, cursor);
		mDataConverter = new ContactDataConverter();
	}
	
	public ContactListAdapter(Context context, LayoutInflater layoutInflater, Cursor cursor, Country country) {
		super(context, layoutInflater, cursor);
		mDataConverter = new ContactDataConverter();
		mCountryUtils = new CountryUtils(country.getCode(), country.getPrefixes());
	}
	
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		final Contact item = mDataConverter.fromCursor(mCursor);
		String number = item.getNumber();
		if( mCountryUtils != null && mCountryUtils.hasCountry() ) {
			number = mCountryUtils.convert(number);
		}
		setText(view, R.id.text1, item.getName());
		setText(view, R.id.text2, "Current: " + item.getNumber());
		setText(view, R.id.text3, "New: " + number);
		view.setTag(item);
	}
	
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		final CheckedRelativeLayout view = (CheckedRelativeLayout) mLayoutInflater.inflate(R.layout.list_item_contacts, parent, false);
		view.setCheckableChild((Checkable) view.findViewById(R.id.checkbox));
		return view;
	}
	
	public void setCountry(Country country) {
		mCountryUtils = new CountryUtils(country.getCode(), country.getPrefixes());
		notifyDataSetChanged();
	}
}
