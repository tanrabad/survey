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

package th.or.nectec.tanrabad.survey.repository.persistence;

import th.or.nectec.tanrabad.entity.Building;
import th.or.nectec.tanrabad.entity.Survey;
import th.or.nectec.tanrabad.entity.User;

import java.util.UUID;

/**
 * Created by blaze on 2/14/2016 AD.
 */
public class SurveyWithChange extends Survey {

    public final int changeStatus;

    public SurveyWithChange(UUID surveyId, User user, Building surveyBuilding, int changeStatus) {
        super(surveyId, user, surveyBuilding);
        this.changeStatus = changeStatus;
    }

    public boolean isNotSynced() {
        return changeStatus != ChangedStatus.UNCHANGED;
    }
}