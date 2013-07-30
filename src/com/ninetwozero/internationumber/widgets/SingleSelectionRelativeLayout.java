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

package com.ninetwozero.internationumber.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.RelativeLayout;

import com.ninetwozero.internationumber.R;

public class SingleSelectionRelativeLayout extends RelativeLayout implements Checkable {
	private boolean mChecked;
	
	public SingleSelectionRelativeLayout(Context context) {
		super(context);
	}

	public SingleSelectionRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SingleSelectionRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public boolean isChecked() {
		return mChecked;
	}

	@Override
	public void setChecked(boolean checked) {
		mChecked = checked;
		updateBackground();
	}

	@Override
	public void toggle() {
		mChecked = !mChecked;
		updateBackground();
	}
	
	private void updateBackground() {
		if( mChecked ) {
			setBackgroundColor(getResources().getColor(R.color.selection_blue));
		} else {
			setBackgroundColor(getResources().getColor(android.R.color.transparent));
		}
	}
}
