package com.shiki01.minecord;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MineCordBlocks {
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MineCord.MOD_ID);
    public static final RegistryObject<Block> MINE_CORD = BLOCKS.register("mine_cord", () -> new Block(BlockBehaviour.Properties.of(Material.EXPLOSIVE).lightLevel(value -> 0)) {
        @Override
        protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
            super.createBlockStateDefinition(builder);
            builder.add(HorizontalDirectionalBlock.FACING);
        }
    });

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}