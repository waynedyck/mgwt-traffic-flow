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

import com.imaginedreal.mgwt.trafficflow.client.activities.about.AboutActivity;
import com.imaginedreal.mgwt.trafficflow.client.activities.about.AboutPlace;
import com.imaginedreal.mgwt.trafficflow.client.activities.camera.CameraActivity;
import com.imaginedreal.mgwt.trafficflow.client.activities.camera.CameraPlace;
import com.imaginedreal.mgwt.trafficflow.client.activities.seattle.SeattleActivity;
import com.imaginedreal.mgwt.trafficflow.client.activities.seattle.SeattlePlace;
import com.imaginedreal.mgwt.trafficflow.client.activities.settings.SettingsActivity;
import com.imaginedreal.mgwt.trafficflow.client.activities.settings.SettingsPlace;
import com.imaginedreal.mgwt.trafficflow.client.activities.tacoma.TacomaActivity;
import com.imaginedreal.mgwt.trafficflow.client.activities.tacoma.TacomaPlace;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;

public class AppMainActivityMapper implements ActivityMapper {

	private final ClientFactory clientFactory;

	private Place lastPlace;

	public AppMainActivityMapper(ClientFactory clientFactory) {
		this.clientFactory = clientFactory;
	}

	@Override
	public Activity getActivity(Place place) {
		Activity activity = getActivity(lastPlace, place);
		lastPlace = place;
		
		return activity;
	}

	private SeattleActivity homeActivity;

	private SeattleActivity getHomeActivity() {
		if (homeActivity == null) {
		    homeActivity = new SeattleActivity(clientFactory);
		}

		return homeActivity;
	}

	private Activity getActivity(Place lastPlace, Place newPlace) {
		if (newPlace instanceof SeattlePlace) {
			return getHomeActivity();
		}
		
		if (newPlace instanceof TacomaPlace) {
			return new TacomaActivity(clientFactory);
		}
		
		if (newPlace instanceof AboutPlace) {
		    return new AboutActivity(clientFactory);
		}
		
        if (newPlace instanceof SettingsPlace) {
            return new SettingsActivity(clientFactory);
        }
        
        if (newPlace instanceof CameraPlace) {
        	return new CameraActivity(clientFactory);
        }

		return null;
	}

}
