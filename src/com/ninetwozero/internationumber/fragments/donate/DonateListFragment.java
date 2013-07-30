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

package com.ninetwozero.internationumber.fragments.donate;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockListFragment;
import com.android.vending.billing.util.IabHelper;
import com.android.vending.billing.util.IabResult;
import com.android.vending.billing.util.Purchase;
import com.ninetwozero.internationumber.R;
import com.ninetwozero.internationumber.GooglePlayDeveloper;
import com.ninetwozero.internationumber.adapters.DonateListAdapter;


public class DonateListFragment extends SherlockListFragment {
	public static final int REQUEST_IAP = 0;
	public static final String TAG = "DonateListFragment";

	private DonateListAdapter mAdapter;
	private IabHelper mHelper;
	private boolean mInAppPaymentIsReady = false;
	
	public DonateListFragment() {
		super();
	}
	
	public static DonateListFragment newInstance() { 
		DonateListFragment fragment = new DonateListFragment();
		fragment.setArguments(new Bundle());
		return fragment;
	} 
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setRetainInstance(true);
		
		mHelper = new IabHelper(getSherlockActivity(), GooglePlayDeveloper.PLAY_API_KEY);
		mHelper.startSetup(
			new IabHelper.OnIabSetupFinishedListener() {
	            public void onIabSetupFinished(IabResult result) {
	                if (result.isFailure()){
	                	Toast.makeText(getSherlockActivity(), "Problem setting up in-app billing: " + result, Toast.LENGTH_SHORT).show();
	                } else {
	                	mInAppPaymentIsReady = true;
	                }
	            }
	        }
		);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_default_list, parent, false);
		setupViews(view);
		setupListView(view);
		return view;
	}
    @Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    
	private void setupListView(View view) {
		final ListView listView = (ListView) view.findViewById(android.R.id.list);
		final TextView textView = (TextView) view.findViewById(android.R.id.empty);
		
		mAdapter = new DonateListAdapter(getSherlockActivity(), getSherlockActivity().getLayoutInflater(), generateValidAmounts());
		listView.setAdapter(mAdapter);
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		textView.setText(getString(R.string.msg_no_donate_options));
	}

	private List<String> generateValidAmounts() {
		List<String> validAmounts = new ArrayList<String>();
		validAmounts.add("1");
		validAmounts.add("3");
		validAmounts.add("5");
		validAmounts.add("10");
		validAmounts.add("30");
		validAmounts.add("50");
		validAmounts.add("100");
		return validAmounts;
	}
	
	private void setupViews(View view) {
		final TextView headerTextView = (TextView) view.findViewById(R.id.heading);
		headerTextView.setText(R.string.text_info_donate_header);
	}

	@Override
	public void onListItemClick(ListView listView, View view, int position, long id) {
		if( mInAppPaymentIsReady ) {
			String token = String.valueOf(view.getTag());
        	mHelper.launchPurchaseFlow(getSherlockActivity(), token, REQUEST_IAP, mOnPurchaseFinishedListener);
		} else {
			Toast.makeText(getSherlockActivity(), R.string.info_text_donate_unable, Toast.LENGTH_SHORT).show();
		}
	}
	
    IabHelper.OnIabPurchaseFinishedListener mOnPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            if (purchase == null || result.isFailure()) {
            	Toast.makeText(getSherlockActivity(), R.string.msg_donation_fail, Toast.LENGTH_SHORT).show();
            	return;
            }
            mHelper.consumeAsync(purchase, mConsumeFinishedListener);
        }
    };
    
    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
        public void onConsumeFinished(Purchase purchase, IabResult result) {
            Log.d(TAG, "Consumption finished. Purchase: " + purchase + ", result: " + result);
            if (result.isSuccess()) {
                Toast.makeText(getSherlockActivity(), R.string.msg_donation_ok, Toast.LENGTH_SHORT).show();
            } else {
            	Toast.makeText(getSherlockActivity(), R.string.msg_donation_fail, Toast.LENGTH_SHORT).show();
                
            }
        }
    };

    @Override
    public void onDestroy() {
    	super.onDestroy();
        if (mHelper != null) {
        	mHelper.dispose();
            mHelper = null;
        }
    }
}