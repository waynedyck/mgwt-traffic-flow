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

package com.imaginedreal.mgwt.trafficflow.shared;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

public class FlowDataFeed extends JavaScriptObject {
	protected FlowDataFeed() {}

	public final native FlowData getFlowData() /*-{ return this.station_data }-*/;

	static public class FlowData extends JavaScriptObject {
		protected FlowData() {}
	
		public final native JsArray<Station> getStations() /*-{ return this.station }-*/;
		public final native String getTimestamp() /*-{ return this.time_stamp; }-*/;

	}
	
	static public class Station extends JavaScriptObject {
		protected Station() {}
		
		public final native String getId() /*-{ return this.id }-*/;
		public final native int getVol() /*-{ return this.vol }-*/;
		public final native int getSpd() /*-{ return this.spd }-*/;
		public final native int getOcc() /*-{ return this.occ }-*/;
		public final native String getStat() /*-{ return this.stat }-*/;	
	}
	
}
