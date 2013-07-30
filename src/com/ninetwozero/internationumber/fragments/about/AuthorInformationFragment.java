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

package com.ninetwozero.internationumber.fragments.about;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.ninetwozero.internationumber.R;

public class AuthorInformationFragment extends SherlockFragment {
	public static final String TAG = "AuthorInformationFragment";
	private SparseArray<Intent> mIntent;
	
	public AuthorInformationFragment() {
		mIntent = new SparseArray<Intent>();
		mIntent.put(R.id.wrap_web, new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.ninetwozero.com")));
		mIntent.put(R.id.wrap_twitter, new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.twitter.com/karllindmark")));
		mIntent.put(R.id.wrap_email, new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:support@ninetwozero.com")));
		mIntent.put(R.id.wrap_facebook, new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/ninetwozeroDOTcom")));
		mIntent.put(R.id.wrap_googleplus, new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/106847624338612843382/")));
	}
	
	public static AuthorInformationFragment newInstance() { 
		final AuthorInformationFragment fragment = new AuthorInformationFragment();
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
		final View view = inflater.inflate(R.layout.fragment_about_author, parent, false);
		for(int count = 0, max = mIntent.size(); count < max; count++ ){
			final int key = mIntent.keyAt(count);
			view.findViewById(key).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						startActivity(mIntent.get(key));
					}
				}
			);
		}
		return view;
	}
}