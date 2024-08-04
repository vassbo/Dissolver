package net.vassbo.vanillaemc;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.particle.EndRodParticle;
import net.minecraft.client.render.RenderLayer;
import net.vassbo.vanillaemc.block.ModBlocks;
import net.vassbo.vanillaemc.entity.ModEntities;
import net.vassbo.vanillaemc.overlay.EMCOverlay;
import net.vassbo.vanillaemc.packets.DataReceiverClient;
import net.vassbo.vanillaemc.particle.ModParticles;
import net.vassbo.vanillaemc.render.CrystalEntityRenderer;
import net.vassbo.vanillaemc.screen.ClientScreenHandlers;

public class VanillaEMCClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		DataReceiverClient.init();
		
        ClientScreenHandlers.registerScreenHandlers();

		// transparent
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.DISSOLVER_BLOCK, RenderLayer.getTranslucent());

		EntityRendererRegistry.register(ModEntities.CRYSTAL_ENTITY, CrystalEntityRenderer::new);

		// particle
		ParticleFactoryRegistry.getInstance().register(ModParticles.CRYSTAL, EndRodParticle.Factory::new);

		EMCOverlay.init();
	}
}