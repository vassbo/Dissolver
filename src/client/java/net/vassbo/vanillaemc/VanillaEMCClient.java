package net.vassbo.vanillaemc;

import net.fabricmc.api.ClientModInitializer;
import net.vassbo.vanillaemc.packets.DataReceiverClient;
import net.vassbo.vanillaemc.screen.ClientScreenHandlers;

public class VanillaEMCClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		DataReceiverClient.init();
		
        ClientScreenHandlers.registerScreenHandlers();
	}
}