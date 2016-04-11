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

package com.imaginedreal.mgwt.trafficflow.client.activities.tacoma;

import java.util.List;
import java.util.Map;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.Context2d.LineCap;
import com.google.gwt.canvas.dom.client.Context2d.LineJoin;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.Touch;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.TouchStartEvent;
import com.google.gwt.event.dom.client.TouchStartHandler;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.dom.client.event.tap.TapEvent;
import com.googlecode.mgwt.dom.client.event.tap.TapHandler;
import com.googlecode.mgwt.ui.client.MGWT;
import com.googlecode.mgwt.ui.client.widget.button.image.MenuImageButton;
import com.googlecode.mgwt.ui.client.widget.button.image.RefreshImageButton;
import com.googlecode.mgwt.ui.client.widget.panel.flex.FlexSpacer;
import com.googlecode.mgwt.ui.client.widget.panel.scroll.ScrollPanel;
import com.googlecode.mgwt.ui.client.widget.progress.ProgressIndicator;
import com.googlecode.mgwt.ui.client.widget.touch.TouchDelegate;
import com.imaginedreal.mgwt.trafficflow.client.resources.Resources;
import com.imaginedreal.mgwt.trafficflow.client.util.ParserUtils;
import com.imaginedreal.mgwt.trafficflow.client.widget.button.image.CameraImageButton;
import com.imaginedreal.mgwt.trafficflow.shared.CameraItem;
import com.imaginedreal.mgwt.trafficflow.shared.StationItem;

public class TacomaViewGwtImpl extends Composite implements TacomaView {

	/**
	 * The UiBinder interface.
	 */	
	interface TacomaViewGwtImplUiBinder extends
			UiBinder<Widget, TacomaViewGwtImpl> {
	}
	
	/**
	 * The UiBinder used to generate the view.
	 */
	private static TacomaViewGwtImplUiBinder uiBinder = GWT
			.create(TacomaViewGwtImplUiBinder.class);
	
	@UiField
	FlowPanel flowPanel;
	
	@UiField
	ProgressIndicator progressIndicator;
	
	@UiField
	ScrollPanel scrollPanel;
	
	@UiField
	FlowPanel container;
	
	@UiField(provided = true)
	Canvas mapCanvas;

	@UiField(provided = true)
	Canvas flowCanvas;
	
	@UiField(provided = true)
	Canvas cameraCanvas;
	
	@UiField
	MenuImageButton menuButton;

    @UiField
    FlexSpacer leftFlexSpacer;

	@UiField
	CameraImageButton cameraButton;
	
	@UiField
	RefreshImageButton refreshButton;

	@UiField
	Label lastUpdatedLabel;

	private Presenter presenter;
	private static Context2d context;
	private static Context2d context2;
	private static Context2d context3;
	private int mouseX = 0;
	private int mouseY = 0;
	private static TouchDelegate touchDelegate;
	private static Storage localStorage;
	
	public TacomaViewGwtImpl() {

		mapCanvas = Canvas.createIfSupported();
		flowCanvas = Canvas.createIfSupported();
		cameraCanvas = Canvas.createIfSupported();
		localStorage = Storage.getLocalStorageIfSupported();
		touchDelegate = new TouchDelegate(cameraCanvas);

		initWidget(uiBinder.createAndBindUi(this));

        if (MGWT.getOsDetection().isAndroid()) {
            leftFlexSpacer.setVisible(false);
        }

		getMap();
	}

	private void getMap() {
		// Tacoma map canvas size in px
	    final int width = 1360;
	    final int height = 1204;
	    
        // get the 2D rendering context
        context = mapCanvas.getContext2d();

        // initialize the canvas
        mapCanvas.setWidth(width + "px");
        mapCanvas.setHeight(height + "px");
        mapCanvas.setPixelSize(width, height);
        mapCanvas.setCoordinateSpaceWidth(width);
        mapCanvas.setCoordinateSpaceHeight(height);
        
        container.setWidth(width + "px");
               
        final Image tacomaImage = new Image(Resources.INSTANCE.tacomaArea());
        tacomaImage.addLoadHandler(new LoadHandler() {

			@Override
			public void onLoad(LoadEvent event) {
                ImageElement imageElement = tacomaImage.getElement().cast();
                context.drawImage(imageElement, 0, 0);
			}
		});
        
        tacomaImage.setVisible(false);
        container.add(tacomaImage);
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}
	
