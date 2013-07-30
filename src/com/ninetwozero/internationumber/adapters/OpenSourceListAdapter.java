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
import com.ninetwozero.internationumber.datatypes.OpenSourceProject;

public class OpenSourceListAdapter extends AbstractListAdapter<OpenSourceProject> {
	public OpenSourceListAdapter(Context context, LayoutInflater layoutInflater, List<OpenSourceProject> items) {
		super(context, layoutInflater, items);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if( convertView == null ) {
			convertView = mLayoutInflater.inflate(R.layout.list_item_opensource, parent, false);
		}
		
		final OpenSourceProject project = getItem(position);
		setText(convertView, R.id.text1, project.getName());
		setText(convertView, R.id.text2, project.getAuthor());
		setText(convertView, R.id.text3, project.getUrl());
		setText(convertView, R.id.text4, 
			String.format(mContext.getString(R.string.msg_licensed_as), project.getLicense())
		);
		return convertView;
	}
}
