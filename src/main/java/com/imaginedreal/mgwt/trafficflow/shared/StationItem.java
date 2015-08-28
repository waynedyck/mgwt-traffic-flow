/*
 * Copyright 2015 Wayne Dyck
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

package com.imaginedreal.mgwt.trafficflow.shared;

import java.io.Serializable;

public class StationItem implements Serializable {

	private static final long serialVersionUID = 3905559927941723828L;
	private String id;
	private String xy;
	private int volume;
	private int speed;
	private int occupancy;
	private String timestamp;
	
	public StationItem() {
	}
	
	public StationItem(String stationId) {
		this.id = stationId;
	}

	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getXY() {
		return xy;
	}
	
	public void setXY(String xy) {
		this.xy = xy;
	}
	
	public int getVolume() {
		return volume;
	}
	
	public void setVolume(int volume) {
		this.volume = volume;
	}
	
	public int getSpeed() {
		return speed;
	}
	
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	
	public int getOccupancy() {
		return occupancy;
	}
	
	public void setOccupancy(int occupancy) {
		this.occupancy = occupancy;
	}
	
	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	@Override
	public boolean equals(Object obj) {
		return obj instanceof StationItem && obj != null
				&& ((StationItem) obj).getId() == this.id;
	}

}
