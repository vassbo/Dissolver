package net.vassbo.vanillaemc.overlay;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import net.vassbo.vanillaemc.data.PlayerDataClient;

public class DrawText {
	private float totalTickDelta = 0.0F;

    public DrawText() {
		HudRenderCallback.EVENT.register((context, tickDeltaManager) -> {
			int color = 0xFFFF0000; // Red
			int targetColor = 0xFF00FF00; // Green

			// Total tick delta is stored in a field, so we can use it later.
			totalTickDelta += tickDeltaManager.getTickDelta(true);

			// "lerp" simply means "linear interpolation", which is a fancy way of saying "blend".
			float lerpedAmount = MathHelper.abs(MathHelper.sin(totalTickDelta / 50F));
			int lerpedColor = ColorHelper.Argb.lerp(lerpedAmount, color, targetColor);

			// Draw a square with the lerped color.
			// x1, x2, y1, y2, z, color
			// context.fill(0, 0, 100, 100, 0, lerpedColor);

			MinecraftClient client = MinecraftClient.getInstance();
			if (client.textRenderer == null) return;

			TextRenderer renderer = client.textRenderer;
			
            int emc = PlayerDataClient.EMC;

			context.drawText(renderer, "EMC: §6" + emc, color, targetColor, lerpedColor, false);

			// int color = 0xFFFF0000; // Red
			// int targetColor = 0xFF00FF00; // Green
			// Matrix4f matrix = new Matrix4f();
			// // VertexConsumerProvider vertexConsumers = VertexConsumerProvider.immediate(null);
			
			// // Matrix4f matrix4f = Rotation3.identity().getMatrix();
			// // VertexConsumerProvider.Immediate immediate = RenderUtil.getVertexConsumer();
			// BufferAllocator buffer = new BufferAllocator(10);
			// VertexConsumerProvider vertexConsumers = VertexConsumerProvider.immediate(buffer);
			
			// TextLayerType layerType = TextLayerType.NORMAL;
			// // String text, float x, float y, int color, boolean shadow, Matrix4f matrix, VertexConsumerProvider vertexConsumers, TextLayerType layerType, int backgroundColor, int light
			// int emc = 0;
			// if (modPlayerData != null) emc = modPlayerData.emc();
			// client.textRenderer.draw("EMC: §6" + emc, 10.0F, 10.0F, color, true, matrix, vertexConsumers, layerType, 1, 1);
		});
    }
}
