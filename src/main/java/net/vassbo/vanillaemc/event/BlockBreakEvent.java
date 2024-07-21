package net.vassbo.vanillaemc.event;

import java.util.List;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.vassbo.vanillaemc.VanillaEMC;
import net.vassbo.vanillaemc.data.PlayerData;
import net.vassbo.vanillaemc.data.StateSaverAndLoader;
import net.vassbo.vanillaemc.packets.PayloadData;
import net.vassbo.vanillaemc.packets.SyncHandler;
import net.vassbo.vanillaemc.packets.SyncHandler.SyncPayload;

public class BlockBreakEvent {
    public static void init() {
        PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, entity) -> {
            if (state.getBlock() == Blocks.GRASS_BLOCK || state.getBlock() == Blocks.DIRT) {
				VanillaEMC.LOGGER.info("BREAK DIRT");
				
                // StateSaverAndLoader serverState = StateSaverAndLoader.getServerState(world.getServer());
                // serverState.totalDirtBlocksBroken += 1;
				
                PlayerData playerState = StateSaverAndLoader.getPlayerState(player);
                playerState.emc += 1;

				MinecraftServer server = world.getServer();

				ServerPlayerEntity playerEntity = server.getPlayerManager().getPlayer(player.getUuid());

                List<PayloadData> dataToSend = PayloadData.create(playerState);
                SyncPayload payload = new SyncPayload(dataToSend);

				server.execute(() -> {
					SyncHandler.send(playerEntity, payload);
				});
			}
		});
    }
}