	@UiHandler("refreshButton")
	protected void onRefreshMapButtonPressed(TapEvent event) {
	    if (presenter != null) {
	        presenter.onRefreshMapButtonPressed();
	    }
	}

	@UiHandler("menuButton")
	protected void onMenuButtonPressed(TapEvent event) {
		if (presenter != null) {
			presenter.onMenuButonPressed();
		}
	}
	
	@UiHandler("cameraButton")
	protected void onCameraButtonPressed(TapEvent event) {
		if (presenter != null) {
			presenter.onCameraButtonPressed(Boolean.valueOf(localStorage
					.getItem("KEY_SHOW_CAMERAS")));
		}
	}
	
    @Override
    public void refresh() {
        scrollPanel.refresh();
    }

	@Override
	public void renderFlowMap(final Map<String, StationItem> stationItems) {
	    // canvas size in px
	    final int width = 1360;
	    final int height = 1204;
	    
        // get the 2D rendering context
        context2 = flowCanvas.getContext2d();

        // initialize the canvas
        flowCanvas.setWidth(width + "px");
        flowCanvas.setHeight(height + "px");
        flowCanvas.setPixelSize(width, height);
        flowCanvas.setCoordinateSpaceWidth(width);
        flowCanvas.setCoordinateSpaceHeight(height);
               
        for (Map.Entry<String, StationItem> entry: stationItems.entrySet()) {
        	String[] values = entry.getValue().getXY().split(";");

            context2.beginPath();
            
            for (int i = 1; i < values.length; i++) {
                String[] coords = values[i].split(",");
                if (i == 1) {
                	context2.moveTo(Double.valueOf(coords[0]) + 0.5, Double.valueOf(coords[1]) + 0.5);
                } else {
                	context2.lineTo(Double.valueOf(coords[0]) + 0.5, Double.valueOf(coords[1]) + 0.5);
                }
            }
            
            String cssColor = getColorStyle(entry.getValue().getSpeed());
            
            context2.closePath();
            context2.setFillStyle(CssColor.make(cssColor));
            context2.fill();
            context2.setStrokeStyle(CssColor.make(cssColor));
            context2.setLineWidth(1);
            context2.setLineCap(LineCap.SQUARE);
            context2.setLineJoin(LineJoin.BEVEL);
            context2.stroke();
        }
	}
	
