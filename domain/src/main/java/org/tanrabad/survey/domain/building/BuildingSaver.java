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

import org.tanrabad.survey.entity.Building;

public class BuildingSaver {
    private final BuildingSavePresenter buildingSavePresenter;
    private final BuildingRepository buildingRepository;
    private final BuildingValidator buildingValidator;

    public BuildingSaver(BuildingRepository buildingRepository,
                         BuildingValidator buildingValidator,
                         BuildingSavePresenter buildingSavePresenter) {
        this.buildingSavePresenter = buildingSavePresenter;
        this.buildingValidator = buildingValidator;
        this.buildingRepository = buildingRepository;
        buildingValidator.setBuildingRepository(buildingRepository);
    }

    public void save(Building building) {
        if (buildingValidator.validate(building) && buildingRepository.save(building)) {
            buildingSavePresenter.displaySaveSuccess();
        } else {
            buildingSavePresenter.displaySaveFail();
        }
    }

    public void update(Building building) {
        if (buildingValidator.validate(building) && buildingRepository.update(building)) {
            buildingSavePresenter.displayUpdateSuccess();
        } else {
            buildingSavePresenter.displayUpdateFail();
        }
    }
}
