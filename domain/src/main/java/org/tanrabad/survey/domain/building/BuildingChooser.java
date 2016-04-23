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

import java.util.List;
import java.util.UUID;

import th.or.nectec.tanrabad.entity.Building;

class BuildingChooser {
    private final BuildingRepository buildingRepository;
    private final BuildingListPresenter presenter;

    public BuildingChooser(BuildingRepository buildingRepository, BuildingListPresenter presenter) {

        this.buildingRepository = buildingRepository;
        this.presenter = presenter;
    }

    public void showBuildingOf(UUID placeUuid) {
        if (placeUuid == null) {
            presenter.alertPlaceNotFound();
            return;
        }

        List<Building> buildingInPlace = buildingRepository.findByPlaceUuid(placeUuid);
        if (buildingInPlace != null)
            presenter.displayBuildingsList(buildingInPlace);
        else
            presenter.alertBuildingsNotFound();
    }
}
