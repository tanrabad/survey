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

package th.or.nectec.tanrabad.domain.building;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import th.or.nectec.tanrabad.entity.Building;
import th.or.nectec.tanrabad.entity.Location;
import th.or.nectec.tanrabad.entity.Place;

import java.util.UUID;


public class BuildingSaverTest {
    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    private UUID buildingUUID;
    private String buildingName;
    private Place place;
    private Location buildingLocation;

    private BuildingValidator buildingValidator;
    private BuildingSavePresenter buildingSavePresenter;
    private BuildingRepository buildingRepository;
    private Building building;

    @Before
    public void setUp() {
        buildingSavePresenter = context.mock(BuildingSavePresenter.class);
        buildingValidator = context.mock(BuildingValidator.class);
        buildingRepository = context.mock(BuildingRepository.class);

        buildingUUID = UUID.nameUUIDFromBytes("3xyz".getBytes());
        buildingName = "123";
        place = Place.withName("Village A");
        buildingLocation = new Location(51.500152, -0.126236);

        building = new Building(buildingUUID, buildingName);
        building.setLocation(buildingLocation);
    }

    @Test
    public void testHappyPath() throws Exception {

        context.checking(new Expectations() {
            {
                oneOf(buildingValidator).setBuildingRepository(with(buildingRepository));
                allowing(BuildingSaverTest.this.buildingValidator).validate(with(building));
                will(returnValue(true));
                oneOf(BuildingSaverTest.this.buildingRepository).save(with(building));
                will(returnValue(true));
                oneOf(BuildingSaverTest.this.buildingSavePresenter).displaySaveSuccess();
            }
        });
        BuildingSaver buildingSaver = new BuildingSaver(buildingRepository, buildingValidator, buildingSavePresenter);
        buildingSaver.save(building);
    }

    @Test
    public void testSadPath() throws Exception {

        building.setName(null);
        context.checking(new Expectations() {
            {
                oneOf(buildingValidator).setBuildingRepository(with(buildingRepository));
                allowing(buildingValidator).validate(building);
                will(returnValue(false));
                never(buildingRepository);
                oneOf(buildingSavePresenter).displaySaveFail();
            }
        });
        BuildingSaver buildingSaver = new BuildingSaver(buildingRepository, buildingValidator, buildingSavePresenter);
        buildingSaver.save(building);
    }

    @Test
    public void testSaveDuplicateBuilding() throws Exception {
        final Building secondBuilding = Building.withName("123");
        secondBuilding.setPlace(place);
        context.checking(new Expectations() {
            {
                oneOf(buildingValidator).setBuildingRepository(with(buildingRepository));
                allowing(buildingValidator).validate(secondBuilding);
                will(returnValue(false));
                never(buildingRepository);
                oneOf(buildingSavePresenter).displaySaveFail();
            }
        });

        BuildingSaver buildingSaverAfter = new BuildingSaver(buildingRepository, buildingValidator, buildingSavePresenter);
        buildingSaverAfter.save(secondBuilding);
    }

}
