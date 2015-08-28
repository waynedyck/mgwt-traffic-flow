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

package com.imaginedreal.mgwt.trafficflow.client;

import com.imaginedreal.mgwt.trafficflow.client.activities.MenuView;
import com.imaginedreal.mgwt.trafficflow.client.activities.MenuViewGwtImpl;
import com.imaginedreal.mgwt.trafficflow.client.activities.about.AboutView;
import com.imaginedreal.mgwt.trafficflow.client.activities.about.AboutViewGwtImpl;
import com.imaginedreal.mgwt.trafficflow.client.activities.camera.CameraView;
import com.imaginedreal.mgwt.trafficflow.client.activities.camera.CameraViewGwtImpl;
import com.imaginedreal.mgwt.trafficflow.client.activities.seattle.SeattleView;
import com.imaginedreal.mgwt.trafficflow.client.activities.seattle.SeattleViewGwtImpl;
import com.imaginedreal.mgwt.trafficflow.client.activities.settings.SettingsView;
import com.imaginedreal.mgwt.trafficflow.client.activities.settings.SettingsViewGwtImpl;
import com.imaginedreal.mgwt.trafficflow.client.activities.tacoma.TacomaView;
import com.imaginedreal.mgwt.trafficflow.client.activities.tacoma.TacomaViewGwtImpl;
import com.imaginedreal.mgwt.trafficflow.client.storage.TrafficFlowDataService;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.place.shared.PlaceController;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;
import com.googlecode.gwtphonegap.client.PhoneGap;
import com.googlecode.mgwt.ui.client.widget.menu.swipe.SwipeMenu;

public class ClientFactoryImpl implements ClientFactory {

	private EventBus eventBus;
	private PlaceController placeController;
	private PhoneGap phoneGap;
	private SwipeMenu swipeMenu;
	private TrafficFlowDataService dbService;
	private SeattleView seattleView;
	private TacomaView tacomaView;
	private AboutViewGwtImpl aboutView;
	private SettingsViewGwtImpl settingsView;
	private MenuViewGwtImpl menuView;
	private CameraViewGwtImpl cameraView;

	public ClientFactoryImpl() {
		eventBus = new SimpleEventBus();
		placeController = new PlaceController(eventBus);
		seattleView = new SeattleViewGwtImpl();
		swipeMenu = new SwipeMenu();
		dbService = GWT.create(TrafficFlowDataService.class);
	}

	@Override
	public SeattleView getSeattleView() {
	    if (seattleView == null) {
            seattleView = new SeattleViewGwtImpl();
	    }
		return seattleView;
	}	
	
	@Override
	public EventBus getEventBus() {
		return eventBus;
	}

	@Override
	public PlaceController getPlaceController() {
		return placeController;
	}

    public void setPhoneGap(PhoneGap phoneGap) {
        this.phoneGap = phoneGap;
    }

	@Override
	public PhoneGap getPhoneGap() {
		return phoneGap;
	}
	
	@Override
	public AboutView getAboutView() {
		if (aboutView == null) {
			aboutView = new AboutViewGwtImpl();
		}
		return aboutView;
	}

    @Override
    public SettingsView getSettingsView() {
        if (settingsView == null) {
            settingsView = new SettingsViewGwtImpl();
        }
        return settingsView;
    }

	@Override
	public SwipeMenu getSwipeMenu() {
		return swipeMenu;
	}

	@Override
	public TrafficFlowDataService getDbService() {
		return dbService;
	}

	@Override
	public MenuView getMenuView() {
		if (menuView == null) {
			menuView = new MenuViewGwtImpl();
		}
		return menuView;
	}

	@Override
	public CameraView getCameraView() {
		if (cameraView == null) {
			cameraView = new CameraViewGwtImpl();
		}
		return cameraView;
	}

	@Override
	public TacomaView getTacomaView() {
		if (tacomaView == null) {
			tacomaView = new TacomaViewGwtImpl();
		}
		return tacomaView;
	}

}
