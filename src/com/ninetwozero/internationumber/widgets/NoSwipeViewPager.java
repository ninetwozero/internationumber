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
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class NoSwipeViewPager extends ViewPager {
    private boolean mAllowSwiping;

    public NoSwipeViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mAllowSwiping = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mAllowSwiping) {
            return super.onTouchEvent(event);
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (mAllowSwiping) {
            return super.onInterceptTouchEvent(event);
        }
        return false;
    }

    public void setAllowSwiping(boolean a) {
        mAllowSwiping = a;
    }
}
