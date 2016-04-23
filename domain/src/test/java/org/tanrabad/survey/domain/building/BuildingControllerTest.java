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

package org.tanrabad.survey.domain.building;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import th.or.nectec.tanrabad.entity.Building;

import java.util.UUID;

public class BuildingControllerTest {

    private static final String BUILDING_NAME = "123";
    private final Building building = Building.withName(BUILDING_NAME);
    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();
    private BuildingRepository buildingRepository;
    private BuildingPresenter buildingPresenter;
    private UUID buildingUuid;

    @Before
    public void setUp() {
        buildingUuid = UUID.nameUUIDFromBytes("3xyz".getBytes());

        buildingRepository = context.mock(BuildingRepository.class);
        buildingPresenter = context.mock(BuildingPresenter.class);

    }

    @Test
    public void testFoundBuilding() throws Exception {

        context.checking(new Expectations() {
            {
                allowing(buildingRepository).findByUuid(buildingUuid);
                will(returnValue(building));
                oneOf(buildingPresenter).displayBuilding(building);
            }
        });
        BuildingController buildingController = new BuildingController(buildingRepository, buildingPresenter);
        buildingController.showBuilding(buildingUuid);
    }


    @Test
    public void testNotFoundBuilding() throws Exception {

        context.checking(new Expectations() {
            {
                allowing(buildingRepository).findByUuid(buildingUuid);
                will(returnValue(null));
                oneOf(buildingPresenter).alertBuildingNotFound();
            }
        });
        BuildingController buildingController = new BuildingController(buildingRepository, buildingPresenter);
        buildingController.showBuilding(buildingUuid);
    }

}
