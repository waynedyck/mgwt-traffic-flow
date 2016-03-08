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

package com.imaginedreal.mgwt.trafficflow.client;

import java.util.ArrayList;
import java.util.List;

import com.google.code.gwt.database.client.Database;
import com.google.code.gwt.database.client.SQLError;
import com.google.code.gwt.database.client.SQLTransaction;
import com.google.code.gwt.database.client.TransactionCallback;
import com.google.code.gwt.database.client.service.DataServiceException;
import com.google.code.gwt.database.client.service.RowIdListCallback;
import com.google.code.gwt.database.client.service.ScalarCallback;
import com.google.code.gwt.database.client.service.VoidCallback;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.place.shared.Place;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.storage.client.StorageMap;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.googlecode.gwtphonegap.client.PhoneGap;
import com.googlecode.gwtphonegap.client.PhoneGapAvailableEvent;
import com.googlecode.gwtphonegap.client.PhoneGapAvailableHandler;
import com.googlecode.gwtphonegap.client.PhoneGapTimeoutEvent;
import com.googlecode.gwtphonegap.client.PhoneGapTimeoutHandler;
import com.googlecode.gwtphonegap.client.event.BackButtonPressedEvent;
import com.googlecode.gwtphonegap.client.event.BackButtonPressedHandler;
import com.googlecode.gwtphonegap.client.plugins.analytics.Analytics;
import com.googlecode.mgwt.mvp.client.AnimatingActivityManager;
import com.googlecode.mgwt.mvp.client.AnimationMapper;
import com.googlecode.mgwt.mvp.client.history.MGWTPlaceHistoryHandler;
import com.googlecode.mgwt.ui.client.MGWT;
import com.googlecode.mgwt.ui.client.MGWTSettings;
import com.googlecode.mgwt.ui.client.MGWTSettings.ViewPort;
import com.googlecode.mgwt.ui.client.util.SuperDevModeUtil;
import com.googlecode.mgwt.ui.client.widget.animation.AnimationWidget;
import com.imaginedreal.mgwt.trafficflow.client.activities.seattle.SeattlePlace;
import com.imaginedreal.mgwt.trafficflow.client.activities.tacoma.TacomaPlace;
import com.imaginedreal.mgwt.trafficflow.client.plugins.admob.AdMob;
import com.imaginedreal.mgwt.trafficflow.client.plugins.admob.AdMobOptions;
import com.imaginedreal.mgwt.trafficflow.client.plugins.admob.AdMobOptions.AdPosition;
import com.imaginedreal.mgwt.trafficflow.client.util.Consts;
import com.imaginedreal.mgwt.trafficflow.shared.CacheItem;

public class AppEntryPoint implements EntryPoint {
  
    private void start() {
        SuperDevModeUtil.showDevMode();

        final ClientFactory clientFactory = new ClientFactoryImpl();

        // Initialize and configure Google Analytics plugin
        final Analytics analytics = GWT.create(Analytics.class);
        analytics.initialize();
        ((ClientFactoryImpl) clientFactory).setAnalytics(analytics);
        analytics.startTrackerWithId(Consts.ANALYTICS_TRACKING_ID);

		final PhoneGap phoneGap = GWT.create(PhoneGap.class);
		phoneGap.addHandler(new PhoneGapAvailableHandler() {

	        @Override
	        public void onPhoneGapAvailable(PhoneGapAvailableEvent event) {
	        	((ClientFactoryImpl) clientFactory).setPhoneGap(phoneGap);
	        	
	        	buildDisplay(clientFactory, phoneGap);
	        }
	    });	
	    
	    phoneGap.addHandler(new PhoneGapTimeoutHandler() {

	        @Override
	        public void onPhoneGapTimeout(PhoneGapTimeoutEvent event) {
	        	Window.alert("Cannot load PhoneGap");
	        }
	    });
		
		phoneGap.initializePhoneGap();
		
        // Initialize and configure AdMob plugin
        final AdMob adMob = GWT.create(AdMob.class);
        adMob.initialize();

        AdMobOptions options = (AdMobOptions)JavaScriptObject.createObject().cast();
        options.setAdId(Consts.AD_UNIT_ID);
        options.setOffsetTopBar(true);
        options.setAutoShow(true);
        options.setPosition(AdPosition.BOTTOM_CENTER.getPosition());

        adMob.createBanner(options);
    }

