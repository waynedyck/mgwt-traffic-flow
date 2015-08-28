package com.imaginedreal.mgwt.trafficflow.client.widget.colorpicker.dialog;

import java.util.Arrays;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public abstract class Dialog extends DialogBox {
	private ClickHandler buttonClickHandler = new ClickHandler() {
		public void onClick(ClickEvent event) {
			buttonClicked((Widget) event.getSource());
		}
	};
	private Widget dialogArea;
	private Button okButton;
	private Button cancelButton;
	
	public Dialog() {
		VerticalPanel panel = new VerticalPanel();
		dialogArea = createDialogArea();
		panel.add(dialogArea);
		panel.add(createButtonBar());
		setWidget(panel);
	}
	
	public HandlerRegistration addDialogClosedHandler(IDialogClosedHandler handler) {
		return addHandler(handler, DialogClosedEvent.getType());
	}

	protected void close(boolean canceled) {
		hide();
		fireDialogClosed(canceled);
	}

	private void fireDialogClosed(boolean canceled) {
		fireEvent(new DialogClosedEvent(canceled));
	}

	protected Widget createButtonBar() {
		FlowPanel buttonsPanel = new FlowPanel();
		buttonsPanel.setStyleName("DialogButtons"); //$NON-NLS-1$
		List<? extends Widget> buttons = createButtonsForButtonBar();
		for (Widget button : buttons) {
			buttonsPanel.add(button);
		}
		return buttonsPanel;
	}

	protected List<? extends Widget> createButtonsForButtonBar() {
		okButton = createButton("OK"); //$NON-NLS-1$
		cancelButton = createButton("Cancel"); //$NON-NLS-1$
		return Arrays.asList(okButton, cancelButton);
	}

	protected Button createButton(String text) {
		return new Button(text, buttonClickHandler);
	}

	protected abstract Widget createDialogArea();
	
	protected Widget getDialogArea() {
		return dialogArea;
	}

	protected abstract void buttonClicked(Widget button);
	
	protected Button getOkButton() {
		return okButton;
	}
	
	protected Button getCancelButton() {
		return cancelButton;
	}
}
