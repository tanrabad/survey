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

package th.or.nectec.tanrabad.survey.presenter.job;

import th.or.nectec.tanrabad.survey.presenter.job.service.RestTambonService;
import th.or.nectec.tanrabad.survey.presenter.job.service.Tambon;
import th.or.nectec.tanrabad.survey.presenter.job.service.TambonService;
import th.or.nectec.tanrabad.survey.repository.AddressRepository;
import th.or.nectec.tanrabad.survey.repository.AddressRepositoryImpl;

import java.util.List;

public class TambonUpdateJob implements Job {

    public static final int ID = 520001;
    private final TambonService service = new RestTambonService();

    @Override
    public int id() {
        return ID;
    }

    @Override
    public void execute() {
        List<Tambon> updateTambon = service.getUpdate();
        AddressRepository addressRespository = AddressRepositoryImpl.getInstance();
        addressRespository.updateTambon(updateTambon);
    }
}
