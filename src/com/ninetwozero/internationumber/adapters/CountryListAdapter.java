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
import android.widget.AlphabetIndexer;
import android.widget.SectionIndexer;

import com.ninetwozero.internationumber.R;
import com.ninetwozero.internationumber.abstractions.AbstractCursorListAdapter;
import com.ninetwozero.internationumber.database.entities.CountryDataConverter;
import com.ninetwozero.internationumber.datatypes.Country;

public class CountryListAdapter extends AbstractCursorListAdapter<Country> implements SectionIndexer {
	private AlphabetIndexer mAlphabetIndexer;
	
	public CountryListAdapter(Context context, LayoutInflater layoutInflater, Cursor cursor) {
		super(context, layoutInflater, cursor);
		mDataConverter = new CountryDataConverter();
		mAlphabetIndexer = new AlphabetIndexer(
			cursor, 
			cursor.getColumnIndex(CountryDataConverter.Columns.NAME), 
			"ABCDEFGHIJKLMNOPQRSTUVWXYZ"
		);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		final Country item = mDataConverter.fromCursor(mCursor);
		setText(view, R.id.text1, item.getName());
		view.setTag(item);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return mLayoutInflater.inflate(R.layout.list_item_selection_single, parent, false);
	}

    @Override
    public int getPositionForSection(int sectionIndex) {
        return mAlphabetIndexer.getPositionForSection(sectionIndex);
    }

    @Override
    public int getSectionForPosition(int position) {
        return mAlphabetIndexer.getSectionForPosition(position);
    }

    @Override
    public Object[] getSections() {
        return mAlphabetIndexer.getSections();
    }
}
