package com.shiki01.minecord.provider;

import com.shiki01.minecord.MineCord;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class MineCordBlockModelProvider extends BlockModelProvider {
    public MineCordBlockModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, MineCord.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        this.withExistingParent("mine_cord" , mcLoc("block/cube_bottom_top"))
                .texture("side", modLoc("block/mine_cord"))
                .texture("top", modLoc("block/mine_cord_td"))
                .texture("bottom", modLoc("block/mine_cord_td"));
    }

    private void createOrientedBlock(String direction) {
        this.withExistingParent("mine_cord_" + direction, mcLoc("block/cube_bottom_top"))
                .texture("side", modLoc("block/mine_cord"))
                .texture("top", modLoc("block/mine_cord_td_" + direction))
                .texture("bottom", modLoc("block/mine_cord_td_" + direction));
    }
}