package th.or.nectec.tanrabad.entity;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class UserTest {

    public static final String AUSTIN_USERNAME = "austin1023";
    public static final String AUSTIN_FIRSTNAME = "Austin";
    public static final String AUSTIN_LASTNAME = "Kydd";
    public static final String AUSTIN_EMAIL = "austin.k@gmail.com";
    public static final int AUSTIN_ORGANIZATION_ID = 201;

    @Test
    public void testSetFirstname() throws Exception {
        User austin = new User(AUSTIN_USERNAME);
        austin.setFirstname(AUSTIN_FIRSTNAME);
        assertEquals(AUSTIN_FIRSTNAME, austin.getFirstname());
    }

    @Test
    public void testSetLastname() throws Exception {
        User austin = new User(AUSTIN_USERNAME);
        austin.setLastname(AUSTIN_LASTNAME);
        assertEquals(AUSTIN_LASTNAME, austin.getLastname());
    }

    @Test
    public void setThenGetEmail() throws Exception {
        User austin = new User(AUSTIN_USERNAME);
        austin.setEmail(AUSTIN_EMAIL);
        assertEquals(AUSTIN_EMAIL, austin.getEmail());
    }

    @Test
    public void testSetOrganizationId() throws Exception {
        User austin = new User(AUSTIN_USERNAME);
        austin.setOrganizationId(AUSTIN_ORGANIZATION_ID);
        assertEquals(AUSTIN_ORGANIZATION_ID, austin.getOrganizationId());
    }

    @Test
    public void getUsername() throws Exception {
        User austin = new User(AUSTIN_USERNAME);
        assertEquals(AUSTIN_USERNAME, austin.getUsername());
    }

    @Test
    public void testFromUsername() throws Exception {
        User austin = User.fromUsername(AUSTIN_USERNAME);
        assertEquals(AUSTIN_USERNAME, austin.getUsername());
    }

    @Test
    public void userWithDifferentFirstnameMustNotEqual() throws Exception {
        User austin1 = new User(AUSTIN_USERNAME);
        austin1.setFirstname(AUSTIN_FIRSTNAME);
        User austin2 = new User(AUSTIN_USERNAME);
        austin2.setFirstname("Austinno");
        assertNotEquals(austin1, austin2);
    }

    @Test
    public void userWithDiffrentLastnameMustNotEqual() throws Exception {
        User austin1 = new User(AUSTIN_USERNAME);
        austin1.setLastname(AUSTIN_LASTNAME);
        User austin2 = new User(AUSTIN_USERNAME);
        austin2.setLastname("Butler");
        assertNotEquals(austin1, austin2);
    }
}