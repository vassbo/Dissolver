package net.vassbo.vanillaemc.event;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.Blocks;
import net.vassbo.vanillaemc.VanillaEMC;
import net.vassbo.vanillaemc.data.PlayerData;
import net.vassbo.vanillaemc.data.StateSaverAndLoader;
import net.vassbo.vanillaemc.helpers.EMCHelper;

public class BlockBreakEvent {
    public static void init() {
        PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, entity) -> {
            if (state.getBlock() == Blocks.GRASS_BLOCK || state.getBlock() == Blocks.DIRT) {
				VanillaEMC.LOGGER.info("BREAK DIRT");
				
                PlayerData playerState = StateSaverAndLoader.getPlayerState(player);
                playerState.EMC += 1;

                EMCHelper.sendStateToClient(player);
			}
		});
    }
}
