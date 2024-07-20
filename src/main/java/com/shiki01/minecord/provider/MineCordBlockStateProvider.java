package com.shiki01.minecord.provider;

import com.shiki01.minecord.MineCord;
import com.shiki01.minecord.MineCordBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class MineCordBlockStateProvider extends BlockStateProvider {
    public MineCordBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, MineCord.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        this.simpleBlockItem(MineCordBlocks.MINE_CORD.get(), this.cubeAll(MineCordBlocks.MINE_CORD.get()));
    }
}