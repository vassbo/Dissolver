package net.vassbo.vanillaemc;

import java.util.HashMap;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ModInitializer;
import net.vassbo.vanillaemc.block.ModBlocks;
import net.vassbo.vanillaemc.block.entity.ModBlockEntities;
import net.vassbo.vanillaemc.command.ModCommands;
import net.vassbo.vanillaemc.data.EMCValues;
import net.vassbo.vanillaemc.event.JoinEvent;
import net.vassbo.vanillaemc.item.ModItemGroups;
import net.vassbo.vanillaemc.item.ModItems;
import net.vassbo.vanillaemc.packets.DataReceiver;
import net.vassbo.vanillaemc.packets.Packets;
import net.vassbo.vanillaemc.screen.MagicScreenHandler;
import net.vassbo.vanillaemc.screen.ModScreenHandlers;

public class VanillaEMC implements ModInitializer {
	public static final String MOD_ID = "vanillaemc";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static HashMap<UUID, MagicScreenHandler> activeHandlers = new HashMap<>();

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing VanillaEMC!");

		// ServerLifecycleEvents.SERVER_STARTING.register((server) -> {});
		EMCValues.init();

		Packets.register();
		DataReceiver.init();

        JoinEvent.init();
		// BlockBreakEvent.init();
		
		ModCommands.registerCommands();

		ModItemGroups.registerItemGroups();
		ModItems.registerItems();
		ModBlocks.registerBlocks();

		ModBlockEntities.registerBlockEntities();
		ModScreenHandlers.registerScreenHandlers();
	}
}