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

package com.ninetwozero.internationumber.fragments.help;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.ninetwozero.internationumber.R;
import com.ninetwozero.internationumber.abstractions.AbstractListFragment;
import com.ninetwozero.internationumber.adapters.GenericTitleContentListAdapter;
import com.ninetwozero.internationumber.datatypes.GenericTitleContentItem;

public class FAQListFragment extends AbstractListFragment {
	public static final String TAG = "FAQListFragment";
	private List<GenericTitleContentItem> mQuestions;
	
	public static FAQListFragment newInstance() { 
		final FAQListFragment fragment = new FAQListFragment();
		fragment.setArguments(new Bundle());
		return fragment;
	} 
	
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setRetainInstance(true);
		setupQuestions();
	}
	
	private void setupQuestions() {
		mQuestions = new ArrayList<GenericTitleContentItem>();
		mQuestions.add(new GenericTitleContentItem(R.string.content_faq_q1, R.string.content_faq_a1));
		mQuestions.add(new GenericTitleContentItem(R.string.content_faq_q2, R.string.content_faq_a2));
		mQuestions.add(new GenericTitleContentItem(R.string.content_faq_q3, R.string.content_faq_a3));
		mQuestions.add(new GenericTitleContentItem(R.string.content_faq_q4, R.string.content_faq_a4));
		mQuestions.add(new GenericTitleContentItem(R.string.content_faq_q5, R.string.content_faq_a5));
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_default_list_noheader, parent, false);
		final ListView listView = (ListView) view.findViewById(android.R.id.list);
		final TextView textView = (TextView) view.findViewById(android.R.id.empty);
		
		final GenericTitleContentListAdapter adapter = new GenericTitleContentListAdapter(getSherlockActivity(), inflater, mQuestions);
		listView.setAdapter(adapter);
		listView.setChoiceMode(ListView.CHOICE_MODE_NONE);
		textView.setText(getString(R.string.msg_no_questions));
		return view;
	}
}