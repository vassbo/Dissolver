package net.vassbo.vanillaemc.packets;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.vassbo.vanillaemc.data.PlayerData;
import net.vassbo.vanillaemc.packets.clientbound.PlayerDataPayload;

public class DataSender {
    public static void sendPlayerData(PlayerEntity player, PlayerData data) {
        MinecraftServer server = player.getServer();
        ServerPlayerEntity playerEntity = getServerPlayer(server, player);
        if (playerEntity == null) return;

        PlayerDataPayload dataToSend = new PlayerDataPayload(data.EMC, data.LEARNED_ITEMS.size(), data.MESSAGE);

        server.execute(() -> {
            ServerPlayNetworking.send(playerEntity, dataToSend);
        });
    }

    // HELPERS

    private static ServerPlayerEntity getServerPlayer(MinecraftServer server, PlayerEntity player) {
        if (player instanceof ServerPlayerEntity) return (ServerPlayerEntity)player;
        if (server == null || !(player instanceof PlayerEntity)) return null;

        ServerPlayerEntity playerEntity = server.getPlayerManager().getPlayer(player.getUuid());
        if (!(playerEntity instanceof ServerPlayerEntity)) return null;

        return playerEntity;
    }

    // private static String listToString(List<String> items) {
    //     String stringList = "";

    //     for (String itemId : items) {
    //         stringList += itemId + ";;";
    //     }
        
    //     return stringList;
    // }
}
