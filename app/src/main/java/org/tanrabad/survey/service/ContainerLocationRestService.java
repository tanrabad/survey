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

package org.tanrabad.survey.service;

import com.bluelinelabs.logansquare.LoganSquare;
import org.tanrabad.survey.TanrabadApp;
import org.tanrabad.survey.entity.lookup.ContainerLocation;
import org.tanrabad.survey.service.json.JsonContainerLocation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ContainerLocationRestService extends AbsRestService<ContainerLocation> {

    private static final String PATH = "/containerlocation";

    public ContainerLocationRestService() {
        this(ImpRestServiceConfig.getInstance().getApiBaseUrl(),
                new ApiSyncInfoPreference(TanrabadApp.getInstance(), PATH));
    }

    private ContainerLocationRestService(String apiBaseUrl, ServiceLastUpdate serviceLastUpdate) {
        super(apiBaseUrl, serviceLastUpdate);
    }

    @Override
    protected String getPath() {
        return PATH;
    }

    @Override
    protected List<ContainerLocation> jsonToEntityList(String responseBody) {
        ArrayList<ContainerLocation> containerLocations = new ArrayList<>();
        try {
            List<JsonContainerLocation> jsonContainerTypeList = LoganSquare.parseList(
                    responseBody, JsonContainerLocation.class);
            for (JsonContainerLocation eachJsonContainerLocation : jsonContainerTypeList) {
                containerLocations.add(eachJsonContainerLocation.getEntity());
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return containerLocations;
    }
}
