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

package com.imaginedreal.mgwt.trafficflow.client.activities.about;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.googlecode.gwtphonegap.client.plugins.analytics.Analytics;
import com.googlecode.mgwt.mvp.client.MGWTAbstractActivity;
import com.imaginedreal.mgwt.trafficflow.client.ClientFactory;
import com.imaginedreal.mgwt.trafficflow.client.util.Consts;

public class AboutActivity extends MGWTAbstractActivity implements
		AboutView.Presenter {

	private final ClientFactory clientFactory;
	private AboutView view;
	private EventBus eventBus;
	private Analytics analytics;

	public AboutActivity(ClientFactory clientFactory) {
		this.clientFactory = clientFactory;
	}

	@Override
	public void start(AcceptsOneWidget panel, final EventBus eventBus) {
		view = clientFactory.getAboutView();
		this.eventBus = eventBus;
		analytics = clientFactory.getAnalytics();
		view.setPresenter(this);

		if (Consts.ANALYTICS_ENABLED) {
		    analytics.trackView("/About");
		}

		panel.setWidget(view);

	}

	@Override
	public void onStop() {
		view.setPresenter(null);
	}
	
	@Override
	public void onMenuButtonPressed() {
		if (!clientFactory.getSwipeMenu().isOpen()) {
			clientFactory.getSwipeMenu().open();
		} else {
			clientFactory.getSwipeMenu().close();
		}
	}

}