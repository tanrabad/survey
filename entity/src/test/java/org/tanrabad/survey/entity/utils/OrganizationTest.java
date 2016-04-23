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

package org.tanrabad.survey.entity.utils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.tanrabad.survey.entity.Organization;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(JUnit4.class)
public class OrganizationTest {

    private static final int ID = 1;
    private static final String สำนักป้องกันควบคุมโรคที่_4 = "สำนักป้องกันควบคุมโรคที่ 4";

    private Organization organization;

    @Before
    public void setUp() throws Exception {
        organization = new Organization(ID, สำนักป้องกันควบคุมโรคที่_4);
        organization.setAreaLevelId(Organization.AREA_LEVEL_ID_PROVINCE);
    }

    @Test
    public void testGetOrganizationId() {
        assertEquals(ID, organization.getOrganizationId());
    }

    @Test
    public void testGetName() {
        assertEquals(สำนักป้องกันควบคุมโรคที่_4, organization.getName());
    }

    @Test
    public void testSetThenGetAreaLevelId() {
        assertEquals(Organization.AREA_LEVEL_ID_PROVINCE, organization.getAreaLevelId());
    }

    @Test
    public void organizationWithDifferentIdMustNotEquals() {
        Organization anotherOrg = new Organization(3, สำนักป้องกันควบคุมโรคที่_4);

        assertNotEquals(organization, anotherOrg);
    }

    @Test
    public void organizationWithDifferentNameMustNotEquals() {
        Organization anotherOrg = new Organization(ID, "รพ.สต.บางแม่นาง");

        assertNotEquals(organization, anotherOrg);
    }

    @Test
    public void organizationWithDifferentAreaLevelIdMustNotEquals() {
        Organization anotherOrg = new Organization(ID, สำนักป้องกันควบคุมโรคที่_4);
        anotherOrg.setAreaLevelId(Organization.AREA_LEVEL_ID_TUMBON);

        assertNotEquals(organization, anotherOrg);
    }

    @Test
    public void organizationWithTheSameIdAndNameAndAreaLevelIdMustEquals() {
        Organization sameOrg = new Organization(ID, สำนักป้องกันควบคุมโรคที่_4);
        sameOrg.setAreaLevelId(Organization.AREA_LEVEL_ID_PROVINCE);

        assertEquals(organization, sameOrg);
    }
}
