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

package th.or.nectec.tanrabad.entity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(JUnit4.class)
public class OrganizationTest {

    private final int organizationId1 = 1;
    private final String name1 = "สำนักป้องกันควบคุมโรคที่ 4";
    private final Organization organization1 = new Organization(organizationId1, name1);
    private final Organization organization2 = new Organization(organizationId1, name1);

    @Test
    public void testSetThenGetOrganizationId() {
        organization1.setOrganizationId(2);
        assertEquals(2, organization1.getOrganizationId());
    }

    @Test
    public void testSetThenGetName() {
        organization1.setName("รพ.สต.บางแม่นาง");
        assertEquals("รพ.สต.บางแม่นาง", organization1.getName());
    }

    @Test
    public void testSetThenAreaLevelId() {
        organization1.setAreaLevelId(Organization.AREA_LEVEL_ID_PROVINCE);
        assertEquals(Organization.AREA_LEVEL_ID_PROVINCE, organization1.getAreaLevelId());
    }

    @Test
    public void organizationWithDifferentIdMustNotEquals() {
        Organization organization2 = new Organization(3, name1);
        assertNotEquals(organization1, organization2);
    }

    @Test
    public void organizationWithDifferentNameMustNotEquals() {
        Organization organization2 = new Organization(organizationId1, "รพ.สต.บางแม่นาง");
        assertNotEquals(organization1, organization2);
    }

    @Test
    public void organizationWithDifferentAreaLevelIdMustNotEquals() {
        organization1.setAreaLevelId(Organization.AREA_LEVEL_ID_AMPHUR);
        organization2.setAreaLevelId(Organization.AREA_LEVEL_ID_TUMBON);
        assertNotEquals(organization1, organization2);
    }

    @Test
    public void organizationWithTheSameIdAndNameAndAreaLevelIdMustEquals() {
        organization1.setAreaLevelId(Organization.AREA_LEVEL_ID_PROVINCE);
        organization2.setAreaLevelId(Organization.AREA_LEVEL_ID_PROVINCE);
        assertEquals(organization1, organization2);
    }
}