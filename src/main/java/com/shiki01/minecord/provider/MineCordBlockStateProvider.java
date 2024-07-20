package com.shiki01.minecord.provider;

import com.shiki01.minecord.MineCord;
import com.shiki01.minecord.MineCordBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class MineCordBlockStateProvider extends BlockStateProvider {
    public MineCordBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, MineCord.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        getVariantBuilder(MineCordBlocks.MINE_CORD.get()).forAllStates(state -> {
            Direction dir = state.getValue(HorizontalDirectionalBlock.FACING);
            return ConfiguredModel.builder()
                    .modelFile(models().getExistingFile(modLoc("block/mine_cord")))
                    .rotationY(getYRotation(dir))
                    .build();
        });
    }

    private int getYRotation(Direction dir) {
        return switch (dir) {
            case NORTH -> 0;
            case SOUTH -> 180;
            case WEST -> 270;
            case EAST -> 90;
            default -> 0;
        };
    }
}