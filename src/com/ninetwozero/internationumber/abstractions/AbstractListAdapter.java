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

package com.ninetwozero.internationumber.abstractions;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public abstract class AbstractListAdapter<T extends Object> extends BaseAdapter {
	protected Context mContext;
	protected LayoutInflater mLayoutInflater;
	protected List<T> mItems;
	
	public AbstractListAdapter(Context context, LayoutInflater layoutInflater, List<T> items) {
		super(); 
		mContext = context;
		mLayoutInflater = layoutInflater;
		mItems = items;
	}

	@Override
	final public int getCount() {
		return mItems == null ? 0 : mItems.size();
	}

	@Override
	final public T getItem(int position) {
		return mItems == null ? null : mItems.get(position);
	}

	final public void setItems(List<T> items) {
		mItems = items;
		notifyDataSetChanged();
	}
	
	final public void setText(View parent, int viewId, int stringResource) {
		setText(parent, viewId, mContext.getString(stringResource));
	}
	
	final public void setText(View parent, int viewId, String text) {
		((TextView) parent.findViewById(viewId)).setText(text);
	}

	@Override
	abstract public long getItemId(int position);
	
	@Override
	abstract public View getView(int position, View convertView, ViewGroup parent);
}
