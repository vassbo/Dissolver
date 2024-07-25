package net.vassbo.vanillaemc.screen;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.vassbo.vanillaemc.VanillaEMC;
import net.vassbo.vanillaemc.VanillaEMCClient;

public class MagicScreen extends HandledScreen<MagicScreenHandler> {
    private static final Identifier TEXTURE = Identifier.of(VanillaEMC.MOD_ID, "textures/gui/magic_block_gui.png");

    public MagicScreen(MagicScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }    

    @Override
    protected void init() {
        super.init();
        // titleY = 1000;
        // playerInventoryTitleY = 1000;
        this.titleX = (this.backgroundWidth - this.textRenderer.getWidth(this.title)) / 2;
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;

        context.drawTexture(TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        context.drawText(this.textRenderer, this.title, this.titleX, this.titleY, 4210752, false);
        context.drawText(this.textRenderer, this.playerInventoryTitle, this.playerInventoryTitleX, this.playerInventoryTitleY, 4210752, false);

        renderText(context, this.titleX, this.titleY + 15);
    }

    private void renderText(DrawContext context, int x, int y) {
        int emc = VanillaEMCClient.getEMCValue();
        context.drawText(this.textRenderer, "EMC: §6" + emc, x, y, 4210752, false);
    }
}
