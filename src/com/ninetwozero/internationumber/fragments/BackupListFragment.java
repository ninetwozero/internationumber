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

package com.ninetwozero.internationumber.fragments;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
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
import android.widget.Toast;

import com.actionbarsherlock.view.ActionMode;
import com.ninetwozero.internationumber.Internationumber;
import com.ninetwozero.internationumber.R;
import com.ninetwozero.internationumber.abstractions.AbstractListFragment;
import com.ninetwozero.internationumber.adapters.BackupListAdapter;
import com.ninetwozero.internationumber.database.entities.BackupDataConverter;
import com.ninetwozero.internationumber.datatypes.Backup;
import com.ninetwozero.internationumber.misc.ContactActionWrapper;

public class BackupListFragment extends AbstractListFragment implements LoaderCallbacks<Cursor>, ContactActionWrapper.Callback {
	private static final int BACKUP_LOADER = 1;
	public static final String TAG = "BackupListFragment";

	private ActionMode mActionMode = null;
	private BackupListAdapter mAdapter;
	private CheckBox mCheckAll;
	private final BackupDataConverter mDataConverter;
	private boolean mDoRestore = false;
	
	public BackupListFragment() {
		super();
		mDataConverter = new BackupDataConverter();
	}
	
	public static BackupListFragment newInstance() { 
		final BackupListFragment fragment = new BackupListFragment();
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
	
	private void setupListView(Cursor cursor) {
		final ListView listView = getListView();
		setupAdapter(cursor);

		if( getListView().getAdapter() == null ) {
			listView.setAdapter(mAdapter);
			listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
			listView.setFastScrollEnabled(true);
			listView.setOnItemLongClickListener(
				new AdapterView.OnItemLongClickListener() {
				    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				      handleLongClick(listView, position);
				      return true;
				    }
				}
			);
		}
	}

	private void setupAdapter(Cursor cursor) {
		if( mAdapter == null ) {
			mAdapter = new BackupListAdapter(getSherlockActivity(), getSherlockActivity().getLayoutInflater(), cursor);
		} else {
			mAdapter.changeCursor(cursor);
		}
	}

