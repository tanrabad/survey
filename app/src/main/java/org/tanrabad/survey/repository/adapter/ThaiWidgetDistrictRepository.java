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

package org.tanrabad.survey.repository.adapter;


import org.tanrabad.survey.repository.persistence.DbDistrictRepository;

import java.util.ArrayList;
import java.util.List;

import nectec.thai.address.AddressRepository;
import nectec.thai.address.District;

public class ThaiWidgetDistrictRepository implements AddressRepository<District> {

    @Override
    public List<District> find() {
        return null;
    }

    @Override
    public List<District> findByParentCode(String s) {
        List<org.tanrabad.survey.entity.lookup.District> districts = DbDistrictRepository
                .getInstance().findByProvinceCode(s);
        List<District> thaiWidgetDistrict = new ArrayList<>();
        for (org.tanrabad.survey.entity.lookup.District district : districts) {
            District widgetDistrict = new District(district.getCode(), district.getName());
            thaiWidgetDistrict.add(widgetDistrict);
        }
        return thaiWidgetDistrict;
    }

    @Override
    public District findByCode(String s) {
        return null;
    }
}
