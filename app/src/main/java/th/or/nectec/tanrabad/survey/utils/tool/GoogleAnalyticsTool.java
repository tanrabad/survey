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

package th.or.nectec.tanrabad.survey.utils.tool;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders.EventBuilder;
import com.google.android.gms.analytics.Tracker;
import th.or.nectec.tanrabad.entity.Building;
import th.or.nectec.tanrabad.entity.Place;
import th.or.nectec.tanrabad.entity.Survey;
import th.or.nectec.tanrabad.entity.User;
import th.or.nectec.tanrabad.survey.BuildConfig;
import th.or.nectec.tanrabad.survey.R;

public final class GoogleAnalyticsTool extends FabricTools {

    public static final String CATEGORY_TOOLS = "Tools";
    public static final String CATEGORY_UX = "UX";
    public static final String CATEGORY_USAGE = "Usage";
    public static final int ORGANIZATION = 1;
    public static final int USER_TYPE = 2;
    public static final int OBJECT_NAME = 4;
    public static final int USER = 3;
    public static final int OBJECT_TYPE = 5;
    private static GoogleAnalyticsTool instance;
    private final Tracker tracker;

    private GoogleAnalyticsTool(Context context) {
        super(context);
        tracker = buildTracker(context);
        suppressLintWarning(context);
    }

    private Tracker buildTracker(Context context) {
        GoogleAnalytics analytics = GoogleAnalytics.getInstance(context);
        analytics.setDryRun(BuildConfig.DEBUG);
        Tracker tracker = analytics.newTracker(R.xml.global_tracker);
        tracker.setAppVersion(context.getString(R.string.app_version));
        tracker.setAppName(context.getString(R.string.app_name));
        tracker.enableAutoActivityTracking(true);
        tracker.enableExceptionReporting(true);
        tracker.enableAdvertisingIdCollection(true);
        return tracker;

    }

    private void suppressLintWarning(Context context) {
        //lint warning UnusedResource
        if (BuildConfig.DEBUG) {
            Log.d("GA", context.getString(R.string.ga_trackingId));
            Log.d("GA", context.getString(R.string.gcm_defaultSenderId));
            Log.d("GA", context.getString(R.string.google_app_id));
        }
    }

    public static GoogleAnalyticsTool getInstance(Context context) {
        if (instance == null) {
            instance = new GoogleAnalyticsTool(context);
        }
        return instance;
    }

    @Override
    public void login(User user) {
        super.login(user);
        tracker.set("&uid", String.valueOf(user.hashCode()));
        tracker.send(new EventBuilder(CATEGORY_UX, "User Login")
                .setCustomDimension(ORGANIZATION, String.valueOf(user.getOrganizationId()))
                .setCustomDimension(USER_TYPE, user.getUserType().toString())
                .build());
    }

    @Override
    public void useTorch(int durationSecond) {
        super.useTorch(durationSecond);
        tracker.send(new EventBuilder()
                .setCategory(CATEGORY_TOOLS)
                .setAction("Torch")
                .setValue(durationSecond)
                .set("duration (sec)", String.valueOf(durationSecond))
                .build());
    }

    @Override
    public void addBuilding(Building building) {
        super.addBuilding(building);
        tracker.send(new EventBuilder(CATEGORY_USAGE, "Add Building")
                .set("User", building.getUpdateBy())
                .set("name", building.getName())
                .setCustomDimension(USER, building.getUpdateBy())
                .setCustomDimension(OBJECT_NAME, building.getName())
                .build());
    }

    @Override
    public void updateBuilding(Building building) {
        super.updateBuilding(building);
        tracker.send(new EventBuilder(CATEGORY_USAGE, "Update Building")
                .set("User", building.getUpdateBy())
                .set("name", building.getName())
                .setCustomDimension(USER, building.getUpdateBy())
                .setCustomDimension(OBJECT_NAME, building.getName())
                .build());
    }

    @Override
    public void addPlace(Place place) {
        super.addPlace(place);
        tracker.send(new EventBuilder(CATEGORY_USAGE, "Add Place")
                .set("User", place.getUpdateBy())
                .setCustomDimension(USER, place.getUpdateBy())
                .setCustomDimension(OBJECT_NAME, place.getName())
                .setCustomDimension(OBJECT_TYPE, String.valueOf(place.getType()))
                .build());
    }

    @Override
    public void updatePlace(Place place) {
        super.updatePlace(place);
        tracker.send(new EventBuilder(CATEGORY_USAGE, "Update Place")
                .set("User", place.getUpdateBy())
                .setCustomDimension(USER, place.getUpdateBy())
                .setCustomDimension(OBJECT_NAME, place.getName())
                .setCustomDimension(OBJECT_TYPE, String.valueOf(place.getType()))
                .build());
    }

    @Override
    public void searchPlace(String query) {
        super.searchPlace(query);
        tracker.send(new EventBuilder(CATEGORY_UX, "Search Place")
                .setLabel("Query Length")
                .setValue(query.length())
                .build());
    }

    @Override
    public void filterBuilding(String query) {
        super.filterBuilding(query);
        tracker.send(new EventBuilder(CATEGORY_UX, "Filter Place")
                .setLabel("Query Length")
                .setValue(query.length())
                .build());
    }

    @Override
    public void firstTimeWithoutInternet() {
        super.firstTimeWithoutInternet();
        tracker.send(new EventBuilder(CATEGORY_UX, "Start app without Internet")
                .build());
    }

    @Override
    public void startSurvey(Place place) {
        super.startSurvey(place);
        tracker.send(new EventBuilder(CATEGORY_UX, "Start Survey Place")
                .setCustomDimension(OBJECT_TYPE, String.valueOf(place.getType()))
                .build());
    }

    @Override
    public void startSurvey(Survey survey) {
        super.startSurvey(survey);
        User surveyor = survey.getUser();
        tracker.send(new EventBuilder(CATEGORY_UX, "Start Survey Building")
                .set("User Type", surveyor.getUserType().toString())
                .setCustomDimension(ORGANIZATION, String.valueOf(surveyor.getOrganizationId()))
                .setCustomDimension(USER_TYPE, surveyor.getUserType().toString())
                .build());
    }

    @Override
    public void updateSurvey(Survey survey) {
        super.updateSurvey(survey);
        User surveyor = survey.getUser();
        tracker.send(new EventBuilder(CATEGORY_UX, "Update Survey Building")
                .set("User Type", surveyor.getUserType().toString())
                .setCustomDimension(ORGANIZATION, String.valueOf(surveyor.getOrganizationId()))
                .setCustomDimension(USER_TYPE, surveyor.getUserType().toString())
                .build());
    }

    @Override
    public void finishSurvey(Place place, boolean success) {
        super.finishSurvey(place, success);
        tracker.send(new EventBuilder(CATEGORY_UX, success ? "Exit Survey with Finish" : "Exit Survey with Back")
                .build());
    }

    @Override
    public void finishSurvey(Survey survey, boolean success) {
        super.finishSurvey(survey, success);
        tracker.send(new EventBuilder(CATEGORY_UX, success ? "Finish Buiding Survey Update" : "Cancel Survey update")
                .build());
    }
}
