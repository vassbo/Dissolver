package net.vassbo.vanillaemc;

import net.fabricmc.api.ClientModInitializer;
import net.vassbo.vanillaemc.packets.DataReceiver;
import net.vassbo.vanillaemc.packets.PayloadData;
import net.vassbo.vanillaemc.screen.ClientScreenHandlers;

public class VanillaEMCClient implements ClientModInitializer {
	public static PayloadData modPlayerData = null;

	@Override
	public void onInitializeClient() {
		DataReceiver.init();
		
        ClientScreenHandlers.registerScreenHandlers();
	}

	public static int getEMCValue() {
		if (VanillaEMCClient.modPlayerData == null) return 0;
		return VanillaEMCClient.modPlayerData.emc();
	}
}