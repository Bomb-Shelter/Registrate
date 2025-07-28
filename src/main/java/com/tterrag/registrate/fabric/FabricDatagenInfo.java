package com.tterrag.registrate.fabric;

import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public record FabricDatagenInfo(
    FabricDataOutput packOutput,
    ExistingFileHelper existingFileHelper,
    CompletableFuture<HolderLookup.Provider> lookupProvider
) {
}
