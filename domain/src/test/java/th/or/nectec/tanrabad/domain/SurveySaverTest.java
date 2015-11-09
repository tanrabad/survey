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
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import th.or.nectec.tanrabad.entity.Building;

import java.util.ArrayList;

public class SurveySaverTest {

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();
    private SurveyValidator surveyValidator;
    private SurveySavePresenter surveySavePresenter;
    private SurveyRepository surveyRepository;
    private Survey survey;

    @Before
    public void setUp() {
        mockInterfaceClass();
        setupSurveyObject();
    }

    private void mockInterfaceClass() {
        surveySavePresenter = context.mock(SurveySavePresenter.class);
        surveyValidator = context.mock(SurveyValidator.class);
        surveyRepository = context.mock(SurveyRepository.class);
    }

    private void setupSurveyObject() {
        ArrayList<SurveyDetail> indoorDetails = new ArrayList<>();
        indoorDetails.add(SurveyDetail.fromResult(Container.fromId(1), 10, 2));
        ArrayList<SurveyDetail> outdoorDetails = new ArrayList<>();
        outdoorDetails.add(SurveyDetail.fromResult(Container.fromId(2), 5, 0));

        survey = new Survey(User.fromUsername("blaze"), Building.withName("214/2"));
        survey.setResidentCount(4);
        survey.setIndoorDetail(indoorDetails);
        survey.setOutdoorDetail(outdoorDetails);
    }

    @Test
    public void testHappyPath() {
        context.checking(new Expectations() {
            {
                allowing(surveyValidator).validate(with(survey));
                will(returnValue(true));
                oneOf(surveyRepository).save(with(survey));
                will(returnValue(true));
                oneOf(surveySavePresenter).showSaveSuccess();
            }
        });
        SurveySaver surveySaver = new SurveySaver(surveySavePresenter, surveyValidator, surveyRepository);
        surveySaver.save(survey);

    }

    @Test
    public void testSadPath() {
        context.checking(new Expectations() {
            {
                allowing(surveyValidator).validate(survey);
                will(returnValue(false));
                never(surveyRepository);

                oneOf(surveySavePresenter).showSaveFail();
            }
        });
        SurveySaver surveySaver = new SurveySaver(surveySavePresenter, surveyValidator, surveyRepository);
        surveySaver.save(survey);
    }


}
