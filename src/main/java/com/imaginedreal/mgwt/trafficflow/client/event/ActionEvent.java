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

package com.imaginedreal.mgwt.trafficflow.client.event;

import com.google.web.bindery.event.shared.Event;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;

public class ActionEvent extends Event<ActionEvent.Handler> {

	public interface Handler {
		void onAction(ActionEvent event);
	}

	private static final Type<ActionEvent.Handler> TYPE = new Type<ActionEvent.Handler>();

	public static void fire(EventBus eventBus, String sourceName) {
		eventBus.fireEventFromSource(new ActionEvent(), sourceName);
	}

	public static HandlerRegistration register(EventBus eventBus,
			String sourceName, Handler handler) {
		
		return eventBus.addHandlerToSource(TYPE, sourceName, handler);
	}

	@Override
	public Type<Handler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ActionEvent.Handler handler) {
		handler.onAction(this);
	}

	protected ActionEvent() {
	}
	
}
