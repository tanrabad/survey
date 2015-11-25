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

package th.or.nectec.tanrabad.survey.validator;

import android.text.TextUtils;
import th.or.nectec.tanrabad.domain.building.BuildingRepository;
import th.or.nectec.tanrabad.domain.building.BuildingValidator;
import th.or.nectec.tanrabad.entity.Building;
import th.or.nectec.tanrabad.entity.Place;
import th.or.nectec.tanrabad.survey.R;
import th.or.nectec.tanrabad.survey.utils.alert.Alert;

import java.util.List;

public class SaveBuildingValidator implements BuildingValidator {
    private BuildingRepository buildingRepository;

    @Override
    public boolean validate(Building building) {

        if (TextUtils.isEmpty(building.getName())) {
            if (building.getPlace().getType() == Place.TYPE_VILLAGE_COMMUNITY)
                Alert.highLevel().show(R.string.please_define_house_no);
            else
                Alert.highLevel().show(R.string.please_define_building_name);

            return false;
        }

        if (building.getLocation() == null) {
            Alert.highLevel().show(R.string.please_define_building_location);
            return false;
        }

        List<Building> buildingInPlace = buildingRepository.findBuildingInPlace(building.getPlace().getId());
        if(buildingInPlace!=null){
            for(Building eachBuilding : buildingInPlace){
                if(eachBuilding.getName().equals(building.getName())){
                    Alert.highLevel().show(R.string.cant_save_same_building_name);
                    return false;
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
