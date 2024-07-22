package net.vassbo.vanillaemc.data;

import java.util.HashMap;

import net.minecraft.text.Text;

public class EMCValues {
    public static Integer get(String key) {
        // does this create the data on each call? It should not do that!
        HashMap<String, Integer> EMC_VALUES = new HashMap<String, Integer>();
        
        // auto generate based on crafting recipes!
        EMC_VALUES.put("minecraft:stone", 1);
        EMC_VALUES.put("minecraft:dirt", 1);
        EMC_VALUES.put("minecraft:grass_block", 2);
        EMC_VALUES.put("vanillaemc:magic_block", 9000);
        EMC_VALUES.put("vanillaemc:magic_item", 1000);

        return EMC_VALUES.getOrDefault(key, 0);
    }

    public static Text tooltipValue(String key) {
        Integer EMC = EMCValues.get(key);
        Text text = Text.literal("");
        if (EMC == 0) return text;

        return Text.translatable("item_tooltip.vanillaemc.emc", EMC);
    }

    // public static void initialize() {       
    // }
}