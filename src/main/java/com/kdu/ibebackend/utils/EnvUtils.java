package com.kdu.ibebackend.utils;

import java.util.Objects;

/**
 * Util function to check local environment and use different credentials and mockers.
 */
public class EnvUtils {
    public static boolean localEnvironmentCheck(String env) {
        return Objects.equals(env, "dev") || Objects.equals(env, "test");
    }
}
