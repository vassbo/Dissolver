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
import net.vassbo.vanillaemc.entity.ModEntities;
import net.vassbo.vanillaemc.event.JoinEvent;
import net.vassbo.vanillaemc.item.ModItemGroups;
import net.vassbo.vanillaemc.item.ModItems;
import net.vassbo.vanillaemc.packets.DataReceiver;
import net.vassbo.vanillaemc.packets.Packets;
import net.vassbo.vanillaemc.screen.DissolverScreenHandler;
import net.vassbo.vanillaemc.screen.ModScreenHandlers;

public class VanillaEMC implements ModInitializer {
	public static final String MOD_ID = "vanillaemc";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static HashMap<UUID, DissolverScreenHandler> activeHandlers = new HashMap<>();

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing VanillaEMC!");

		EMCValues.init();

		Packets.init();
		DataReceiver.init();

        JoinEvent.init();
		// BlockBreakEvent.init();
		
		ModCommands.init();

		ModItemGroups.init();
		ModItems.init();
		ModBlocks.init();

		// ModParticles.init();
		ModEntities.init();

		ModBlockEntities.init();
		ModScreenHandlers.init();
	}
}