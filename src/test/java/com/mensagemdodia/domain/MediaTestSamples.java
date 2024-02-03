package com.mensagemdodia.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class MediaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Media getMediaSample1() {
        return new Media().id(1L).url("url1").width(1L).height(1L);
    }

    public static Media getMediaSample2() {
        return new Media().id(2L).url("url2").width(2L).height(2L);
    }

    public static Media getMediaRandomSampleGenerator() {
        return new Media()
            .id(longCount.incrementAndGet())
            .url(UUID.randomUUID().toString())
            .width(longCount.incrementAndGet())
            .height(longCount.incrementAndGet());
    }
}
