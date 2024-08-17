package net.vassbo.vanillaemc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ModInitializer;
import net.vassbo.vanillaemc.block.ModBlocks;
import net.vassbo.vanillaemc.block.entity.ModBlockEntities;
import net.vassbo.vanillaemc.command.ModCommands;
import net.vassbo.vanillaemc.config.ModConfig;
import net.vassbo.vanillaemc.data.EMCValues;
import net.vassbo.vanillaemc.entity.ModEntities;
import net.vassbo.vanillaemc.event.JoinEvent;
import net.vassbo.vanillaemc.helpers.RecipeGenerator;
import net.vassbo.vanillaemc.item.ModItemGroups;
import net.vassbo.vanillaemc.item.ModItems;
import net.vassbo.vanillaemc.packets.DataReceiver;
import net.vassbo.vanillaemc.packets.Packets;
import net.vassbo.vanillaemc.screen.ModScreenHandlers;

public class VanillaEMC implements ModInitializer {
	// NOTE: originally called VanillaEMC, but could not have vanilla in the name on SourceForge, so it's Dissolver now!
	public static final String MOD_ID = "vanillaemc";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// Please use this code as inspiration for your projects! :)
		// For God so loved the world that he gave his one and only Son, that whoever believes in him shall not perish but have eternal life.
		// John 3:16

		LOGGER.info("Initializing Dissolver (VanillaEMC)!");

		ModConfig.init();
		RecipeGenerator.init();

		EMCValues.init();

		Packets.init();
		DataReceiver.init();

        JoinEvent.init();
		
		ModCommands.init();

		ModItemGroups.init();
		ModItems.init();
		ModBlocks.init();

		// ModParticles.init();
		ModEntities.init();

		ModBlockEntities.init();
		ModScreenHandlers.init();

		LOGGER.info("VanillaEMC ready!");
	}
}