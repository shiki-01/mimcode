package com.shiki01.minecord.provider;

import com.shiki01.minecord.MineCord;
import com.shiki01.minecord.MineCordBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class MineCordRecipeProvider extends RecipeProvider {
    public MineCordRecipeProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void buildCraftingRecipes(@NotNull Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(MineCordBlocks.MINE_CORD.get(), 1)
                .pattern("LRL")
                .pattern("ODS")
                .pattern("LRL")
                .define('L', Items.LAPIS_LAZULI)
                .define('R', Items.REDSTONE)
                .define('O', Items.OBSERVER)
                .define('D', Items.DIAMOND)
                .define('S', Items.STRING)
                .unlockedBy("has_item", has(Items.DIAMOND))
                .save(consumer, new ResourceLocation(MineCord.MOD_ID, "mine_cord"));
    }
}