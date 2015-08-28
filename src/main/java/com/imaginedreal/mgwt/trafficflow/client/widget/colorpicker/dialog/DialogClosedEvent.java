package com.imaginedreal.mgwt.trafficflow.client.widget.colorpicker.dialog;

import com.google.gwt.event.shared.GwtEvent;

public class DialogClosedEvent extends GwtEvent<IDialogClosedHandler> {
	private static GwtEvent.Type<IDialogClosedHandler> TYPE;

	private boolean canceled;
	
	DialogClosedEvent(boolean canceled) {
		this.canceled = canceled;
	}
	
	public static GwtEvent.Type<IDialogClosedHandler> getType() {
		if (TYPE == null) {
			TYPE = new Type<IDialogClosedHandler>();
		}
		return TYPE;
	}
	
	@Override
	public GwtEvent.Type<IDialogClosedHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(IDialogClosedHandler handler) {
		handler.dialogClosed(this);
	}
	
	public boolean isCanceled() {
		return canceled;
	}
}
