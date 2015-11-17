/*
 * Copyright (c) 2015  NECTEC
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
public class UserTest {

    public static final String AUSTIN_USERNAME = "austin1023";
    public static final String AUSTIN_FIRSTNAME = "Austin";
    public static final String AUSTIN_LASTNAME = "Kydd";
    public static final String AUSTIN_EMAIL = "austin.k@gmail.com";
    public static final int AUSTIN_ORGANIZATION_ID = 201;
    private final User austin1 = new User(AUSTIN_USERNAME);
    private final User austin2 = new User(AUSTIN_USERNAME);

    @Test
    public void testSetThenGetFirstname() throws Exception {
        austin1.setFirstname(AUSTIN_FIRSTNAME);
        assertEquals(AUSTIN_FIRSTNAME, austin1.getFirstname());
    }

    @Test
    public void testSetThenGetLastname() throws Exception {
        austin1.setLastname(AUSTIN_LASTNAME);
        assertEquals(AUSTIN_LASTNAME, austin1.getLastname());
    }

    @Test
    public void setThenGetEmail() throws Exception {
        austin1.setEmail(AUSTIN_EMAIL);
        assertEquals(AUSTIN_EMAIL, austin1.getEmail());
    }

    @Test
    public void testSetThenGetOrganizationId() throws Exception {
        austin1.setOrganizationId(AUSTIN_ORGANIZATION_ID);
        assertEquals(AUSTIN_ORGANIZATION_ID, austin1.getOrganizationId());
    }

    @Test
    public void getUsername() throws Exception {
        assertEquals(AUSTIN_USERNAME, austin1.getUsername());
    }

    @Test
    public void testFromUsername() throws Exception {
        User austin = User.fromUsername(AUSTIN_USERNAME);
        assertEquals(AUSTIN_USERNAME, austin.getUsername());
    }

    @Test
    public void userWithDifferentFirstnameMustNotEqual() throws Exception {
        austin1.setFirstname(AUSTIN_FIRSTNAME);
        austin2.setFirstname("Austinno");
        assertNotEquals(austin1, austin2);
    }

    @Test
    public void userWithDifferentLastnameMustNotEqual() throws Exception {
        austin1.setLastname(AUSTIN_LASTNAME);
        austin2.setLastname("Butler");
        assertNotEquals(austin1, austin2);
    }

    @Test
    public void userWithDifferentOrganizationMustNotEqual() throws Exception {
        austin1.setOrganizationId(AUSTIN_ORGANIZATION_ID);
        austin2.setOrganizationId(408);
        assertNotEquals(austin1, austin2);
    }

    @Test
    public void userWithTheSameUsernameFirstnameLastnameOrganizationIdMustEqual() throws Exception {
        austin1.setFirstname(AUSTIN_FIRSTNAME);
        austin1.setLastname(AUSTIN_LASTNAME);
        austin1.setOrganizationId(AUSTIN_ORGANIZATION_ID);
        austin2.setFirstname(austin1.getFirstname());
        austin2.setLastname(austin1.getLastname());
        austin2.setOrganizationId(austin1.getOrganizationId());
        assertEquals(austin1, austin2);
    }
}