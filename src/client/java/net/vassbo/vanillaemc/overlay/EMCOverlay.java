package net.vassbo.vanillaemc.overlay;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;
import net.vassbo.vanillaemc.config.ModConfig;
import net.vassbo.vanillaemc.data.PlayerDataClient;

public class EMCOverlay implements HudRenderCallback {
    private static final int WHITE_COLOR = 0xFFFFFF;

    public static void init() {
        HudRenderCallback.EVENT.register(new EMCOverlay());
    }

    @Override
    public void onHudRender(DrawContext drawContext, RenderTickCounter tickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();

        if (client == null) return;
        if (client.inGameHud.getDebugHud().shouldShowDebugHud() || client.inGameHud.getSpectatorHud().isOpen() || client.options.hudHidden) return;

        if (ModConfig.EMC_ON_HUD == false) return;

        // String emc = NumberHelpers.format(PlayerDataClient.EMC);
        String emc = String.valueOf(PlayerDataClient.EMC); // show full amount when on HUD
        Text text = Text.translatable("emc.title", emc);
        drawContext.drawText(client.textRenderer, text, 4, 4, WHITE_COLOR, false);
    }
}
