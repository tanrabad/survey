package th.or.nectec.tanrabad.entity.utils;

import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;


public class UUIDUtilsTest {

    @Test
    public void testOrder() throws Exception {
        UUID uuid = UUID.fromString("58e0a7d7-eebc-11d8-9669-0800200c9a66");
        UUID orderedUuid = UUIDUtils.order(uuid);

        assertEquals("11d8eebc-58e0-a7d7-9669-0800200c9a66", orderedUuid.toString());
    }
}