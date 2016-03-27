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

package th.or.nectec.tanrabad.entity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(JUnit4.class)
public class UserTest {

    private static final String AUSTIN_USERNAME = "austin1023";
    private static final String AUSTIN_FIRSTNAME = "Austin";
    private static final String AUSTIN_LASTNAME = "Kydd";
    private static final String AUSTIN_EMAIL = "austin.k@gmail.com";
    private static final int AUSTIN_ORGANIZATION_ID = 201;
    private final User austin = new User(AUSTIN_USERNAME);
    private final User austin2 = new User(AUSTIN_USERNAME);

    @Test
    public void testSetThenGetFirstname() {
        austin.setFirstname(AUSTIN_FIRSTNAME);

        assertEquals(AUSTIN_FIRSTNAME, austin.getFirstname());
    }

    @Test
    public void testSetThenGetLastname() {
        austin.setLastname(AUSTIN_LASTNAME);

        assertEquals(AUSTIN_LASTNAME, austin.getLastname());
    }

    @Test
    public void setThenGetEmail() {
        austin.setEmail(AUSTIN_EMAIL);

        assertEquals(AUSTIN_EMAIL, austin.getEmail());
    }

    @Test
    public void testSetThenGetOrganizationId() {
        austin.setOrganizationId(AUSTIN_ORGANIZATION_ID);

        assertEquals(AUSTIN_ORGANIZATION_ID, austin.getOrganizationId());
    }

    @Test
    public void getUsername() {
        assertEquals(AUSTIN_USERNAME, austin.getUsername());
    }

    @Test
    public void testFromUsername() {
        User austin = User.fromUsername(AUSTIN_USERNAME);

        assertEquals(AUSTIN_USERNAME, austin.getUsername());
    }

    @Test
    public void userWithDifferentFirstnameMustNotEqual() {
        austin.setFirstname(AUSTIN_FIRSTNAME);
        austin2.setFirstname("Austinno");

        assertNotEquals(austin, austin2);
    }

    @Test
    public void userWithDifferentLastnameMustNotEqual() {
        austin.setLastname(AUSTIN_LASTNAME);
        austin2.setLastname("Butler");

        assertNotEquals(austin, austin2);
    }

    @Test
    public void userWithDifferentOrganizationMustNotEqual() {
        austin.setOrganizationId(AUSTIN_ORGANIZATION_ID);
        austin2.setOrganizationId(408);

        assertNotEquals(austin, austin2);
    }

    @Test
    public void userWithTheSameUsernameFirstnameLastnameOrganizationIdMustEqual() {
        austin.setFirstname(AUSTIN_FIRSTNAME);
        austin.setLastname(AUSTIN_LASTNAME);
        austin.setOrganizationId(AUSTIN_ORGANIZATION_ID);
        austin2.setFirstname(austin.getFirstname());
        austin2.setLastname(austin.getLastname());
        austin2.setOrganizationId(austin.getOrganizationId());

        assertEquals(austin, austin2);
    }

    @Test
    public void testHashcodeOfDifferentDataObjectShouldNotEqual() throws Exception {
        User bob = User.fromUsername("bob");

        assertNotEquals(austin.hashCode(), bob.hashCode());
    }
}
