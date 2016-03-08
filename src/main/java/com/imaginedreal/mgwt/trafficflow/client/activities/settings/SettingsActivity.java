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

package com.imaginedreal.mgwt.trafficflow.client.activities.settings;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.googlecode.gwtphonegap.client.plugins.analytics.Analytics;
import com.googlecode.mgwt.mvp.client.MGWTAbstractActivity;
import com.imaginedreal.mgwt.trafficflow.client.ClientFactory;
import com.imaginedreal.mgwt.trafficflow.client.util.Consts;

public class SettingsActivity extends MGWTAbstractActivity implements
		SettingsView.Presenter {

	private final ClientFactory clientFactory;
	private SettingsView view;
	private EventBus eventBus;
	private Analytics analytics;

	public SettingsActivity(ClientFactory clientFactory) {
		this.clientFactory = clientFactory;
	}

	@Override
	public void start(AcceptsOneWidget panel, final EventBus eventBus) {
		view = clientFactory.getSettingsView();
		this.eventBus = eventBus;
		analytics = clientFactory.getAnalytics();
		view.setPresenter(this);

        if (Consts.ANALYTICS_ENABLED) {
            analytics.trackView("/Settings");
        }

		panel.setWidget(view);

	}

	@Override
	public void onStop() {
		view.setPresenter(null);
	}

	@Override
	public void onMenuButonPressed() {
		if (!clientFactory.getSwipeMenu().isOpen()) {
			clientFactory.getSwipeMenu().open();
		} else {
			clientFactory.getSwipeMenu().close();
		}
	}

    @Override
    public void onFlowColorSet(String key, String color) {
        if (Consts.ANALYTICS_ENABLED) {
            analytics.trackEvent("Flow Colors", key, color, 0);
        }
    }

}