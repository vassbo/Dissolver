package net.vassbo.vanillaemc;

import java.util.ArrayList;
import java.util.List;

import net.fabricmc.api.ClientModInitializer;
import net.vassbo.vanillaemc.packets.DataReceiver;
import net.vassbo.vanillaemc.packets.PayloadData;

public class VanillaEMCClient implements ClientModInitializer {
	public static List<PayloadData> modPlayerData = new ArrayList<>();

	@Override
	public void onInitializeClient() {		
        DataReceiver.init();
	}
}