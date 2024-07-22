package net.vassbo.vanillaemc;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
// import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
// import net.vassbo.vanillaemc.block.entity.ModBlockEntities;
// import net.vassbo.vanillaemc.block.entity.renderer.MagicBlockRenderer;
import net.vassbo.vanillaemc.packets.DataReceiver;
import net.vassbo.vanillaemc.packets.PayloadData;
import net.vassbo.vanillaemc.screen.MagicScreen;
import net.vassbo.vanillaemc.screen.ModScreenHandlers;

public class VanillaEMCClient implements ClientModInitializer {
	public static PayloadData modPlayerData = null;

	@Override
	public void onInitializeClient() {
		DataReceiver.init();
		
        // ModScreenHandlers.registerScreenHandlers();
        HandledScreens.register(ModScreenHandlers.MAGIC_SCREEN_HANDLER, MagicScreen::new);

        // BlockEntityRendererFactories.register(ModBlockEntities.MAGIC_BLOCK_ENTITY, MagicBlockRenderer::new);
	}
}