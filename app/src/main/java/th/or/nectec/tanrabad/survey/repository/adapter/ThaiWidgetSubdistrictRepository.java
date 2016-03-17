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

package th.or.nectec.tanrabad.survey.repository.adapter;


import java.util.ArrayList;
import java.util.List;

import th.or.nectec.tanrabad.survey.repository.persistence.DbSubdistrictRepository;
import th.or.nectec.thai.address.AddressRepository;
import th.or.nectec.thai.address.SubDistrict;


public class ThaiWidgetSubdistrictRepository implements AddressRepository<SubDistrict> {

    @Override
    public List<SubDistrict> find() {
        return null;
    }

    @Override
    public List<SubDistrict> findByParentCode(String s) {
        List<th.or.nectec.tanrabad.entity.lookup.Subdistrict> subdistricts = DbSubdistrictRepository
                .getInstance().findByDistrictCode(s);
        List<SubDistrict> thaiWidgetSubdistrict = new ArrayList<>();
        for (th.or.nectec.tanrabad.entity.lookup.Subdistrict subdistrict : subdistricts) {
            SubDistrict widgetSubdistrict = new SubDistrict(subdistrict.getCode(), subdistrict.getName(), "00000");
            thaiWidgetSubdistrict.add(widgetSubdistrict);
        }
        return thaiWidgetSubdistrict;
    }

    @Override
    public SubDistrict findByCode(String s) {
        return null;
    }
}