	@Override
	public void renderCameras(final List<CameraItem> cameraItems) {
		// canvas size in px
	    final int width = 1121;
	    final int height = 2585;
        
	    // get the 2D rendering context
        context3 = cameraCanvas.getContext2d();

        // initialize the canvas
        cameraCanvas.setWidth(width + "px");
        cameraCanvas.setHeight(height + "px");
        cameraCanvas.setPixelSize(width, height);
        cameraCanvas.setCoordinateSpaceWidth(width);
        cameraCanvas.setCoordinateSpaceHeight(height);

        for (final CameraItem item : cameraItems) {
        	final Image cameraImage = new Image(Resources.INSTANCE.camera());
            cameraImage.addLoadHandler(new LoadHandler() {

    			@Override
    			public void onLoad(LoadEvent event) {
                    ImageElement imageElement = cameraImage.getElement().cast();
					context3.drawImage(imageElement,
							Double.parseDouble(item.getX()),
							Double.parseDouble(item.getY()));
    			}
    		});
            
            cameraImage.setVisible(false);
            container.add(cameraImage);
        }
        
        cameraCanvas.addMouseDownHandler(new MouseDownHandler() {
			@Override
			public void onMouseDown(MouseDownEvent event) {
				mouseX = event.getRelativeX(cameraCanvas.getElement());
				mouseY = event.getRelativeY(cameraCanvas.getElement());
				for (CameraItem item : cameraItems) {
					if (((mouseX >= Double.parseDouble(item.getX()) && (mouseX <= (Double
							.parseDouble(item.getX()) + Double.parseDouble(item
							.getWidth())))) && ((mouseY >= Double
							.parseDouble(item.getY())) && (mouseY <= (Double
							.parseDouble(item.getY()) + Double.parseDouble(item
							.getHeight())))))) {
						if (presenter != null) {
							presenter.onCameraImagePressed(item);
						}
					}
				}
			}
		});
        
        cameraCanvas.addTouchStartHandler(new TouchStartHandler() {
			@Override
			public void onTouchStart(TouchStartEvent event) {
			    event.preventDefault();
				if (event.getTouches().length() > 0) {
					Touch touch = event.getTouches().get(0);
					mouseX = touch.getRelativeX(cameraCanvas.getElement());
					mouseY = touch.getRelativeY(cameraCanvas.getElement());
				}
			}
		});
        
        touchDelegate.addTapHandler(new TapHandler() {
			@Override
			public void onTap(TapEvent event) {
				for (CameraItem item : cameraItems) {
					if (((mouseX >= Double.parseDouble(item.getX()) && (mouseX <= (Double
							.parseDouble(item.getX()) + Double.parseDouble(item
							.getWidth())))) && ((mouseY >= Double
							.parseDouble(item.getY())) && (mouseY <= (Double
							.parseDouble(item.getY()) + Double.parseDouble(item
							.getHeight())))))) {
						if (presenter != null) {
							presenter.onCameraImagePressed(item);
						}
					}
				}
			}
		});
        
		if (Boolean.valueOf(localStorage.getItem("KEY_SHOW_CAMERAS"))) {
			showCameras();
		} else {
			hideCameras();
		}
	}
	
	protected String getColorStyle(int speed) {
		String cssColor = localStorage.getItem("KEY_COLOR_NOEQUIPMENT");
		
		if (speed < 1) {
			cssColor = localStorage.getItem("KEY_COLOR_NODATA");
		} else if (speed >= 1 && speed < 30) {
			cssColor = localStorage.getItem("KEY_COLOR_STOPANDGO"); // black
		} else if (speed >= 30 && speed < 40) {
			cssColor = localStorage.getItem("KEY_COLOR_HEAVY"); // red
		} else if (speed >= 40 && speed < 50) {
			cssColor = localStorage.getItem("KEY_COLOR_MODERATE"); // yellow
		} else if (speed >= 50) {
			cssColor = localStorage.getItem("KEY_COLOR_WIDEOPEN"); // green
		}
		
		return "#" + cssColor;
	}

	@Override
	public void showProgressIndicator() {
		progressIndicator.setVisible(true);
	}

	@Override
	public void hideProgressIndicator() {
		progressIndicator.setVisible(false);
	}

	@Override
    public void refreshMap() {
		context2 = flowCanvas.getContext2d();
		context2.clearRect(0, 0, 1360, 1204);
    }

	@Override
	public void hideCameras() {
		cameraCanvas.setVisible(false);
		localStorage.setItem("KEY_SHOW_CAMERAS", "false");
	}

	@Override
	public void showCameras() {
		cameraCanvas.setVisible(true);
		localStorage.setItem("KEY_SHOW_CAMERAS", "true");
	}

	@Override
	public String getMapXY() {
		int x = scrollPanel.getX();
		int y = scrollPanel.getY();
		
		return x + "," + y;
	}

	@Override
	public void setMapXY(String xyPosition) {
		String[] xy = xyPosition.split(",");
		int x = Integer.parseInt(xy[0]);
		int y = Integer.parseInt(xy[1]);		
		
		scrollPanel.scrollTo(x, y);
	}
	
	@Override
	public void setLastUpdated(String timestamp) {
		lastUpdatedLabel.setText(ParserUtils.relativeTime(timestamp,
				"MMMM d, yyyy h:mm a", false));
	}
}