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

package th.or.nectec.tanrabad.domain.building;

import org.jmock.Expectations;
import org.jmock.auto.Mock;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

import java.util.UUID;

import th.or.nectec.tanrabad.entity.Building;


public class BuildingSaverTest {
    private final Building building = new Building(UUID.randomUUID(), "MockBuilding");
    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();
    @Mock
    private BuildingValidator validator;
    @Mock
    private BuildingSavePresenter presenter;
    @Mock
    private BuildingRepository repository;

    @Test
    public void testSaveSuccess() throws Exception {
        context.checking(new Expectations() {
            {
                oneOf(validator).setBuildingRepository(with(repository));
                allowing(BuildingSaverTest.this.validator).validate(with(building));
                will(returnValue(true));
                oneOf(BuildingSaverTest.this.repository).save(with(building));
                will(returnValue(true));
                oneOf(BuildingSaverTest.this.presenter).displaySaveSuccess();
            }
        });
        BuildingSaver buildingSaver = new BuildingSaver(repository, validator, presenter);
        buildingSaver.save(building);
    }

    @Test
    public void testSaveNotPassValidator() throws Exception {
        building.setName(null);
        context.checking(new Expectations() {
            {
                oneOf(validator).setBuildingRepository(with(repository));
                allowing(validator).validate(building);
                will(returnValue(false));
                never(repository);
                oneOf(presenter).displaySaveFail();
            }
        });
        BuildingSaver buildingSaver = new BuildingSaver(repository, validator, presenter);
        buildingSaver.save(building);
    }

    @Test
    public void testSavePassValidatorButSaveNotSuccess() throws Exception {
        context.checking(new Expectations() {
            {
                oneOf(validator).setBuildingRepository(with(repository));
                allowing(validator).validate(building);
                will(returnValue(true));
                allowing(repository).save(building);
                will(returnValue(false));
                oneOf(presenter).displaySaveFail();
            }
        });
        BuildingSaver buildingSaverAfter = new BuildingSaver(repository, validator, presenter);
        buildingSaverAfter.save(building);
    }

    @Test
    public void testUpdateSuccess() throws Exception {
        context.checking(new Expectations() {
            {
                oneOf(validator).setBuildingRepository(with(repository));
                allowing(validator).validate(with(building));
                will(returnValue(true));
                oneOf(BuildingSaverTest.this.repository).update(with(building));
                will(returnValue(true));
                oneOf(BuildingSaverTest.this.presenter).displayUpdateSuccess();
            }
        });
        BuildingSaver buildingSaver = new BuildingSaver(repository, validator, presenter);
        buildingSaver.update(building);
    }

    @Test
    public void testUpdateNotPassValidator() throws Exception {
        context.checking(new Expectations() {
            {
                oneOf(validator).setBuildingRepository(with(repository));
                allowing(validator).validate(building);
                will(returnValue(false));
                never(repository);
                oneOf(presenter).displayUpdateFail();
            }
        });
        BuildingSaver buildingSaver = new BuildingSaver(repository, validator, presenter);
        buildingSaver.update(building);
    }

    @Test
    public void testUpdatePassValidatorButUpdateNotSuccess() throws Exception {
        building.setName(null);
        context.checking(new Expectations() {
            {
                oneOf(validator).setBuildingRepository(with(repository));
                allowing(validator).validate(building);
                will(returnValue(true));
                allowing(repository).update(building);
                will(returnValue(false));
                oneOf(presenter).displayUpdateFail();
            }
        });
        BuildingSaver buildingSaver = new BuildingSaver(repository, validator, presenter);
        buildingSaver.update(building);
    }

}
