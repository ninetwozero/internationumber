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

package com.ninetwozero.internationumber.database.entities;

import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import com.ninetwozero.internationumber.datatypes.Contact;

final public class ContactDataConverter implements IDataConverter<Contact> {
	public static final Uri URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
	public static final Uri EDIT_URI = ContactsContract.RawContacts.CONTENT_URI;
	
	public Contact fromCursor(Cursor c) {
		final int id = c.getInt(c.getColumnIndex(Columns.ID));
		final String name = c.getString(c.getColumnIndex(Columns.NAME));
		final String number = c.getString(c.getColumnIndex(Columns.NUMBER));
		return new Contact(id, name, number);
	}
	
	public ContentValues toContentValues(Contact contact) {
		final ContentValues contentValues = new ContentValues();
		contentValues.put(Columns.ID, contact.getId());
		contentValues.put(Columns.NAME, contact.getName());
		contentValues.put(Columns.NUMBER, contact.getNumber());
		return contentValues;
	}
	
	public ContentValues[] toContentValueArray(List<Contact> contacts) {
		final int numContacts = contacts.size();
		final ContentValues[] contentValues = new ContentValues[numContacts];
		for( int count = 0; count < numContacts; count++ ) {
			final Contact data = contacts.get(count);
			contentValues[count] = new ContentValues();
			
			contentValues[count].put(Columns.ID, data.getId());
			contentValues[count].put(Columns.NAME, data.getName());
			contentValues[count].put(Columns.NUMBER, data.getNumber());
		}
		return contentValues;
	}
	
	public static class Columns {
		public static final String ID = ContactsContract.RawContacts._ID;
		public static final String NAME = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME;
		public static final String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
		
		public static final String[] getAll() {
			return new String[] { ID, NAME, NUMBER };
		}
	}
}
