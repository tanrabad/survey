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

import th.or.nectec.tanrabad.entity.ContainerType;
import th.or.nectec.tanrabad.entity.Survey;

import java.util.ArrayList;
import java.util.List;

public class KeyContainer {

    private final List<Survey> surveys;

    public KeyContainer(Survey survey) {
        this();
        surveys.add(survey);
    }

    public KeyContainer() {
        surveys = new ArrayList<>();
    }

    public KeyContainer(List<Survey> surveys) {
        this.surveys = surveys;
    }

    public ContainerType numberOne() {
        return null;
    }

    public ContainerType numberTwo() {
        return null;
    }

    public ContainerType numberThree() {
        return null;
    }

    public void calculate() {

    }
}
