package com.shiki01.minecord.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.shiki01.minecord.MineCordContainer;
import com.shiki01.minecord.util.MineCordBlockState;
import com.shiki01.minecord.util.MineCordLogger;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class MineCordGuiScreen extends AbstractContainerScreen<MineCordContainer> {
    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation("minecord", "textures/gui/background.png");
    private EditBox messageInput;
    private String message = "";
    private boolean signalState = false;
    private final MineCordButtonState[][] mineCordButtonStates = new MineCordButtonState[3][3];

    public MineCordGuiScreen(MineCordContainer container, Inventory inv, Component title) {
        super(container, inv, title);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.messageInput.isFocused()) {
            return this.messageInput.keyPressed(keyCode, scanCode, modifiers);
        } else {
            return super.keyPressed(keyCode, scanCode, modifiers);
        }
    }

    @Override
    protected void init() {
        super.init();
        this.clearWidgets();

        Map<BlockPos, MineCordBlockState> blockStates = Map.of();
        try {
            blockStates = MineCordBlockState.loadFromFile("blockStates.dat");
            if (!blockStates.isEmpty()) {
                BlockPos BlockPos = blockStates.keySet().iterator().next();
                MineCordBlockState BlockState = blockStates.get(BlockPos);
            }
        } catch (IOException e) {
            MineCordLogger.logger.error("Failed to load block states", e);
        }
        int startX = this.leftPos + 10;
        int startY = this.topPos + 30;
        int buttonSize = 20;
        int padding = 5;

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                final int r = row, c = col;
                Button button = new Button(startX + (buttonSize + padding) * col, startY + (buttonSize + padding) * row, buttonSize, buttonSize, Component.literal(mineCordButtonStates[r][c].name()), b -> {
                    switch (mineCordButtonStates[r][c]) {
                        case DISABLED:
                            mineCordButtonStates[r][c] = MineCordButtonState.INPUT;
                            break;
                        case INPUT:
                            mineCordButtonStates[r][c] = MineCordButtonState.OUTPUT;
                            break;
                        case OUTPUT:
                            mineCordButtonStates[r][c] = MineCordButtonState.DISABLED;
                            break;
                    }
                    b.setMessage(Component.literal(mineCordButtonStates[r][c].name()));
                }) {
                    @Override
                    public void renderButton(@NotNull PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
                        this.fillGradient(poseStack, this.x, this.y, this.x + this.width, this.y + this.height,
                                switch (mineCordButtonStates[r][c]) {
                                    case INPUT -> 0xFFFF0000;
                                    case OUTPUT -> 0xFF0000FF;
                                    default -> 0xFFC0C0C0;
                                },
                                switch (mineCordButtonStates[r][c]) {
                                    case INPUT -> 0xFFFF0000;
                                    case OUTPUT -> 0xFF0000FF;
                                    default -> 0xFFC0C0C0;
                                });
                        super.renderButton(poseStack, mouseX, mouseY, partialTicks);
                    }
                };
                this.addRenderableWidget(button);
            }
        }

        this.addRenderableWidget(new Button(this.leftPos + 90, this.topPos + 30, 50, 20, Component.literal(signalState ? "ON" : "OFF"), button -> {
            signalState = !signalState;
            button.setMessage(Component.literal(signalState ? "ON" : "OFF"));
        }));

        messageInput = new EditBox(this.font, this.leftPos + 10, this.topPos + 115, 160, 20, Component.literal(message));
        messageInput.setMaxLength(100);
        this.addRenderableWidget(messageInput);

        Button confirmButton = getButton(blockStates, this.menu.getBlockPos());
        this.addRenderableWidget(confirmButton);
    }

    private @NotNull Button getButton(Map<BlockPos, MineCordBlockState> blockStates, BlockPos currentBlockPos) {
        return new Button(this.leftPos + 120, this.topPos + 140, 50, 20, Component.literal("Confirm"), button -> {
            MineCordBlockState blockState = new MineCordBlockState(mineCordButtonStates);
            blockState.setBlockPos(currentBlockPos);
            blockStates.put(currentBlockPos, blockState);
            try {
                MineCordBlockState.saveToFile(blockStates, "blockStates.dat");
            } catch (IOException e) {
                MineCordLogger.logger.error("Failed to save block state", e);
            }
        });
    }

    @Override
    protected void renderBg(@NotNull PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
        renderBackground(poseStack);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE);
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        blit(poseStack, x, y, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    public void render(@NotNull PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        super.render(poseStack, mouseX, mouseY, partialTicks);
        messageInput.render(poseStack, mouseX, mouseY, partialTicks);

        int textColor = 0xFFFFFF;
        this.font.draw(poseStack, "Input/Output", this.leftPos + 10, this.topPos + 20, textColor);
        this.font.draw(poseStack, "Signal When:", this.leftPos + 90, this.topPos + 20, textColor);
        this.font.draw(poseStack, "Message:", this.leftPos + 10, this.topPos + 105, textColor);
    }
}