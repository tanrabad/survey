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

package th.or.nectec.tanrabad.domain.survey;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;
import th.or.nectec.tanrabad.entity.lookup.ContainerType;

import java.util.ArrayList;

public class ContainerTypeControllerTest {
    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    @Test
    public void testHappyPath() throws Exception {
        final ContainerTypeRepository containerTypeRepository = context.mock(ContainerTypeRepository.class);
        final ContainerPresenter containerPresenter = context.mock(ContainerPresenter.class);

        final ArrayList<ContainerType> containerTypes = new ArrayList<>();
        containerTypes.add(new ContainerType(1, "น้ำใช้"));
        containerTypes.add(new ContainerType(2, "น้ำดื่ม"));

        context.checking(new Expectations() {{
            allowing(containerTypeRepository).find();
            will(returnValue(containerTypes));
            oneOf(containerPresenter).displayContainerList(with(containerTypes));
        }
        });

        ContainerController containerController = new ContainerController(containerTypeRepository, containerPresenter);
        containerController.showList();

    }

    @Test
    public void testNotFoundPath() throws Exception {
        final ContainerTypeRepository containerTypeRepository = context.mock(ContainerTypeRepository.class);
        final ContainerPresenter containerPresenter = context.mock(ContainerPresenter.class);

        final ArrayList<ContainerType> containerTypes = new ArrayList<>();
        containerTypes.add(new ContainerType(1, "น้ำใช้"));
        containerTypes.add(new ContainerType(2, "น้ำดื่ม"));

        context.checking(new Expectations() {
            {
                allowing(containerTypeRepository).find();
                will(returnValue(null));
                oneOf(containerPresenter).alertContainerNotFound();
            }
        });

        ContainerController containerController = new ContainerController(containerTypeRepository, containerPresenter);
        containerController.showList();

    }

}
