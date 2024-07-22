package net.vassbo.vanillaemc;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ModInitializer;
import net.minecraft.item.Item;
import net.vassbo.vanillaemc.block.ModBlocks;
import net.vassbo.vanillaemc.block.entity.ModBlockEntities;
import net.vassbo.vanillaemc.command.ModCommands;
import net.vassbo.vanillaemc.data.EMCValues;
import net.vassbo.vanillaemc.event.BlockBreakEvent;
import net.vassbo.vanillaemc.event.JoinEvent;
import net.vassbo.vanillaemc.item.ModItemGroups;
import net.vassbo.vanillaemc.item.ModItems;
import net.vassbo.vanillaemc.packets.SyncHandler;
import net.vassbo.vanillaemc.screen.ModScreenHandlers;

public class VanillaEMC implements ModInitializer {
	public static final String MOD_ID = "vanillaemc";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static List<Item> LEARNED_ITEMS = new ArrayList<>();

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing VanillaEMC!");

		EMCValues.init();

		LEARNED_ITEMS.add(ModItems.MAGIC_ITEM);
		for (int i = 1; i < 10; i++) {
			LEARNED_ITEMS.add(Item.byRawId(i));
		}

		SyncHandler.init();

        JoinEvent.init();
		BlockBreakEvent.init();
		
		ModCommands.registerCommands();

		ModItemGroups.registerItemGroups();
		ModItems.registerItems();
		ModBlocks.registerBlocks();

		ModBlockEntities.registerBlockEntities();
		ModScreenHandlers.registerScreenHandlers();
	}
}