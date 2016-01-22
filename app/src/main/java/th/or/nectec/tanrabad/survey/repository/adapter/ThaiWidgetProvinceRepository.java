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

import th.or.nectec.domain.thai.address.ProvinceRepository;
import th.or.nectec.entity.thai.Province;
import th.or.nectec.entity.thai.Region;

import java.util.ArrayList;
import java.util.List;

public class ThaiWidgetProvinceRepository implements ProvinceRepository {

    th.or.nectec.tanrabad.domain.address.ProvinceRepository dbProvinceRepository;

    public ThaiWidgetProvinceRepository(th.or.nectec.tanrabad.domain.address.ProvinceRepository dbProvinceRepository) {
        this.dbProvinceRepository = dbProvinceRepository;
    }

    @Override
    public List<Province> findByRegion(Region region) {
        return null;
    }

    @Override
    public Province findByProvinceCode(String provinceCode) {
        th.or.nectec.tanrabad.entity.Province province = dbProvinceRepository.findByCode(provinceCode);

        return province == null ? null : new Province(province.getCode(), province.getName(), Region.CENTER);
    }

    @Override
    public List<Province> find() {
        List<th.or.nectec.tanrabad.entity.Province> provinces = dbProvinceRepository.find();
        List<Province> thaiWidgetProvicnes = new ArrayList<>();
        for (th.or.nectec.tanrabad.entity.Province province : provinces) {
            Province widgetProvince = new Province(province.getCode(), province.getName(), Region.CENTER);
            thaiWidgetProvicnes.add(widgetProvince);
        }
        return thaiWidgetProvicnes;
    }


}
