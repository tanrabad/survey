package th.or.nectec.tanrabad.entity.utils;

import java.security.InvalidParameterException;
import java.util.Random;
import java.util.UUID;

public class UuidUtils {
    private static final String MAC_ADDRESS_PATTERN = "^([0-9a-fA-F][0-9a-fA-F]:){5}([0-9a-fA-F][0-9a-fA-F])$";
    private static final Random r = new Random();
    private static long lastTime;
    private static long clockSequence = (long) (r.nextDouble() * 255L);

    public static UUID generateOrdered(String macAddress) {
        UUID uuid = generateV1(macAddress);
        return UuidUtils.order(uuid);
    }

    private static UUID generateV1(String macAddress) {
        long currentTime = (System.currentTimeMillis() * 10000L) + 122192928000000000L;
        currentTime += System.nanoTime() % 10000;
        UuidUtils uuid = new UuidUtils();
        return uuid.generateIdFromTimestamp(currentTime, macAddress);
    }

    private UUID generateIdFromTimestamp(long currentTime, String macAddress) {
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
        long variantClockSequence = 0x0000;
        variantClockSequence |= 0xA000 | clockSequenceHi;
        variantClockSequence <<= 48;
        long lsb = variantClockSequence | getHostId(macAddress);
        return new UUID(time, lsb);
    }

    private long getHostId(String macAddress) {
        if (!macAddress.matches(MAC_ADDRESS_PATTERN)) {
            throw new InvalidParameterException(
                    ":d MAC Address " + macAddress + " is incorrect. it should be like 01:23:45:67:89:ab");
        }
        return Long.parseLong(macAddress.replace(":", ""), 16);
    }

    public static UUID order(UUID uuid) {
        String[] listUuid = uuid.toString().split("-");
        String temp = listUuid[2] + listUuid[1] + listUuid[0] + listUuid[3] + listUuid[4];
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
