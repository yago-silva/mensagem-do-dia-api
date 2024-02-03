package com.mensagemdodia.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AdTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Ad getAdSample1() {
        return new Ad().id(1L).locale("locale1").deviceType("deviceType1").affiliateLink("affiliateLink1");
    }

    public static Ad getAdSample2() {
        return new Ad().id(2L).locale("locale2").deviceType("deviceType2").affiliateLink("affiliateLink2");
    }

    public static Ad getAdRandomSampleGenerator() {
        return new Ad()
            .id(longCount.incrementAndGet())
            .locale(UUID.randomUUID().toString())
            .deviceType(UUID.randomUUID().toString())
            .affiliateLink(UUID.randomUUID().toString());
    }
}
