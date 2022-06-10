package com.james.calcite.utils;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class RandomUtil {

    public static UUID randomUUID() {
        return new UUID(ThreadLocalRandom.current().nextLong(), ThreadLocalRandom.current().nextLong());
    }

    public static String randomUUIDStr() {
        return randomUUID().toString();
    }
}
