package com.shiki01.minecord.provider;

import com.shiki01.minecord.MineCord;
import com.shiki01.minecord.MineCordBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;

import java.util.function.Consumer;

public class MineCordRecipeProvider extends RecipeProvider {
    public MineCordRecipeProvider(DataGenerator generator) {
        super(generator);
    }

    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(MineCordBlocks.MINE_CORD.get(), 2)
                .pattern("SSS")
                .pattern("SIS")
                .pattern("SSS")
                .define('S', Items.STRING)
                .define('I', Items.IRON_INGOT)
                .unlockedBy("has_item", has(Items.STRING))
                .save(consumer, new ResourceLocation(MineCord.MOD_ID, "mine_cord"));
    }
}