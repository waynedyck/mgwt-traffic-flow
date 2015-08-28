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

package com.imaginedreal.mgwt.trafficflow.client.activities.seattle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.code.gwt.database.client.GenericRow;
import com.google.code.gwt.database.client.service.DataServiceException;
import com.google.code.gwt.database.client.service.ListCallback;
import com.google.code.gwt.database.client.service.RowIdListCallback;
import com.google.code.gwt.database.client.service.VoidCallback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.jsonp.client.JsonpRequestBuilder;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;
import com.googlecode.mgwt.mvp.client.MGWTAbstractActivity;
import com.imaginedreal.mgwt.trafficflow.client.ClientFactory;
import com.imaginedreal.mgwt.trafficflow.client.activities.camera.CameraPlace;
import com.imaginedreal.mgwt.trafficflow.client.resources.Resources;
import com.imaginedreal.mgwt.trafficflow.client.storage.TrafficFlowContract.CachesColumns;
import com.imaginedreal.mgwt.trafficflow.client.storage.TrafficFlowContract.StationsColumns;
import com.imaginedreal.mgwt.trafficflow.client.storage.TrafficFlowDataService;
import com.imaginedreal.mgwt.trafficflow.client.storage.TrafficFlowDataService.Tables;
import com.imaginedreal.mgwt.trafficflow.shared.CacheItem;
import com.imaginedreal.mgwt.trafficflow.shared.CameraItem;
import com.imaginedreal.mgwt.trafficflow.shared.FlowDataFeed;
import com.imaginedreal.mgwt.trafficflow.shared.StationItem;

