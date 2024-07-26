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
        // DEFAULT ITEM VALUES LIST (https://minecraftitemids.com/)
        // Some of these can be crafted/smelted etc. into other items.
        // This is automatically queried.
        // NOTE: items with block recipes should be dividable by 9 etc. to prevent reduced values (INFINITE EMC)

        // nature
        EMC_VALUES.put("minecraft:cobblestone", 1);
        EMC_VALUES.put("minecraft:dirt", 1);
        EMC_VALUES.put("minecraft:grass_block", 4);
        EMC_VALUES.put("minecraft:bamboo", 2);
        EMC_VALUES.put("minecraft:ice", 5);
        EMC_VALUES.put("minecraft:vine", 5);
        // nether
        EMC_VALUES.put("minecraft:magma_cream", 800);
        // special
        EMC_VALUES.put("minecraft:nether_star", 10000);
        EMC_VALUES.put("minecraft:wayfinder_armor_trim_smithing_template", 10000);
        // custom
        EMC_VALUES.put("vanillaemc:magic_item", 1000);
        
        // BY TAGS (https://mcreator.net/wiki/minecraft-item-tags-list)
        // nature
        EMC_TAG_VALUES.put("minecraft:stone", 1);
        EMC_TAG_VALUES.put("minecraft:logs", 10);
        EMC_TAG_VALUES.put("minecraft:sand", 1);
        EMC_TAG_VALUES.put("minecraft:sandstone", 4);
        EMC_TAG_VALUES.put("minecraft:terracotta", 1000);
        // materials
        EMC_TAG_VALUES.put("minecraft:coal_ores", 100);
        EMC_TAG_VALUES.put("minecraft:redstone_ores", 200);
        EMC_TAG_VALUES.put("minecraft:copper_ores", 200);
        EMC_TAG_VALUES.put("minecraft:lapis_ores", 300);
        EMC_TAG_VALUES.put("minecraft:iron_ores", 450); // dividable by 9
        EMC_TAG_VALUES.put("minecraft:gold_ores", 900); // dividable by 9
        EMC_TAG_VALUES.put("minecraft:diamond_ores", 1800);
        EMC_TAG_VALUES.put("minecraft:emerald_ores", 1800);
    }

    private static boolean tags_loaded = false;
    public static void tagsLoaded(HashMap<String, Integer> NEW_EMC_VALUES) {
        EMC_VALUES.putAll(NEW_EMC_VALUES);
        tags_loaded = true;

        if (tags_loaded && !RECIPES.isEmpty()) startQuery();
    }

    private static HashMap<String, List<String>> RECIPES = new HashMap<String, List<String>>();
    public static void recipesLoaded(HashMap<String, List<String>> recipes) {
        RECIPES = recipes;

        if (tags_loaded && !RECIPES.isEmpty()) startQuery();
    }

    private static void startQuery() {
        VanillaEMC.LOGGER.info("Searching through " + RECIPES.size() + " recipes!");
        queryRecipes(RECIPES);
    }

    public static void queryRecipes(HashMap<String, List<String>> RECIPES) {
        for (Map.Entry<String, List<String>> recipe : RECIPES.entrySet()) {
            checkRecipe(recipe);
        }

        // brute force! (i'm sure there's a more optimized way)
        if (COMPLETED.size() < RECIPES.size()) queryRecipes(RECIPES);
        else {
            RECIPE_ITEM_VALUES.forEach((resultId, emcValues) -> {
                int emcValue = getAverage(emcValues);
                // mostly stonecutter items!
                if (emcValues.size() > 1) VanillaEMC.LOGGER.info("Found item with multiple different recipes: " + ItemHelper.getName(ItemHelper.getById(resultId)) + ". Using average: " + emcValue + " " + emcValues);
                EMC_VALUES.put(resultId, emcValue);
            });

            // LOG ITEMS WITH MISSING EMC - that does not have a crafting recipe!
            for (String missing : MISSING) {
                if (!COMPLETED.contains(missing)) VanillaEMC.LOGGER.info("FOUND ITEM WITH NO RECIPE OR EMC: " + ItemHelper.getName(ItemHelper.getById(missing)) + " (" +missing+ ")");
            }

            int maxedOutCount = 0;
            for (Integer count : TRIED.values()) {
                if (count > MAX_TRIES) maxedOutCount++;
            }
            if (maxedOutCount > 0) VanillaEMC.LOGGER.info("FOUND " + maxedOutCount + " ITEMS WITH NO EMC!");

            VanillaEMC.LOGGER.info("Loaded EMC values for " + RECIPE_ITEM_VALUES.size() + " recipes!");
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
    private static final HashMap<String, List<Integer>> RECIPE_ITEM_VALUES = new HashMap<String, List<Integer>>();
    private static final HashMap<String, List<String>> PARENTS = new HashMap<String, List<String>>();
    private static final HashMap<String, Integer> TRIED = new HashMap<String, Integer>();
    private static int MAX_TRIES = 100; // WIP reduce this
    private static void checkRecipe(Map.Entry<String, List<String>> recipe) {
        String id = recipe.getKey();
        if (COMPLETED.contains(id)) return;

        // brute force query
        if (!TRIED.containsKey(id)) TRIED.put(id, 0);
        TRIED.replace(id, TRIED.get(id) + 1);

        if (TRIED.get(id) > MAX_TRIES) {
            // VanillaEMC.LOGGER.info("Could not get item EMC: " + ItemHelper.getName(ItemHelper.getById(id.split("__")[0])));
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

        // don't allow "children" to change the item they received emc value from
        if (PARENTS.containsKey(resultId)) {
            if (!RECIPE_ITEM_VALUES.containsKey(resultId)) return;

            // check if emc values are different
            int previousEMC = RECIPE_ITEM_VALUES.get(resultId).get(0);
            int newEMC = totalInputEMC / resultCount;
            if (previousEMC == newEMC) return;

            VanillaEMC.LOGGER.info("Found child & parent ITEMS with unmatching EMC values: " + resultId + " - " + PARENTS.get(resultId) + " (THIS COULD RESULT IN INFINITE EMC)!");
            return;
        } else if (ingredients.size() == 1) {
            List<String> children = new ArrayList<>();
            String parentId = ingredients.get(0);
            if (PARENTS.containsKey(parentId)) children = PARENTS.get(parentId);
            children.add(resultId);
            PARENTS.put(parentId, children);
        }

        List<Integer> values = new ArrayList<>();
        if (RECIPE_ITEM_VALUES.containsKey(resultId)) values = RECIPE_ITEM_VALUES.get(resultId);

        totalInputEMC /= resultCount; // divide value on output item count
        
        if (values.contains(totalInputEMC)) return; // same value
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
