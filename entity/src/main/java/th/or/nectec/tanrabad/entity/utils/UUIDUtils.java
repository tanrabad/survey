package th.or.nectec.tanrabad.entity.utils;

import java.security.InvalidParameterException;
import java.util.Random;
import java.util.UUID;

public class UUIDUtils {
    public static final String MAC_ADDRESS_PATTERN = "^([0-9a-fA-F][0-9a-fA-F]:){5}([0-9a-fA-F][0-9a-fA-F])$";
    public static Random r = new Random();
    private static long lastTime;
    private static long clockSequence = (long) (r.nextDouble() * 255L);

    public static UUID generateOrdered(String macAddress) {
        UUID uuid = generateV1(macAddress);
        return UUIDUtils.order(uuid);
    }

    public static UUID generateV1(String macAddress) {
        long currentTime = (System.currentTimeMillis() * 10000L) + 122192928000000000L;
        currentTime += System.nanoTime() % 10000;
        UUIDUtils uuid = new UUIDUtils();
        return uuid.generateIdFromTimestamp(currentTime, macAddress);
    }

    private UUID generateIdFromTimestamp(long currentTime, String macAddress) {
        long variantclockSequence = 0x0000;
        long time;
        // low Time
        time = currentTime << 32;
        // mid Time
        time |= ((currentTime & 0xFFFF00000000L) >> 16);
        // hi Time
        time |= 0x1000 | ((currentTime >> 48) & 0x0FFF);

        if (currentTime > lastTime) {
            lastTime = currentTime;
        } else {
            clockSequence++;
            lastTime = currentTime;
        }
        long clockSequenceHi = clockSequence;
        variantclockSequence |= 0xA000 | clockSequenceHi;
        variantclockSequence <<= 48;
        long lsb = variantclockSequence | getHostId(macAddress);
        return new UUID(time, lsb);
    }

    private long getHostId(String macAddress) {
        if (!macAddress.matches(MAC_ADDRESS_PATTERN)) {
            throw new InvalidParameterException(":d MAC Address " + macAddress + " is incorrect. it should be like 01:23:45:67:89:ab");
        }
        return Long.parseLong(macAddress.replace(":", ""), 16);
    }

    public static UUID order(UUID uuid) {
        String[] listUUID = uuid.toString().split("-");
        String temp = listUUID[2] + listUUID[1] + listUUID[0] + listUUID[3] + listUUID[4];
        String uuidStr = "";
        for (int i = 0; i < temp.length(); i++) {
            if (i == 8 | i == 12 | i == 16 | i == 20) {  //add '-' into index 8,12,16,20
                uuidStr += "-";
                char c = temp.charAt(i);
                uuidStr += c;
            } else {
                char c = temp.charAt(i);
                uuidStr += c;
            }
        }
        uuid = UUID.fromString(uuidStr);
        return uuid;

    }


}