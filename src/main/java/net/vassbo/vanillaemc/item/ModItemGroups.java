package net.vassbo.vanillaemc.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroup.DisplayContext;
import net.minecraft.item.ItemGroup.Entries;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.vassbo.vanillaemc.VanillaEMC;
import net.vassbo.vanillaemc.block.ModBlocks;

public class ModItemGroups {
    public static final ItemGroup VANILLAEMC_GROUP = registerItemGroup("vanillaemc_group", "vanillaemc.modname", ModItems.CRYSTAL_FRAME_ITEM, ModItemGroups::addToCustomInventory);

    private static void addToCustomInventory(DisplayContext displayContext, Entries entries) {
        entries.add(ModItems.CRYSTAL_FRAME_ITEM);
        entries.add(ModBlocks.DISSOLVER_BLOCK);
    }

    // HELPERS

	private static ItemGroup registerItemGroup(String id, String name, Item icon, ItemGroup.EntryCollector entryList) {
		return Registry.register(
            Registries.ITEM_GROUP,
            Identifier.of(VanillaEMC.MOD_ID, id),
            FabricItemGroup.builder()
            .displayName(Text.translatable(name))
            .icon(() -> new ItemStack(icon))
            .entries(entryList)
            .build()
        );
    }

    // INITIALIZE

    public static void init() {
    }
}