	private void setupViews(View view) {
		final TextView headerTextView = (TextView) view.findViewById(R.id.heading);
		final TextView textView = (TextView) view.findViewById(android.R.id.empty);
		
		headerTextView.setText(R.string.text_info_backup_header);
		textView.setText(R.string.msg_no_backups);
		
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

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		Log.i(TAG, "Getting the backups...");
		return new CursorLoader(
			getSherlockActivity(), 
			BackupDataConverter.URI, 
			new String[] {
				BackupDataConverter.Columns.ID,
				BackupDataConverter.Columns.NAME,
				BackupDataConverter.Columns.BEFORE,
				BackupDataConverter.Columns.AFTER,
				BackupDataConverter.Columns.ROW_ID
			}, 
			null, 
			null, 
			BackupDataConverter.Columns.NAME + " ASC"
		);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		setupListView(data);
		if( mDoRestore ) { 
			mDoRestore = false;
			if( getSelectedIdArray().length > 0 ) {
				new AsyncRestoreTask().execute(data);
			} else {
				Toast.makeText(getSherlockActivity(), R.string.msg_backup_not_selected, Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		mAdapter.changeCursor(null);
	}
	
	public void load() {
		getLoaderManager().initLoader(BACKUP_LOADER, new Bundle(), this);
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

	public void initRestore() {
		Log.i(TAG, "Restoration initiated.");
		mDoRestore = true;
		if( getListView().getCheckedItemIds().length > 0 ) {
			load();
		} else {
			Toast.makeText(getSherlockActivity(), R.string.msg_backup_not_selected, Toast.LENGTH_SHORT).show();
		}
	}	
	
	private class AsyncRestoreTask extends AsyncTask<Cursor, Integer, Boolean> {		
		private Cursor mCursor;
		private List<Backup> mBackups;
		private Integer mResultCounter;
		private boolean mAllContactsConverted = true;
		private int mConvertedContacts;
		private int mNumContacts;
		
		@Override
		protected void onPreExecute() {
			Toast.makeText(getSherlockActivity(), R.string.msg_restore_start, Toast.LENGTH_SHORT).show();
		}
		
		@Override
		protected Boolean doInBackground(Cursor... params) {
			mCursor = params[0];
			
			Log.i(TAG, "Fetching the backups from the database...");
			mBackups = getContactsViaFilter(mCursor);
			mNumContacts = mBackups.size();
			
			Log.i(TAG, "Restoring the selected contacts in the phonebook... (" + mBackups.size() + ")");
			if( !mBackups.isEmpty() ) {
				Log.i(TAG, "Converting contacts...");
				if ( doConvertInPhonebook() ) {
					Log.i(TAG, "Contacts converted. Removing backups.");
					if( removeBackupWhenRestored() ) {
						Log.i(TAG, "Executed successfully.");
						return true;
					} else {
						Log.i(TAG, "Could not remove the backups.");
					}
				} else {
					Log.i(TAG, "Could not restore the contacts.");
				}
			}
			Log.i(TAG, "Executed unsuccessfully.");
			return false;
		}		
		
		@Override
		protected void onPostExecute(Boolean result) {
			if( result ) {
				if( mAllContactsConverted ) {
					Toast.makeText(getSherlockActivity(), R.string.msg_restore_ok, Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(
						getSherlockActivity(), 
						String.format(getString(R.string.msg_restore_semi_ok), mConvertedContacts, mNumContacts),
						Toast.LENGTH_SHORT
					).show();
				}
			} else {
				Toast.makeText(getSherlockActivity(), R.string.msg_restore_fail, Toast.LENGTH_SHORT).show();
			}
		}
		private boolean doConvertInPhonebook() {
			final List<ContentProviderOperation> operations = new ArrayList<ContentProviderOperation>();
			String selection = null;
			String[] selectionArgs = null;
			
			int currentResult = 0;
			mResultCounter = 0;
			
			for( Backup data : mBackups ) {
				selection = ContactsContract.RawContacts._ID + "=? AND " + 
					ContactsContract.Data.MIMETYPE + "='"  + 
					ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE + "'";
		        selectionArgs = new String[]{String.valueOf(data.getContactId())}; 
		        operations.add(
					ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
		                .withSelection(selection, selectionArgs)
		                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, data.getPreviousNumber())
		                .build()
				);
			}
			
			try {
				final ContentProviderResult[] resultArray = getSherlockActivity().getContentResolver().applyBatch(
					ContactsContract.AUTHORITY, (ArrayList<ContentProviderOperation>) operations
				);
				for( int count = 0, max = resultArray.length; count < max; count++ ) {
					currentResult = resultArray[count].count;
					mResultCounter += currentResult;
					
					if( currentResult == 0 ) {
						mAllContactsConverted = false;
						mBackups.set(count, null);
					}
				}
				mConvertedContacts = mBackups.size();
				return mResultCounter > 0;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		
		private boolean removeBackupWhenRestored() {
			final List<ContentProviderOperation> operations = new ArrayList<ContentProviderOperation>();
			String selection = null;
			String[] selectionArgs = null;
			mResultCounter = 0;
			
			for( Backup data : mBackups ) {
				if( data == null ) {
					continue;
				}
				selection = BackupDataConverter.Columns.ID + "=?";
		        selectionArgs = new String[]{String.valueOf(data.getId())}; 
		        operations.add(
		        	ContentProviderOperation.newDelete(BackupDataConverter.URI)
		                .withSelection(selection, selectionArgs)
		                .build()
				);
			}
			
			try {
				final ContentProviderResult[] resultArray = getSherlockActivity().getContentResolver().applyBatch(
					Internationumber.AUTHORITY, (ArrayList<ContentProviderOperation>) operations
				);
				for( ContentProviderResult result : resultArray ) {
					mResultCounter += result.count;
				}
				return mResultCounter == resultArray.length;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
	}
	
	private List<Backup> getContactsViaFilter(Cursor data) {
		List<Backup> backups = new ArrayList<Backup>();
		long[] idArray = getSelectedIdArray();
		data.moveToPosition(-1);
		while( data.moveToNext()) {
			for (long id : idArray ) {
				if (data.getLong(data.getColumnIndex(BackupDataConverter.Columns.ID)) == id) {
					backups.add(mDataConverter.fromCursor(data));
					break;
				}
			}
		}
		return backups;
	}
}