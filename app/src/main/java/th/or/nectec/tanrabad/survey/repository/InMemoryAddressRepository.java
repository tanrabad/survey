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

package th.or.nectec.tanrabad.survey.repository;

import th.or.nectec.tanrabad.domain.address.AddressRepository;
import th.or.nectec.tanrabad.domain.address.DistrictRepository;
import th.or.nectec.tanrabad.domain.address.ProvinceRepository;
import th.or.nectec.tanrabad.domain.address.SubdistrictRepository;
import th.or.nectec.tanrabad.entity.utils.Address;

public class InMemoryAddressRepository implements AddressRepository {

    private static InMemoryAddressRepository instance;
    private SubdistrictRepository subdistrictRepository;
    private DistrictRepository districtRepository;
    private ProvinceRepository provinceRepository;

    private InMemoryAddressRepository(SubdistrictRepository subdistrictRepository, DistrictRepository districtRepository, ProvinceRepository provinceRepository) {
        this.subdistrictRepository = subdistrictRepository;
        this.districtRepository = districtRepository;
        this.provinceRepository = provinceRepository;
    }

    public static InMemoryAddressRepository getInstance() {
        return getInstance(InMemorySubdistrictRepository.getInstance(),
                InMemoryDistrictRepository.getInstance(),
                InMemoryProvinceRepository.getInstance());
    }

    public static InMemoryAddressRepository getInstance(SubdistrictRepository subdistrictRepository,
                                                        DistrictRepository districtRepository,
                                                        ProvinceRepository provinceRepository) {
        if (instance == null) {
            instance = new InMemoryAddressRepository(subdistrictRepository, districtRepository, provinceRepository);
        }
        return instance;
    }

    @Override
    public Address findBySubdistrictCode(String subdistrictCode) {
        Address address = new Address();
        address.setAddressCode(subdistrictCode);
        address.setSubdistrict(subdistrictRepository.findByCode(subdistrictCode).getName());
        address.setDistrict(districtRepository.findByCode(subdistrictCode.substring(0, 4)).getName());
        address.setProvince(provinceRepository.findByCode(subdistrictCode.substring(0, 2)).getName());
        return address;
    }
}
