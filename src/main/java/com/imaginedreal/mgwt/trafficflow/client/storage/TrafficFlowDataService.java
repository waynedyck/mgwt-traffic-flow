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

import java.util.List;

import com.google.code.gwt.database.client.GenericRow;
import com.google.code.gwt.database.client.service.Connection;
import com.google.code.gwt.database.client.service.DataService;
import com.google.code.gwt.database.client.service.ListCallback;
import com.google.code.gwt.database.client.service.RowIdListCallback;
import com.google.code.gwt.database.client.service.ScalarCallback;
import com.google.code.gwt.database.client.service.Select;
import com.google.code.gwt.database.client.service.Update;
import com.google.code.gwt.database.client.service.VoidCallback;
import com.imaginedreal.mgwt.trafficflow.client.storage.TrafficFlowContract.CachesColumns;
import com.imaginedreal.mgwt.trafficflow.client.storage.TrafficFlowContract.StationsColumns;
import com.imaginedreal.mgwt.trafficflow.shared.CacheItem;
import com.imaginedreal.mgwt.trafficflow.shared.StationItem;

@Connection(name="trafficflow", version="", description="TrafficFlow Database", maxsize=2000000)
public interface TrafficFlowDataService extends DataService {
	
	interface Tables {
    	String CACHES = "caches";
        String STATIONS = "stations";
    }
	
	/**
	 * Create initial table structures in database.
	 * 
	 * @param callback
	 */
	@Update("CREATE TABLE IF NOT EXISTS " + Tables.CACHES + " ("
			+ CachesColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ CachesColumns.CACHE_TABLE_NAME + " TEXT,"
			+ CachesColumns.CACHE_LAST_UPDATED + " INTEGER)")
	void createCachesTable(VoidCallback callback);

	@Update("CREATE TABLE IF NOT EXISTS " + Tables.STATIONS + " ("
			+ StationsColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ StationsColumns.STATION_ID + " TEXT,"
			+ StationsColumns.STATION_VOLUME + " INTEGER NOT NULL default 0,"
			+ StationsColumns.STATION_SPEED + " INTEGER NOT NULL default 0,"
			+ StationsColumns.STATION_OCCUPANCY + " INTEGER NOT NULL default 0)")
	void createStationsTable(VoidCallback callback);

	/**
	 * Initialize cache table.
	 * 
	 * @param items
	 * @param callback
	 */
	@Update(sql="INSERT INTO " + Tables.CACHES + " ("
			+ CachesColumns.CACHE_TABLE_NAME + ", "
			+ CachesColumns.CACHE_LAST_UPDATED + ") "
			+ "VALUES ({_.getTableName()}, {_.getLastUpdated()})", foreach="cacheItems")
	void initCachesTable(List<CacheItem> cacheItems, RowIdListCallback callback);

	/**
	 * Update timestamp in cache table.
	 * 
	 * @param cacheItems
	 * @param callback
	 */
	@Update(sql="UPDATE " + Tables.CACHES
			+ " SET " + CachesColumns.CACHE_LAST_UPDATED + " = {_.getLastUpdated()}" 
			+ " WHERE " + CachesColumns.CACHE_TABLE_NAME + " LIKE {_.getTableName()}", foreach="cacheItems")
	void updateCachesTable(List<CacheItem> cacheItems, VoidCallback callback);	
	
	/**
	 * Obtains the number of entries in the caches table.
	 * 
	 * @param callback
	 */
	@Select("SELECT count(*) FROM " + Tables.CACHES)
	void getCachesTableCount(ScalarCallback<Integer> callback);

	/** 
	 * Returns the last time data was downloaded and cached for a specific table.
	 * 
	 * @param tableName
	 * @param callback
	 */
	@Select("SELECT " + CachesColumns.CACHE_LAST_UPDATED
			+ " FROM " + Tables.CACHES
			+ " WHERE "	+ CachesColumns.CACHE_TABLE_NAME
			+ " LIKE {tableName}")
	void getCacheLastUpdated(String tableName, ListCallback<GenericRow> callback);

	/**
	 * Delete all rows from stations table.
	 * 
	 * @param callback
	 */
	@Update("DELETE FROM " + Tables.STATIONS)
	void deleteStations(VoidCallback callback);
	
	/**
	 * Insert stations into table.
	 * 
	 * @param stationItems
	 * @param callback
	 */
	@Update(sql="INSERT INTO " + Tables.STATIONS + " ("
			+ StationsColumns.STATION_ID + ", "
			+ StationsColumns.STATION_VOLUME + ", "
			+ StationsColumns.STATION_SPEED + ", "
			+ StationsColumns.STATION_OCCUPANCY + ") "
			+ "VALUES "
			+ "({_.getId()}, {_.getVolume()}, "
			+ "{_.getSpeed()}, {_.getOccupancy()})", foreach="stationItems")
	void insertStations(List<StationItem> stationItems, RowIdListCallback callback);
	
	/**
	 * Retrieve stations from table.
	 * 
	 * @param callback
	 */
	@Select("SELECT * FROM " + Tables.STATIONS)
	void getStations(ListCallback<GenericRow> callback);
	
	/**
	 * Retrieve individual station.
	 * 
	 * @param stationId
	 * @param callback
	 */
	@Select("SELECT * FROM " + Tables.STATIONS
			+ " WHERE " + StationsColumns.STATION_ID
			+ " = {stationId}")
	void getStation(String stationId, ListCallback<GenericRow> callback);	
}
