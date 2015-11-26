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

package th.or.nectec.tanrabad.entity.utils;

import org.junit.Test;
import th.or.nectec.tanrabad.entity.Building;
import th.or.nectec.tanrabad.entity.Survey;

import static org.junit.Assert.assertEquals;

public class KeyContainerTest {


    @Test
    public void testFindOfOneSurvey() throws Exception {
        Survey survey = new Survey.Builder().setBuilding(Building.withName("1"))
                .setResident(4)
                .addIndoorDetail(ContainerTypeStub.น้ำดื่ม, 4, 2)
                .addIndoorDetail(ContainerTypeStub.ที่รองกันมด, 4, 4)
                .addIndoorDetail(ContainerTypeStub.จานรองกระถาง, 10, 5)
                .addOutdoorDetail(ContainerTypeStub.ภาชนะที่ไม่ใช้, 3, 3)
                .build();
        KeyContainer keyContainer = new KeyContainer(survey);
        keyContainer.calculate();

        assertEquals(ContainerTypeStub.จานรองกระถาง, keyContainer.numberOne());
        assertEquals(ContainerTypeStub.ที่รองกันมด, keyContainer.numberTwo());
        assertEquals(ContainerTypeStub.น้ำดื่ม, keyContainer.numberTwo());
        assertEquals(null, keyContainer.numberTwo());

    }
}