package com.imaginedreal.mgwt.trafficflow.client.widget.colorpicker;

import com.google.gwt.event.shared.GwtEvent;

public class HueChangedEvent extends GwtEvent<IHueChangedHandler> {
	private static GwtEvent.Type<IHueChangedHandler> TYPE;

	private int hue;
	
	HueChangedEvent(int hue) {
		this.hue = hue;
	}
	
	public static GwtEvent.Type<IHueChangedHandler> getType() {
		if (TYPE == null) {
			TYPE = new Type<IHueChangedHandler>();
		}
		return TYPE;
	}
	
	@Override
	public GwtEvent.Type<IHueChangedHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(IHueChangedHandler handler) {
		handler.hueChanged(this);
	}
	
	public int getHue() {
		return hue;
	}
}
