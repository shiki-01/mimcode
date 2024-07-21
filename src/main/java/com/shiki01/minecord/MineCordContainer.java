package com.shiki01.minecord;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

public class MineCordContainer extends AbstractContainerMenu {
    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, "minecord");
    public static final RegistryObject<MenuType<MineCordContainer>> MINE_CORD_CONTAINER = CONTAINERS.register("mine_cord_container", () -> IForgeMenuType.create((windowId, inv, data) -> {
        FriendlyByteBuf buf = new FriendlyByteBuf(data);
        BlockPos pos = buf.readBlockPos();
        return new MineCordContainer(windowId, inv.player.level, pos, inv, inv.player);
    }));

    public static void register(IEventBus eventBus) {
        CONTAINERS.register(eventBus);
    }

    public MineCordContainer(int id, Level level, BlockPos pos, Inventory playerInventory, Player player) {
        super(MINE_CORD_CONTAINER.get(), id);

        ItemStackHandler itemStackHandler = new ItemStackHandler(1);
        this.addSlot(new SlotItemHandler(itemStackHandler, 0, 64, 24));

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
        }
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return true;
    }

    @Override
    public @NotNull ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }
}