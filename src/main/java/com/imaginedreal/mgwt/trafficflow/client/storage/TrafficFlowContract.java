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

package com.imaginedreal.mgwt.trafficflow.client.storage;

public interface TrafficFlowContract {

	interface CachesColumns {
		String _ID = "_ID";
		String CACHE_TABLE_NAME = "cache_table_name";
		String CACHE_LAST_UPDATED = "cache_last_updated";
	}
	
	interface StationsColumns {
	    String _ID = "_ID";
	    String STATION_ID = "id";
	    String STATION_VOLUME = "volume";
	    String STATION_SPEED = "speed";
	    String STATION_OCCUPANCY = "occupancy";
	}
}
