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


import org.tanrabad.survey.repository.persistence.DbProvinceRepository;

import java.util.ArrayList;
import java.util.List;

import nectec.thai.address.AddressRepository;
import nectec.thai.address.Province;
import nectec.thai.address.Region;

public class ThaiWidgetProvinceRepository implements AddressRepository<Province> {

    @Override
    public List<Province> find() {
        List<org.tanrabad.survey.entity.lookup.Province> provinces = DbProvinceRepository.getInstance().find();
        List<Province> thaiWidgetProvinces = new ArrayList<>();
        for (org.tanrabad.survey.entity.lookup.Province province : provinces) {
            Province widgetProvince = new Province(province.getCode(), province.getName(), Region.CENTER);
            thaiWidgetProvinces.add(widgetProvince);
        }
        return thaiWidgetProvinces;
    }

    @Override
    public List<Province> findByParentCode(String s) {
        return null;
    }

    @Override
    public Province findByCode(String s) {
        return null;
    }
}
