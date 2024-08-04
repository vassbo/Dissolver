package net.vassbo.vanillaemc.mixin;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry.Reference;
import net.minecraft.registry.tag.TagManagerLoader;
import net.minecraft.registry.tag.TagManagerLoader.RegistryTags;
import net.minecraft.util.Identifier;
import net.vassbo.vanillaemc.VanillaEMC;
import net.vassbo.vanillaemc.data.EMCValues;

@Mixin(TagManagerLoader.class)
public abstract class TagManagerLoaderMixin {
    private static final HashMap<String, Integer> NEW_EMC_VALUES = new HashMap<String, Integer>();
    @Shadow private List<RegistryTags<?>> registryTags = List.of();

	@Inject(method = "getRegistryTags", at = @At("HEAD"), cancellable = true)
	public List<RegistryTags<?>> getRegistryTags(CallbackInfoReturnable<String> cir) {
        VanillaEMC.LOGGER.info("Searching through " + registryTags.size() + " tags!");
        // TAG TYPE LIST (1.21): minecraft:banner_pattern, minecraft:worldgen/structure, minecraft:entity_type, minecraft:worldgen/world_preset, minecraft:fluid, minecraft:cat_variant, minecraft:game_event, minecraft:instrument, minecraft:item, minecraft:worldgen/biome, minecraft:enchantment, minecraft:block, minecraft:worldgen/flat_level_generator_preset, minecraft:painting_variant, minecraft:point_of_interest_type, minecraft:damage_type
        // List<String> tagTypeList = new ArrayList<>();

        registryTags.forEach(tagSet -> {
            for (Map.Entry<Identifier, ?> tag : tagSet.tags().entrySet()) {
                Identifier tagId = tag.getKey();
                Collection<?> value = (Collection<?>)tag.getValue();

                List<String> itemIds = new ArrayList<>();
                value.forEach(tagValue -> {
                    Reference<?> ref = (Reference<?>)tagValue;
                    RegistryKey<?> key = (RegistryKey<?>)ref.getKey().orElseGet(null);
                    if (key == null) return;

                    Identifier tagTypeId = key.getRegistry();
                    
                    // if (!tagTypeList.contains(tagTypeId.toString())) tagTypeList.add(tagTypeId.toString());
                    if (!tagTypeId.toString().contains("block") && !tagTypeId.toString().contains("item")) return;

                    Identifier itemId = key.getValue();
                    itemIds.add(itemId.toString());
                });

                if (itemIds.size() > 0) checkTagForEMC(tagId, itemIds);
            }
        });

        EMCValues.tagsLoaded(NEW_EMC_VALUES);

        // VanillaEMC.LOGGER.info("TAG TYPE LIST: " + tagTypeList);
        return this.registryTags;
	}

    private void checkTagForEMC(Identifier tagId, List<String> itemIds) {
        String tagIdString = tagId.toString();

        if (!EMCValues.EMC_TAG_VALUES.containsKey(tagIdString)) {
            // this will also log tags unrelated to crafting
            // String firstItem = itemIds.get(0);
            // VanillaEMC.LOGGER.info("FOUND TAG WITH NO EMC: " + tagId + " ITEM: " + firstItem);
            return;
        }

        int emcValue = EMCValues.EMC_TAG_VALUES.get(tagIdString);
        itemIds.forEach((itemId) -> {
            NEW_EMC_VALUES.put(itemId, emcValue);
        });
    }
}