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

import th.or.nectec.tanrabad.domain.address.SubdistrictRepository;
import th.or.nectec.tanrabad.entity.Subdistrict;
import th.or.nectec.tanrabad.survey.service.RestService;
import th.or.nectec.tanrabad.survey.service.TambonRestService;

import java.util.ArrayList;

public class SubdistrictUpdateJob implements Job {

    public static final int ID = 100001;

    private final SubdistrictRepository subdistrictRepository;

    public SubdistrictUpdateJob(SubdistrictRepository subdistrictRepository) {
        this.subdistrictRepository = subdistrictRepository;
    }

    @Override
    public int id() {
        return ID;
    }

    @Override
    public void execute() throws JobException {
        RestService<Subdistrict> service = new TambonRestService();
        ArrayList<Subdistrict> subdistrictArrayList = new ArrayList<>();

        do {
            subdistrictArrayList.addAll(service.getUpdate());
        } while (service.hasNextRequest());

        subdistrictRepository.updateOrInsert(subdistrictArrayList);
    }
}
