package com.kdu.ibebackend.utils;

import java.util.Objects;

public class EnvUtils {
    public static boolean localEnvironmentCheck(String env) {
        return Objects.equals(env, "dev") || Objects.equals(env, "test");
    }
}
