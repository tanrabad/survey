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

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.tanrabad.survey.entity.SurveyDetail;
import org.tanrabad.survey.entity.lookup.ContainerType;

import java.util.UUID;

@RunWith(JUnit4.class)
public class SurveyDetailTest {

    private final ContainerType water = new ContainerType(1, "water");

    @Test(expected = SurveyDetail.ContainerFoundLarvaOverTotalException.class)
    public void containerFoundLarvaMoreThanTotalMustThrowException() {
        new SurveyDetail(UUID.randomUUID(), water, 2, 10);
    }

    @Test
    public void testIsFoundLarvae() throws Exception {
        Assert.assertEquals(false, new SurveyDetail(UUID.randomUUID(), water, 10, 0).isFoundLarva());
        Assert.assertEquals(true, new SurveyDetail(UUID.randomUUID(), water, 10, 3).isFoundLarva());
        Assert.assertEquals(true, new SurveyDetail(UUID.randomUUID(), water, 10, 10).isFoundLarva());
    }
}
