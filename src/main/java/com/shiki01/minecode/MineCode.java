package com.shiki01.minecode;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(MineCode.MOD_ID)
public class MineCode {
    public static final String MOD_ID = "minecode";

    public MineCode() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ItemInit.ITEMS.register(modEventBus);
        MineCodeItems.register(modEventBus);
    }
}
