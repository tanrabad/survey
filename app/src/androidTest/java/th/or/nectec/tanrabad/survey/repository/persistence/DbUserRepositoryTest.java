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

package th.or.nectec.tanrabad.survey.repository.persistence;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import th.or.nectec.tanrabad.domain.organization.OrganizationRepository;
import th.or.nectec.tanrabad.entity.Organization;
import th.or.nectec.tanrabad.entity.User;
import th.or.nectec.tanrabad.survey.base.SurveyDbTestRule;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class DbUserRepositoryTest {

    public static final String DPC_USER = "dpc-user";
    @Rule
    public SurveyDbTestRule dbTestRule = new SurveyDbTestRule();
    private OrganizationRepository organizationRepository;


    @Before
    public void setup() {
        organizationRepository = Mockito.mock(OrganizationRepository.class);
        Mockito.when(organizationRepository.findById(100)).thenReturn(stubOrganization());
    }

    private Organization stubOrganization() {
        Organization organization = new Organization(100, "กรมควบคุมโรค");
        organization.setHealthRegionCode("dpc-13");
        return organization;
    }

    @Test
    public void testSave() throws Exception {
        User user = new User("hkn-user");
        user.setFirstname("ทดสอบ");
        user.setLastname("ทดสอบหน่อย");
        user.setPassword("55557");
        user.setEmail("hhk@gmail.com");
        user.setOrganizationId(100);
        user.setPhoneNumber("081-2242612");

        Context context = InstrumentationRegistry.getTargetContext();
        DbUserRepository repository = new DbUserRepository(context);
        boolean success = repository.save(user);

        SQLiteDatabase db = SurveyLiteDatabase.getInstance(context).getReadableDatabase();
        Cursor cursor = db.query(DbUserRepository.TABLE_NAME,
                UserColumn.wildcard(),
                UserColumn.USERNAME + "=?",
                new String[]{user.getUsername()},
                null, null, null);

        assertEquals(true, success);
        assertEquals(true, cursor.moveToFirst());
        assertEquals(1, cursor.getCount());

        assertEquals("hkn-user", cursor.getString(cursor.getColumnIndex(UserColumn.USERNAME)));
        assertEquals("ทดสอบ", cursor.getString(cursor.getColumnIndex(UserColumn.FIRSTNAME)));
        assertEquals("ทดสอบหน่อย", cursor.getString(cursor.getColumnIndex(UserColumn.LASTNAME)));
        assertEquals("55557", cursor.getString(cursor.getColumnIndex(UserColumn.PASSWORD)));
        assertEquals("hhk@gmail.com", cursor.getString(cursor.getColumnIndex(UserColumn.EMAIL)));
        assertEquals("081-2242612", cursor.getString(cursor.getColumnIndex(UserColumn.PHONE_NUMBER)));
        assertEquals(100, cursor.getInt(cursor.getColumnIndex(UserColumn.ORG_ID)));
        cursor.close();
    }


    @Test
    public void testUpdate() throws Exception {
        User user = getUser();
        user.setEmail("a.555@gmail.com");

        Context context = InstrumentationRegistry.getTargetContext();
        DbUserRepository repository = new DbUserRepository(context);
        boolean success = repository.update(user);

        SQLiteDatabase db = SurveyLiteDatabase.getInstance(context).getReadableDatabase();
        Cursor cursor = db.query(DbUserRepository.TABLE_NAME,
                UserColumn.wildcard(),
                UserColumn.USERNAME + "=?",
                new String[]{user.getUsername()},
                null, null, null);

        assertEquals(true, success);
        assertEquals(true, cursor.moveToFirst());
        assertEquals(1, cursor.getCount());
        assertEquals(DPC_USER, cursor.getString(cursor.getColumnIndex(UserColumn.USERNAME)));
        assertEquals("ซาร่า", cursor.getString(cursor.getColumnIndex(UserColumn.FIRSTNAME)));
        assertEquals("คิดส์", cursor.getString(cursor.getColumnIndex(UserColumn.LASTNAME)));
        assertEquals("5555", cursor.getString(cursor.getColumnIndex(UserColumn.PASSWORD)));
        assertEquals("a.555@gmail.com", cursor.getString(cursor.getColumnIndex(UserColumn.EMAIL)));
        assertEquals("081-2345678", cursor.getString(cursor.getColumnIndex(UserColumn.PHONE_NUMBER)));
        assertEquals(100, cursor.getInt(cursor.getColumnIndex(UserColumn.ORG_ID)));
        cursor.close();
    }

    @NonNull
    private User getUser() {
        User dpcUser = new User(DPC_USER);
        dpcUser.setFirstname("ซาร่า");
        dpcUser.setLastname("คิดส์");
        dpcUser.setPassword("5555");
        dpcUser.setEmail("sara.k@gmail.com");
        dpcUser.setOrganizationId(100);
        dpcUser.setPhoneNumber("081-2345678");
        return dpcUser;
    }

    @Test
    public void testFindByUserName() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();
        DbUserRepository dbUserRepository = new DbUserRepository(context, organizationRepository);

        User user = dbUserRepository.findByUsername(DPC_USER);
        assertEquals(DPC_USER, user.getUsername());
        assertEquals("ซาร่า", user.getFirstname());
        assertEquals("คิดส์", user.getLastname());
        assertEquals("5555", user.getPassword());
        assertEquals("sara.k@gmail.com", user.getEmail());
        assertEquals("081-2345678", user.getPhoneNumber());
        assertEquals("dpc-13", user.getHealthRegionCode());
        assertEquals(100, user.getOrganizationId());
    }
}