package com.tterrag.registrate.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;

import java.util.function.Supplier;

public class RegistrateDistExecutor {
    public static void unsafeRunWhenOn(EnvType dist, Supplier<Runnable> toRun) {
        if (dist == FabricLoader.getInstance().getEnvironmentType()) {
            toRun.get().run();
        }
    }
}
