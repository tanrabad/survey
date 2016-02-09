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
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.crashlytics.android.core.CrashlyticsCore;
import io.fabric.sdk.android.Fabric;
import th.or.nectec.tanrabad.entity.Survey;
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
    public void cancelSurvey(Survey survey) {
        answers.logCustom(new CustomEvent("cancel finishSurvey")
                .putCustomAttribute("building-name", survey.getSurveyBuilding().getName())
                .putCustomAttribute("user-id", survey.getUser().getUsername())
                .putCustomAttribute("user-org-id", survey.getUser().getOrganizationId()));
    }

    @Override
    public void updateSurvey(Survey lastSurvey, Survey survey) {
        answers.logCustom(new CustomEvent("update finishSurvey")
                .putCustomAttribute("building-id", survey.getSurveyBuilding().getId().toString())
                .putCustomAttribute("building-name", survey.getSurveyBuilding().getName())
                .putCustomAttribute("user-id", survey.getUser().getUsername())
                .putCustomAttribute("user-org-id", survey.getUser().getOrganizationId())
                .putCustomAttribute("diff-resident-count", lastSurvey.getResidentCount() - survey.getResidentCount())
                .putCustomAttribute("diff-indoor-container-type-count", lastSurvey.getIndoorDetail().size() - survey.getIndoorDetail().size())
                .putCustomAttribute("diff-outdoor-container-type-count", lastSurvey.getIndoorDetail().size() - survey.getOutdoorDetail().size())
        );
    }

    @Override
    public void finishSurvey(Survey survey) {
        answers.logCustom(new CustomEvent("finish finishSurvey")
                .putCustomAttribute("building-id", survey.getSurveyBuilding().getId().toString())
                .putCustomAttribute("building-name", survey.getSurveyBuilding().getName())
                .putCustomAttribute("resident-count", survey.getResidentCount())
                .putCustomAttribute("indoor-container-type-count", survey.getIndoorDetail().size())
                .putCustomAttribute("outdoor-container-type-count", survey.getOutdoorDetail().size())
                .putCustomAttribute("user-id", survey.getUser().getUsername())
                .putCustomAttribute("user-org-id", survey.getUser().getOrganizationId()));
    }
}

