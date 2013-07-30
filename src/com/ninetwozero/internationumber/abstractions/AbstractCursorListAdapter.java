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

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ninetwozero.internationumber.database.entities.IDataConverter;

public abstract class AbstractCursorListAdapter<T extends Object> extends CursorAdapter {
	protected LayoutInflater mLayoutInflater;
	protected IDataConverter<T> mDataConverter;
	protected int mPosition = 0;
	
	public AbstractCursorListAdapter(Context context, LayoutInflater layoutInflater, Cursor cursor) {
		super(context, cursor, true);
		mLayoutInflater = layoutInflater;
	}
	
	@Override
	final public int getCount() {
		return mCursor == null ? 0 : mCursor.getCount();
	}
	
	@SuppressWarnings("unchecked")
	final public <VT extends View> VT findView(View parent, int id) {
		return (VT) parent.findViewById(id);
	}
	
	final public void setText(View parent, int viewId, int stringResource) {
		setText(parent, viewId, mContext.getString(stringResource));
	}
	
	final public void setText(View parent, int viewId, String text) {
		((TextView) parent.findViewById(viewId)).setText(text);
	}

	final public void setText(TextView textView, String text) {
		textView.setText(text);
	}
	
	@Override
	final public View getView(int position, View convertView, ViewGroup parent) {
		mPosition = position;
		
		if (!mDataValid) {
	        throw new IllegalStateException("Invalid cursor => " + mCursor);
	    }
		
	    if (!mCursor.moveToPosition(position)) {
	        throw new IllegalStateException("Invalid position in Cursor => " + position);
	    }
		
	    if( convertView == null ) {
			convertView = newView(mContext, mCursor, parent);
		}
		bindView(convertView, mContext, mCursor);
		return convertView;
	}
	
	final public void redraw() {
		notifyDataSetChanged();
	}
}
