package net.vassbo.vanillaemc.event;

import java.util.List;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.vassbo.vanillaemc.data.PlayerData;
import net.vassbo.vanillaemc.data.StateSaverAndLoader;
import net.vassbo.vanillaemc.packets.PayloadData;
import net.vassbo.vanillaemc.packets.SyncHandler;
import net.vassbo.vanillaemc.packets.SyncHandler.SyncPayload;

public class JoinEvent {
    public static void init() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			ServerPlayerEntity playerEntity = handler.getPlayer();
            PlayerData playerState = StateSaverAndLoader.getPlayerState(playerEntity);

            List<PayloadData> dataToSend = PayloadData.create(playerState);
			SyncPayload payload = new SyncPayload(dataToSend);

            server.execute(() -> {
				SyncHandler.send(playerEntity, payload, handler);
            });
        });
    }
}
