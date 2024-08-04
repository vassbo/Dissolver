package net.vassbo.vanillaemc.helpers;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class ItemHelper {
    public static Item getById(String itemId) {
        return Registries.ITEM.get(identifierById(itemId));
    }

    public static String getName(Item item) {
        return item.getName().getString();
    }

    public static double getDurabilityPercentage(ItemStack stack) {
        // reduce EMC value based on current item durability
        float MAX_DURABILITY = stack.getMaxDamage();
        float CURRENT_DURABILITY = MAX_DURABILITY - stack.getDamage();
        return MAX_DURABILITY == 0 ? 1 : CURRENT_DURABILITY / MAX_DURABILITY;
    }

    // HELPERS

    public static Identifier identifierById(String fullItemId) {
        String[] parts = fullItemId.split(":");
        String modId = parts[0];
        String itemId = parts[1];

        return Identifier.of(modId, itemId);
    }
}
