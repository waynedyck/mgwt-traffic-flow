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

import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.History;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.googlecode.mgwt.dom.client.event.mouse.HandlerRegistrationCollection;
import com.googlecode.mgwt.mvp.client.history.HistoryHandler;
import com.googlecode.mgwt.mvp.client.history.HistoryObserver;
import com.imaginedreal.mgwt.trafficflow.client.event.ActionEvent;
import com.imaginedreal.mgwt.trafficflow.client.event.ActionNames;

public class AppHistoryObserver implements HistoryObserver {

	@Override
	public void onPlaceChange(Place place, HistoryHandler handler) {
	}

	@Override
	public void onHistoryChanged(Place place, HistoryHandler handler) {
	}

	@Override
	public void onAppStarted(Place place, HistoryHandler historyHandler) {
		onPhoneNav(place, historyHandler);
	}

	@Override
	public HandlerRegistration bind(EventBus eventBus, final HistoryHandler historyHandler) {
		HandlerRegistration backButtonRegistration = ActionEvent.register(
				eventBus, ActionNames.BACK, new ActionEvent.Handler() {

					@Override
					public void onAction(ActionEvent event) {
						History.back();
					}
				});

		HandlerRegistration animationEndRegistration = ActionEvent.register(
				eventBus, ActionNames.ANIMATION_END, new ActionEvent.Handler() {

					@Override
					public void onAction(ActionEvent event) {
						History.back();
					}
				});

		HandlerRegistrationCollection col = new HandlerRegistrationCollection();
		col.addHandlerRegistration(backButtonRegistration);
		col.addHandlerRegistration(animationEndRegistration);

		return col;
	}

	private void onPhoneNav(Place place, HistoryHandler historyHandler) {
	}
}