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

package com.imaginedreal.mgwt.trafficflow.client.activities.tacoma;

import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceTokenizer;

public class TacomaPlace extends Place {

	public static class TacomaPlaceTokenizer implements
			PlaceTokenizer<TacomaPlace> {

		@Override
		public TacomaPlace getPlace(String token) {
			return new TacomaPlace();
		}

		@Override
		public String getToken(TacomaPlace place) {
			return "";
		}

	}

	@Override
	public int hashCode() {
		return 3;
	}

}
