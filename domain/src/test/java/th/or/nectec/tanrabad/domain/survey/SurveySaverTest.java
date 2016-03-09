/*
 * Copyright (c) 2015 NECTEC
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

package th.or.nectec.tanrabad.domain.survey;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import java.util.UUID;

import th.or.nectec.tanrabad.entity.Building;
import th.or.nectec.tanrabad.entity.Survey;
import th.or.nectec.tanrabad.entity.User;

public class SurveySaverTest {

    private final Survey survey = new Survey(UUID.randomUUID(), User.fromUsername("blaze"), Building.withName("214/2"));
    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();
    @Mock
    private SurveyValidator validator;
    @Mock
    private SurveySavePresenter presenter;
    @Mock
    private SurveyRepository repository;
    
    @Test
    public void testSaveSuccess() {
        context.checking(new Expectations() {
            {
                allowing(validator).validate(with(survey));
                will(returnValue(true));
                oneOf(repository).save(with(survey));
                will(returnValue(true));
                oneOf(presenter).displaySaveSuccess();
            }
        });
        SurveySaver surveySaver = new SurveySaver(presenter, validator, repository);
        surveySaver.save(survey);
    }

    @Test
    public void testSaveNotPassValidator() {
        context.checking(new Expectations() {
            {
                allowing(validator).validate(survey);
                will(returnValue(false));
                never(repository);
                oneOf(presenter).displaySaveFail();
            }
        });
        SurveySaver surveySaver = new SurveySaver(presenter, validator, repository);
        surveySaver.save(survey);
    }


    @Test
    public void testSavePassValidatorButCannotSave() {
        context.checking(new Expectations() {
            {
                allowing(validator).validate(survey);
                will(returnValue(true));
                allowing(repository).save(survey);
                will(returnValue(false));
                oneOf(presenter).displaySaveFail();
            }
        });
        SurveySaver surveySaver = new SurveySaver(presenter, validator, repository);
        surveySaver.save(survey);
    }


    @Test
    public void testUpdateSuccess() {
        context.checking(new Expectations() {
            {
                allowing(validator).validate(with(survey));
                will(returnValue(true));
                oneOf(repository).update(with(survey));
                will(returnValue(true));
                oneOf(presenter).displayUpdateSuccess();
            }
        });
        SurveySaver surveySaver = new SurveySaver(presenter, validator, repository);
        surveySaver.update(survey);
    }

    @Test
    public void testUpdateNotPassValidator() {
        context.checking(new Expectations() {
            {
                allowing(validator).validate(survey);
                will(returnValue(false));
                never(repository);
                oneOf(presenter).displayUpdateFail();
            }
        });
        SurveySaver surveySaver = new SurveySaver(presenter, validator, repository);
        surveySaver.update(survey);
    }


    @Test
    public void testUpdatePassValidatorButCannotUpdate() {
        context.checking(new Expectations() {
            {
                allowing(validator).validate(survey);
                will(returnValue(true));
                allowing(repository).update(survey);
                will(returnValue(false));
                oneOf(presenter).displayUpdateFail();
            }
        });
        SurveySaver surveySaver = new SurveySaver(presenter, validator, repository);
        surveySaver.update(survey);
    }


}
