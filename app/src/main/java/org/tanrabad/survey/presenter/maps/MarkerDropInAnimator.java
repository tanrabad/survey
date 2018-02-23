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

package org.tanrabad.survey.presenter.maps;


import android.content.Context;
import android.graphics.Point;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.view.animation.Interpolator;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import org.tanrabad.survey.utils.android.DpCalculator;

class MarkerDropInAnimator {

    private GoogleMap map;
    private Context context;
    private Marker marker;

    public MarkerDropInAnimator(GoogleMap map, Context context, Marker marker) {
        this.map = map;
        this.context = context;
        this.marker = marker;
    }

    public void start() {
        long duration = calculateDropInDuration(marker);
        LatLng startLatLng = startLatLng(marker);
        Handler handler = new Handler();
        handler.post(new MarkerAnimationRunnable(handler, startLatLng, marker.getPosition(), duration));
    }

    private long calculateDropInDuration(Marker targetLatLng) {
        Point targetPoint = map.getProjection().toScreenLocation(targetLatLng.getPosition());
        return (long) (200 + DpCalculator.from(context).toDp(targetPoint.y));
    }

    private LatLng startLatLng(Marker marker) {
        Projection projection = map.getProjection();
        Point startPoint = projection.toScreenLocation(marker.getPosition());
        startPoint.y = 0;
        return projection.fromScreenLocation(startPoint);
    }

    private class MarkerAnimationRunnable implements Runnable {
        long startTime;
        long duration;
        Handler handler;
        LatLng target;
        LatLng startLatLng;
        Interpolator interpolator = new LinearOutSlowInInterpolator();

        MarkerAnimationRunnable(Handler handler, LatLng startLatLng, LatLng target, long duration) {
            this.handler = handler;
            this.target = target;
            this.startLatLng = startLatLng;
            this.duration = duration;
        }

        @Override
        public void run() {
            if (startTime == 0) startTime = SystemClock.uptimeMillis();
            long elapsed = SystemClock.uptimeMillis() - startTime;
            float progress = interpolator.getInterpolation((float) elapsed / duration);
            double lng = progress * target.longitude + (1 - progress) * startLatLng.longitude;
            double lat = progress * target.latitude + (1 - progress) * startLatLng.latitude;
            marker.setPosition(new LatLng(lat, lng));
            if (progress < 1.0) {
                handler.postDelayed(this, 16);
            }
        }
    }

}
