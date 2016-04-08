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
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.ui.client.MGWT;
import com.googlecode.mgwt.ui.client.widget.button.image.MenuImageButton;
import com.googlecode.mgwt.ui.client.widget.panel.flex.FlexSpacer;
import com.googlecode.mgwt.ui.client.widget.panel.scroll.ScrollPanel;
import com.imaginedreal.mgwt.trafficflow.client.util.Consts;

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

	@UiField
	FlexSpacer leftFlexSpacer;

	@UiField
	ScrollPanel scrollPanel;

	@UiField
	Anchor email;

	private Presenter presenter;
	
	public AboutViewGwtImpl() {
	    String appVersion = Consts.APP_VERSION;
	    String mailTo = "mailto:support@imaginedreal.com?subject=Traffic Flow ";
	    email = new Anchor();

		initWidget(uiBinder.createAndBindUi(this));

        if (MGWT.getOsDetection().isAndroid()) {
            leftFlexSpacer.setVisible(false);
            scrollPanel.setBounce(false);
            email.setHref(mailTo + "Android App v" + appVersion);
        } else if (MGWT.getOsDetection().isIOs()) {
            email.setHref(mailTo + "iPhone App v" + appVersion);
        } else {
            email.setHref(mailTo + "Mobile App v" + appVersion);
        }

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