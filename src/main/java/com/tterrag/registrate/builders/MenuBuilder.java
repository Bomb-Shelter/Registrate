package com.tterrag.registrate.builders;

import javax.annotation.Nullable;

import com.tterrag.registrate.AbstractRegistrate;

import com.tterrag.registrate.util.RegistrateDistExecutor;
import com.tterrag.registrate.util.entry.MenuEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import com.tterrag.registrate.util.nullness.NonnullType;

import io.github.fabricators_of_create.porting_lib.registry.DeferredHolder;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public class MenuBuilder<T extends AbstractContainerMenu, S extends Screen & MenuAccess<T>,  P, D> extends AbstractBuilder<MenuType<?>, MenuType<T>, P, MenuBuilder<T, S, P, D>> {
    
    public interface MenuFactory<T extends AbstractContainerMenu> {
        
        T create(MenuType<T> type, int windowId, Inventory inv);
    }

    public interface FabricMenuFactory<T extends AbstractContainerMenu, D> {

        T create(MenuType<T> type, int windowId, Inventory inv, @Nullable D buffer);
    }
    
    public interface ScreenFactory<M extends AbstractContainerMenu, T extends Screen & MenuAccess<M>> {
        
        T create(M menu, Inventory inv, Component displayName);
    }
    
    private final FabricMenuFactory<T, D> factory;
    private final NonNullSupplier<ScreenFactory<T, S>> screenFactory;
    private final StreamCodec<RegistryFriendlyByteBuf, D> streamCodec;

    public MenuBuilder(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, MenuFactory<T> factory, NonNullSupplier<ScreenFactory<T, S>> screenFactory) {
        this(owner, parent, name, callback, (type, windowId, inv, $) -> factory.create(type, windowId, inv), screenFactory, null);
    }

    public MenuBuilder(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, FabricMenuFactory<T, D> factory, NonNullSupplier<ScreenFactory<T, S>> screenFactory, StreamCodec<RegistryFriendlyByteBuf, D> streamCodec) {
        super(owner, parent, name, callback, Registries.MENU);
        this.factory = factory;
        this.screenFactory = screenFactory;
        this.streamCodec = streamCodec;
    }

    @Override
    protected @NonnullType MenuType<T> createEntry() {
        FabricMenuFactory<T, D> factory = this.factory;
        final var supplier = this.asSupplier();
        MenuType<T> ret = streamCodec != null ?
            new ExtendedScreenHandlerType<>((windowId, inv, data) -> factory.create(supplier.get(), windowId, inv, data), streamCodec) :
            new MenuType<>((windowId, inv) -> factory.create(supplier.get(), windowId, inv, null), FeatureFlagSet.of());
        RegistrateDistExecutor.unsafeRunWhenOn(EnvType.CLIENT, () -> () -> {
            ScreenFactory<T, S> screenFactory = this.screenFactory.get();
            MenuScreens.register(ret, screenFactory::create);
        });
        return ret;
    }

    @Override
    protected RegistryEntry<MenuType<?>, MenuType<T>> createEntryWrapper(DeferredHolder<MenuType<?>, MenuType<T>> delegate) {
        return new MenuEntry<>(getOwner(), delegate);
    }

    @Override
    public MenuEntry<T> register() {
        return (MenuEntry<T>) super.register();
    }
}
