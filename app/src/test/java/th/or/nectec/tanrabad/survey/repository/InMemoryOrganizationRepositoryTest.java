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

import org.junit.BeforeClass;
import org.junit.Test;

import th.or.nectec.tanrabad.domain.organization.OrganizationRepositoryException;
import th.or.nectec.tanrabad.entity.Organization;

import static org.junit.Assert.assertEquals;


public class InMemoryOrganizationRepositoryTest {

    public static final int ORGANIZATION_ID = 1;
    private static InMemoryOrganizationRepository repository = InMemoryOrganizationRepository.getInstance();

    @BeforeClass
    public static void setUp() throws Exception {
        repository.save(stubOrganization());
    }

    private static Organization stubOrganization() {
        Organization organization = new Organization(ORGANIZATION_ID, "กรมควบคุมโรค");
        organization.setAreaLevelId(Organization.AREA_LEVEL_ID_AMPHUR);
        organization.setHealthRegionCode("dpc-13");
        return organization;
    }

    @Test(expected = OrganizationRepositoryException.class)
    public void testSaveExistPlaceMustThrowException() throws Exception {
        repository.save(stubOrganization());
    }

    @Test
    public void testEveryCallGetInstanceMustGotSameInstance() throws Exception {
        assertEquals(repository, InMemoryOrganizationRepository.getInstance());
    }

    @Test
    public void testSearchByOrganizationId() throws Exception {
        assertEquals(stubOrganization(), repository.findById(ORGANIZATION_ID));
    }

    @Test
    public void testUpdate() throws Exception {
        Organization organization = new Organization(ORGANIZATION_ID, "รพ.สต.ทดสอบ");
        repository.update(organization);
        assertEquals(organization, repository.findById(ORGANIZATION_ID));
    }

    @Test(expected = OrganizationRepositoryException.class)
    public void testUpdateNotExistPlaceMustThrowException() throws Exception {
        repository.update(new Organization(2, "รพ.สต.ทดสอบ"));
    }
}
