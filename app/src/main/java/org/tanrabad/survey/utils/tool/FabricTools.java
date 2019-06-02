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

package org.tanrabad.survey.utils.tool;

import android.content.Context;
import android.util.Log;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.crashlytics.android.answers.LevelEndEvent;
import com.crashlytics.android.answers.LevelStartEvent;
import com.crashlytics.android.answers.LoginEvent;
import com.crashlytics.android.answers.SearchEvent;
import com.crashlytics.android.core.CrashlyticsCore;
import io.fabric.sdk.android.Fabric;
import org.tanrabad.survey.BuildConfig;
import org.tanrabad.survey.domain.entomology.ContainerIndex;
import org.tanrabad.survey.entity.Building;
import org.tanrabad.survey.entity.Place;
import org.tanrabad.survey.entity.Survey;
import org.tanrabad.survey.entity.User;
import org.tanrabad.survey.repository.BrokerPlaceSubTypeRepository;
import org.tanrabad.survey.repository.BrokerPlaceTypeRepository;
import org.tanrabad.survey.service.AbsRestService;

public class FabricTools implements ExceptionLogger, ActionLogger {

    private static FabricTools instance;
    private Answers answers;

    FabricTools(Context context) {
        init(context);
    }

    private void init(Context context) {
        Crashlytics crashlytics = new Crashlytics.Builder()
                .core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
                .build();
        Fabric.with(context, crashlytics, new Answers());
        answers = Answers.getInstance();
    }

    public static FabricTools getInstance(Context context) {
        if (instance == null) {
            instance = new FabricTools(context);
        }
        return instance;
    }

    @Override
    public void log(Exception e) {
        if (BuildConfig.DEBUG)
            e.printStackTrace();
        else
            Crashlytics.logException(e);
    }

    @Override
    public void log(String message) {
        if (BuildConfig.DEBUG)
            Log.e("ทันระบาด", message);
    }

    @Override
    public void login(User user) {
        Crashlytics.setUserName(user.getUsername());
        answers.logLogin(new LoginEvent()
                .putCustomAttribute("Health-Region Code", user.getHealthRegionCode())
                .putCustomAttribute("Organization ID", String.valueOf(user.getOrganizationId()))
                .putCustomAttribute("User Type", user.getUserType().toString())
                .putSuccess(true));
    }

    @Override
    public void useTorch(int durationSecond) {
        answers.logCustom(new CustomEvent("Use Torch")
                .putCustomAttribute("duration (sec)", durationSecond));
    }

    @Override
    public void addBuilding(Building building) {
        answers.logCustom(new CustomEvent(Event.ADD_BUILDING));
    }

    @Override
    public void updateBuilding(Building building) {
        answers.logCustom(new CustomEvent(Event.EDIT_BUILDING));
    }

    @Override
    public void addPlace(Place place) {
        answers.logCustom(new CustomEvent(Event.ADD_PLACE));
    }

    @Override
    public void updatePlace(Place place) {
        answers.logCustom(new CustomEvent(Event.EDIT_PLACE));
    }

    @Override
    public void searchPlace(String query) {
        answers.logSearch(new SearchEvent()
                .putCustomAttribute("Entity", "Place")
                .putQuery(query));
    }

    @Override
    public void filterBuilding(String query) {
        answers.logSearch(new SearchEvent()
                .putCustomAttribute("Entity", "Building")
                .putQuery(query));
    }

    @Override
    public void firstTimeWithoutInternet() {
        answers.logCustom(new CustomEvent("Start Without Internet"));
    }

    @Override
    public void startSurvey(Place place, String type) {
        CustomEvent event = new CustomEvent(Event.START_PLACE_SURVEY)
            .putCustomAttribute("Place Name", place.getName())
            .putCustomAttribute("Place Type", getPlaceTypeName(place))
            .putCustomAttribute("Place Sub Type", getPlaceSubTypeName(place))
            .putCustomAttribute("Have Location", place.getLocation() != null ? "yes" : "no")
            .putCustomAttribute("Start Type", type);
        if (place.getWeight() > 0.0)
            event.putCustomAttribute("Suggestion Weight", place.getWeight());
        answers.logCustom(event);
    }

    private String getPlaceTypeName(Place place) {
        return BrokerPlaceTypeRepository.getInstance().findById(place.getType()).getName();
    }

    private String getPlaceSubTypeName(Place place) {
        return BrokerPlaceSubTypeRepository.getInstance().findById(place.getSubType()).getName();
    }

    @Override
    public void startSurvey(Survey survey) {
        answers.logLevelStart(new LevelStartEvent()
                .putCustomAttribute("Mode", "NEW")
                .putCustomAttribute("User Type", survey.getUser().getUserType().toString())
                .putLevelName(Level.SURVEY_BUILDING));
    }

    @Override
    public void updateSurvey(Survey survey) {
        answers.logLevelStart(new LevelStartEvent()
                .putCustomAttribute("Mode", "UPDATE")
                .putCustomAttribute("Last Score", getScore(survey, true))
                .putCustomAttribute("User Type", survey.getUser().getUserType().toString())
                .putLevelName(Level.SURVEY_BUILDING));
    }

    @Override
    public void finishSurvey(Place place, boolean success) {
        answers.logCustom(new CustomEvent(Event.FINISH_PLACE_SURVEY)
                .putCustomAttribute("Finish Method", success ? "finish button" : "back button"));
    }

    @Override
    public void finishSurvey(Survey survey, boolean success) {
        answers.logLevelEnd(new LevelEndEvent()
                .putScore(getScore(survey, success))
                .putSuccess(success)
                .putLevelName(Level.SURVEY_BUILDING));
    }

    @Override public void logout(User user) {
        if (user == null) {
            answers.logCustom(new CustomEvent("Logout")
                .putCustomAttribute("User Info", "false"));
        } else {
            answers.logCustom(new CustomEvent("Logout")
                .putCustomAttribute("Health-Region Code", user.getHealthRegionCode())
                .putCustomAttribute("Organization ID", String.valueOf(user.getOrganizationId()))
                .putCustomAttribute("User Type", user.getUserType().toString())
                .putCustomAttribute("User Info", "true")
            );
        }
    }

    @Override public void cacheHit(AbsRestService<?> restService) {
        answers.logCustom(new CustomEvent("Cache-hit")
            .putCustomAttribute("url", restService.getUrl())
            .putCustomAttribute("class", restService.getClass().getSimpleName()));
    }

    private int getScore(Survey survey, boolean success) {
        int score = 0;
        if (success) {
            ContainerIndex ci = new ContainerIndex(survey);
            ci.calculate();
            score = ci.getTotalContainer();
        }
        return score;
    }

    public void setAppOptOut(boolean appOptOut) {
        //TODO turn off log
    }

    private static class Event {
        public static final String ADD_BUILDING = "Add Building";
        public static final String EDIT_BUILDING = "Edit building";
        public static final String ADD_PLACE = "Add Place";
        public static final String EDIT_PLACE = "Edit Place";
        public static final String START_PLACE_SURVEY = "Start Place Survey";
        public static final String FINISH_PLACE_SURVEY = "Finish Place Survey";
    }

    private static class Level {
        public static final String SURVEY_BUILDING = "Survey Building";
    }

}

