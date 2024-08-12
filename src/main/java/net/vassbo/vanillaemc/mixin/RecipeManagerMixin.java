package net.vassbo.vanillaemc.mixin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mojang.serialization.JsonOps;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.RegistryOps;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import net.vassbo.vanillaemc.VanillaEMC;
import net.vassbo.vanillaemc.data.EMCValues;
import net.vassbo.vanillaemc.helpers.RecipeGenerator;

@Mixin(RecipeManager.class)
public class RecipeManagerMixin {
    @Shadow @Final private RegistryWrapper.WrapperLookup registryLookup;

    // CUSTOM RECIPE
    @Inject(method = "apply", at = @At("HEAD"))
    public void interceptApply(Map<Identifier, JsonElement> map, ResourceManager resourceManager, Profiler profiler, CallbackInfo info) {
        if (RecipeGenerator.DISSOLVER_RECIPE != null) {
            map.put(Identifier.of(VanillaEMC.MOD_ID, "dissolver_block_recipe"), RecipeGenerator.DISSOLVER_RECIPE);
        }
    }

    @Inject(method = "apply", at = @At("HEAD"))
    private void applyMixin(Map<Identifier, JsonElement> map, ResourceManager resourceManager, Profiler profiler, CallbackInfo info) {
        VanillaEMC.LOGGER.info("Getting " + map.size() + " recipes!");
        RegistryOps<JsonElement> registryOps = this.registryLookup.getOps(JsonOps.INSTANCE);

        // let tag items load before looking through recipes
        new Thread(() -> {
            wait(800);

            Iterator<Map.Entry<Identifier, JsonElement>> recipeIterator = map.entrySet().iterator();
            while (recipeIterator.hasNext()) {
                try {
                    Map.Entry<Identifier, JsonElement> entry = recipeIterator.next();
                    getRecipe(entry, registryOps);
                }catch (Exception e) {
                    VanillaEMC.LOGGER.error("FOUND RECIPE WITH ISSUE" , e);
                }
            }

            EMCValues.recipesLoaded(RECIPES, STONE_CUTTER_LIST);
        }).start();
    }

