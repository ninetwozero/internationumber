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
import android.view.View;
import android.widget.Checkable;
import android.widget.RelativeLayout;

public class CheckedRelativeLayout extends RelativeLayout implements Checkable {
	private Checkable mCheckableChild;
	private boolean mChecked;
	
	public CheckedRelativeLayout(Context context) {
		super(context);
	}

	public CheckedRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CheckedRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public boolean isChecked() {
		return mChecked;
	}

	@Override
	public void setChecked(boolean checked) {
		mChecked = checked;
		updateChild(mChecked);
	}

	@Override
	public void toggle() {
		mChecked = !mChecked;
		updateChild(mChecked);
	}
	
	public void setCheckableChild(Checkable view) {
		mCheckableChild = view;
		((View) mCheckableChild).setFocusable(false);
		((View) mCheckableChild).setFocusableInTouchMode(false);
		((View) mCheckableChild).setClickable(false);
	}
	
	private void updateChild(boolean checked) {
		if( mCheckableChild != null ) {
			mCheckableChild.setChecked(checked);
		}
	}
}
