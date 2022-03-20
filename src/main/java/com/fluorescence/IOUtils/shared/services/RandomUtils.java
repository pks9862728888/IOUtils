package com.fluorescence.IOUtils.shared.services;

import java.util.UUID;

public interface RandomUtils {

    static String generateRandomUUID() {
        return UUID.randomUUID().toString();
    }
}
