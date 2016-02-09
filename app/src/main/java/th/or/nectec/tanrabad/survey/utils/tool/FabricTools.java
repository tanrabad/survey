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
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.*;
import com.crashlytics.android.core.CrashlyticsCore;
import io.fabric.sdk.android.Fabric;
import th.or.nectec.tanrabad.entity.Building;
import th.or.nectec.tanrabad.entity.Place;
import th.or.nectec.tanrabad.entity.Survey;
import th.or.nectec.tanrabad.entity.User;
import th.or.nectec.tanrabad.entity.utils.ContainerIndex;
import th.or.nectec.tanrabad.survey.BuildConfig;

public class FabricTools implements ExceptionLogger, ActionLogger {

    private static FabricTools instance;
    private Answers answers;

    private FabricTools(Context context) {
        init(context);
    }

    public void init(Context context) {
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
        Crashlytics.logException(e);
    }

    @Override
    public void login(User user) {
        Crashlytics.setUserName(user.getUsername());
        answers.logLogin(new LoginEvent()
                .putCustomAttribute("Health-Region Code", user.getHealthRegionCode())
                .putCustomAttribute("Organization ID", user.getOrganizationId())
                .putCustomAttribute("User Type", user.getUserType().toString())
                .putSuccess(true));
    }

    @Override
    public void turnOnTorch() {
        answers.logLevelStart(new LevelStartEvent()
                .putLevelName(Level.TORCH));
    }

    @Override
    public void turnOffTorch() {
        answers.logLevelEnd(new LevelEndEvent()
                .putLevelName(Level.TORCH));
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
                .putCustomAttribute("Entity", Place.class.getName())
                .putQuery(query));
    }

    @Override
    public void filterBuilding(String query) {
        answers.logSearch(new SearchEvent()
                .putCustomAttribute("Entity", Building.class.getName())
                .putQuery(query));
    }

    @Override
    public void firstTimeWithoutInternet() {
        answers.logCustom(new CustomEvent("Start Without Internet"));
    }

    @Override
    public void startSurvey(Place place) {
        answers.logLevelStart(new LevelStartEvent()
                .putCustomAttribute("Place Name", place.getName())
                .putCustomAttribute("Place Type", place.getType())
                .putLevelName(Level.SURVEY_PLACE));

    }

    @Override
    public void finishSurvey(Place place, boolean success) {
        answers.logLevelEnd(new LevelEndEvent()
                .putSuccess(success)
                .putLevelName(Level.SURVEY_PLACE));

    }

    @Override
    public void startSurvey(Survey survey) {
        answers.logLevelStart(new LevelStartEvent()
                .putCustomAttribute("Mode", "NEW")
                .putCustomAttribute("Place Type", survey.getSurveyBuilding().getPlace().getSubType())
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
    public void finishSurvey(Survey survey, boolean success) {
        answers.logLevelEnd(new LevelEndEvent()
                .putScore(getScore(survey, success))
                .putSuccess(success)
                .putLevelName(Level.SURVEY_BUILDING));
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

    private static class Event {
        public static final String ADD_BUILDING = "Add Building";
        public static final String EDIT_BUILDING = "Edit building";
        public static final String ADD_PLACE = "Add Place";
        public static final String EDIT_PLACE = "Edit Place";
    }


    private static class Level {
        public static final String SURVEY_BUILDING = "Survey Building";
        public static final String SURVEY_PLACE = "Survey Place";
        public static final String TORCH = "Torch";
    }


}

