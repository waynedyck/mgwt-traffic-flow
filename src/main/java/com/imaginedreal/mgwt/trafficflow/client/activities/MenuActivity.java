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

package com.imaginedreal.mgwt.trafficflow.client.activities;

import java.util.ArrayList;
import java.util.List;

import com.imaginedreal.mgwt.trafficflow.client.ClientFactory;
import com.imaginedreal.mgwt.trafficflow.client.activities.about.AboutPlace;
import com.imaginedreal.mgwt.trafficflow.client.activities.seattle.SeattlePlace;
import com.imaginedreal.mgwt.trafficflow.client.activities.settings.SettingsPlace;
import com.imaginedreal.mgwt.trafficflow.client.activities.tacoma.TacomaPlace;
import com.imaginedreal.mgwt.trafficflow.shared.Topic;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.web.bindery.event.shared.EventBus;
import com.googlecode.mgwt.mvp.client.MGWTAbstractActivity;

public class MenuActivity extends MGWTAbstractActivity implements
		MenuView.Presenter {

	private final ClientFactory clientFactory;
	private MenuView view;
	@SuppressWarnings("unused")
    private EventBus eventBus;
	
	public MenuActivity(ClientFactory clientFactory) {
		this.clientFactory = clientFactory;
	}
	
	@Override
	public void start(AcceptsOneWidget panel, final EventBus eventBus) {
		view = clientFactory.getMenuView();
		this.eventBus = eventBus;
		view.setPresenter(this);
		view.render(createTopicsList());
		
		panel.setWidget(view);
	}

	@Override
	public void onStop() {
		view.setPresenter(null);
	}

	@Override
	public void onItemSelected(int index) {
		if (index == 0) {
			clientFactory.getSwipeMenu().close(false);
			clientFactory.getPlaceController().goTo(new SeattlePlace());
			
			return;
		}
		if (index == 1) {
			clientFactory.getSwipeMenu().close(false);
			clientFactory.getPlaceController().goTo(new TacomaPlace());

			return;
		}
	}
	
	private List<Topic> createTopicsList() {
		ArrayList<Topic> list = new ArrayList<Topic>();
		
		list.add(new Topic("Seattle Area"));
		list.add(new Topic("Tacoma / Olympia Area"));
		
		return list;
	}

	@Override
	public void onSettingsButonPressed() {
		clientFactory.getSwipeMenu().close(false);
		clientFactory.getPlaceController().goTo(new SettingsPlace());
	}

	@Override
	public void onAboutButtonPressed() {
		clientFactory.getSwipeMenu().close(false);
		clientFactory.getPlaceController().goTo(new AboutPlace());
	}

}
