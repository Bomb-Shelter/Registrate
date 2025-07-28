package com.tterrag.registrate.providers;

import com.tterrag.registrate.AbstractRegistrate;
import io.github.fabricators_of_create.porting_lib.data.DatapackBuiltinEntriesProvider;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.registries.RegistryPatchGenerator;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class RegistrateDatapackProvider extends DatapackBuiltinEntriesProvider implements RegistrateLookupFillerProvider {

	public RegistrateDatapackProvider(AbstractRegistrate<?> parent, FabricDataOutput output, CompletableFuture<HolderLookup.Provider> provider) {
		super(output, RegistryPatchGenerator.createLookup(provider, parent.getDataGenInitializer().getDatapackRegistryProviders()), Set.of(parent.getModid()));
	}

	@Override
	public CompletableFuture<HolderLookup.Provider> getFilledProvider() {
		return getRegistryProvider();
	}

	@Override
	public EnvType getSide() {
		return EnvType.SERVER;
	}

}
