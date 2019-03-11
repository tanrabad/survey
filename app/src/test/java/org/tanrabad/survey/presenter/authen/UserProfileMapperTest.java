/*
 * Copyright (c) 2018 NECTEC
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

package org.tanrabad.survey.presenter.authen;

import com.bluelinelabs.logansquare.LoganSquare;
import org.junit.Test;
import org.tanrabad.survey.utils.ResourceFile;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UserProfileMapperTest {

    @Test
    public void parseJson() throws Exception {
        UserProfile profile = LoganSquare.parse(ResourceFile.read("userProfile.json"),
            UserProfile.class);

        assertEquals(profile.email, "hello2@gmail.com");
        assertEquals(profile.userName, "hello2");
        assertEquals(profile.name, "สำนักงานป้องกันควบคุมโรคที่ 13 ทดสอบ");
        assertEquals(profile.orgId, "13");
        assertEquals(profile.orgName, "สถาบันป้องกันควบคุมโรคเขตเมือง");
        assertEquals(profile.getFirstName(), "สำนักงานป้องกันควบคุมโรคที่ 13");
        assertEquals(profile.getLastName(), "ทดสอบ");
        assertTrue(profile.isEmailVerified());
        assertTrue(profile.isActive());

        Param param = profile.getParam();
        assertEquals(param.orgQueryString, "hr_code=dpc-13");
        assertEquals(param.orgHealthRegionCode, "dpc-13");
        assertEquals(param.orgHealthRegionTypeID, "4");
    }
}
