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
import com.ninetwozero.internationumber.database.entities.BackupDataConverter;
import com.ninetwozero.internationumber.datatypes.Backup;
import com.ninetwozero.internationumber.widgets.CheckedRelativeLayout;

public class BackupListAdapter extends AbstractCursorListAdapter<Backup> {
	public static final String TAG = "BackupListAdapter";

	public BackupListAdapter(Context context, LayoutInflater layoutInflater, Cursor cursor) {
		super(context, layoutInflater, cursor);
		mDataConverter = new BackupDataConverter();
	}
	
	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		final Backup item = mDataConverter.fromCursor(mCursor);
		setText(view, R.id.text1, item.getName());
		setText(view, R.id.text2, "Original: " + item.getPreviousNumber());
		setText(view, R.id.text3, "Converted: " + item.getCurrentNumber());
		view.setTag(item);
	}
	
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		final CheckedRelativeLayout view = (CheckedRelativeLayout) mLayoutInflater.inflate(R.layout.list_item_contacts, parent, false);
		view.setCheckableChild((Checkable) view.findViewById(R.id.checkbox));
		return view;
	}
}
