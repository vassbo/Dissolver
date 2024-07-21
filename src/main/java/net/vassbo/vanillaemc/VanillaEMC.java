package net.vassbo.vanillaemc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ModInitializer;
import net.vassbo.vanillaemc.block.ModBlocks;
import net.vassbo.vanillaemc.command.ModCommands;
import net.vassbo.vanillaemc.event.BlockBreakEvent;
import net.vassbo.vanillaemc.event.JoinEvent;
import net.vassbo.vanillaemc.item.ModItemGroups;
import net.vassbo.vanillaemc.item.ModItems;
import net.vassbo.vanillaemc.packets.SyncHandler;

public class VanillaEMC implements ModInitializer {
	public static final String MOD_ID = "vanillaemc";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing VanillaEMC!");

		SyncHandler.init();

        JoinEvent.init();
		BlockBreakEvent.init();
		
		ModCommands.registerCommands();

		ModItemGroups.registerItemGroups();
		ModItems.registerItems();
		ModBlocks.registerBlocks();
	}
}