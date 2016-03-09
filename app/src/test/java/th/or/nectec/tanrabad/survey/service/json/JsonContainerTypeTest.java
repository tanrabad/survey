package th.or.nectec.tanrabad.survey.service.json;

import com.bluelinelabs.logansquare.LoganSquare;
import org.junit.Test;
import th.or.nectec.tanrabad.entity.lookup.ContainerType;

import static org.junit.Assert.assertEquals;

public class JsonContainerTypeTest {

    String containerTypeJson = "{\"container_type_id\":1,\"container_type_name\":\"น้ำใช้\"}";

    @Test
    public void testParseToJsonString() throws Exception {
        JsonContainerType jsonContainerType = LoganSquare.parse(containerTypeJson, JsonContainerType.class);
        assertEquals(1, jsonContainerType.containerTypeId);
        assertEquals("น้ำใช้", jsonContainerType.containerTypeName);
    }

    @Test
    public void testParseToContainerTypeEntity() throws Exception {
        JsonContainerType jsonContainerType = LoganSquare.parse(containerTypeJson, JsonContainerType.class);
        ContainerType containerType = jsonContainerType.getEntity();
        assertEquals(1, containerType.getId());
        assertEquals("น้ำใช้", containerType.getName());
    }
}
