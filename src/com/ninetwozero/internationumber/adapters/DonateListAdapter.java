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

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ninetwozero.internationumber.R;
import com.ninetwozero.internationumber.abstractions.AbstractListAdapter;

public class DonateListAdapter extends AbstractListAdapter<String> {
	public DonateListAdapter(Context context, LayoutInflater layoutInflater, List<String> items) {
		super(context, layoutInflater, items);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if( convertView == null ) {
			convertView = mLayoutInflater.inflate(android.R.layout.simple_list_item_single_choice, parent, false);
		}
		setText(convertView, android.R.id.text1, 
			String.format(mContext.getString(R.string.info_text_donate), getItem(position))
		);
		convertView.setTag("ud_" + getItem(position));
		return convertView;
	}
}
