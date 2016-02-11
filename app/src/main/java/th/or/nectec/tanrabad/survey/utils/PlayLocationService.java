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
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import org.joda.time.DateTime;
import th.or.nectec.tanrabad.survey.TanrabadApp;
import th.or.nectec.tanrabad.survey.utils.alert.Alert;

import static com.google.android.gms.location.LocationServices.FusedLocationApi;

public class PlayLocationService {

    private static final long MAX_NO_UPDATE = 5 * 60 * 1000;
    private static final long UPDATE_INTERVAL_MS = 60 * 1000;
    private static final long FASTEST_INTERVAL_MS = 15 * 1000;
    private static PlayLocationService instance;
    private final Context context;
    private GoogleApiClient locationApiClient;
    private Location currentLocation;

    private OnConnectionFailedListener connectionFailedListener = new OnConnectionFailedListener() {
        @Override
        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
            Alert.highLevel().show("ไม่สามารถเชื่อมต่อ Google Play Services ได้");
        }
    };

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            currentLocation = location;
        }
    };

    private ConnectionCallbacks connectionCallbacks = new ConnectionCallbacks() {
        @Override
        public void onConnected(@Nullable Bundle bundle) {
            setupLocationUpdateService();
        }

        @Override
        public void onConnectionSuspended(int i) {
            Alert.lowLevel().show("Google Play Service ระงับการติดต่อชั่วคราว");
        }
    };

    private PlayLocationService(Context context) {
        this.context = context;
        setupLocationAPI();
    }

    private void setupLocationAPI() {
        locationApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(connectionCallbacks)
                .addOnConnectionFailedListener(connectionFailedListener)
                .build();
    }

    public static PlayLocationService getInstance() {
        if (instance == null)
            instance = new PlayLocationService(TanrabadApp.getInstance());
        return instance;
    }

    public void connect() {
        if (!locationApiClient.isConnecting() && !locationApiClient.isConnected()) {
            locationApiClient.connect();
        }
    }

    public void disconnect() {
        locationApiClient.disconnect();
    }

    public Location getCurrentLocation() {
        if (currentLocation == null) {
            Alert.lowLevel().show("no location data");
            return null;
        }
        long diffTime = DateTime.now().getMillis() - currentLocation.getTime();
        if (diffTime <= MAX_NO_UPDATE) return currentLocation;
        else {
            Alert.lowLevel().show("location not update");
            return null;
        }
    }

    public Location getLastKnowLocation() {
        return currentLocation;
    }

    public boolean isAvailable() {
        return locationApiClient.isConnected();
    }

    private void setupLocationUpdateService() {
        try {
            FusedLocationApi.requestLocationUpdates(
                    locationApiClient, getLocationRequest(), locationListener);
        } catch (SecurityException securityException) {
            TanrabadApp.log(securityException);
            Alert.lowLevel().show("ไม่มีสิทธิเข้าถึงตำแหน่งปัจจุบัน");
        }
    }

    private LocationRequest getLocationRequest() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERVAL_MS);
        locationRequest.setFastestInterval(FASTEST_INTERVAL_MS);
        return locationRequest;
    }

    public void addConnectionCallbacks(ConnectionCallbacks connectionCallbacks) {
        locationApiClient.registerConnectionCallbacks(connectionCallbacks);
    }

    public void removeConnectionCallbacks(ConnectionCallbacks connectionCallbacks) {
        locationApiClient.unregisterConnectionCallbacks(connectionCallbacks);
    }

}
