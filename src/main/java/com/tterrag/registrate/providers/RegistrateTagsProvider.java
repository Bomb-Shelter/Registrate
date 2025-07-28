package com.tterrag.registrate.providers;

import com.tterrag.registrate.AbstractRegistrate;
import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import io.github.fabricators_of_create.porting_lib.data.PortingLibTagsProvider;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public interface RegistrateTagsProvider<T> extends RegistrateLookupFillerProvider {

    TagsProvider.TagAppender<T> addTag(TagKey<T> tag);

    CompletableFuture<TagsProvider.TagLookup<T>> contentsGetter();

	ResourceKey<? extends Registry<T>> registry();

    class Impl<T> extends PortingLibTagsProvider<T> implements RegistrateTagsProvider<T> {
        private final AbstractRegistrate<?> owner;
        private final ProviderType<? extends Impl<T>> type;
        private final String name;

        public Impl(AbstractRegistrate<?> owner, ProviderType<? extends Impl<T>> type, String name, FabricDataOutput packOutput, ResourceKey<? extends Registry<T>> registryIn, CompletableFuture<HolderLookup.Provider> registriesLookup, ExistingFileHelper existingFileHelper) {
            super(packOutput, registryIn, registriesLookup, owner.getModid(), existingFileHelper);

            this.owner = owner;
            this.type = type;
            this.name = name;
        }

        @Override
        public String getName() {
            return "Tags (%s)".formatted(name);
        }

        @Override
        protected void addTags(HolderLookup.Provider provider) {
            owner.genData(type, this);
        }

        @Override
        public EnvType getSide() {
            return EnvType.SERVER;
        }

        @Override
        public TagAppender<T> addTag(TagKey<T> tag) {
            return super.tag(tag);
        }

        @Override
        public CompletableFuture<HolderLookup.Provider> getFilledProvider() {
            return createContentsProvider();
        }

		@Override
		public ResourceKey<? extends Registry<T>> registry() {
			return registryKey;
		}

	}

    class IntrinsicImpl<T> extends IntrinsicHolderTagsProvider<T> implements RegistrateTagsProvider<T> {
        private final AbstractRegistrate<?> owner;
        private final ProviderType<? extends IntrinsicImpl<T>> type;
        private final String name;

        public IntrinsicImpl(AbstractRegistrate<?> owner, ProviderType<? extends IntrinsicImpl<T>> type, String name, PackOutput packOutput, ResourceKey<? extends Registry<T>> registryIn, CompletableFuture<HolderLookup.Provider> registriesLookup, Function<T, ResourceKey<T>> keyExtractor, ExistingFileHelper existingFileHelper) {
            super(packOutput, registryIn, registriesLookup, keyExtractor);

            this.owner = owner;
            this.type = type;
            this.name = name;
        }

        @Override
        public String getName() {
            return "Tags (%s)".formatted(name);
        }

        @Override
        protected void addTags(HolderLookup.Provider provider) {
            owner.genData(type, this);
        }

        @Override
        public EnvType getSide() {
            return EnvType.SERVER;
        }

        @Override
        public IntrinsicTagAppender<T> addTag(TagKey<T> tag) {
            return super.tag(tag);
        }

        @Override
        public CompletableFuture<HolderLookup.Provider> getFilledProvider() {
            return createContentsProvider();
        }

		@Override
		public ResourceKey<? extends Registry<T>> registry() {
			return registryKey;
		}

	}
}
