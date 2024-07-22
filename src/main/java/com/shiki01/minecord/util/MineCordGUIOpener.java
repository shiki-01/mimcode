package com.shiki01.minecord.util;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.level.Level;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import com.shiki01.minecord.MineCordContainer;

public class MineCordGUIOpener {
    public static void openCustomGui(ServerPlayer player, Level world, BlockPos pos) {
        if (!world.isClientSide) {
            MenuProvider menuProvider = new SimpleMenuProvider(
                (windowId, inv, p) -> new MineCordContainer(windowId, pos),
                Component.literal("Mine Cord")
            );
            player.openMenu(menuProvider);
        }
    }
}