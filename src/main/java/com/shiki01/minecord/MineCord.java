package com.shiki01.minecord;

import com.shiki01.minecord.provider.MineCordBlockModelProvider;
import com.shiki01.minecord.provider.MineCordBlockStateProvider;
import com.shiki01.minecord.provider.MineCordItemModelProvider;
import com.shiki01.minecord.provider.MineCordLangProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ItemModelProvider;
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
        MineCordBlocks.register(modEventBus);
        MineCordItems.register(modEventBus);
    }

    private void registerProviders(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        gen.addProvider(event.includeClient(),new MineCordBlockStateProvider(gen, event.getExistingFileHelper()));
        gen.addProvider(event.includeClient(),new MineCordBlockModelProvider(gen, event.getExistingFileHelper()));
        gen.addProvider(event.includeClient(),new MineCordItemModelProvider(gen, event.getExistingFileHelper()));
        gen.addProvider(event.includeClient(),new MineCordLangProvider.MineCordLangEn(gen));
        gen.addProvider(event.includeClient(),new MineCordLangProvider.MineCordLangJa(gen));
    }
}
