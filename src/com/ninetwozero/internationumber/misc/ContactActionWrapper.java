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

package com.ninetwozero.internationumber.misc;

import android.util.Log;

import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.ninetwozero.internationumber.R;

public final class ContactActionWrapper implements ActionMode.Callback {
	public static final String TAG = "ContactActionModeWrapper";
	
	private final Callback mCaller;
	
	public ContactActionWrapper(Callback c) {
		mCaller = c;
	}
	
	@Override
	public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
		return true;
	}
	
	@Override
	public void onDestroyActionMode(ActionMode mode) {
		mCaller.closeActionMode();
	}
	
	@Override
	public boolean onCreateActionMode(ActionMode mode, Menu menu) {
		mode.getMenuInflater().inflate(R.menu.fragment_contacts_cab, menu);
		return true;
	}
	
	@Override
	public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch( item.getItemId() ) {
        	case R.id.menu_select:
        		mCaller.checkTheList(true);
        		break;
        	case R.id.menu_deselect:
        		mCaller.checkTheList(false);
        		break;
        	default:
    			Log.i(TAG, "Unknown menu option: " + item.getTitle());
        		return false;
        }
        mode.finish();
        return true;
	}
	
	public interface Callback {
		void closeActionMode();
		void checkTheList(boolean check);
	}
}