    private void buildDisplay(final ClientFactory clientFactory, final PhoneGap phoneGap) {
    	ViewPort viewPort = new MGWTSettings.ViewPort();
        viewPort.setUserScaleAble(false).setInitialScale(1.0).setMinimumScale(1.0).setMaximumScale(1.0);
    	Storage localStorage;
    	Place defaultPlace;

        MGWTSettings settings = new MGWTSettings();
        settings.setViewPort(viewPort);
        settings.setIconUrl("logo.png");
        settings.setFullscreen(true);
        settings.setFixIOS71BodyBug(true);
        settings.setPreventScrolling(true);

        MGWT.applySettings(settings);
        
        createLocalStorageDefaults();
        
		// Create initial database tables if supported.
        if (Database.isSupported()) {
        	createDatabaseTables(clientFactory);
        } else {
        	GWT.log("WebSQL database not supported.");
        }

		phoneGap.getEvent().getBackButton()
				.addBackButtonPressedHandler(new BackButtonPressedHandler() {

					@Override
					public void onBackButtonPressed(BackButtonPressedEvent event) {
						Place place = clientFactory.getPlaceController().getWhere();
						if (place instanceof SeattlePlace || place instanceof TacomaPlace) {
							phoneGap.exitApp();
						} else {
							History.back();
						}
					}
				});
        
        // Start PlaceHistoryHandler with our PlaceHistoryMapper
        AppPlaceHistoryMapper historyMapper = GWT.create(AppPlaceHistoryMapper.class);
        
        createPhoneDisplay(clientFactory);
        
    	localStorage = Storage.getLocalStorageIfSupported();
    	String currentMap = localStorage.getItem("KEY_CURRENT_MAP");
    	
    	if (currentMap.equalsIgnoreCase("seattle")) {
    		defaultPlace = new SeattlePlace(); 
    	} else {
    		defaultPlace = new TacomaPlace();
    	}

        AppHistoryObserver historyObserver = new AppHistoryObserver();
        MGWTPlaceHistoryHandler historyHandler = new MGWTPlaceHistoryHandler(historyMapper, historyObserver);
        historyHandler.register(clientFactory.getPlaceController(), clientFactory.getEventBus(), defaultPlace);
        historyHandler.handleCurrentHistory();
    }
    
    private void createLocalStorageDefaults() {
    	Storage localStorage;
    	StorageMap storageMap;
		
    	localStorage = Storage.getLocalStorageIfSupported();
		if (localStorage != null) {
			storageMap = new StorageMap(localStorage);
			if (!storageMap.containsKey("KEY_LAST_UPDATED")) {
				localStorage.setItem("KEY_LAST_UPDATED", "January 1, 1970 0:00 AM");
			}
			if (!storageMap.containsKey("KEY_CURRENT_MAP")) {
				localStorage.setItem("KEY_CURRENT_MAP", "seattle");
			}
			if (!storageMap.containsKey("KEY_SEATTLE_MAP_XY")) {
				localStorage.setItem("KEY_SEATTLE_MAP_XY", "-35,-1475");
			}
			if (!storageMap.containsKey("KEY_TACOMA_MAP_XY")) {
				localStorage.setItem("KEY_TACOMA_MAP_XY", "-475,-225");
			}
			if (!storageMap.containsKey("KEY_SHOW_CAMERAS")) {
				localStorage.setItem("KEY_SHOW_CAMERAS", "false");
			}
			if (!storageMap.containsKey("KEY_COLOR_STOPANDGO")) {
				localStorage.setItem("KEY_COLOR_STOPANDGO", "000000");
			}
			if (!storageMap.containsKey("KEY_COLOR_HEAVY")) {
				localStorage.setItem("KEY_COLOR_HEAVY", "ff0000");
			}
			if (!storageMap.containsKey("KEY_COLOR_MODERATE")) {
				localStorage.setItem("KEY_COLOR_MODERATE", "ffff00");
			}
			if (!storageMap.containsKey("KEY_COLOR_WIDEOPEN")) {
				localStorage.setItem("KEY_COLOR_WIDEOPEN", "00ff00");
			}
			if (!storageMap.containsKey("KEY_COLOR_NODATA")) {
				localStorage.setItem("KEY_COLOR_NODATA", "ffffff");
			}
			if (!storageMap.containsKey("KEY_COLOR_NOEQUIPMENT")) {
				localStorage.setItem("KEY_COLOR_NOEQUIPMENT", "808080");
			}
		}
	}

