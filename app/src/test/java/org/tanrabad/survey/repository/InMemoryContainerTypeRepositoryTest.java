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

package org.tanrabad.survey.repository;

import org.junit.Before;
import org.junit.Test;
import org.tanrabad.survey.entity.lookup.ContainerType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class InMemoryContainerTypeRepositoryTest {

    private final InMemoryContainerTypeRepository repository = InMemoryContainerTypeRepository.getInstance();
    private final ContainerType jar = new ContainerType(1, "jar");

    @Before
    public void setUp() throws Exception {
        repository.save(jar);
    }

    @Test
    public void testFind() throws Exception {
        assertEquals(jar, repository.find().get(0));
    }

    @Test
    public void testFind2item() throws Exception {
        repository.save(new ContainerType(2, "vehicle"));

        assertEquals(2, repository.find().size());
    }

    @Test
    public void testUpdate() throws Exception {
        ContainerType updateJar = new ContainerType(1, "jars");

        repository.update(updateJar);

        assertEquals(updateJar, repository.findById(updateJar.getId()));
    }

    @Test
    public void testDelete() throws Exception {
        repository.delete(jar);

        assertNull(repository.findById(jar.getId()));
    }

    @Test
    public void testSingleton() throws Exception {
        assertEquals(repository, InMemoryContainerTypeRepository.getInstance());
    }
}
