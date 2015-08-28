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

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.googlecode.mgwt.mvp.client.MGWTAbstractActivity;
import com.imaginedreal.mgwt.trafficflow.client.ClientFactory;
import com.imaginedreal.mgwt.trafficflow.client.event.ActionEvent;
import com.imaginedreal.mgwt.trafficflow.client.event.ActionNames;
import com.imaginedreal.mgwt.trafficflow.shared.CameraItem;

public class CameraActivity extends MGWTAbstractActivity implements
		CameraView.Presenter {

	private final ClientFactory clientFactory;
	private CameraView view;
	private EventBus eventBus;
	private String cameraId;
	private CameraItem cameraItem;

	public CameraActivity(ClientFactory clientFactory) {
		this.clientFactory = clientFactory;
	}

	@Override
	public void start(AcceptsOneWidget panel, final EventBus eventBus) {
		view = clientFactory.getCameraView();
		this.eventBus = eventBus;
		view.setPresenter(this);

		Place place = clientFactory.getPlaceController().getWhere();
		
		if (place instanceof CameraPlace) {
			CameraPlace cameraPlace = (CameraPlace) place;
			cameraId = cameraPlace.getId();
			cameraItem = cameraPlace.getCameraItem();
			
			view.showProgressIndicator();
			view.renderCameraImage(cameraItem.getUrl());
		}
				
		panel.setWidget(view);

	}

	@Override
	public void onStop() {
		view.setPresenter(null);
	}

	@Override
	public void onBackButtonPressed() {
		ActionEvent.fire(eventBus, ActionNames.BACK);		
	}

}