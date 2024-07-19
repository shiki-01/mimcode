package com.shiki01.minecord;

import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(MineCord.MOD_ID)
public class MineCord {
    public static final String MOD_ID = "minecord";

    public MineCord() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::registerProviders);
        ItemInit.register(modEventBus);
    }

    private void registerProviders(GatherDataEvent event) {
        event.getGenerator().addProvider(new ItemModelProvider(event.getGenerator(), MOD_ID, event.getExistingFileHelper()));
        event.getGenerator().addProvider(new BlockStateProvider(event.getGenerator(), MOD_ID, event.getExistingFileHelper()));
        event.getGenerator().addProvider(new ItemTagProvider(event.getGenerator(), MOD_ID, event.getExistingFileHelper()));
        event.getGenerator().addProvider(new BlockTagProvider(event.getGenerator(), MOD_ID, event.getExistingFileHelper()));
    }
}
