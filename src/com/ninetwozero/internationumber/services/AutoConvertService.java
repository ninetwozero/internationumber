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

package com.ninetwozero.internationumber.services;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.ninetwozero.internationumber.R;
import com.ninetwozero.internationumber.activities.BackupActivity;
import com.ninetwozero.internationumber.database.entities.BackupDataConverter;
import com.ninetwozero.internationumber.database.entities.ContactDataConverter;
import com.ninetwozero.internationumber.datatypes.Contact;
import com.ninetwozero.internationumber.datatypes.Country;
import com.ninetwozero.internationumber.utils.ContactUtils;
import com.ninetwozero.internationumber.utils.ConvertUtils;

public class AutoConvertService extends Service {
	public static final String INTENT_ID = "idArray";
	public static final String INTENT_COUNTRY = "country";
	public static final String INTENT_PENDING_INTENT = "pendingIntent";
	
	private static final int NOTIFICATION = R.string.service_name;
	private static final String TAG = "AutoConvertService";

	private BackupDataConverter mBackupDataConverter;
	private ContactDataConverter mContactDataConverter;
	private long[] mContactIdArray;
	private Country mCountry;
    private boolean mIsRunningFromGui = false;
    private NotificationManager mNotificationManager;
    private PendingIntent mPendingIntent;
    
    public class LocalBinder extends Binder {
        AutoConvertService getService() {
            return AutoConvertService.this;
        }
    }

    @Override
    public void onCreate() {
        mNotificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        mContactIdArray = new long[] {};
		mContactDataConverter = new ContactDataConverter();
		mBackupDataConverter = new BackupDataConverter();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if( intent.hasExtra(INTENT_COUNTRY) ) {
        	getIntentData(intent);
        	if( hasValidCountry() ) {
		        load();
		        return START_STICKY;
        	}
    	}
    	Log.w(TAG, "Missing required data: country. Stopping service.");
    	stopSelf();
    	return START_NOT_STICKY;
    }

    private void getIntentData(final Intent intent) {
    	mCountry = intent.getParcelableExtra(INTENT_COUNTRY);
    	
    	if( intent.hasExtra(INTENT_ID) ) {
        	mContactIdArray = intent.getLongArrayExtra(INTENT_ID);
        }
    	
    	if( intent.hasExtra(INTENT_PENDING_INTENT) ) {
    		mIsRunningFromGui = true;
    		mPendingIntent = intent.getParcelableExtra(INTENT_PENDING_INTENT);
    	}
    }
    
    private boolean hasValidCountry() {
    	if( mCountry == null || mCountry.getCode() == 0 ) {
    		return false;
    	}
    	return true;
    }
    
	@Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private final IBinder mBinder = new LocalBinder();
    
	private void load() {
		new AsyncFinishTask().execute();
	}
	
	private List<Contact> getContactsViaFilter(Cursor data) {
		List<Contact> contacts = new ArrayList<Contact>();
		while ( data.moveToNext() ) {
			if( mContactIdArray.length == 0 ) {
				contacts.add(mContactDataConverter.fromCursor(data));
			} else {
				for (long id : mContactIdArray) {
					if (data.getLong(data.getColumnIndex(ContactDataConverter.Columns.ID)) == id) {
						contacts.add(mContactDataConverter.fromCursor(data));
						break;
					}
				}
			}
		}
		return contacts;
	}
	
	private boolean doBackupToDatabase(List<Contact> contacts) {
		return getContentResolver().bulkInsert(
			BackupDataConverter.URI, 
			mBackupDataConverter.fromContacts(contacts, mCountry)
		) > 0;
	}
	
	private class AsyncFinishTask extends AsyncTask<Cursor, Integer, Integer> {		

		@Override
		protected Integer doInBackground(Cursor... params) {
	    	final Cursor cursor = getContentResolver().query(
				ContactDataConverter.URI, 
				ContactDataConverter.Columns.getAll(),
				ContactUtils.getQueryString(),
				null, 
				ContactDataConverter.Columns.NAME + " ASC"
			);

			Log.i(TAG, "Checking for contacts to convert...");
	    	if( cursor.getCount() > 0 ) {
				List<Contact> contacts = getContactsViaFilter(cursor);
				Log.i(TAG, "Backing up the altered contacts to the DB...");
				if( contacts.size() > 0 && doBackupToDatabase(contacts) ) {
					Log.i(TAG, "Updating the contacts in the phonebook...");
					if( ConvertUtils.convertInPhoneBook(getApplicationContext(), mCountry, contacts) ) {
						Log.i(TAG, "Executed successfully.");
						return contacts.size();
					}
				}
	    	}
			Log.i(TAG, "No contacts found in need of conversion.");
			return 0;
		}		
		
		@Override
		protected void onPostExecute(Integer results) {
			final Context context = getApplicationContext();
			if( mIsRunningFromGui ) {
				try {
					final Intent data = new Intent().putExtra("numberOfConversions", results);
					mPendingIntent.send(context, Activity.RESULT_OK, data);
				} catch (CanceledException e) {
					e.printStackTrace();
				}
			} else {
				if( results > 0 ) {
					final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
					if( preferences.getBoolean("should_notify", true) ) {
						showNotification(results);
					}
				}
			}
			stopSelf();
		}
	}

    private void showNotification(final int numberOfContacts) {
    	 final Notification notification = new NotificationCompat.Builder(AutoConvertService.this)
	         .setContentTitle(String.format(getString(R.string.msg_service_title), numberOfContacts))
	         .setContentText(getString(R.string.msg_service_subtitle))
	         .setSmallIcon(R.drawable.ic_launcher)
	         .setWhen(System.currentTimeMillis())
	         .setAutoCancel(true)
	         .setContentIntent(PendingIntent.getActivity(this, 0, new Intent(this, BackupActivity.class), 0)).build();
        mNotificationManager.notify(NOTIFICATION, notification);
    }
    
    public static final PendingIntent getPendingIntent(Context c, Intent data) {
    	return PendingIntent.getService(c, 0, data, PendingIntent.FLAG_CANCEL_CURRENT);
    }
    
    public static final Intent getIntent(final Context context) {
    	return new Intent(context, AutoConvertService.class);
    }
    
    public static final Intent getIntent(final Context context, final long[] idArray, final Country country, final PendingIntent intent) {
    	return new Intent(context, AutoConvertService.class)
			.putExtra(AutoConvertService.INTENT_ID, idArray)
			.putExtra(AutoConvertService.INTENT_COUNTRY, country)
			.putExtra(AutoConvertService.INTENT_PENDING_INTENT, intent);
    }
}