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

package th.or.nectec.tanrabad.domain.place;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;
import th.or.nectec.tanrabad.entity.Place;

import java.util.UUID;

public class PlaceSaverTest {
    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();
    @Mock
    protected PlaceValidator validator;
    @Mock
    protected PlaceSavePresenter presenter;
    @Mock
    protected PlaceRepository repository;

    private Place place = new Place(UUID.randomUUID(), "test Place");

    @Test
    public void testSaveSuccess() throws Exception {
        context.checking(new Expectations() {
            {
                allowing(PlaceSaverTest.this.validator).setPlaceRepository(repository);
                allowing(PlaceSaverTest.this.validator).validate(with(place));
                will(returnValue(true));
                oneOf(PlaceSaverTest.this.repository).save(with(place));
                will(returnValue(true));
                oneOf(PlaceSaverTest.this.presenter).displaySaveSuccess();
            }
        });
        PlaceSaver placeSaver = new PlaceSaver(repository, validator, presenter);
        placeSaver.save(place);
    }

    @Test
    public void testUpdateSuccess() throws Exception {

        context.checking(new Expectations() {
            {
                allowing(PlaceSaverTest.this.validator).setPlaceRepository(repository);
                allowing(PlaceSaverTest.this.validator).validate(with(place));
                will(returnValue(true));
                oneOf(PlaceSaverTest.this.repository).update(with(place));
                will(returnValue(true));
                oneOf(PlaceSaverTest.this.presenter).displayUpdateSuccess();
            }
        });
        PlaceSaver placeSaver = new PlaceSaver(repository, validator, presenter);
        placeSaver.update(place);
    }

    @Test
    public void testSaveNotPassValidator() throws Exception {
        context.checking(new Expectations() {
            {
                allowing(PlaceSaverTest.this.validator).setPlaceRepository(repository);
                allowing(validator).validate(place);
                will(returnValue(false));
                never(repository);
                oneOf(presenter).displaySaveFail();
            }
        });
        PlaceSaver placeSaver = new PlaceSaver(repository, validator, presenter);
        placeSaver.save(place);
    }

    @Test
    public void testSavePassValidatorButSaveFail() throws Exception {
        context.checking(new Expectations() {
            {
                allowing(PlaceSaverTest.this.validator).setPlaceRepository(repository);
                allowing(validator).validate(place);
                will(returnValue(false));
                allowing(repository).save(place);
                will(returnValue(false));
                oneOf(presenter).displaySaveFail();
            }
        });
        PlaceSaver placeSaver = new PlaceSaver(repository, validator, presenter);
        placeSaver.save(place);
    }

    @Test
    public void testUpdateNotPassValidator() throws Exception {
        context.checking(new Expectations() {
            {
                allowing(PlaceSaverTest.this.validator).setPlaceRepository(repository);
                allowing(validator).validate(place);
                will(returnValue(false));
                never(repository);
                oneOf(presenter).displayUpdateFail();
            }
        });
        PlaceSaver placeSaver = new PlaceSaver(repository, validator, presenter);
        placeSaver.update(place);
    }

    @Test
    public void testUpdatePassValidatorButUpdateFail() throws Exception {
        context.checking(new Expectations() {
            {
                allowing(PlaceSaverTest.this.validator).setPlaceRepository(repository);
                allowing(validator).validate(place);
                will(returnValue(true));
                allowing(repository).update(place);
                will(returnValue(false));
                oneOf(presenter).displayUpdateFail();
            }
        });
        PlaceSaver placeSaver = new PlaceSaver(repository, validator, presenter);
        placeSaver.update(place);
    }
}