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

package com.imaginedreal.mgwt.trafficflow.client.activities.camera;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;
import com.imaginedreal.mgwt.trafficflow.shared.CameraItem;

public class CameraPlace extends Place {
	
	private String id;
	private CameraItem item;
	
	public CameraPlace(String id, CameraItem item) {
		this.id = id;;
		this.item = item;
	}

	public String getId() {
		return id;
	}
	
	public CameraItem getCameraItem() {
		return item;
	}

	public static class CameraPlaceTokenizer implements
			PlaceTokenizer<CameraPlace> {

		@Override
		public CameraPlace getPlace(String token) {
			return new CameraPlace(token, null);
		}

		@Override
		public String getToken(CameraPlace place) {
			return place.getId();
		}

	}
}