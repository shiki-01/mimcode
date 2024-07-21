package com.shiki01.minecord.provider;

import com.shiki01.minecord.MineCord;
import com.shiki01.minecord.MineCordBlocks;
import net.minecraft.core.Direction;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.logging.Logger;

public class MineCordBlockStateProvider extends BlockStateProvider {
    public MineCordBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, MineCord.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        models().cubeAll("mine_cord", modLoc("block/mine_cord"));
        getVariantBuilder(MineCordBlocks.MINE_CORD.get()).forAllStates(state -> ConfiguredModel.builder()
                .modelFile(models().getExistingFile(modLoc("block/mine_cord")))
                .rotationY((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot())
                .build()
        );
    }
}