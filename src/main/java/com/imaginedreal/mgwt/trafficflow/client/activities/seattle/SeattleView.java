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

import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.ui.IsWidget;
import com.imaginedreal.mgwt.trafficflow.shared.CameraItem;
import com.imaginedreal.mgwt.trafficflow.shared.StationItem;

public interface SeattleView extends IsWidget {
	
	public void setPresenter(Presenter presenter);
	
	public interface Presenter {
		
		public void onMenuButonPressed();

		public void onCameraButtonPressed(boolean showCameras);

		public void onRefreshMapButtonPressed();

		public void onCameraImagePressed(CameraItem item);
		
	}
	
	public void renderFlowMap(Map<String, StationItem> stationItems);
	
	public void renderCameras(List<CameraItem> cameraItems);
	
	public void refresh();
	
	public void showProgressIndicator();

	public void hideProgressIndicator();
	
	public void hideCameras();
	
	public void showCameras();
	
	public void refreshMap();
	
	public String getMapXY();
	
	public void setMapXY(String xyPosition);
	
	public void setLastUpdated(String timestamp);

}
