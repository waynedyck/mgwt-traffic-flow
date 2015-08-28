package com.imaginedreal.mgwt.trafficflow.client.widget.colorpicker;

import com.google.gwt.core.client.JsArrayInteger;
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
import com.imaginedreal.mgwt.trafficflow.client.widget.colorpicker.canvas.Gradient;
import com.imaginedreal.mgwt.trafficflow.client.widget.colorpicker.canvas.ImageData;
import com.imaginedreal.mgwt.trafficflow.client.widget.colorpicker.canvas.RenderingContext;

public class SaturationLightnessPicker extends Composite {
	private Canvas canvas;
	private int hue = 180;
	private int handleX = 90;
	private int handleY = 90;
	private boolean mouseDown;

	public SaturationLightnessPicker() {
		canvas = new Canvas();
		canvas.setCanvasSize(180, 180);
		
		initWidget(canvas);

		canvas.addMouseDownHandler(new MouseDownHandler() {
			public void onMouseDown(MouseDownEvent event) {
				handleX = event.getRelativeX(canvas.getElement());
				handleY = event.getRelativeY(canvas.getElement());
				drawGradient(false);
				String color = getColorAtPixel(handleX, handleY);
				drawGradient(true);
				fireColorChanged(color);
				
				mouseDown = true;
			}
		});
		canvas.addMouseMoveHandler(new MouseMoveHandler() {
			public void onMouseMove(MouseMoveEvent event) {
				if (mouseDown) {
					handleX = event.getRelativeX(canvas.getElement());
					handleY = event.getRelativeY(canvas.getElement());
					drawGradient(false);
					String color = getColorAtPixel(handleX, handleY);
					drawGradient(true);
					fireColorChanged(color);
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
		drawGradient(true);
	}

	private void drawGradient(boolean drawHandle) {
		RenderingContext ctx = canvas.getContext();
		
		// draw gradient
		for (int x = 0; x <= 179; x++) {
			Gradient grad = ctx.createLinearGradient(x, 0, x, 179);
			int s = Math.round(x * 100 / 179);
			String hex = ColorUtils.hsl2hex(hue, s, 0);
			grad.addColorStop(0, "#" + hex); //$NON-NLS-1$
			hex = ColorUtils.hsl2hex(hue, s, 100);
			grad.addColorStop(1, "#" + hex); //$NON-NLS-1$
			ctx.setFillStyle(grad);
			ctx.fillRect(x, 0, 1, 180);
		}

		// draw handle
		if (drawHandle) {
			ctx.beginPath();
			ctx.arc(handleX, handleY, 3, 0, Math.PI * 2, false);
			ctx.closePath();
			ctx.setFillStyle("#ffffff"); //$NON-NLS-1$
			ctx.fill();
			
			ctx.beginPath();
			ctx.arc(handleX, handleY, 2, 0, Math.PI * 2, false);
			ctx.closePath();
			ctx.setFillStyle("#000000"); //$NON-NLS-1$
			ctx.fill();
		}
	}
	
	public HandlerRegistration addColorChangedHandler(IColorChangedHandler handler) {
		return addHandler(handler, ColorChangedEvent.getType());
	}
	
	private void fireColorChanged(String color) {
		fireEvent(new ColorChangedEvent(color));
	}
	
	private String getColorAtPixel(int x, int y) {
		x = Math.max(Math.min(x, 179), 0);
		y = Math.max(Math.min(y, 179), 0);
		RenderingContext ctx = canvas.getContext();
		ImageData imageData = ctx.getImageData(x, y, 1, 1);
		JsArrayInteger data = imageData.getData();
		return ColorUtils.rgb2hex(data.get(0), data.get(1), data.get(2));
	}
	
	public void setHue(int hue) {
		this.hue = hue;
		drawGradient(false);
		String color = getColorAtPixel(handleX, handleY);
		drawGradient(true);
		fireColorChanged(color);
	}

	public String getColor() {
		drawGradient(false);
		String color = getColorAtPixel(handleX, handleY);
		drawGradient(true);
		return color;
	}

	public void setColor(String color) {
		int[] rgb = ColorUtils.getRGB(color);
		int[] hsl = ColorUtils.rgb2hsl(rgb);
		hue = hsl[0];
		handleX = (int) Math.min(Math.max(Math.round(hsl[1] * 180d / 100d), 0), 179);
		handleY = (int) Math.min(Math.max(Math.round(hsl[2] * 180d / 100d), 0), 179);
		drawGradient(true);
		fireColorChanged(color);
	}
}
