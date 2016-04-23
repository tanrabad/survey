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

package org.tanrabad.survey.validator;

import android.text.TextUtils;
import org.tanrabad.survey.domain.building.BuildingRepository;
import org.tanrabad.survey.domain.building.BuildingValidator;
import org.tanrabad.survey.entity.Building;
import org.tanrabad.survey.entity.lookup.PlaceType;
import org.tanrabad.survey.R;

import java.util.List;

public class UpdateBuildingValidator implements BuildingValidator {
    private BuildingRepository buildingRepository;

    @Override
    public boolean validate(Building building) {

        if (TextUtils.isEmpty(building.getName())) {

            throw new ValidatorException(building.getPlace().getType() == PlaceType.VILLAGE_COMMUNITY
                    ? R.string.please_define_house_no : R.string.please_define_building_name);
        }

        if (building.getLocation() == null) {
            throw new ValidatorException(R.string.please_define_building_location);
        }

        List<Building> buildingInPlace = buildingRepository.findByPlaceUuid(building.getPlace().getId());
        if (buildingInPlace != null) {
            for (Building eachBuilding : buildingInPlace) {
                if (eachBuilding.getName().equals(building.getName())
                        && !eachBuilding.getId().equals(building.getId())) {
                    throw new ValidatorException(R.string.cant_save_same_building_name);
                }
            }
        }
        return true;
    }

    @Override
    public void setBuildingRepository(BuildingRepository buildingRepository) {
        this.buildingRepository = buildingRepository;
    }
}
