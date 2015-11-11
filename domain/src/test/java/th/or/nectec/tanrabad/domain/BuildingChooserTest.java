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
import java.util.List;
import java.util.UUID;

public class BuildingChooserTest {
    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    private UUID placeUuid = UUID.randomUUID();
    private BuildingRepository buildingRepository;
    private BuildingPresenter presenter;

    @Before
    public void setup() {
        buildingRepository = context.mock(BuildingRepository.class);
        presenter = context.mock(BuildingPresenter.class);
    }

    @Test
    public void foundBuilding() {
        context.checking(new Expectations() {
            {
                List<Building> buildings = new ArrayList<>();
                buildings.add(Building.withName("214/43"));

                allowing(buildingRepository).findBuildingInPlace(placeUuid);
                will(returnValue(buildings));

                oneOf(presenter).displayBuildingList(buildings);
            }
        });
        BuildingChooser buildingChooser = new BuildingChooser(buildingRepository, presenter);
        buildingChooser.showBuildingOf(placeUuid);
    }

    @Test
    public void notFoundBuilding() {
        context.checking(new Expectations() {
            {
                allowing(buildingRepository).findBuildingInPlace(placeUuid);
                will(returnValue(null));

                oneOf(presenter).displayNotFoundBuilding();
            }
        });
        BuildingChooser buildingChooser = new BuildingChooser(buildingRepository, presenter);
        buildingChooser.showBuildingOf(placeUuid);
    }

    @Test
    public void emptyPlaceUuid() {
        context.checking(new Expectations() {
            {
                never(buildingRepository);

                oneOf(presenter).displayPleaseSpecityPlace();
            }
        });
        BuildingChooser buildingChooser = new BuildingChooser(buildingRepository, presenter);
        buildingChooser.showBuildingOf(null);
    }


}
