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

package com.imaginedreal.mgwt.trafficflow.client.widget.image;

import com.imaginedreal.mgwt.trafficflow.client.widget.image.LocalImageHolder.LocalImageHolderAppearance.Images;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ImageResource;

public class LocalImageHolder {
    private static final LocalImageHolderAppearance APPEARANCE = GWT
            .create(LocalImageHolderAppearance.class);
    
    public interface LocalImageHolderAppearance {
        public interface Images {
            ImageResource menu();
            ImageResource camera();
        }
        
        Images get();
    }
    
    public static Images get() {
        return APPEARANCE.get();
    }
}
