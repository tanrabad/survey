/*
 * Copyright (c) 2015  NECTEC
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

package th.or.nectec.tanrabad.domain;

import th.or.nectec.tanrabad.entity.Survey;

public class SurveySaver {
    private final SurveyValidator saveValidator;
    private final SurveyRepository surveyRepository;
    private SurveySavePresenter surveySavePresenter;

    public SurveySaver(SurveySavePresenter surveySavePresenter, SurveyValidator validator, SurveyRepository surveyRepository) {
        this.surveySavePresenter = surveySavePresenter;
        saveValidator = validator;
        this.surveyRepository = surveyRepository;
    }

    public void save(Survey survey) {
        if (saveValidator.validate(survey)) {
            if (surveyRepository.save(survey))
                surveySavePresenter.showSaveSuccess();
        } else {
            surveySavePresenter.showSaveFail();
        }
    }
}
