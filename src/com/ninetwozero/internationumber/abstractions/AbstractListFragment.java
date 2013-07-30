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

import android.os.Build;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListFragment;

abstract public class AbstractListFragment extends SherlockListFragment {

	@SuppressWarnings("deprecation")
	public long[] getSelectedIdArray() {
		if( Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO ) {
			return getListView().getCheckItemIds();
		} else {
			return getListView().getCheckedItemIds();
		}
	}
	
	protected void selectAllRows(boolean check) {
		final ListView listView = getListView();
		final int max = listView.getCount();
		for( int count = 0; count < max; count++ ) {
			listView.setItemChecked(count, check);
		}
	}
}