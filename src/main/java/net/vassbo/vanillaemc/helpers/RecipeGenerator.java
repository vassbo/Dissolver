package net.vassbo.vanillaemc.helpers;

import java.util.ArrayList;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.util.Identifier;
import net.vassbo.vanillaemc.VanillaEMC;
import net.vassbo.vanillaemc.config.ModConfig;

public class RecipeGenerator {
    public static JsonObject DISSOLVER_RECIPE;

    public static void init() {
        String craftingDifficulty = ModConfig.DIFFICULTY.toLowerCase();
        // hard
        Identifier frameItem = Identifier.of(VanillaEMC.MOD_ID, "crystal_frame_item");
        Identifier centerItem = Identifier.ofVanilla("nether_star");

        if (craftingDifficulty.contains("easy")) {
            frameItem = Identifier.ofVanilla("glass_pane");
            centerItem = Identifier.ofVanilla("redstone");
        } else if (craftingDifficulty.contains("normal")) {
            centerItem = Identifier.ofVanilla("phantom_membrane");
        }

        DISSOLVER_RECIPE = createShapedRecipeJson(
            Lists.newArrayList('C', '#'),
            Lists.newArrayList(frameItem, centerItem),
            Lists.newArrayList("item", "item"),
            Lists.newArrayList(
                "CCC",
                "C#C",
                "CCC"
            ),
            Identifier.of(VanillaEMC.MOD_ID, "dissolver_block")
        );
    }

    // source: https://fabricmc.net/wiki/tutorial:dynamic_recipe_generation
    public static JsonObject createShapedRecipeJson(ArrayList<Character> keys, ArrayList<Identifier> items, ArrayList<String> type, ArrayList<String> pattern, Identifier output) {
        JsonObject json = new JsonObject();
        json.addProperty("type", "minecraft:crafting_shaped");

        JsonArray jsonArray = new JsonArray();
        jsonArray.add(pattern.get(0));
        jsonArray.add(pattern.get(1));
        jsonArray.add(pattern.get(2));
        json.add("pattern", jsonArray);

        JsonObject individualKey;
        JsonObject keyList = new JsonObject();

        for (int i = 0; i < keys.size(); ++i) {
            individualKey = new JsonObject();
            individualKey.addProperty(type.get(i), items.get(i).toString());
            keyList.add(keys.get(i) + "", individualKey);
        }

        json.add("key", keyList);

        JsonObject result = new JsonObject();
        result.addProperty("id", output.toString());
        result.addProperty("count", 1);
        json.add("result", result);

        return json;
    }

}
