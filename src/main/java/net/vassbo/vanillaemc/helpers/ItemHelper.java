package net.vassbo.vanillaemc.helpers;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class ItemHelper {
    public static Item getById(String fullItemId) {
        String[] parts = fullItemId.split(":");
        String modId = parts[0];
        String itemId = parts[1];
        Identifier identifier = Identifier.of(modId, itemId);

        return Registries.ITEM.get(identifier);
    }

    public static String getName(Item item) {
        return item.getName().getString();
    }
}
