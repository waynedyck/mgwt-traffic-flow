/*
 * Copyright 2015 Wayne Dyck
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.imaginedreal.mgwt.trafficflow.client.util;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.TimeZone;

public class ParserUtils {

	/**
	 * 
	 * @param createdAt
	 * @param datePattern
	 * @param isUTC
	 * @return
	 */
	public static String relativeTime(String createdAt, String datePattern, boolean isUTC) {
		DateTimeFormat parseDateFormat = DateTimeFormat.getFormat(datePattern);
		Date parseDate;

		try {
			if (isUTC) {
				parseDate = parseDateFormat.parse(parseDateFormat.format(
						parseDateFormat.parse(createdAt),
						TimeZone.createTimeZone(0)));
			} else {		
				parseDate = parseDateFormat.parse(createdAt);
			}
		} catch (IllegalArgumentException e) {
			return "Unavailable";
		}

		return getRelative(parseDate);

	}

	/**
	 * 
	 * @param createdAt
	 * @return
	 */
	public static String relativeTime(Date createdAt) { 
		return(getRelative(createdAt));
	}

	/**
	 * 
	 * @param date
	 * @return
	 */
	private static String getRelative(Date date) {
		DateTimeFormat displayDateFormat = DateTimeFormat.getFormat("MMMM d, yyyy h:mm a");	
		int delta = 0;

		try {
			Date relativeDate = new Date();
			delta = (int)((relativeDate.getTime() - date.getTime()) / 1000); // convert to seconds
			if (delta < 60) {
				return delta + " seconds ago";
			} else if (delta < 120) {
				return "1 minute ago";
			} else if (delta < (60*60)) {
				return Integer.toString(delta / 60) + " minutes ago";
			} else if (delta < (120*60)) {
				return "1 hour ago";
			} else if (delta < (24*60*60)) {
				return Integer.toString(delta / 3600) + " hours ago";
			} else {
				return displayDateFormat.format(date);
			}
		} catch (Exception e) {
			return "Unavailable";
		}		

	}

}
