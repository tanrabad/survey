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

package th.or.nectec.tanrabad.survey.job;

import th.or.nectec.tanrabad.survey.repository.InMemoryContainerTypeRepository;
import th.or.nectec.tanrabad.survey.repository.InMemoryDistrictRepository;
import th.or.nectec.tanrabad.survey.repository.InMemoryProvinceRepository;
import th.or.nectec.tanrabad.survey.repository.InMemorySubdistrictRepository;

public class InMemoryInitializeJob implements Job {

    public static final int ID = 100120;

    @Override
    public int id() {
        return ID;
    }

    @Override
    public void execute() {
        InMemoryContainerTypeRepository.getInstance();
        InMemorySubdistrictRepository.getInstance();
        InMemoryDistrictRepository.getInstance();
        InMemoryProvinceRepository.getInstance();
    }
}
