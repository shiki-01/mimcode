package com.shiki01.minecord.provider;

import com.shiki01.minecord.MineCord;
import com.shiki01.minecord.MineCordBlocks;
import net.minecraft.core.Direction;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.common.data.ExistingFileHelper;

public class MineCordBlockStateProvider extends BlockStateProvider {
    public MineCordBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, MineCord.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        models().cubeAll("mine_cord", modLoc("block/mine_cord"));
        getVariantBuilder(MineCordBlocks.MINE_CORD.get()).forAllStates(state -> {
            Direction dir = state.getValue(HorizontalDirectionalBlock.FACING);
            return ConfiguredModel.builder()
                    .modelFile(models().getExistingFile(modLoc("block/mine_cord")))
                    .rotationY(getYRotation(dir))
                    .uvLock(true)
                    .build();
        });
    }

    private int getYRotation(Direction dir) {
        return switch (dir) {
            case SOUTH -> 180;
            case WEST -> 270;
            case EAST -> 90;
            default -> 0;
        };
    }
}