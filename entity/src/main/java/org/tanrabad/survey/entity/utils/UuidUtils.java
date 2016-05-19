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

package org.tanrabad.survey.entity.utils;

import java.security.InvalidParameterException;
import java.util.Random;
import java.util.UUID;

public class UuidUtils {
    private static final String MAC_ADDRESS_PATTERN = "^([0-9a-fA-F][0-9a-fA-F]:){5}([0-9a-fA-F][0-9a-fA-F])$";
    private static final String DEFAULT_MAC_ADDRESS = "05:21:09:09:87:88";
    private static final Random RANDOM = new Random();

    private static long lastTime;
    private static long clockSequence = (long) (RANDOM.nextDouble() * 255L);

    public static UUID generateOrdered(String macAddress) {
        UUID uuid = generateV1(macAddress);
        return UuidUtils.order(uuid);
    }

    private static UUID generateV1(String macAddress) {
        if (macAddress == null) macAddress = DEFAULT_MAC_ADDRESS;
        long currentTime = System.currentTimeMillis() * 10000L + 122192928000000000L;
        currentTime += System.nanoTime() % 10000;
        UuidUtils uuid = new UuidUtils();
        return uuid.generateIdFromTimestamp(currentTime, macAddress);
    }

    public static UUID order(UUID uuid) {
        String[] listUuid = uuid.toString().split("-");
        String temp = listUuid[2] + listUuid[1] + listUuid[0] + listUuid[3] + listUuid[4];
        StringBuilder uuidString = new StringBuilder();
        for (int i = 0; i < temp.length(); i++) {
            if (i == 8 | i == 12 | i == 16 | i == 20) {
                uuidString.append('-');
            }
            uuidString.append(temp.charAt(i));
        }
        return UUID.fromString(uuidString.toString());
    }

    private UUID generateIdFromTimestamp(long currentTime, String macAddress) {
        long time;
        // low Time
        time = currentTime << 32;
        // mid Time
        time |= (currentTime & 0xFFFF00000000L) >> 16;
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
}
