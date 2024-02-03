package com.mensagemdodia.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PhraseTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Phrase getPhraseSample1() {
        return new Phrase().id(1L).content("content1").slug("slug1");
    }

    public static Phrase getPhraseSample2() {
        return new Phrase().id(2L).content("content2").slug("slug2");
    }

    public static Phrase getPhraseRandomSampleGenerator() {
        return new Phrase().id(longCount.incrementAndGet()).content(UUID.randomUUID().toString()).slug(UUID.randomUUID().toString());
    }
}
