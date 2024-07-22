package com.shiki01.minecord.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class MineCordGUI extends Screen {
    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation("minecord", "textures/gui/background.png");

    public MineCordGUI(Component title) {
        super(title);
    }

    @Override
    public void render(@NotNull PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, partialTicks);
        // Add your GUI rendering code here
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}