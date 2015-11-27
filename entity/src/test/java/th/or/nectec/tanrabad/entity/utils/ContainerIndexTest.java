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

import static org.junit.Assert.assertEquals;

public class ContainerIndexTest {

    public static final float DELTA = 0.01f;

    @Test
    public void testCalculateWithOneSurvey() throws Exception {
        ContainerIndex ci = new ContainerIndex(SurveyStub.withResult(10, 1));
        ContainerIndex ci2 = new ContainerIndex(SurveyStub.withResult(ContainerTypeStub.จานรองกระถาง, 20, 2));
        ContainerIndex ci3 = new ContainerIndex(SurveyStub.withIndoorOutdoorResult(5, 0, 5, 1));

        assertEquals(10.0f, ci.calculate(), DELTA);
        assertEquals(10.0f, ci2.calculate(), DELTA);
        assertEquals(10.0f, ci3.calculate(), DELTA);
    }

    @Test
    public void testCalculateWithThreeSurvey() throws Exception {
        ContainerIndex ci = new ContainerIndex();
        ci.add(SurveyStub.withResult(10, 3));
        ci.add(SurveyStub.withResult(40, 15));
        ci.add(SurveyStub.withResult(50, 7));

        assertEquals(25.0f, ci.calculate(), DELTA);
    }

    @Test
    public void testCalculateWithDifferentContainerType() throws Exception {
        ContainerIndex ci = new ContainerIndex();
        ci.add(SurveyStub.withResult(50, 20));
        ci.add(SurveyStub.withResult(ContainerTypeStub.จานรองกระถาง, 50, 0));

        assertEquals(20.0f, ci.calculate(), DELTA);
    }

    @Test
    public void testCalculateWithIndoorAndOutdoor() throws Exception {
        ContainerIndex ci = new ContainerIndex();
        ci.add(SurveyStub.withIndoorOutdoorResult(30, 0, 20, 4));
        ci.add(SurveyStub.withResult(ContainerTypeStub.จานรองกระถาง, 50, 0));

        assertEquals(4.0f, ci.calculate(), DELTA);

    }

    @Test
    public void testGetTotalContainerAnd() throws Exception {
        ContainerIndex ci = new ContainerIndex();
        ci.add(SurveyStub.withIndoorOutdoorResult(30, 0, 20, 4));
        ci.add(SurveyStub.withResult(ContainerTypeStub.จานรองกระถาง, 50, 0));
        ci.calculate();

        assertEquals(4, ci.getFoundLarvaeContainer());
        assertEquals(100, ci.getTotalContainer());

    }
}