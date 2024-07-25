package net.vassbo.vanillaemc.event;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.vassbo.vanillaemc.helpers.EMCHelper;

public class JoinEvent {
    public static void init() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			ServerPlayerEntity playerEntity = handler.getPlayer();
            EMCHelper.sendStateToClient(playerEntity);
        });
    }
}
