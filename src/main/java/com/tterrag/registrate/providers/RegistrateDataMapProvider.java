package com.tterrag.registrate.providers;

import com.tterrag.registrate.AbstractRegistrate;
import io.github.fabricators_of_create.porting_lib.data.DataMapProvider;
import net.fabricmc.api.EnvType;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;

import java.util.concurrent.CompletableFuture;

public class RegistrateDataMapProvider extends DataMapProvider implements RegistrateProvider {

	private final AbstractRegistrate<?> parent;

	protected RegistrateDataMapProvider(AbstractRegistrate<?> parent, PackOutput output, CompletableFuture<HolderLookup.Provider> pvd) {
		super(output, pvd);
		this.parent = parent;
	}

	@Override
	public EnvType getSide() {
		return EnvType.SERVER;
	}

	@Override
	protected void gather(HolderLookup.Provider provider) {
		parent.genData(ProviderType.DATA_MAP, this);
	}

}
