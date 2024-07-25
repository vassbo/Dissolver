package net.vassbo.vanillaemc.data;

import java.util.HashMap;
import java.util.Set;

import net.minecraft.text.Text;

public class EMCValues {
    private static final HashMap<String, Integer> EMC_VALUES = new HashMap<String, Integer>();

    public static void init() {
        EMC_VALUES.put("minecraft:stone", 1);
        EMC_VALUES.put("minecraft:dirt", 1);
        EMC_VALUES.put("minecraft:grass_block", 2);
        EMC_VALUES.put("minecraft:diamond", 1800);
        EMC_VALUES.put("minecraft:diamond_block", 1800 * 9);
        EMC_VALUES.put("minecraft:gold_ingot", 800);
        EMC_VALUES.put("minecraft:gold_block", 800 * 9);
        EMC_VALUES.put("vanillaemc:magic_block", 9000);
        EMC_VALUES.put("vanillaemc:magic_item", 1000);
        
        // TODO: auto generate based on crafting recipes!
    }

    public static Integer get(String key) {
        return EMC_VALUES.getOrDefault(key, 0);
    }

    public static Set<String> getList() {
        return EMC_VALUES.keySet();
    }

    public static Text tooltipValue(String key) {
        Integer EMC = EMCValues.get(key);
        Text text = Text.literal("");
        if (EMC == 0) return text;

        return Text.translatable("item_tooltip.vanillaemc.emc", EMC);
    }
}
