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

package th.or.nectec.tanrabad.domain;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;
import th.or.nectec.tanrabad.entity.Container;

import java.util.ArrayList;

public class ContainerControllerTest {
    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    @Test
    public void testHappyPath() throws Exception {
        final ContainerRepository containerRepository = context.mock(ContainerRepository.class);
        final ContainerPresenter containerPresenter = context.mock(ContainerPresenter.class);

        final ArrayList<Container> containers = new ArrayList<>();
        containers.add(new Container(1));
        containers.add(Container.fromId(2));

        context.checking(new Expectations(){{
            allowing(containerRepository).find();
            will(returnValue(containers));
        oneOf(containerPresenter).showContainerList(with(containers));
        }
        });

        ContainerController containerController = new ContainerController(containerRepository, containerPresenter);
        containerController.showList();

    }

    @Test
    public void testNotFoundPath() throws Exception {
        final ContainerRepository containerRepository = context.mock(ContainerRepository.class);
        final ContainerPresenter containerPresenter = context.mock(ContainerPresenter.class);

        final ArrayList<Container> containers = new ArrayList<>();
        containers.add(new Container(1));
        containers.add(Container.fromId(2));

        context.checking(new Expectations() {
            {
                allowing(containerRepository).find();
                will(returnValue(null));
                oneOf(containerPresenter).showContainerNotFound();
            }
        });

        ContainerController containerController = new ContainerController(containerRepository, containerPresenter);
        containerController.showList();

    }

}