public class SeattleActivity extends MGWTAbstractActivity implements
		SeattleView.Presenter {

	private final ClientFactory clientFactory;
	private SeattleView view;
	private TrafficFlowDataService dbService;
    private static final String FLOW_LINES = Resources.INSTANCE.seattleAreaXY().getText();
    private static final String CAMERA_LINES = Resources.INSTANCE.seattleAreaCamerasXY().getText();
    private static Map<String, StationItem> stationItemsMap = new HashMap<String, StationItem>();
    private static List<StationItem> stationItems = new ArrayList<StationItem>();
    private static List<CameraItem> cameraItems = new ArrayList<CameraItem>();
    private static Storage localStorage;
	private static DateTimeFormat parseDateFormat = DateTimeFormat.getFormat("EEE MMM d HH:mm:ss yyyy"); // Sun Jan 25 00:29:22 2015
	private static DateTimeFormat displayDateFormat = DateTimeFormat.getFormat("MMMM d, yyyy h:mm a"); // January 25, 2015 00:29 AM
    
	public SeattleActivity(ClientFactory clientFactory) {
		this.clientFactory = clientFactory;
	}

	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		view = clientFactory.getSeattleView();
		dbService = clientFactory.getDbService();
		view.setPresenter(this);
		
		localStorage = Storage.getLocalStorageIfSupported();
		
		buildStationList();
		getCameras();
		getFlowData();
		panel.setWidget(view);
	}

	@Override
	public void onStop() {
		localStorage.setItem("KEY_SEATTLE_MAP_XY", view.getMapXY());
		view.setPresenter(null);
	}
	
	private void buildStationList() {
		MatchResult headingMatcher;
		
		// Regular expression patterns
		String headingPattern = "^\\*\\*~~([A-Z']+)~~\\*\\*"; // **~~LOOPS~~**
		String routesPattern = "^\\*\\*([A-Z0-9\\s\\-]+)\\*\\*"; // **I-5**
		
		// Compile and use regular expressions
		RegExp headingRegExp = RegExp.compile(headingPattern);
		RegExp routesRegExp = RegExp.compile(routesPattern);
		
		List<String> loops = Arrays.asList(FLOW_LINES.split("\\n"));
		
		stationItemsMap.clear();
		
        for (String loop: loops.subList(6, loops.size())) { // Start at **~~LOOPS~~**
			headingMatcher = headingRegExp.exec(loop);
			boolean headingMatchFound = (headingMatcher != null);
			
			if (headingMatchFound) {
				if (headingMatcher.getGroup(1).equalsIgnoreCase("loops")) {
					continue;
				}
			}

			MatchResult routesMatcher = routesRegExp.exec(loop);
			boolean routesMatchFound = (routesMatcher != null);

			if (routesMatchFound) {
					continue;
			}
			
			StationItem stationItem = new StationItem();
            String[] values = loop.split(",", 2); // 002es00068:_ME_Stn,410,707;
            stationItem.setId(values[0]); // 002es00068:_ME_Stn
            stationItem.setXY(values[1]); // 410,707;416,708;404,708;405,705;416,705
            
            stationItemsMap.put(stationItem.getId(), stationItem);
        }
	}

	private void getCameras() {
		MatchResult headingMatcher;
		
		// Regular expression patterns
		String headingPattern = "^\\*\\*~~([A-Z']+)~~\\*\\*"; // **~~CAMERAS~~**
		String routesPattern = "^\\*\\*([A-Z0-9\\s\\-]+)\\*\\*"; // **I-5**
		
		// Compile and use regular expressions
		RegExp headingRegExp = RegExp.compile(headingPattern);
		RegExp routesRegExp = RegExp.compile(routesPattern);
		
		List<String> loops = Arrays.asList(CAMERA_LINES.split("\\n"));
		
        for (String loop: loops.subList(6, loops.size())) { // Start at **~~CAMERAS~~**
			headingMatcher = headingRegExp.exec(loop);
			boolean headingMatchFound = (headingMatcher != null);
			
			if (headingMatchFound) {
				if (headingMatcher.getGroup(1).equalsIgnoreCase("cameras")) {
					continue;
				}
			}

			MatchResult routesMatcher = routesRegExp.exec(loop);
			boolean routesMatchFound = (routesMatcher != null);

			if (routesMatchFound) {
				continue;
			}
			
			CameraItem cameraItem = new CameraItem();
            String[] values = loop.split(","); // 002vc00001,410,699
            cameraItem.setId(values[0]); // 002vc00001
            cameraItem.setX(values[1]); // 410
            cameraItem.setY(values[2]); // 699
            cameraItem.setUrl("http://images.wsdot.wa.gov/nw/" + values[0] + ".jpg");
            cameraItem.setWidth("16"); // 14
            cameraItem.setHeight("10"); // 8
            
            cameraItems.add(cameraItem);
        }
        
        view.renderCameras(cameraItems);
        view.refresh();
	}
	
	private void getFlowData() {
		
		/** 
		 * Check the cache table for the last time data was downloaded. If we are within
		 * the allowed time period, don't sync, otherwise get fresh data from the server.
		 */
		dbService.getCacheLastUpdated(Tables.STATIONS, new ListCallback<GenericRow>() {

			@Override
			public void onFailure(DataServiceException error) {
			}

			@Override
			public void onSuccess(List<GenericRow> result) {
				String currentMap = localStorage.getItem("KEY_CURRENT_MAP");
				boolean shouldUpdate = true;

				if (!result.isEmpty()) {
					double now = System.currentTimeMillis();
					double lastUpdated = result.get(0).getDouble(CachesColumns.CACHE_LAST_UPDATED);
					shouldUpdate = (Math.abs(now - lastUpdated) > (3 * 60000)); // Refresh every 3 minutes.
				}

			    view.showProgressIndicator();
			    
			    if (!currentMap.equalsIgnoreCase("seattle")) {
			    	shouldUpdate = true;
					localStorage.setItem("KEY_CURRENT_MAP", "seattle");
			    }
			    
			    if (shouldUpdate) {
			    	try {
			    	    String url = "http://trafficflow-imaginedreal.rhcloud.com/api/flowdata/MinuteDataNW";
			    	    JsonpRequestBuilder jsonp = new JsonpRequestBuilder();
			    	    jsonp.setTimeout(30000); // 30 seconds
			    	    jsonp.requestObject(url, new AsyncCallback<FlowDataFeed>() {

			    			@Override
			    			public void onFailure(Throwable caught) {
			    				view.hideProgressIndicator();
			    				GWT.log("Failure calling traffic flow api.");
			    			}

			    			@Override
			    			public void onSuccess(FlowDataFeed result) {
			    				stationItems.clear();
			    				
			    				if (result.getFlowData() != null) {
			    					int numStations = result.getFlowData().getStations().length();
			    					String timestamp = result.getFlowData().getTimestamp();
			    					timestamp = timestamp.replaceAll("PST|PDT", "");
			    					Date date = parseDateFormat.parse(timestamp);
			    					String lastUpdated = displayDateFormat.format(date);
			    					localStorage.setItem("KEY_LAST_UPDATED", lastUpdated);
			    					StationItem item;
			    					
			    					for (int i = 0; i < numStations; i++) {
			    						String stationId = result.getFlowData().getStations().get(i).getId();
			    						String status = result.getFlowData().getStations().get(i).getStat();
			    						
			    						if (stationItemsMap.containsKey(stationId) && status.equalsIgnoreCase("good")) {
				    						item = new StationItem();
				    						
				    						item.setId(stationId);
			    							item.setVolume(result.getFlowData().getStations().get(i).getVol());
			    							item.setSpeed(result.getFlowData().getStations().get(i).getSpd());
			    							item.setOccupancy(result.getFlowData().getStations().get(i).getOcc());
			    							
			    							stationItems.add(item);
			    						}
			    					}
			    				}
			    				
			    				// Purge existing stations covered by incoming data
			    				dbService.deleteStations(new VoidCallback() {

									@Override
									public void onFailure(DataServiceException error) {
									}

									@Override
									public void onSuccess() {
										// Bulk insert all the new stations and data.
										dbService.insertStations(stationItems, new RowIdListCallback() {

											@Override
											public void onFailure(DataServiceException error) {
												view.hideProgressIndicator();
											}

											@Override
											public void onSuccess(List<Integer> rowIds) {
												// Update the cache table with the time we did the update
												List<CacheItem> cacheItems = new ArrayList<CacheItem>();
												cacheItems.add(new CacheItem(Tables.STATIONS, System.currentTimeMillis()));
												dbService.updateCachesTable(cacheItems, new VoidCallback() {

													@Override
													public void onFailure(DataServiceException error) {
													}

													@Override
													public void onSuccess() {
														// Get all the stations and data just inserted.
												    	dbService.getStations(new ListCallback<GenericRow>() {

															@Override
															public void onFailure(DataServiceException error) {
															}

															@Override
															public void onSuccess(List<GenericRow> result) {
																getStations(result);
																
															}
														});
													}
												});
											}
										});
									}
								});
			    			}
			    		});

			    	} catch (Exception e) {
			    		// TODO Do something with the exception
			    	}
			    } else {
			    	dbService.getStations(new ListCallback<GenericRow>() {

						@Override
						public void onFailure(DataServiceException error) {
						}

						@Override
						public void onSuccess(List<GenericRow> result) {
							getStations(result);
							
						}
					});
			    }
			}
		});
	}
	
	/**
	 * 
	 * @param result
	 */
	private void getStations(List<GenericRow> result) {
		int numStations = result.size();
		
		for (int i = 0; i < numStations; i++) {
			String stationId = result.get(i).getString(StationsColumns.STATION_ID);

			stationItemsMap.get(stationId).setVolume(result.get(i).getInt(StationsColumns.STATION_SPEED));
			stationItemsMap.get(stationId).setSpeed(result.get(i).getInt(StationsColumns.STATION_SPEED));
			stationItemsMap.get(stationId).setOccupancy(result.get(i).getInt(StationsColumns.STATION_OCCUPANCY));
		}
		
		view.renderFlowMap(stationItemsMap);
		view.setLastUpdated(localStorage.getItem("KEY_LAST_UPDATED"));
	    view.hideProgressIndicator();
		view.setMapXY(localStorage.getItem("KEY_SEATTLE_MAP_XY"));
	    view.refresh();
	}
	
	@Override
	public void onRefreshMapButtonPressed() {
		localStorage.setItem("KEY_SEATTLE_MAP_XY", view.getMapXY());
		view.refreshMap();
		getFlowData();
	}

	@Override
	public void onCameraButtonPressed(boolean showCameras) {
		if (showCameras) {
			view.hideCameras();
		} else {
			view.showCameras();
		}
	}

	@Override
	public void onMenuButonPressed() {
		if (!clientFactory.getSwipeMenu().isOpen()) {
			clientFactory.getSwipeMenu().open();
		} else {
			clientFactory.getSwipeMenu().close();
		}
	}

	@Override
	public void onCameraImagePressed(CameraItem item) {
		clientFactory.getPlaceController().goTo(
				new CameraPlace(item.getId(), item));
	}

}