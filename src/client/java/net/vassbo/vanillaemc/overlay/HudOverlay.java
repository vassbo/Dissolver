package net.vassbo.vanillaemc.overlay;

import com.mojang.blaze3d.systems.RenderSystem;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.vassbo.vanillaemc.VanillaEMC;

public class HudOverlay implements HudRenderCallback {

    @Override
    public void onHudRender(DrawContext drawContext, RenderTickCounter tickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();

        if (client == null) return;

        VanillaEMC.LOGGER.info("DEBUgHUD: " + client.getDebugHud());
        // if (client.getDebugHud() || )
        if (client.options.hudHidden) return;

        // !mc.getDebugOverlay().showDebugScreen() && !mc.options.hideGui
        // mc.level != null && mc.level.isLoaded(pos)

        int x = 0;
        int y = 0;
        int width = client.getWindow().getScaledWidth();
        int height = client.getWindow().getScaledHeight();

        x = width / 2;
        y = height;

        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        // RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        // RenderSystem.drawTe
        VanillaEMC.LOGGER.info("POS: " + x + y);
    }
}
