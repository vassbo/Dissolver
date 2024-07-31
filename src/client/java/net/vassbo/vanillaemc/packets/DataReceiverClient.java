package net.vassbo.vanillaemc.packets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.vassbo.vanillaemc.VanillaEMC;
import net.vassbo.vanillaemc.data.PlayerDataClient;
import net.vassbo.vanillaemc.packets.clientbound.PlayerDataPayload;

public class DataReceiverClient {
    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(PlayerDataPayload.ID, (payload, context) -> {
			PlayerEntity player = context.player();
            PlayerDataPayload playerData = payload;

            if (player == null) {
                VanillaEMC.LOGGER.error("Something went wrong! No client player receiver.");
                return;
            }

			receivedData(player, playerData);
        });
    }

	private static void receivedData(PlayerEntity player, PlayerDataPayload playerData) {
		PlayerDataClient.EMC = playerData.emc();
		PlayerDataClient.LEARNED_ITEMS_SIZE = playerData.learnedItemsSize();
		PlayerDataClient.MESSAGE = playerData.message();
	}

	// HELPERS

	public static List<String> stringToList(String stringList) {
		if (stringList.length() == 0) return new ArrayList<>();
		List<String> list = Arrays.asList(stringList.split(";;"));
		// list.remove(list.size() - 1); // last one is always empty (but automatically removed!)

		return list;
	}
}
