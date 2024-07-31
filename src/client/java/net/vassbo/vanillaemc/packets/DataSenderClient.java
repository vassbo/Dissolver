package net.vassbo.vanillaemc.packets;

import java.util.List;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.vassbo.vanillaemc.packets.serverbound.ClientPayload;

public class DataSenderClient {
	public static void sendDataToServer(String messageId, String data) {
        ClientPayload payload = new ClientPayload(messageId, data);
		ClientPlayNetworking.send(payload);
        // client.execute(() -> ClientPlayNetworking.send(payload));
	}

	// HELPERS

    public static String listToString(List<String> items) {
        String stringList = "";

        for (String itemId : items) {
            stringList += itemId + ";;";
        }
        
        return stringList;
    }
}
