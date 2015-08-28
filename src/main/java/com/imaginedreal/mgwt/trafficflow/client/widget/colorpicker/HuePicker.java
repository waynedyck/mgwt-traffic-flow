package com.imaginedreal.mgwt.trafficflow.client.widget.colorpicker;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.imaginedreal.mgwt.trafficflow.client.widget.colorpicker.canvas.Canvas;
import com.imaginedreal.mgwt.trafficflow.client.widget.colorpicker.canvas.RenderingContext;

public class HuePicker extends Composite {
	private Canvas canvas;
	private int handleY = 90;
	private boolean mouseDown;

	public HuePicker() {
		canvas = new Canvas();
		canvas.setStylePrimaryName("subshell-HuePicker");
		canvas.setCanvasSize(26, 180);
		
		initWidget(canvas);

		canvas.addMouseDownHandler(new MouseDownHandler() {
			public void onMouseDown(MouseDownEvent event) {
				handleY = event.getRelativeY(canvas.getElement());
				drawGradient();
				fireHueChanged(getHue());
				
				mouseDown = true;
			}
		});
		canvas.addMouseMoveHandler(new MouseMoveHandler() {
			public void onMouseMove(MouseMoveEvent event) {
				if (mouseDown) {
					handleY = event.getRelativeY(canvas.getElement());
					drawGradient();
					fireHueChanged(getHue());
				}
			}
		});
		canvas.addMouseUpHandler(new MouseUpHandler() {
			public void onMouseUp(MouseUpEvent event) {
				mouseDown = false;
			}
		});
		canvas.addMouseOutHandler(new MouseOutHandler() {
			public void onMouseOut(MouseOutEvent event) {
				mouseDown = false;
			}
		});
	}

	@Override
	protected void onAttach() {
		super.onAttach();
		drawGradient();
	}

	private void drawGradient() {
		RenderingContext ctx = canvas.getContext();

		// draw gradient
		ctx.setFillStyle("#ffffff"); //$NON-NLS-1$
		ctx.fillRect(0, 0, 26, 180);
		for (int y = 0; y <= 179; y++) {
			String hex = ColorUtils.hsl2hex(y * 2, 100, 100);
			ctx.setFillStyle("#" + hex); //$NON-NLS-1$
			ctx.fillRect(3, y, 20, 1);
		}

		// draw handle
		if (handleY >= 0) {
			ctx.setFillStyle("#000000"); //$NON-NLS-1$

			ctx.beginPath();
			ctx.moveTo(3, handleY);
			ctx.lineTo(0, handleY - 3);
			ctx.lineTo(0, handleY + 3);
			ctx.closePath();
			ctx.fill();
			
			ctx.moveTo(23, handleY);
			ctx.lineTo(26, handleY - 3);
			ctx.lineTo(26, handleY + 3);
			ctx.closePath();
			ctx.fill();
		}
	}
	
	public HandlerRegistration addHueChangedHandler(IHueChangedHandler handler) {
		return addHandler(handler, HueChangedEvent.getType());
	}
	
	private void fireHueChanged(int hue) {
		fireEvent(new HueChangedEvent(hue));
	}
	
	public int getHue() {
		return handleY * 2;
	}

	public void setHue(int hue) {
		handleY = (int) Math.min(Math.max(Math.round(hue / 2d), 0d), 179d);
		drawGradient();
		fireHueChanged(hue);
	}
}
