package com.imaginedreal.mgwt.trafficflow.client.widget.colorpicker.canvas;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayInteger;

public class ImageData extends JavaScriptObject {
	protected ImageData() {}
	
	public final native int getWidth() /*-{
		return this.width;
	}-*/;

	public final native int getHeight() /*-{
		return this.height;
	}-*/;
	
	public final native JsArrayInteger getData() /*-{
		return this.data;
	}-*/;
}
