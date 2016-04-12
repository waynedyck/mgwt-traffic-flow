/*
 * Copyright 2016 Wayne Dyck
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

public class Consts {

	public static final String HOST_URL = "INSERT_SERVER_URL_HERE";
	public static final boolean ANALYTICS_ENABLED = false;
	public static final boolean ADS_ENABLED = false;
	
	/**
	 * The Event Category title in Google Analytics.
	 */
	public static final String EVENT_TRACKING_CATEGORY = "TrafficFlow";
	
	/**
	 * Current version number and build of the app.
	 */
	public static final String APP_VERSION = "1.0.0";

    /**
     * Ad unit Id for banner
     */
    public static final String AD_UNIT_ID = "/6499/example/banner";

    /**
     * Unique Tracking Id from Property Settings in Google Analytics
     */
    public static final String ANALYTICS_TRACKING_ID = "UA-XXXXXX-XX";

	/**
	 * The caller references the constants using <tt>Consts.EMPTY_STRING</tt>,
	 * and so on. Thus, the caller should be prevented from constructing objects of
	 * this class, by declaring this private constructor.
	 */	
	private Consts() {
		//this prevents even the native class from 
		//calling this ctor as well :
		throw new AssertionError();
	}
}
