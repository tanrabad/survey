package org.tanrabad.survey.service.json;

import com.bluelinelabs.logansquare.LoganSquare;

import org.junit.Test;

import java.util.List;

import org.tanrabad.survey.entity.lookup.ContainerLocation;
import org.tanrabad.survey.utils.ResourceFile;

import static org.junit.Assert.assertEquals;

public class JsonContainerLocationTest {

    private String containerLocationJson = "{\"container_location_id\":1,\"container_location_name\":\"ภายในอาคาร\"}";

    @Test
    public void testParseToJsonString() throws Exception {
        JsonContainerLocation jsonContainerLocation = LoganSquare.parse(
                containerLocationJson, JsonContainerLocation.class);
        assertEquals(1, jsonContainerLocation.containerLocationId);
        assertEquals("ภายในอาคาร", jsonContainerLocation.containerLocationName);
    }

    @Test
    public void testParseToContainerLocationEntity() throws Exception {
        JsonContainerLocation jsonContainerLocation = LoganSquare.parse(
                containerLocationJson, JsonContainerLocation.class);
        ContainerLocation containerLocation = jsonContainerLocation.getEntity();
        assertEquals(1, containerLocation.id);
        assertEquals("ภายในอาคาร", containerLocation.name);
    }

    @Test
    public void testParseToContainerTypeEntity() throws Exception {
        List<JsonContainerLocation> containerLocationList = LoganSquare.parseList(
                ResourceFile.read("containerLocation.json"), JsonContainerLocation.class);
        ContainerLocation indoorLocation = containerLocationList.get(0).getEntity();
        assertEquals(1, indoorLocation.id);
        assertEquals("ภายในอาคาร", indoorLocation.name);

        ContainerLocation outdoorLocation = containerLocationList.get(1).getEntity();
        assertEquals(2, outdoorLocation.id);
        assertEquals("ภายนอกอาคาร", outdoorLocation.name);
    }
}
