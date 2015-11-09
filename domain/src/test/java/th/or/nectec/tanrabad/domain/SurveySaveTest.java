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

        final ArrayList<SurveyResult> surveyResults = new ArrayList<SurveyResult>();
        surveyResults.add(SurveyResult.fromResult(Container.fromId(1), SurveyResult.INDOOR, 10, 2));
        surveyResults.add(SurveyResult.fromResult(Container.fromId(2), SurveyResult.OUTDOOR, 5, 0));

        final Building surveyBuilding = new Building(UUID.randomUUID(), "214/2");
        context.checking(new Expectations() {
            {
                allowing(saveValidator).validate();
                will(returnValue(true));
                oneOf(repository).save(with(surveyBuilding),
                        with(TRBUser.fromUserName("blaze")),
                        with(surveyResults));
                will(returnValue(true));
                oneOf(savePresenter).showSaveSuccess();
            }
        });

        SurveySave surveySave = new SurveySave(savePresenter, saveValidator, repository);
        surveySave.save(surveyBuilding, TRBUser.fromUserName("blaze"), surveyResults);

    }

    @Test
    public void testBadPath() throws Exception {
        final SavePresenter savePresenter = context.mock(SavePresenter.class);
        final SurveySaveValidator saveValidator = context.mock(SurveySaveValidator.class);
        final ResultRepository repository = context.mock(ResultRepository.class);

        ArrayList<SurveyResult> surveyResults = new ArrayList<SurveyResult>();
        surveyResults.add(SurveyResult.fromResult(Container.fromId(1), SurveyResult.INDOOR, 10, 2));
        surveyResults.add(SurveyResult.fromResult(Container.fromId(2), SurveyResult.OUTDOOR, 5, 0));

        final Building surveyBuilding = new Building(UUID.randomUUID(), "214/2");
        context.checking(new Expectations() {
            {
                allowing(saveValidator).validate();
                will(returnValue(false));
                oneOf(repository).save(with(surveyBuilding),
                        with(TRBUser.fromUserName("blaze")),
                        with(surveyResults));
                will(returnValue(true));
                oneOf(savePresenter).showSaveFail();
            }
        });

        SurveySave surveySave = new SurveySave(savePresenter, saveValidator, repository);
        surveySave.save(surveyBuilding, TRBUser.fromUserName("blaze"), surveyResults);
    }

    public interface SavePresenter {
        void showSaveSuccess();

        void showSaveFail();
    }


    public interface ResultRepository {

        boolean save(Building surveyBuilding, TRBUser surveyor, ArrayList<SurveyResult> results);
    }

    public static class Container {


        private final int typeId;

        public Container(int typeId) {
            this.typeId = typeId;
        }

        public static Container fromId(int typeId) {
            return new Container(typeId);
        }

        @Override
        public boolean equals(Object obj) {
            return this.typeId == ((Container) obj).typeId;
        }
    }

    public static class SurveyResult {
        public static final int INDOOR = 1;
        public static final int OUTDOOR = 2;

        private final Container container;
        private final int location;
        private final int total;
        private final int found;

        public SurveyResult(Container container, int location, int total, int found) {

            this.container = container;
            this.location = location;
            this.total = total;
            this.found = found;
        }

        public static SurveyResult fromResult(Container container, int location, int total, int found) {
            return new SurveyResult(container, location, total, found);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            SurveyResult that = (SurveyResult) o;

            if (location != that.location) return false;
            if (total != that.total) return false;
            if (found != that.found) return false;
            return container.equals(that.container);

        }

        @Override
        public int hashCode() {
            int result = container.hashCode();
            result = 31 * result + location;
            result = 31 * result + total;
            result = 31 * result + found;
            return result;
        }
    }

    public class SurveySave {
        private final SurveySaveValidator saveValidator;
        private final ResultRepository resultRepository;
        private SavePresenter savePresenter;

        public SurveySave(SavePresenter savePresenter, SurveySaveValidator validator, ResultRepository resultRepository) {
            this.savePresenter = savePresenter;
            saveValidator = validator;
            this.resultRepository = resultRepository;
        }

        public void save(Building building, TRBUser blaze, ArrayList<SurveyResult> surveyResults) {
            if (saveValidator.validate()) {
                if (resultRepository.save(building, blaze, surveyResults))
                    savePresenter.showSaveSuccess();
            } else {
                savePresenter.showSaveFail();
            }
        }
    }
}
