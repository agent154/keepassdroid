/*
 * Copyright 2011-2014 Brian Pellin.
 *     
 * This file is part of KeePassDroid.
 *
 *  KeePassDroid is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  KeePassDroid is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with KeePassDroid.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.keepassdroid.database.iterator;

import java.util.NoSuchElementException;

import com.keepassdroid.database.PwEntryV3;
import com.keepassdroid.database.SearchParameters;

public class EntrySearchStringIteratorV3 extends EntrySearchStringIterator {

	private PwEntryV3					entry;
	private SearchParameters	sp;

	public EntrySearchStringIteratorV3(PwEntryV3 entry) {
		this.entry = entry;
		this.sp = SearchParameters.DEFAULT;
	}

	public EntrySearchStringIteratorV3(PwEntryV3 entry, SearchParameters sp) {
		this.entry = entry;
		this.sp = sp;
	}

	private static final int	title				= 0;
	private static final int	url					= 1;
	private static final int	username		= 2;
	private static final int	comment			= 3;
	private static final int	maxEntries	= 4;

	private int								current			= 0;

	@Override
	public boolean hasNext() {
		return current < maxEntries;
	}

	@Override
	public String next() {
		// Past the end of the list
		if (current == maxEntries) {
			throw new NoSuchElementException("Past final string");
		}

		useSearchParameters();

		String str = getCurrentString();
		current++;
		return str;
	}

	private void useSearchParameters() {

		if (sp == null) {
			return;
		}

		boolean found = false;

		while (!found) {
			switch (current) {
				case title:
					found = sp.searchInTitles;

				case url:
					found = sp.searchInUrls;

				case username:
					found = sp.searchInUserNames;

				case comment:
					found = sp.searchInNotes;

				default:
					found = true;
			}

			if (!found) {
				current++;
			}
		}
	}

	private String getCurrentString() {
		switch (current) {
			case title:
				return entry.getTitle();

			case url:
				return entry.getUrl();

			case username:
				return entry.getUsername();

			case comment:
				return entry.getNotes();

			default:
				return "";
		}
	}

}
