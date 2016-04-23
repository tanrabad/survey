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

import th.or.nectec.tanrabad.domain.organization.OrganizationRepository;
import th.or.nectec.tanrabad.domain.organization.OrganizationRepositoryException;
import th.or.nectec.tanrabad.entity.Organization;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryOrganizationRepository implements OrganizationRepository {
    private static InMemoryOrganizationRepository instance;

    private Map<Integer, Organization> organizationMapping = new HashMap<>();

    static InMemoryOrganizationRepository getInstance() {
        if (instance == null) {
            instance = new InMemoryOrganizationRepository();
        }
        return instance;
    }

    @Override
    public Organization findById(int organizationId) {
        if (organizationMapping.containsKey(organizationId)) {
            return organizationMapping.get(organizationId);
        } else {
            return null;
        }
    }

    @Override
    public boolean save(Organization organization) {
        if (organizationMapping.containsKey(organization.getOrganizationId())) {
            throw new OrganizationRepositoryException();
        }

        organizationMapping.put(organization.getOrganizationId(), organization);
        return true;
    }

    @Override
    public boolean update(Organization organization) {
        if (!organizationMapping.containsKey(organization.getOrganizationId())) {
            throw new OrganizationRepositoryException();
        }
        organizationMapping.put(organization.getOrganizationId(), organization);
        return true;
    }

    @Override
    public boolean delete(Organization organization) {
        if (!organizationMapping.containsKey(organization.getOrganizationId())) {
            throw new OrganizationRepositoryException();
        }
        organizationMapping.remove(organization.getOrganizationId());
        return true;
    }

    @Override
    public void updateOrInsert(List<Organization> organizations) {
    }
}
