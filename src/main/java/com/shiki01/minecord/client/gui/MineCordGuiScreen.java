package com.shiki01.minecord.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.shiki01.minecord.MineCordContainer;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

enum ButtonState {
    INPUT, OUTPUT, DISABLED
}

@OnlyIn(Dist.CLIENT)
public class MineCordGuiScreen extends AbstractContainerScreen<MineCordContainer> {
    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation("minecord", "textures/gui/background.png");
    private EditBox messageInput;
    private boolean signalState = false;
    private final ButtonState[][] buttonStates = new ButtonState[3][3];

    public MineCordGuiScreen(MineCordContainer container, Inventory inv, Component title) {
        super(container, inv, title);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttonStates[i][j] = ButtonState.DISABLED;
            }
        }
    }

    @Override
    protected void init() {
        super.init();
        int startX = this.leftPos + 10;
        int startY = this.topPos + 30;
        int buttonSize = 20;
        int padding = 5;

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                final int r = row, c = col;
                Button button = new Button(startX + (buttonSize + padding) * col, startY + (buttonSize + padding) * row, buttonSize, buttonSize, Component.literal(""), (b) -> {
                    switch (buttonStates[r][c]) {
                        case DISABLED:
                            buttonStates[r][c] = ButtonState.INPUT;
                            break;
                        case INPUT:
                            buttonStates[r][c] = ButtonState.OUTPUT;
                            break;
                        case OUTPUT:
                            buttonStates[r][c] = ButtonState.DISABLED;
                            break;
                    }
                    b.setMessage(Component.literal(buttonStates[r][c].name()));
                }) {
                    @Override
                    public void renderButton(@NotNull PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
                        this.fillGradient(poseStack, this.x, this.y, this.x + this.width, this.y + this.height,
                                switch (buttonStates[r][c]) {
                                    case INPUT -> 0xFFFF0000;
                                    case OUTPUT -> 0xFF0000FF;
                                    default -> 0xFFC0C0C0;
                                },
                                switch (buttonStates[r][c]) {
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

        this.addRenderableWidget(new Button(this.leftPos + 120, this.topPos + 30, 50, 20, Component.literal("ON"), button -> {
            signalState = !signalState;
            button.setMessage(Component.literal(signalState ? "ON" : "OFF"));
        }));

        messageInput = new EditBox(this.font, this.leftPos + 10, this.topPos + 115, 160, 20, Component.literal(""));
        messageInput.setMaxLength(100);
        this.addRenderableWidget(messageInput);
        Button confirmButton = new Button(this.leftPos + 120, this.topPos + 140, 50, 20, Component.literal("Confirm"), button -> {
        });
        this.addRenderableWidget(confirmButton);
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