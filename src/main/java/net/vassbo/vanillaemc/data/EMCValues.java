package net.vassbo.vanillaemc.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.OptionalDouble;
import java.util.Set;

import org.spongepowered.asm.mixin.injection.struct.InjectorGroupInfo.Map;

import net.vassbo.vanillaemc.VanillaEMC;
import net.vassbo.vanillaemc.helpers.ItemHelper;

public class EMCValues {
    private static final HashMap<String, Integer> EMC_VALUES = new HashMap<String, Integer>();
    public static final HashMap<String, Integer> EMC_TAG_VALUES = new HashMap<String, Integer>();

    public static Integer get(String key) {
        return EMC_VALUES.getOrDefault(key, 0);
    }

    public static Set<String> getList() {
        return EMC_VALUES.keySet();
    }

    public static void init() {
        // Some of these can be crafted/smelted etc. into other items.
        // This is automatically queried.

        EMC_VALUES.put("minecraft:cobblestone", 1);
        EMC_VALUES.put("minecraft:dirt", 1);
        EMC_VALUES.put("minecraft:grass_block", 2);
        EMC_VALUES.put("minecraft:diamond_ore", 1800);
        EMC_VALUES.put("minecraft:gold_ore", 800);
        EMC_VALUES.put("minecraft:nether_star", 10000);
        EMC_VALUES.put("vanillaemc:magic_item", 1000);
        
        // BY TAGS
        EMC_TAG_VALUES.put("minecraft:terracotta", 1000);
        EMC_TAG_VALUES.put("minecraft:logs", 10);
    }

    private static boolean tags_loaded = false;
    public static void tagsLoaded(HashMap<String, Integer> NEW_EMC_VALUES) {
        EMC_VALUES.putAll(NEW_EMC_VALUES);
        tags_loaded = true;
        if (tags_loaded && !RECIPES.isEmpty()) queryRecipes(RECIPES);
    }

    private static HashMap<String, List<String>> RECIPES = new HashMap<String, List<String>>();
    public static void recipesLoaded(HashMap<String, List<String>> recipes) {
        RECIPES = recipes;
        if (tags_loaded && !RECIPES.isEmpty()) queryRecipes(RECIPES);
    }

    public static void queryRecipes(HashMap<String, List<String>> RECIPES) {
        VanillaEMC.LOGGER.info("Searching through " + RECIPES.size() + " recipes!");
        for (Map.Entry<String, List<String>> recipe : RECIPES.entrySet()) {
            checkRecipe(recipe);
        }

        // brute force! (i'm sure there's a more optimized way)
        if (COMPLETED.size() < RECIPES.size()) queryRecipes(RECIPES);
        else {
            VanillaEMC.LOGGER.info("Loaded EMC values for " + RECIPE_ITEM_VALUES.size() + " recipes!");
            RECIPE_ITEM_VALUES.forEach((resultId, emcValues) -> {
                int emcValue = getAverage(emcValues);
                if (emcValues.size() > 1) VanillaEMC.LOGGER.info("FOUND ITEM WITH MULTIPLE RECIPES (" + ItemHelper.getName(ItemHelper.getById(resultId)) + "), SETTING AVERAGE: " + emcValue + " " + emcValues);
                EMC_VALUES.put(resultId, emcValue);
            });

            // LOG ITEMS WITH MISSING EMC - that does not have a crafting recipe!
            for (String missing : MISSING) {
                if (!COMPLETED.contains(missing)) VanillaEMC.LOGGER.info("FOUND ITEM WITH NO RECIPE OR EMC: " + ItemHelper.getName(ItemHelper.getById(missing)) + " (" +missing+ ")");
            }
        }
    }

    private static int getAverage(List<Integer> list) {
        OptionalDouble average = list
            .stream()
            .mapToDouble(a -> a)
            .average();

        return (int)(average.isPresent() ? average.getAsDouble() : 0); 
    }

    private static final List<String> COMPLETED = new ArrayList<String>();
    private static final List<String> MISSING = new ArrayList<String>();
    private static final HashMap<String, Integer> TRIED = new HashMap<String, Integer>();
    private static final HashMap<String, List<Integer>> RECIPE_ITEM_VALUES = new HashMap<String, List<Integer>>();
    private static void checkRecipe(Map.Entry<String, List<String>> recipe) {
        String id = recipe.getKey();
        if (COMPLETED.contains(id)) return;

        // brute force query
        if (!TRIED.containsKey(id)) TRIED.put(id, 0);
        TRIED.replace(id, TRIED.get(id) + 1);

        if (TRIED.get(id) > 50) {
            VanillaEMC.LOGGER.info("Could not get item EMC: " + ItemHelper.getName(ItemHelper.getById(id.split("__")[0])));
            COMPLETED.add(id);
            return;
        }

        List<String> ingredients = recipe.getValue();
        int totalInputEMC = combineEMC(ingredients);
        if (totalInputEMC == 0) return; // try again!

        COMPLETED.add(id);

        String[] parts = id.split("__");
        String resultId = parts[0];
        int resultCount = Integer.parseInt(parts[1]);

        List<Integer> values = new ArrayList<>();
        if (RECIPE_ITEM_VALUES.containsKey(resultId)) values = RECIPE_ITEM_VALUES.get(resultId);

        totalInputEMC /= resultCount; // divide value on output item count
        
        if (values.size() > 0 && values.get(0) == totalInputEMC) return; // same value
        values.add(totalInputEMC);

        RECIPE_ITEM_VALUES.put(resultId, values);
    }

    private static int combineEMC(List<String> itemIds) {
        int totalEmcValue = 0;

        for (String itemId : itemIds) {
            int emcValue = get(itemId);
            if (emcValue == 0) {
                // could not get value for all blocks
                if (!RECIPE_ITEM_VALUES.containsKey(itemId)) {
                    if (!MISSING.contains(itemId)) MISSING.add(itemId);
                    return 0;
                }

                if (MISSING.contains(itemId)) MISSING.remove(itemId);
                emcValue = RECIPE_ITEM_VALUES.get(itemId).get(0);
            }

            totalEmcValue += emcValue;
        }

        return totalEmcValue;
    }
}
