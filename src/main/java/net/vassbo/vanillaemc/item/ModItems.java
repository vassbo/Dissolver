package net.vassbo.vanillaemc.item;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.vassbo.vanillaemc.VanillaEMC;

public class ModItems {
    public static final Item CRYSTAL_FRAME_ITEM = registerItem("crystal_frame_item", new CrystalFrameItem(new Item.Settings()));
    // public static final Item GLOWING_ITEM = registerItem("glowing_item", new GlowingItem(new Item.Settings()));

    // private static void addToVanillaTools(FabricItemGroupEntries entries) {
    //     entries.add(MAGIC_ITEM);
    // }

    // HELPERS

	private static Item registerItem(String id, Item item) {
		Identifier itemID = Identifier.of(VanillaEMC.MOD_ID, id);
		Item registeredItem = Registry.register(Registries.ITEM, itemID, item);

		return registeredItem;
    }

    // INITIALIZE

    public static void init() {
        // ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register(ModItems::addToVanillaTools);
    }
}