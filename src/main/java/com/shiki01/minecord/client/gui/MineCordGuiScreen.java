package com.shiki01.minecord.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.shiki01.minecord.MineCordContainer;
import com.shiki01.minecord.util.MineCordBlockState;
import com.shiki01.minecord.util.MineCordLogger;
import net.minecraft.client.gui.GuiComponent;
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
import java.util.HashMap;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class MineCordGuiScreen extends AbstractContainerScreen<MineCordContainer> {
    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation("minecord", "textures/gui/background.png");
    private EditBox messageInput;
    private String message = "";
    private boolean signalState = false;
    private MineCordButtonState[][] mineCordButtonStates = new MineCordButtonState[3][3];

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

        Map<BlockPos, MineCordBlockState> blockStates;
        try {
            blockStates = MineCordBlockState.loadFromFile("blockStates.dat");
        } catch (IOException | ClassNotFoundException e) {
            blockStates = new HashMap<>();
            MineCordLogger.logger.error("Failed to load block states", e);
        }

        BlockPos currentBlockPos = this.menu.getBlockPos();

        MineCordBlockState currentBlockState = new MineCordBlockState(mineCordButtonStates, signalState, message);
        currentBlockState.codeBlockStatus(currentBlockPos, blockStates);

        this.message = currentBlockState.getMessage();
        this.mineCordButtonStates = currentBlockState.getStates();
        this.signalState = currentBlockState.getSignalState();

        initializeGuiComponents();

        BlockPos blockPos = this.menu.getBlockPos();

        Button confirmButton = getButton(blockStates, blockPos);
        this.addRenderableWidget(confirmButton);
    }

    private static final int COLOR_DISABLED = 0xFFC0C0C0;
    private static final int COLOR_INPUT = 0xFFFF0000;
    private static final int COLOR_OUTPUT = 0xFF0000FF;

    private void initializeGuiComponents() {
        int startX = this.leftPos + 10;
        int startY = this.topPos + 30;
        int buttonSize = 20;
        int padding = 5;

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                final int r = row, c = col;
                Button button = new Button(startX + (buttonSize + padding) * col, startY + (buttonSize + padding) * row, buttonSize, buttonSize, Component.literal(""), b -> {
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
                }) {
                    @Override
                    public void renderButton(@NotNull PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
                        int color = switch (mineCordButtonStates[r][c]) {
                            case INPUT -> COLOR_INPUT;
                            case OUTPUT -> COLOR_OUTPUT;
                            default -> COLOR_DISABLED;
                        };
                        fill(poseStack, this.x, this.y, this.x + this.width, this.y + this.height, color);
                        if (this.isHoveredOrFocused()) {
                            drawCenteredString(poseStack, MineCordGuiScreen.this.font, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, 0xFFFFFF);
                        }
                        if (isHoveredOrFocused()) {
                            renderTooltip(poseStack, Component.literal(mineCordButtonStates[r][c].name()), mouseX, mouseY);
                        }
                    }
                };
                this.addRenderableWidget(button);
            }
        }

        this.addRenderableWidget(new Button(this.leftPos + 90, this.topPos + 30, 50, 20, Component.literal(signalState ? "ON" : "OFF"), button -> {
            signalState = !signalState;
            button.setMessage(Component.literal(signalState ? "ON" : "OFF"));
        }));

        messageInput = new EditBox(this.font, this.leftPos + 10, this.topPos + 115, 160, 20, Component.literal(""));
        messageInput.setValue(this.message);
        messageInput.setMaxLength(100);
        this.addRenderableWidget(messageInput);
    }

    private @NotNull Button getButton(Map<BlockPos, MineCordBlockState> blockStates, BlockPos currentBlockPos) {
        return new Button(this.leftPos + 120, this.topPos + 140, 50, 20, Component.literal("Confirm"), button -> {
            MineCordBlockState currentBlockState = new MineCordBlockState(mineCordButtonStates, signalState, messageInput.getValue());
            blockStates.put(currentBlockPos, currentBlockState);
            try {
                MineCordBlockState.saveToFile(blockStates, "blockStates.dat");
            } catch (IOException e) {
                MineCordLogger.logger.error("Failed to save block states", e);
            }
            onClose();
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
    protected void renderTooltip(@NotNull PoseStack poseStack, int mouseX, int mouseY) {
        super.renderTooltip(poseStack, mouseX, mouseY);
    }

    @Override
    public void render(@NotNull PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        super.render(poseStack, mouseX, mouseY, partialTicks);
        messageInput.render(poseStack, mouseX, mouseY, partialTicks);

        int textColor = 0x333333;
        this.font.draw(poseStack, "Input/Output", this.leftPos + 10, this.topPos + 20, textColor);
        this.font.draw(poseStack, "Signal When:", this.leftPos + 90, this.topPos + 20, textColor);
        this.font.draw(poseStack, "Message:", this.leftPos + 10, this.topPos + 105, textColor);
        this.renderTooltip(poseStack, mouseX, mouseY);
    }
}