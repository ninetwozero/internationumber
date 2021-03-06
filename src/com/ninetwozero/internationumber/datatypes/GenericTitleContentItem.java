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

package com.ninetwozero.internationumber.datatypes;


public class GenericTitleContentItem {
	private final int mTitle;
	private final int mContent;
	
	public GenericTitleContentItem(int title, int content) {
		mTitle = title;
		mContent = content;
	}

	public int getTitle() {
		return mTitle;
	}
	
	public int getContent() {
		return mContent;
	}
}
