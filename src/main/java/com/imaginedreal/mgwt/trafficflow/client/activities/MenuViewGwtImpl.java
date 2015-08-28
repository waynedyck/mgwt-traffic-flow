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

import java.util.List;

import com.imaginedreal.mgwt.trafficflow.shared.Topic;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.ui.client.widget.button.image.AboutImageButton;
import com.googlecode.mgwt.ui.client.widget.button.image.SettingsImageButton;
import com.googlecode.mgwt.ui.client.widget.list.celllist.BasicCell;
import com.googlecode.mgwt.ui.client.widget.list.celllist.CellList;
import com.googlecode.mgwt.ui.client.widget.list.celllist.CellSelectedEvent;

public class MenuViewGwtImpl extends Composite implements MenuView {

	/**
	 * The UiBinder interface.
	 */	
	interface MenuViewGwtImplUiBinder extends
			UiBinder<Widget, MenuViewGwtImpl> {
	}
	
	/**
	 * The UiBinder used to generate the view.
	 */
	private static MenuViewGwtImplUiBinder uiBinder = GWT
			.create(MenuViewGwtImplUiBinder.class);
	
	@UiField(provided = true)
	CellList<Topic> cellList;
	
	@UiField
	SettingsImageButton settingsButton;
	
	@UiField
	AboutImageButton aboutButton;
	
	private Presenter presenter;
	
	public MenuViewGwtImpl() {
		
		cellList = new CellList<Topic>(new BasicCell<Topic>() {

			@Override
			public String getDisplayString(Topic model) {
				return model.getName();
			}

			@Override
			public boolean canBeSelected(Topic model) {
				return true;
			}
		});
		
		initWidget(uiBinder.createAndBindUi(this));
		
	}

	@UiHandler("cellList")
	protected void onCellSelected(CellSelectedEvent event) {
		if (presenter != null) {
			int index = event.getIndex();
			presenter.onItemSelected(index);
		}
	}

	@UiHandler("settingsButton")
	protected void onSettingsButtonPressed(TapEvent event) {
		if (presenter != null) {
			presenter.onSettingsButonPressed();
		}
	}
	
	@UiHandler("aboutButton")
	protected void onAboutButtonPressed(TapEvent event) {
		if (presenter != null) {
			presenter.onAboutButtonPressed();
		}
	}
	
	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void render(List<Topic> createTopicsList) {
		cellList.render(createTopicsList);
	}

	@Override
	public void setSelected(int lastIndex, boolean b) {
		cellList.setSelectedIndex(lastIndex, b);
	}

}
