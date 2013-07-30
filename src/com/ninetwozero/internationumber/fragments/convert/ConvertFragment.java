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

package com.ninetwozero.internationumber.fragments.convert;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.ninetwozero.internationumber.R;
import com.ninetwozero.internationumber.datatypes.Country;
import com.ninetwozero.internationumber.services.AutoConvertService;

public class ConvertFragment extends SherlockFragment {
	public static final String TAG = "ConvertFragment";
	
	private long[] mContactIdArray;
	private Country mCountry;
	private View mProgressBar;
	private TextView mTextStatus;
	private TextView mTextSubStatus;
	private View mWrapStatus;
	
	
	public static ConvertFragment newInstance() { 
		ConvertFragment fragment = new ConvertFragment();
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
		final View view = inflater.inflate(R.layout.fragment_convert_finish, parent, false);

		mProgressBar = view.findViewById(R.id.progressBar1);
		mWrapStatus = view.findViewById(R.id.wrap_status);
		mTextStatus = (TextView) mWrapStatus.findViewById(R.id.text1);
		mTextSubStatus = (TextView) mWrapStatus.findViewById(R.id.text2);
		
		mProgressBar.setVisibility(View.VISIBLE);
		mWrapStatus.setVisibility(View.INVISIBLE);
		return view;
	}

	public void setCountry(Country country) {
		mCountry = country;
	}
	
	public void setContactIdArrayAndLoad(long[] idArray) {
		mContactIdArray = idArray.clone();
		load();
	}
	
	public void load() {
		final SherlockFragmentActivity activity = getSherlockActivity();
		PendingIntent pendingIntent = activity.createPendingResult(0, new Intent(), PendingIntent.FLAG_ONE_SHOT);
		Intent intent = AutoConvertService.getIntent(activity, mContactIdArray, mCountry, pendingIntent);
		activity.startService(intent);
		toggleViewPort(true);
	}
	
	public void toggleViewPort(boolean isLoading) {
		if( isLoading ) {
			mProgressBar.setVisibility(View.VISIBLE);
			mWrapStatus.setVisibility(View.INVISIBLE);
		} else {
			mProgressBar.setVisibility(View.INVISIBLE);
			mWrapStatus.setVisibility(View.VISIBLE);
		}
	}
	
	private void showResultForUser(Integer numberOfConversions) {
		if( numberOfConversions > 0 ) {
			mTextStatus.setText(R.string.msg_ok);
			if( numberOfConversions == 1 ) {
				mTextSubStatus.setText(R.string.msg_num_contact_converted);
			} else {
				mTextSubStatus.setText(String.format(getString(R.string.msg_num_contacts_converted), numberOfConversions));
			}
			Toast.makeText(getSherlockActivity(), R.string.msg_convert_ok, Toast.LENGTH_SHORT).show();
		} else {
			mTextStatus.setText(R.string.msg_fail);
			mTextSubStatus.setText("");
			Toast.makeText(getSherlockActivity(), R.string.msg_convert_fail, Toast.LENGTH_SHORT).show();
		}
		toggleViewPort(false);
	}
	
	public void onServiceFinished(int numberOfConversions) {
		showResultForUser(numberOfConversions);
	}
}