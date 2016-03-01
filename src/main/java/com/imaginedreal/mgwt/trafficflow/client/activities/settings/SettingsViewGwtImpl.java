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

import com.google.gwt.core.client.GWT;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.dom.client.event.tap.TapHandler;
import com.googlecode.mgwt.ui.client.widget.button.ImageButton;
import com.googlecode.mgwt.ui.client.widget.button.image.MenuImageButton;
import com.googlecode.mgwt.ui.client.widget.form.Form;
import com.googlecode.mgwt.ui.client.widget.form.FormEntry;
import com.googlecode.mgwt.ui.client.widget.image.ImageHolder;
import com.imaginedreal.mgwt.trafficflow.client.widget.colorpicker.ColorPickerDialog;
import com.imaginedreal.mgwt.trafficflow.client.widget.colorpicker.dialog.DialogClosedEvent;
import com.imaginedreal.mgwt.trafficflow.client.widget.colorpicker.dialog.IDialogClosedHandler;

public class SettingsViewGwtImpl extends Composite implements SettingsView {

	/**
	 * The UiBinder interface.
	 */	
	interface SettingsViewGwtImplUiBinder extends
			UiBinder<Widget, SettingsViewGwtImpl> {
	}

	/**
	 * The UiBinder used to generate the view.
	 */
	private static SettingsViewGwtImplUiBinder uiBinder = GWT
			.create(SettingsViewGwtImplUiBinder.class);
	

	@UiField
	MenuImageButton menuButton;
	
	@UiField
	FlowPanel container;
	
	private Presenter presenter;
	private ImageButton stopAndGoButton;
	private ImageButton heavyButton;
	private ImageButton moderateButton;
	private ImageButton wideOpenButton;
	private ImageButton noDataButton;
	private ImageButton noEquipmentButton;
	private static Storage localStorage;
	
	String stopAndGoButtonColor;
	String heavyButtonColor;
	String moderateButtonColor;
	String wideOpenButtonColor;
	String noDataButtonColor;
	String noEquipmentButtonColor;
	
	public SettingsViewGwtImpl() {
		
		localStorage = Storage.getLocalStorageIfSupported();
		
		initWidget(uiBinder.createAndBindUi(this));
		
		Form widgetList = new Form();
		widgetList.setHeader(new Label("Flow Colors"));
		
		stopAndGoButton = new ImageButton(ImageHolder.get().stop());
		stopAndGoButtonColor = localStorage.getItem("KEY_COLOR_STOPANDGO");
		stopAndGoButton.setIconColor("#" + stopAndGoButtonColor);
		stopAndGoButton.setWidth("100px");
		stopAndGoButton.setText("Select...");
		stopAndGoButton.addTapHandler(new TapHandler() {
			@Override
			public void onTap(TapEvent event) {
				pickColor(stopAndGoButton, "KEY_COLOR_STOPANDGO", stopAndGoButtonColor);
			}
		});
	    
		heavyButton = new ImageButton(ImageHolder.get().stop());
		heavyButtonColor = localStorage.getItem("KEY_COLOR_HEAVY");
		heavyButton.setIconColor("#" + heavyButtonColor);
		heavyButton.setWidth("100px");
		heavyButton.setText("Select...");
		heavyButton.addTapHandler(new TapHandler() {
			@Override
			public void onTap(TapEvent event) {
				pickColor(heavyButton, "KEY_COLOR_HEAVY", heavyButtonColor);
			}
		});
		
		moderateButton = new ImageButton(ImageHolder.get().stop());
		moderateButtonColor = localStorage.getItem("KEY_COLOR_MODERATE");
		moderateButton.setIconColor("#" + moderateButtonColor);
		moderateButton.setWidth("100px");
		moderateButton.setText("Select...");
		moderateButton.addTapHandler(new TapHandler() {
			@Override
			public void onTap(TapEvent event) {
				pickColor(moderateButton, "KEY_COLOR_MODERATE", moderateButtonColor);
			}
		});
		
		wideOpenButton = new ImageButton(ImageHolder.get().stop());
		wideOpenButtonColor = localStorage.getItem("KEY_COLOR_WIDEOPEN");
		wideOpenButton.setIconColor("#" + wideOpenButtonColor);
		wideOpenButton.setWidth("100px");
		wideOpenButton.setText("Select...");
		wideOpenButton.addTapHandler(new TapHandler() {
			@Override
			public void onTap(TapEvent event) {
				pickColor(wideOpenButton, "KEY_COLOR_WIDEOPEN", wideOpenButtonColor);
			}
		});
		
		noDataButton = new ImageButton(ImageHolder.get().stop());
		noDataButtonColor = localStorage.getItem("KEY_COLOR_NODATA");
		noDataButton.setIconColor("#" + noDataButtonColor);
		noDataButton.setWidth("100px");
		noDataButton.setText("Select...");
		noDataButton.addTapHandler(new TapHandler() {
			@Override
			public void onTap(TapEvent event) {
				pickColor(noDataButton, "KEY_COLOR_NODATA", noDataButtonColor);
			}
		});
		
		noEquipmentButton = new ImageButton(ImageHolder.get().stop());
		noEquipmentButtonColor = localStorage.getItem("KEY_COLOR_NOEQUIPMENT");
		noEquipmentButton.setIconColor("#" + noEquipmentButtonColor);
		noEquipmentButton.setWidth("100px");
		noEquipmentButton.setText("Select...");
		noEquipmentButton.addTapHandler(new TapHandler() {
			@Override
			public void onTap(TapEvent event) {
				pickColor(noEquipmentButton, "KEY_COLOR_NOEQUIPMENT", noEquipmentButtonColor);
			}
		});
		
		widgetList.add(new FormEntry("< 30 mph", stopAndGoButton)); // Stop and go
		widgetList.add(new FormEntry("30-40 mph", heavyButton)); // Heavy
		widgetList.add(new FormEntry("40-50 mph", moderateButton)); // Moderate
		widgetList.add(new FormEntry("50+ mph", wideOpenButton)); // Wide open
		widgetList.add(new FormEntry("No data", noDataButton)); // No data
		widgetList.add(new FormEntry("No equipment", noEquipmentButton)); // No equipment

		container.add(widgetList);
	}

	@UiHandler("menuButton")
	protected void onMenuButtonPressed(TapEvent event) {
		if (presenter != null) {
			presenter.onMenuButonPressed();
		}
	}
	
	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}
	
	/**
	 * 
	 * @param imageButton
	 * @param key
	 * @param iconColor
	 */
	private void pickColor(final ImageButton imageButton, final String key, String iconColor) {
		final ColorPickerDialog dlg = new ColorPickerDialog();
		dlg.setColor(iconColor);
		dlg.addDialogClosedHandler(new IDialogClosedHandler() {
			public void dialogClosed(DialogClosedEvent event) {
				if (!event.isCanceled()) {
					setColor(imageButton, key, dlg.getColor());
				}
			}
		});
		dlg.center();
	}
	
	/**
	 * 
	 * @param imageButton
	 * @param key
	 * @param color
	 */
	private void setColor(ImageButton imageButton, String key, String color) {
		imageButton.setIconColor("#" + color);
		localStorage.setItem(key, color);
	}

}