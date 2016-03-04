/*
 * Copyright (c) 2016 NECTEC
 *   National Electronics and Computer Technology Center, Thailand
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package th.or.nectec.tanrabad.survey.utils;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorRes;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;

import th.or.nectec.tanrabad.entity.field.Location;
import th.or.nectec.tanrabad.survey.presenter.maps.LocationUtils;
import th.or.nectec.tanrabad.survey.utils.android.ResourceUtils;

public class MapUtils {
    public static BitmapDescriptor getIconBitmapDescriptor(Context context, @ColorRes int colorRes) {
        int color = ResourceUtils.from(context).getColor(colorRes);
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }

    public static CameraUpdate locationZoom(Location location, int zoomLevel) {
        LatLng position = LocationUtils.convertLocationToLatLng(location);
        return CameraUpdateFactory.newLatLngZoom(position, zoomLevel);
    }
}
