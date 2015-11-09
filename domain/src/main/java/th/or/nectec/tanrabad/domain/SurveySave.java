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

import th.or.nectec.tanrabad.entity.Building;

import java.util.ArrayList;

public class SurveySave {
    private final SurveySaveValidator saveValidator;
    private final ResultRepository resultRepository;
    private SavePresenter savePresenter;

    public SurveySave(SavePresenter savePresenter, SurveySaveValidator validator, ResultRepository resultRepository) {
        this.savePresenter = savePresenter;
        saveValidator = validator;
        this.resultRepository = resultRepository;
    }

    public void save(Building building, TRBUser blaze, ArrayList<SurveyDetail> surveyDetails) {
        if (saveValidator.validate()) {
            if (resultRepository.save(building, blaze, surveyDetails))
                savePresenter.showSaveSuccess();
        } else {
            savePresenter.showSaveFail();
        }
    }

    public void save(Survey survey) {
        if (saveValidator.validate(survey)) {
            if (resultRepository.save(survey))
                savePresenter.showSaveSuccess();
        }
    }
}
