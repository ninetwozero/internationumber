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

import com.ninetwozero.internationumber.Internationumber;
import com.ninetwozero.internationumber.datatypes.Backup;
import com.ninetwozero.internationumber.datatypes.Contact;
import com.ninetwozero.internationumber.datatypes.Country;
import com.ninetwozero.internationumber.utils.CountryUtils;

final public class BackupDataConverter implements IDataConverter<Backup> {
	public static final Uri URI = Uri.parse("content://" + Internationumber.AUTHORITY + "/backup/");
	
	public Backup fromCursor(Cursor c) {
		final int id = c.getInt(c.getColumnIndex(Columns.ID));
		final int contactRowId = c.getInt(c.getColumnIndex(Columns.ROW_ID));
		final String name = c.getString(c.getColumnIndex(Columns.NAME));
		final String previous = c.getString(c.getColumnIndex(Columns.BEFORE));
		final String current = c.getString(c.getColumnIndex(Columns.AFTER));
		return new Backup(id, name, previous, current, contactRowId);
	}
	
	public ContentValues toContentValues(Backup backup) {
		final ContentValues contentValues = new ContentValues();
		contentValues.put(Columns.ID, backup.getId());
		contentValues.put(Columns.ROW_ID, backup.getContactId());
		contentValues.put(Columns.NAME, backup.getName());
		contentValues.put(Columns.BEFORE, backup.getPreviousNumber());
		contentValues.put(Columns.AFTER, backup.getCurrentNumber());
		return contentValues;
	}

	@Override
	public ContentValues[] toContentValueArray(List<Backup> backups) {
		final int numBackups = backups.size();
		final ContentValues[] contentValues = new ContentValues[numBackups];
		for( int count = 0; count < numBackups; count++ ) {	
			contentValues[count] = toContentValues(backups.get(count));
		}
		return contentValues;
	}

	public ContentValues[] fromContacts(List<Contact> contacts, Country country) {
		final int numContacts = contacts.size();
		final ContentValues[] contentValues = new ContentValues[numContacts];
		final CountryUtils countryUtils = new CountryUtils(country.getCode(), country.getPrefixes());
		for( int count = 0; count < numContacts; count++ ) {	
			final Contact contact = contacts.get(count);
			final String number = countryUtils.convert(contact.getNumber());
			
			contentValues[count] = new ContentValues();
			contentValues[count].put(Columns.ROW_ID, contact.getId());
			contentValues[count].put(Columns.NAME, contact.getName());
			contentValues[count].put(Columns.BEFORE, contact.getNumber());
			contentValues[count].put(Columns.AFTER, number);
		}
		return contentValues;
	}
	
	public static class Columns {
		public static final String ID = "_id";
		public static final String NAME = "name";
		public static final String BEFORE = "before";
		public static final String AFTER = "after";
		public static final String ROW_ID = "ROW_ID";
		
		public static final String[] getAll() {
			return new String[] { ID, NAME, BEFORE, AFTER, ROW_ID };
		}
	}
}
