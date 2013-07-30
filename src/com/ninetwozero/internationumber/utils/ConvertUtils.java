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

package com.ninetwozero.internationumber.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.Context;
import android.provider.ContactsContract;

import com.ninetwozero.internationumber.database.entities.ContactDataConverter;
import com.ninetwozero.internationumber.datatypes.Contact;
import com.ninetwozero.internationumber.datatypes.Country;

final public class ConvertUtils {
	public static boolean convertInPhoneBook(Context activity, Country country, List<Contact> contacts) {
		final List<ContentProviderOperation> operations = new ArrayList<ContentProviderOperation>();
		final CountryUtils countryUtils = new CountryUtils(country.getCode(), country.getPrefixes());
		String number = null;
		String phoneSelector = null;
		String[] phoneSelectorArgs = null;
		
		for( Contact data : contacts ) {
	        number = countryUtils.convert(data.getNumber());
			phoneSelector = ContactDataConverter.Columns.ID + "=? AND " + ContactsContract.Data.MIMETYPE + "='"  + ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE + "'";
	        phoneSelectorArgs = new String[]{String.valueOf(data.getId())}; 
			operations.add(
				ContentProviderOperation.newUpdate(ContactsContract.Data.CONTENT_URI)
                .withSelection(phoneSelector, phoneSelectorArgs)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, number)
                .build()
			);
		}
		
		try {
			int resultCounter = 0;
			ContentProviderResult[] resultArray = activity.getContentResolver().applyBatch(
				ContactsContract.AUTHORITY, (ArrayList<ContentProviderOperation>) operations
			);
			for( ContentProviderResult result : resultArray ) {
				resultCounter += result.count;
			}
			return resultCounter == resultArray.length;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
