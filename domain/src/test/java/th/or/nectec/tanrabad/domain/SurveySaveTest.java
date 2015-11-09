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

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;
import th.or.nectec.tanrabad.entity.Building;

import java.util.ArrayList;
import java.util.UUID;

public class SurveySaveTest {

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    @Test
    public void testHappyPath() throws Exception {
        final SavePresenter savePresenter = context.mock(SavePresenter.class);
        final SurveySaveValidator saveValidator = context.mock(SurveySaveValidator.class);
        final ResultRepository repository = context.mock(ResultRepository.class);

        final ArrayList<SurveyDetail> surveyDetails = new ArrayList<>();
        surveyDetails.add(SurveyDetail.fromResult(Container.fromId(1), SurveyDetail.INDOOR, 10, 2));
        surveyDetails.add(SurveyDetail.fromResult(Container.fromId(2), SurveyDetail.OUTDOOR, 5, 0));

        final Building surveyBuilding = new Building(UUID.randomUUID(), "214/2");
        context.checking(new Expectations() {
            {
                allowing(saveValidator).validate();
                will(returnValue(true));
                oneOf(repository).save(with(surveyBuilding),
                        with(TRBUser.fromUserName("blaze")),
                        with(surveyDetails));
                will(returnValue(true));
                oneOf(savePresenter).showSaveSuccess();
            }
        });

        SurveySave surveySave = new SurveySave(savePresenter, saveValidator, repository);
        surveySave.save(surveyBuilding, TRBUser.fromUserName("blaze"), surveyDetails);

    }

    @Test
    public void testBadPath() throws Exception {
        final SavePresenter savePresenter = context.mock(SavePresenter.class);
        final SurveySaveValidator saveValidator = context.mock(SurveySaveValidator.class);
        final ResultRepository repository = context.mock(ResultRepository.class);

        final ArrayList<SurveyDetail> surveyDetails = new ArrayList<>();
        surveyDetails.add(SurveyDetail.fromResult(Container.fromId(1), SurveyDetail.INDOOR, 10, 2));
        surveyDetails.add(SurveyDetail.fromResult(Container.fromId(2), SurveyDetail.OUTDOOR, 5, 0));

        final Building surveyBuilding = new Building(UUID.randomUUID(), "214/2");
        context.checking(new Expectations() {
            {
                allowing(saveValidator).validate();
                will(returnValue(false));
                never(repository);

                oneOf(savePresenter).showSaveFail();
            }
        });

        SurveySave surveySave = new SurveySave(savePresenter, saveValidator, repository);
        surveySave.save(surveyBuilding, TRBUser.fromUserName("blaze"), surveyDetails);
    }


}
