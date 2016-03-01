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

package com.imaginedreal.mgwt.trafficflow.client.activities.about;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.ui.client.widget.button.image.MenuImageButton;

public class AboutViewGwtImpl extends Composite implements AboutView {

	/**
	 * The UiBinder interface.
	 */	
	interface AboutViewGwtImplUiBinder extends
			UiBinder<Widget, AboutViewGwtImpl> {
	}

	/**
	 * The UiBinder used to generate the view.
	 */
	private static AboutViewGwtImplUiBinder uiBinder = GWT
			.create(AboutViewGwtImplUiBinder.class);
	

	@UiField
	MenuImageButton menuButton;
	
	private Presenter presenter;
	
	public AboutViewGwtImpl() {
	
		initWidget(uiBinder.createAndBindUi(this));

	}

	@UiHandler("menuButton")
	protected void onMenuButtonPressed(TapEvent event) {
		if (presenter != null) {
			presenter.onMenuButtonPressed();
		}
	}
	
	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

}