    private static final HashMap<String, List<String>> RECIPES = new HashMap<String, List<String>>();
    private static final List<String> STONE_CUTTER_LIST = new ArrayList<>();
    private void getRecipe(Map.Entry<Identifier, JsonElement> entry, RegistryOps<JsonElement> registryOps) {
        // Identifier identifier = entry.getKey(); // JSON RECIPE FILE NAME
        Recipe<?> recipe = Recipe.CODEC.parse(registryOps, entry.getValue()).getOrThrow(JsonParseException::new);

        // could filter by crafting only, but nice to have all recipes like smelting & stone cutting for more coverage!
        // if (recipe.getType() != RecipeType.CRAFTING) return;
        if (recipe.getType() == RecipeType.SMITHING) return; // no ingredients!
        
        ItemStack resultItem = recipe.getResult(this.registryLookup);
        String resultId = resultItem.getItem().toString();
        int resultCount = resultItem.getCount();

        boolean isCooking = recipe.getType() == RecipeType.SMELTING || recipe.getType() == RecipeType.BLASTING || recipe.getType() == RecipeType.SMOKING || recipe.getType() == RecipeType.CAMPFIRE_COOKING;

        // smelting armor/tools will give nuggets - that should not be the same emc value!!
        if ((isCooking) && resultId.contains("nugget")) return;

        List<String> INGREDIENTS = new ArrayList<>();
        HashMap<String, List<String>> REPLACE_INGREDIENTS = new HashMap<>();
        for (Ingredient ingredient : recipe.getIngredients()) {
            // mostly just one ingredient (per slot) - but e.g. stone cutter can have multiple!
            int index = -1;
            for (int rawId : ingredient.getMatchingItemIds()) {
                String itemId = Item.byRawId(rawId).toString();
                index++;

                if (index == 0) {
                    INGREDIENTS.add(itemId);
                } else if (recipe.getType() == RecipeType.STONECUTTING) {
                    List<String> INGREDIENT = new ArrayList<>();
                    INGREDIENT.add(itemId);
                    addRecipe(resultId + "__" + 1, 0, INGREDIENT);
                } else if (resultId.contains("bed") || resultId.contains("glass")) {
                    // don't do anything
                    // bed: bed colors
                    // glass: smelting red sand
                } else {
                    // example: TNT is normally sand, but slot can also contain red_sand
                    // wool should only add white wool
                    String rootItemId = Item.byRawId(ingredient.getMatchingItemIds().getInt(0)).toString();
                    if (!resultId.contains("wool") || itemId.contains("dye")) {
                        List<String> REPLACE = new ArrayList<>();
                        if (REPLACE_INGREDIENTS.containsKey(rootItemId)) REPLACE = REPLACE_INGREDIENTS.get(rootItemId);
                        REPLACE.add(itemId);
                        REPLACE_INGREDIENTS.put(rootItemId, REPLACE);
                    }
                }
            }
        }
        
        if (recipe.getType() == RecipeType.CRAFTING) {
            boolean DEFAULT_GLASS = INGREDIENTS.contains("minecraft:glass") || INGREDIENTS.contains("minecraft:glass_pane");
            if (resultId.contains("glass") && listSearch(INGREDIENTS, "glass") && !DEFAULT_GLASS) return; // glass into glass (dye)
            if (resultId.contains("carpet") && listSearch(INGREDIENTS, "carpet")) return; // carpet into carpet (dye)
            if (resultId.contains("bed") && listSearch(INGREDIENTS, "bed")) return; // bed into bed (dye)
            boolean NOT_WHITE_WOOL_OR_WHITE_DYE = !listSearch(INGREDIENTS, "white_wool") || !listSearch(INGREDIENTS, "dye");
            if (resultId.contains("wool") && listSearch(INGREDIENTS, "wool") && NOT_WHITE_WOOL_OR_WHITE_DYE) return; // wool into wool (dye) (only one recipe because of REPLACE_INGREDIENTS)
        }

        // some items does not have any ingredients, so manually add those! (it uses tags)
        if (INGREDIENTS.size() == 0) {
            // if (resultId.contains("_planks")) {
            //     String plank_type = resultId.substring(resultId.indexOf(":") + 1, resultId.indexOf("_planks"));
            //     if (resultId.contains("warped") || resultId.contains("crimson")) INGREDIENTS.add("minecraft:" + plank_type + "_stem");
            //     else if (resultId.contains("bamboo")) INGREDIENTS.add("minecraft:" + plank_type + "_block");
            //     else INGREDIENTS.add("minecraft:" + plank_type + "_log");
            // } else if (resultId == "minecraft:glass") {
            //     INGREDIENTS.add("minecraft:sand");
            // } else if (resultId == "minecraft:charcoal") {
            //     INGREDIENTS.add("minecraft:oak_log");
            // } else {
            //     VanillaEMC.LOGGER.info("FOUND ITEM RECIPE WITH NO INGREDIENTS: " + resultId);
            // }

            if (!resultId.contains("minecraft:air") && !resultId.contains("firework") && !RECIPES.containsKey(resultId)) {
                VanillaEMC.LOGGER.info("FOUND ITEM RECIPE WITH NO INGREDIENTS: " + resultId);
            }
            return;
        }

        // use this to debug items having weird multiple emc values!
        // if (resultId.contains("item_id")) VanillaEMC.LOGGER.info("Found with the type " + recipe.getType() + ". Ingredients: " + INGREDIENTS);

        boolean isOre = listSearch(INGREDIENTS, "ore");
        boolean isStone = listSearch(INGREDIENTS, "stone");

        // add extra EMC if cooking (because of fuel+time)
        addRecipe(resultId + "__" + resultCount, isCooking && !isOre && !isStone ? 10 : 0, INGREDIENTS);
        if (recipe.getType() == RecipeType.STONECUTTING && !STONE_CUTTER_LIST.contains(resultId)) STONE_CUTTER_LIST.add(resultId);

        if (REPLACE_INGREDIENTS.size() > 0) {
            for (Map.Entry<String, List<String>> replace : REPLACE_INGREDIENTS.entrySet()) {
                String key = replace.getKey();
                List<String> replacedIngredients = replace.getValue();

                for (String replacedIngredient : replacedIngredients) {
                    List<String> NEW_INGREDIENTS = new ArrayList<>();

                    for (String ingredient : INGREDIENTS) {
                        NEW_INGREDIENTS.add(key.contains(ingredient) ? replacedIngredient : ingredient);
                    }
                    
                    addRecipe(resultId + "__" + resultCount, 0, NEW_INGREDIENTS);
                }
            }
        }
    }

    private static void addRecipe(String id, int extraEMC, List<String> INGREDIENTS) {
        // multiple recipes for same output
        int index = 1;
        while (RECIPES.containsKey(id + "__" + extraEMC + "__" + index)) {
            index++;
        }

        RECIPES.put(id + "__" + extraEMC + "__" + index, INGREDIENTS);
    }

    static private boolean listSearch(List<String> INGREDIENTS, String keyId) {
        for (String key : INGREDIENTS){
            if (key.contains(keyId)) return true;
        }

        return false;
    }

    private static void wait(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}