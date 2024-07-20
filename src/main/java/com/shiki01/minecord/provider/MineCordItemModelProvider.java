package com.shiki01.minecord.provider;

import com.shiki01.minecord.MineCord;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class MineCordItemModelProvider extends ItemModelProvider {
    public MineCordItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, MineCord.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        this.getBuilder("mine_cord")
                .parent(new ModelFile.UncheckedModelFile(modLoc("block/mine_cord")));
    }
}