	private void createDatabaseTables(final ClientFactory clientFactory) {
	    
    	final String VER_1 = "1.0";
	    final String DATABASE_VERSION = VER_1;
	    
	    if (clientFactory.getDbService().getDatabase().getVersion().equals("")) {
	        clientFactory.getDbService().getDatabase()
	                .changeVersion("", DATABASE_VERSION, new TransactionCallback() {

                @Override
                public void onTransactionStart(SQLTransaction transaction) {
                    clientFactory.getDbService().createCachesTable(new VoidCallback() {

                        @Override
                        public void onFailure(DataServiceException error) {
                        }

                        @Override
                        public void onSuccess() {
                            
                            clientFactory.getDbService().getCachesTableCount(new ScalarCallback<Integer>() {

                                @Override
                                public void onFailure(DataServiceException error) {
                                }

                                @Override
                                public void onSuccess(Integer result) {
                                    if (result == 0) initCachesTable(clientFactory);
                                }
                            });
                        }
                    
                    });
                    
                    clientFactory.getDbService().createStationsTable(new VoidCallback() {

                        @Override
                        public void onFailure(DataServiceException error) {
                        }

                        @Override
                        public void onSuccess() {
                        }

                    });

                }

                @Override
                public void onTransactionSuccess() {
                    GWT.log("Database at version " + DATABASE_VERSION);
                }

                @Override
                public void onTransactionFailure(SQLError error) {
                }
                
            });
	    }
		
	}

	private void initCachesTable(ClientFactory clientFactory) {

		List<CacheItem> cacheItems = new ArrayList<CacheItem>();
		cacheItems.add(new CacheItem("stations", 0));
		
		clientFactory.getDbService().initCachesTable(cacheItems, new RowIdListCallback() {

			@Override
			public void onFailure(DataServiceException error) {
			}

			@Override
			public void onSuccess(List<Integer> rowIds) {
			}
		
		});

	}
    
	private void createPhoneDisplay(ClientFactory clientFactory) {
        AnimationWidget navDisplay = new AnimationWidget();
        ActivityMapper navActivityMapper = new AppNavActivityMapper(clientFactory);
        AnimationMapper navAnimationMapper = new AppNavAnimationMapper();
		AnimatingActivityManager navActivityManager = new AnimatingActivityManager(
				navActivityMapper, navAnimationMapper,
				clientFactory.getEventBus());
		
        navActivityManager.setDisplay(navDisplay);
        clientFactory.getSwipeMenu().setMenuDisplay(navDisplay);
        
        AnimationWidget mainDisplay = new AnimationWidget();
        AppMainActivityMapper tabletMainActivityMapper = new AppMainActivityMapper(clientFactory);
        AnimationMapper tabletMainAnimationMapper = new AppMainAnimationMapper();
		AnimatingActivityManager mainActivityManager = new AnimatingActivityManager(
				tabletMainActivityMapper, tabletMainAnimationMapper,
				clientFactory.getEventBus());
		
        mainActivityManager.setDisplay(mainDisplay);
        clientFactory.getSwipeMenu().setContentDisplay(mainDisplay);

        RootPanel.get().add(clientFactory.getSwipeMenu());
    }

    @Override
    public void onModuleLoad() {
        start();
    }
  
}
