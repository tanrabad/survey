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

package org.tanrabad.survey.repository;

import org.tanrabad.survey.TanrabadApp;
import org.tanrabad.survey.repository.persistence.DbOrganizationRepository;
import org.tanrabad.survey.domain.organization.OrganizationRepository;
import th.or.nectec.tanrabad.entity.Organization;

import java.util.List;

public class BrokerOrganizationRepository implements OrganizationRepository {

    private static BrokerOrganizationRepository instance;
    private OrganizationRepository cache;
    private OrganizationRepository database;

    public BrokerOrganizationRepository(OrganizationRepository cache, OrganizationRepository database) {
        this.cache = cache;
        this.database = database;
    }

    public static BrokerOrganizationRepository getInstance() {
        if (instance == null)
            instance = new BrokerOrganizationRepository(InMemoryOrganizationRepository.getInstance(),
                    new DbOrganizationRepository(TanrabadApp.getInstance()));
        return instance;
    }

    @Override
    public Organization findById(int organizationId) {
        Organization organization = cache.findById(organizationId);
        if (organization == null) {
            organization = database.findById(organizationId);
            if (organization != null) cache.save(organization);
        }
        return organization;
    }

    @Override
    public boolean save(Organization organization) {
        boolean success = database.save(organization);
        if (success) {
            cache.save(organization);
        }
        return success;
    }

    @Override
    public boolean update(Organization organization) {
        boolean success = database.update(organization);
        if (success) {
            cache.update(organization);
        }
        return success;
    }

    @Override
    public boolean delete(Organization organization) {
        boolean success = database.delete(organization);
        if (success) {
            cache.delete(organization);
        }
        return success;
    }

    @Override
    public void updateOrInsert(List<Organization> organizations) {

    }
}
