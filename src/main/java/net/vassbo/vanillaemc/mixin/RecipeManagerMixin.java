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

@Mixin(RecipeManager.class)
public class RecipeManagerMixin {
    @Shadow @Final private RegistryWrapper.WrapperLookup registryLookup;

    @Inject(method = "apply", at = @At("HEAD"))
    private void applyMixin(Map<Identifier, JsonElement> map, ResourceManager resourceManager, Profiler profiler, CallbackInfo info) {
        VanillaEMC.LOGGER.info("Getting " + map.size() + " recipes!");
        RegistryOps<JsonElement> registryOps = this.registryLookup.getOps(JsonOps.INSTANCE);

        Iterator<Map.Entry<Identifier, JsonElement>> recipeIterator = map.entrySet().iterator();
        while (recipeIterator.hasNext()) {
            Map.Entry<Identifier, JsonElement> entry = recipeIterator.next();
            getRecipe(entry, registryOps);
        }

        EMCValues.recipesLoaded(RECIPES);
    }

    private static final HashMap<String, List<String>> RECIPES = new HashMap<String, List<String>>();
    private void getRecipe(Map.Entry<Identifier, JsonElement> entry, RegistryOps<JsonElement> registryOps) {
        // Identifier identifier = entry.getKey(); // JSON RECIPE FILE NAME
        Recipe<?> recipe = Recipe.CODEC.parse(registryOps, entry.getValue()).getOrThrow(JsonParseException::new);

        // good to have all recipes like ore smelting & stone cutting for more coverage!
        // if (recipe.getType() != RecipeType.CRAFTING) return;
        
        ItemStack resultItem = recipe.getResult(this.registryLookup);
        String resultId = resultItem.getItem().toString();
        int resultCount = resultItem.getCount();

        // smelting armor/tools will give nuggets - that should not be the same emc value!!
        if ((recipe.getType() == RecipeType.SMELTING || recipe.getType() == RecipeType.BLASTING) && resultId.contains("nugget")) return;

        List<String> INGREDIENTS = new ArrayList<>();
        for (Ingredient ingredient : recipe.getIngredients()) {
            // mostly just one ingredient (per slot) - but e.g. stone cutter can have multiple!
            int index = -1;
            for (int rawId : ingredient.getMatchingItemIds()) {
                String itemId = Item.byRawId(rawId).toString();
                index++;

                // use this to debug items having weird multiple emc values!
                // if (resultId.contains("item_id")) VanillaEMC.LOGGER.info("Found " + itemId   + " with the type " + recipe.getType());

                if (index == 0) {
                    INGREDIENTS.add(itemId);
                } else {
                    List<String> INGREDIENT = new ArrayList<>();
                    INGREDIENT.add(itemId);
                    addRecipe(resultId + "__" + 1, INGREDIENT);
                }
            }
        }

        addRecipe(resultId + "__" + resultCount, INGREDIENTS);
    }

    private static void addRecipe(String id, List<String> INGREDIENTS) {
        // multiple recipes for same output
        int index = 1;
        while (RECIPES.containsKey(id + "__" + index)) {
            index++;
        }

        RECIPES.put(id + "__" + index, INGREDIENTS);
    }
}