package com.imaginedreal.mgwt.trafficflow.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.TextResource;

public interface Resources extends ClientBundle {
	
    public static final Resources INSTANCE = GWT.create(Resources.class);
	
	@Source("app.css")
	public Styles css();

	/**
	 * Map images and icons
	 */
	@Source("SeattleArea.png")
	ImageResource seattleArea();
	
	@Source("TacomaArea.png")
	ImageResource tacomaArea();
	
	@Source("camera.png")
	ImageResource camera();
	
	/**
	 * XY coordinates for Seattle area map 
	 */
	@Source("SeattleArea.xy")
	TextResource seattleAreaXY();

	/**
	 * XY coordinates for Seattle area cameras 
	 */
	@Source("SeattleAreaCameras.xy")
	TextResource seattleAreaCamerasXY();

	/**
	 * XY coordinates for Tacoma area map 
	 */
	@Source("TacomaArea.xy")
	TextResource tacomaAreaXY();

	/**
	 * XY coordinates for Tacoma area cameras 
	 */
	@Source("TacomaAreaCameras.xy")
	TextResource tacomaAreaCamerasXY();
	
	public interface Styles extends CssResource {
		String seattleFlowMap();
	}
}
