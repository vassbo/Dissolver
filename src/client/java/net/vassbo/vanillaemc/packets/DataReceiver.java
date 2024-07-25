package net.vassbo.vanillaemc.packets;

import java.util.List;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.vassbo.vanillaemc.VanillaEMC;
import net.vassbo.vanillaemc.VanillaEMCClient;

public class DataReceiver {
    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(SyncHandler.SyncPayload.ID, (payload, context) -> {
			PlayerEntity player = context.player();
            List<PayloadData> playerData = payload.value();

            if (player == null) {
                VanillaEMC.LOGGER.error("Something went wrong! No client player receiver.");
                return;
            }

			receivedData(player, playerData);
        });
    }

	private static void receivedData(PlayerEntity player, List<PayloadData> playerData) {
		VanillaEMCClient.modPlayerData = playerData.get(0);

		if (playerData.isEmpty()) {
			player.sendMessage(Text.literal("Empty!"));
			return;
		}

		// player.sendMessage(Text.literal("Current EMC: §6" + playerData.get(0).emc()));
	}
}
