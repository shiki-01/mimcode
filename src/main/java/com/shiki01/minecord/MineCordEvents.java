package com.shiki01.minecord;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(modid = MineCord.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MineCordEvents {
    public static final CreativeModeTab MINECORD_TAB = new CreativeModeTab("minecord_tab") {
        @Override
        public @NotNull ItemStack makeIcon() {
            return new ItemStack(MineCordBlocks.MINE_CORD.get());
        }
    };

    public static void register(IEventBus eventBus) {
        FMLJavaModLoadingContext.get().getModEventBus().register(MineCordEvents.class);
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            MINECORD_TAB.setRecipeFolderName("minecord");
        });
    }
}

