package net.vassbo.vanillaemc.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.OptionalDouble;
import java.util.Set;

import net.vassbo.vanillaemc.data.model.EMCRecord;
import org.spongepowered.asm.mixin.injection.struct.InjectorGroupInfo.Map;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.vassbo.vanillaemc.VanillaEMC;
import net.vassbo.vanillaemc.config.ModConfig;
import net.vassbo.vanillaemc.helpers.ItemHelper;

public class EMCValues {
    protected static final Set<String> CONFIG_OVERRIDDEN = new HashSet<>();

    protected static final HashMap<String, Integer> EMC_VALUES = new HashMap<String, Integer>();
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

        // These does not show up with EMC NBT data, but they still work:
        // Shield, Crossbow, Banners & Banner patterns

        // Currently missing (probably not added):
        // Potions (including water bottle), Enchanted books, etc. (see more down below)

        int DIRT = 1;
        int GRASS = 2;
        int LEAVES = 5;
        int BAMBOO = 1; // this * 9 should be dividable by 2
        int COBBLESTONE = 2; // should be at least 2 (because of slabs)
        int SAND = 2;
        int DEEPSLATE = 4;
        int COPPER = 216;
        int IRON = 225; // dividable by 9
        int GOLD = 1350; // dividable by 9
        int DIAMOND = 4200;

        // CUSTOM MODES
        String emcMode = ModConfig.MODE.toLowerCase();
        if (emcMode.contains("skyblock")) {
            DIRT = 80;
            SAND = 80;
            GRASS = 90;
            IRON = 500;
            GOLD = 1800;
            DIAMOND = 8000;
            // please give feedback on this
        }

        // nature
        setEMCUnchecked("minecraft:cobblestone", COBBLESTONE);
        setEMCUnchecked("minecraft:dirt", DIRT);
        setEMCUnchecked("minecraft:dirt_path", DIRT);
        setEMCUnchecked("minecraft:farmland", DIRT);
        setEMCUnchecked("minecraft:gravel", SAND);
        setEMCUnchecked("minecraft:suspicious_gravel", SAND);
        setEMCUnchecked("minecraft:flint", DEEPSLATE);
        setEMCUnchecked("minecraft:bamboo", BAMBOO);
        setEMCUnchecked("minecraft:grass_block", GRASS);
        setEMCUnchecked("minecraft:moss_block", GRASS);
        setEMCUnchecked("minecraft:raw_iron", IRON - 10);
        setEMCUnchecked("minecraft:raw_gold", GOLD - 10);
        setEMCUnchecked("minecraft:raw_copper", COPPER - 10);
        setEMCUnchecked("minecraft:deepslate", DEEPSLATE);
        setEMCUnchecked("minecraft:cobbled_deepslate", COBBLESTONE);
        setEMCUnchecked("minecraft:blackstone", COBBLESTONE);
        setEMCUnchecked("minecraft:gilded_blackstone", GOLD);
        setEMCUnchecked("minecraft:tuff", DEEPSLATE);
        setEMCUnchecked("minecraft:mud", DIRT);
        setEMCUnchecked("minecraft:clay_ball", LEAVES);
        setEMCUnchecked("minecraft:short_grass", DIRT);
        setEMCUnchecked("minecraft:tall_grass", DIRT);
        setEMCUnchecked("minecraft:fern", GRASS);
        setEMCUnchecked("minecraft:large_fern", GRASS);
        setEMCUnchecked("minecraft:azalea", LEAVES);
        setEMCUnchecked("minecraft:mycelium", LEAVES);
        setEMCUnchecked("minecraft:podzol", GRASS);
        setEMCUnchecked("minecraft:ice", GRASS);
        setEMCUnchecked("minecraft:vine", GRASS);
        setEMCUnchecked("minecraft:glow_lichen", GRASS);
        setEMCUnchecked("minecraft:lily_pad", GRASS);
        setEMCUnchecked("minecraft:big_dripleaf", GRASS + 2);
        setEMCUnchecked("minecraft:small_dripleaf", GRASS + 2);
        setEMCUnchecked("minecraft:mangrove_roots", LEAVES);
        setEMCUnchecked("minecraft:sugar_cane", LEAVES * 5);
        setEMCUnchecked("minecraft:pointed_dripstone", LEAVES);
        setEMCUnchecked("minecraft:hanging_roots", DIRT);
        setEMCUnchecked("minecraft:rooted_dirt", LEAVES);
        setEMCUnchecked("minecraft:cactus", LEAVES);
        setEMCUnchecked("minecraft:dead_bush", DIRT);
        setEMCUnchecked("minecraft:snowball", DIRT);
        setEMCUnchecked("minecraft:stripped_bamboo_block", BAMBOO * 9);
        setEMCUnchecked("minecraft:pitcher_pod", GRASS);
        setEMCUnchecked("minecraft:torchflower_seeds", GRASS);
        setEMCUnchecked("minecraft:honeycomb", LEAVES * 10);
        setEMCUnchecked("minecraft:honey_bottle", LEAVES * 15);
        setEMCUnchecked("minecraft:bee_nest", 174);
        setEMCUnchecked("minecraft:goat_horn", 1200);
        setEMCUnchecked("minecraft:frogspawn", 60);
        setEMCUnchecked("minecraft:turtle_egg", 70);
        setEMCUnchecked("minecraft:sniffer_egg", 80);
        // froglight
        setEMCUnchecked("minecraft:shroomlight", 80);
        setEMCUnchecked("minecraft:verdant_froglight", 400);
        setEMCUnchecked("minecraft:ochre_froglight", 400);
        setEMCUnchecked("minecraft:pearlescent_froglight", 400);
        // mushroom
        setEMCUnchecked("minecraft:brown_mushroom", 5);
        setEMCUnchecked("minecraft:red_mushroom", 5);
        setEMCUnchecked("minecraft:brown_mushroom_block", 20);
        setEMCUnchecked("minecraft:red_mushroom_block", 20);
        setEMCUnchecked("minecraft:mushroom_stem", 20);
        // ancient
        setEMCUnchecked("minecraft:sculk", 600);
        setEMCUnchecked("minecraft:sculk_sensor", 1400);
        setEMCUnchecked("minecraft:sculk_shrieker", 2800);
        setEMCUnchecked("minecraft:sculk_catalyst", 800);
        setEMCUnchecked("minecraft:sculk_vein", 400);
        setEMCUnchecked("minecraft:echo_shard", 1800);
        // amethyst
        setEMCUnchecked("minecraft:amethyst_cluster", 4800);
        setEMCUnchecked("minecraft:amethyst_shard", 1800);
        setEMCUnchecked("minecraft:calcite", 600);
        // ocean
        setEMCUnchecked("minecraft:prismarine_shard", 160);
        setEMCUnchecked("minecraft:prismarine_crystals", 400);
        setEMCUnchecked("minecraft:nautilus_shell", 200);
        setEMCUnchecked("minecraft:kelp", GRASS);
        setEMCUnchecked("minecraft:sea_pickle", LEAVES);
        setEMCUnchecked("minecraft:wet_sponge", 90);
        setEMCUnchecked("minecraft:heart_of_the_sea", 500);
        setEMCUnchecked("minecraft:seagrass", GRASS);
        // loot
        setEMCUnchecked("minecraft:string", 12);
        setEMCUnchecked("minecraft:cobweb", 12);
        setEMCUnchecked("minecraft:gunpowder", 56);
        setEMCUnchecked("minecraft:turtle_scute", 180);
        setEMCUnchecked("minecraft:armadillo_scute", 180);
        setEMCUnchecked("minecraft:spider_eye", 25);
        setEMCUnchecked("minecraft:breeze_rod", 90);
        setEMCUnchecked("minecraft:rabbit_hide", 20);
        setEMCUnchecked("minecraft:blaze_rod", 60);
        setEMCUnchecked("minecraft:ender_pearl", 80);
        setEMCUnchecked("minecraft:ink_sac", 25);
        setEMCUnchecked("minecraft:glow_ink_sac", 120);
        setEMCUnchecked("minecraft:feather", 8);
        setEMCUnchecked("minecraft:egg", 12);
        setEMCUnchecked("minecraft:ghast_tear", 220);
        setEMCUnchecked("minecraft:magma_cream", 150);
        setEMCUnchecked("minecraft:bone", 27);
        setEMCUnchecked("minecraft:slime_ball", 30);
        setEMCUnchecked("minecraft:rotten_flesh", DIRT);
        setEMCUnchecked("minecraft:rabbit_foot", 22);
        setEMCUnchecked("minecraft:phantom_membrane", 150);
        // crops
        setEMCUnchecked("minecraft:wheat_seeds", GRASS);
        setEMCUnchecked("minecraft:wheat", 20);
        setEMCUnchecked("minecraft:beetroot_seeds", GRASS);
        setEMCUnchecked("minecraft:beetroot", 20);
        setEMCUnchecked("minecraft:poisonous_potato", DIRT);
        setEMCUnchecked("minecraft:potato", 24);
        setEMCUnchecked("minecraft:carrot", 24);
        setEMCUnchecked("minecraft:pumpkin", 36);
        setEMCUnchecked("minecraft:carved_pumpkin", 40);
        setEMCUnchecked("minecraft:melon_slice", 4);
        setEMCUnchecked("minecraft:cocoa_beans", 12);
        // food
        setEMCUnchecked("minecraft:mutton", 40);
        setEMCUnchecked("minecraft:porkchop", 50);
        setEMCUnchecked("minecraft:beef", 50);
        setEMCUnchecked("minecraft:chicken", 40);
        setEMCUnchecked("minecraft:rabbit", 40);
        setEMCUnchecked("minecraft:apple", 30);
        setEMCUnchecked("minecraft:cod", 35);
        setEMCUnchecked("minecraft:salmon", 35);
        setEMCUnchecked("minecraft:tropical_fish", 35);
        setEMCUnchecked("minecraft:pufferfish", 38);
        setEMCUnchecked("minecraft:sweet_berries", 18);
        setEMCUnchecked("minecraft:glow_berries", 24);
        // buckets
        int BUCKET = IRON * 3;
        setEMCUnchecked("minecraft:water_bucket", BUCKET + 20);
        setEMCUnchecked("minecraft:lava_bucket", BUCKET + 200);
        setEMCUnchecked("minecraft:milk_bucket", BUCKET + 50);
        setEMCUnchecked("minecraft:powder_snow_bucket", BUCKET + 80);
        // nether
        int ANCIENT_DEBRIS = 800;
        setEMCUnchecked("minecraft:ancient_debris", ANCIENT_DEBRIS);
        setEMCUnchecked("minecraft:obsidian", 140);
        setEMCUnchecked("minecraft:crying_obsidian", 140);
        setEMCUnchecked("minecraft:glowstone_dust", 38);
        setEMCUnchecked("minecraft:nether_wart", 40);
        setEMCUnchecked("minecraft:basalt", DEEPSLATE);
        setEMCUnchecked("minecraft:nether_quartz_ore", 120);
        setEMCUnchecked("minecraft:netherrack", DIRT);
        setEMCUnchecked("minecraft:soul_sand", 3);
        setEMCUnchecked("minecraft:soul_soil", 3);
        setEMCUnchecked("minecraft:warped_fungus", GRASS);
        setEMCUnchecked("minecraft:crimson_fungus", GRASS);
        setEMCUnchecked("minecraft:warped_roots", GRASS);
        setEMCUnchecked("minecraft:nether_sprouts", GRASS);
        setEMCUnchecked("minecraft:crimson_nylium", GRASS);
        setEMCUnchecked("minecraft:crimson_roots", GRASS);
        setEMCUnchecked("minecraft:twisting_vines", GRASS);
        setEMCUnchecked("minecraft:weeping_vines", GRASS);
        setEMCUnchecked("minecraft:warped_nylium", GRASS);
        setEMCUnchecked("minecraft:warped_wart_block", LEAVES);
        // end
        int SHULKER_SHELL = 1000;
        setEMCUnchecked("minecraft:chorus_fruit", 50);
        setEMCUnchecked("minecraft:chorus_plant", 5);
        setEMCUnchecked("minecraft:shulker_shell", SHULKER_SHELL);
        setEMCUnchecked("minecraft:end_stone", 12);
        setEMCUnchecked("minecraft:dragon_breath", 150);
        // heads
        setEMCUnchecked("minecraft:dragon_head", 1500);
        setEMCUnchecked("minecraft:wither_skeleton_skull", 4000);
        setEMCUnchecked("minecraft:creeper_head", 10000);
        setEMCUnchecked("minecraft:piglin_head", 10000);
        setEMCUnchecked("minecraft:skeleton_skull", 10000);
        setEMCUnchecked("minecraft:zombie_head", 10000);
        setEMCUnchecked("minecraft:player_head", 12000);
        // armor
        int CHAINMAIL = (int)(IRON * 0.8); // CUSTOM VALUE
        setEMCUnchecked("minecraft:chainmail_helmet", CHAINMAIL * 5);
        setEMCUnchecked("minecraft:chainmail_chestplate", CHAINMAIL * 8);
        setEMCUnchecked("minecraft:chainmail_leggings", CHAINMAIL * 7);
        setEMCUnchecked("minecraft:chainmail_boots", CHAINMAIL * 4);
        setEMCUnchecked("minecraft:diamond_horse_armor", (int) (DIAMOND * 1.2));
        setEMCUnchecked("minecraft:golden_horse_armor", (int) (GOLD * 1.2));
        setEMCUnchecked("minecraft:iron_horse_armor", (int) (IRON * 1.2));
        int NETHERITE_INGOT = (int)(ANCIENT_DEBRIS * 4.455);
        setEMCUnchecked("minecraft:netherite_sword", NETHERITE_INGOT + (DIAMOND * 2) + 2);
        setEMCUnchecked("minecraft:netherite_pickaxe", NETHERITE_INGOT + (DIAMOND * 3) + (2 * 2));
        setEMCUnchecked("minecraft:netherite_axe", NETHERITE_INGOT + (DIAMOND * 3) + (2 * 2));
        setEMCUnchecked("minecraft:netherite_shovel", NETHERITE_INGOT + DIAMOND + (2 * 2));
        setEMCUnchecked("minecraft:netherite_hoe", NETHERITE_INGOT + (DIAMOND * 2) + (2 * 2));
        setEMCUnchecked("minecraft:netherite_helmet", NETHERITE_INGOT + (DIAMOND * 5));
        setEMCUnchecked("minecraft:netherite_chestplate", NETHERITE_INGOT + (DIAMOND * 8));
        setEMCUnchecked("minecraft:netherite_leggings", NETHERITE_INGOT + (DIAMOND * 7));
        setEMCUnchecked("minecraft:netherite_boots", NETHERITE_INGOT + (DIAMOND * 4));
        // special
        setEMCUnchecked("minecraft:dragon_egg", 180000);
        setEMCUnchecked("minecraft:nether_star", 120000);
        setEMCUnchecked("minecraft:enchanted_golden_apple", 50000);
        setEMCUnchecked("minecraft:heavy_core", 2200);
        setEMCUnchecked("minecraft:experience_bottle", 600);
        setEMCUnchecked("minecraft:trident", 3000);
        setEMCUnchecked("minecraft:saddle", 250);
        setEMCUnchecked("minecraft:name_tag", 250);
        setEMCUnchecked("minecraft:bell", 800);
        setEMCUnchecked("minecraft:elytra", 1500);
        setEMCUnchecked("minecraft:trial_key", 1200);
        setEMCUnchecked("minecraft:ominous_trial_key", 1300);
        setEMCUnchecked("minecraft:totem_of_undying", 60000);

        // creative
        if (ModConfig.CREATIVE_ITEMS) {
            setEMCUnchecked("minecraft:bedrock", 1000000);
            setEMCUnchecked("minecraft:barrier", 1000000);
            setEMCUnchecked("minecraft:end_portal_frame", 800000);
            setEMCUnchecked("minecraft:reinforced_deepslate", 100000);
            setEMCUnchecked("minecraft:structure_block", 400000);
            setEMCUnchecked("minecraft:jigsaw", 400000);
            setEMCUnchecked("minecraft:command_block", 500000);
            setEMCUnchecked("minecraft:repeating_command_block", 500000);
            setEMCUnchecked("minecraft:chain_command_block", 500000);
            setEMCUnchecked("minecraft:command_block_minecart", 500000 + 2250);
            setEMCUnchecked("minecraft:vault", 400000);
            setEMCUnchecked("minecraft:trial_spawner", 400000);
            setEMCUnchecked("minecraft:spawner", 400000);
            setEMCUnchecked("minecraft:budding_amethyst", 8000);
        }
        
        // EMC_VALUES.put("minecraft:filled_map", 1300); // same as empty map (this does nothing)

        // infested (should not be obtainable)
        // EMC_VALUES.put("minecraft:infested_stone", 1);
        // EMC_VALUES.put("minecraft:infested_chiseled_stone_bricks", 1);
        // EMC_VALUES.put("minecraft:infested_cobblestone", 1);
        // EMC_VALUES.put("minecraft:infested_deepslate", 1);
        // EMC_VALUES.put("minecraft:infested_cracked_stone_bricks", 1);
        // EMC_VALUES.put("minecraft:infested_stone_bricks", 1);
        // EMC_VALUES.put("minecraft:infested_mossy_stone_bricks", 1);
        
        // DOES NOT WORK BECAUSE OF NBT DATA
        // EMC_VALUES.put("minecraft:disc_fragment_5", 800);
        // EMC_VALUES.put("minecraft:axolotl_bucket", BUCKET + 70);
        // EMC_VALUES.put("minecraft:tadpole_bucket", BUCKET + 60);
        // EMC_VALUES.put("minecraft:pufferfish_bucket", BUCKET + 60);
        // EMC_VALUES.put("minecraft:tropical_fish_bucket", BUCKET + 50);
        // EMC_VALUES.put("minecraft:cod_bucket", BUCKET + 30);
        // EMC_VALUES.put("minecraft:salmon_bucket", BUCKET + 40);
        // EMC_VALUES.put("minecraft:suspicious_stew", 60); // THIS WORKS (BUT NO EFFECT IS STORED)
        // EMC_VALUES.put("minecraft:firework_star", 50);
        // EMC_VALUES.put("minecraft:firework_rocket", 80);
        // EMC_VALUES.put("minecraft:enchanted_book", 1000); // THIS WORKS (BUT NBT IS NOT STORED)
        // EMC_VALUES.put("minecraft:ominous_bottle", 1000); // THIS WORKS (BUT ONLY BOTTLE EFFECT #1 IS STORED)
        // EMC_VALUES.put("minecraft:potion", 800);
        // EMC_VALUES.put("minecraft:splash_potion", 800);
        // EMC_VALUES.put("minecraft:lingering_potion", 800);
        // water bottle
        // banner pattern (special ones)
        setEMCUnchecked("minecraft:flow_banner_pattern", 200);
        setEMCUnchecked("minecraft:guster_banner_pattern", 200);
        setEMCUnchecked("minecraft:piglin_banner_pattern", 200);
        setEMCUnchecked("minecraft:globe_banner_pattern", 200);

        // BY TAGS (https://mcreator.net/wiki/minecraft-item-tags-list)
        // nature
        int LOGS = 16; // bamboo planks (BAMBOO * 9 / 2) * 4 (all planks should be same EMC)
        int FLOWERS = 8; // at least 8 so dyed glass is more expensive
        EMC_TAG_VALUES.put("minecraft:stone", COBBLESTONE * 2);
        EMC_TAG_VALUES.put("minecraft:logs", LOGS); // dividable by 4
        EMC_TAG_VALUES.put("minecraft:sand", SAND);
        EMC_TAG_VALUES.put("minecraft:flowers", FLOWERS);
        EMC_TAG_VALUES.put("minecraft:leaves", LEAVES);
        EMC_TAG_VALUES.put("minecraft:arrow", 10);
        // materials
        int COAL = 40;
        EMC_TAG_VALUES.put("minecraft:coal_ores", COAL);
        EMC_TAG_VALUES.put("minecraft:redstone_ores", 50);
        EMC_TAG_VALUES.put("minecraft:lapis_ores", 400);
        EMC_TAG_VALUES.put("minecraft:iron_ores", IRON);
        EMC_TAG_VALUES.put("minecraft:gold_ores", GOLD);
        EMC_TAG_VALUES.put("minecraft:diamond_ores", DIAMOND);
        EMC_TAG_VALUES.put("minecraft:emerald_ores", 1600);

        // CUSTOM
        setEMCUnchecked("minecraft:charcoal", COAL);
        setEMCUnchecked("minecraft:exposed_chiseled_copper", (int) (COPPER * 4));
        setEMCUnchecked("minecraft:waxed_exposed_chiseled_copper", (int) (COPPER * 4.2));
        // waxed weathered chiseled copper is currently higher than the waxed oxidized chiseled copper

        // GENERATE

        // copper
        EMC_TAG_VALUES.put("minecraft:copper_ores", COPPER);
        List<String> copper_states = Arrays.asList("", "exposed_", "weathered_", "oxidized_");
        List<String> copper_types = Arrays.asList("block", "door", "trapdoor");
        int copperIndex = -1;
        for (String state : copper_states) {
            copperIndex++;
            for (String type : copper_types) {
                int value = COPPER * (type == "block" ? 9 : 6) + (copperIndex * 100);
                if (state != "") setEMCUnchecked(
                    "minecraft:" + state + "copper" + (type != "block" ? "_" + type : ""),
                    value / (type == "door" ? 3 : type == "trapdoor" ? 2 : 1)
                );
                // EMC_VALUES.put("minecraft:waxed_" + state + "copper" + (type != "block" || state == "" ? "_" + type : ""), value + 12);
            }
        }

        // coral
        List<String> coral_blocks = Arrays.asList("tube", "brain", "bubble", "fire", "horn");
        List<String> coral_types = Arrays.asList("", "_block", "_fan");
        int CORAL = 4;
        for (String block : coral_blocks) {
            for (String type : coral_types) {
                setEMCUnchecked(
                    "minecraft:" + block + "_coral" + type, CORAL * (Objects.equals(type, "_block") ? 2 : 1));
                setEMCUnchecked(
                    "minecraft:dead_" + block + "_coral" + type, CORAL / (Objects.equals(type, "_block") ? 1 : 2));
            }
        }
        
        // saplings
        List<String> saplings = Arrays.asList("oak", "spruce", "birch", "jungle", "acacia", "dark_oak", "cherry");
        for (String sapling : saplings) {
            setEMCUnchecked("minecraft:" + sapling + "_sapling", 3);
        }

        // shulker boxes & dyes
        int SHULKER_BOX = LOGS * 2 + (SHULKER_SHELL * 2);
        List<String> colors = Arrays.asList("black", "blue", "brown", "cyan", "gray", "green", "light_blue", "light_gray", "lime", "magenta", "orange", "pink", "purple", "red", "white", "yellow");
        for (String color : colors) {
            setEMCUnchecked("minecraft:" + color + "_shulker_box", SHULKER_BOX);
            setEMCUnchecked("minecraft:" + color + "_dye", FLOWERS);
        }
        // EMC_TAG_VALUES.put("minecraft:dyes", FLOWERS); // all should be same because of all the recipes using dyes

        // music discs
        int MUSIC_DISC = 8000;
        List<String> discs = Arrays.asList("mall", "chirp", "wait", "strad", "creator", "far", "cat", "ward", "blocks", "otherside", "relic", "11", "mellohi", "pigstep", "13", "stal", "creator_music_box", "precipice", "5");
        for (String disc : discs) {
            setEMCUnchecked("minecraft:music_disc_" + disc, MUSIC_DISC);
        }

        // pottery sherds
        int POTTERY_SHERD = 2500;
        List<String> sherds = Arrays.asList("brewer", "blade", "burn", "archer", "arms_up", "shelter", "howl", "heartbreak", "scrape", "guster", "explorer", "miner", "plenty", "heart", "skull", "flow", "sheaf", "danger", "prize", "friend", "mourner", "angler", "snort");
        for (String sherd : sherds) {
            setEMCUnchecked("minecraft:" + sherd + "_pottery_sherd", POTTERY_SHERD);
        }

        // smithing templates (change based on rarity?)
        int SMITHING_TEMPLATE = 9200;
        setEMCUnchecked("minecraft:netherite_upgrade_smithing_template", SMITHING_TEMPLATE + 500);
        List<String> smithing_templates = Arrays.asList("wayfinder", "wild", "coast", "shaper", "snout", "sentry", "spire", "raiser", "tide", "vex", "ward", "bolt", "silence", "eye", "dune", "host", "rib", "flow");
        for (String template : smithing_templates) {
            setEMCUnchecked("minecraft:" + template + "_armor_trim_smithing_template", SMITHING_TEMPLATE);
        }

        loadConfig();

        // EXTRA: minecraft:light?
        // states: minecraft:large_amethyst_bud, minecraft:small_amethyst_bud
        // minecraft:damaged_anvil, minecraft:chipped_anvil, minecraft:written_book
        // minecraft:tipped_arrow
        // helpers: minecraft:knowledge_book, minecraft:debug_stick, minecraft:structure_void
        // past: minecraft:petrified_oak_slab
        // future: minecraft:bundle
    }

    private static void loadConfig() {
        if(ModConfig.EMC_OVERRIDES == null || ModConfig.EMC_OVERRIDES.isEmpty() ){
            VanillaEMC.LOGGER.debug("No EMC Overrides");
            return;
        }

        for (EMCRecord emcOverride : ModConfig.EMC_OVERRIDES) {
            String blockName = emcOverride.getBlockName();
            Integer value = emcOverride.getEmc();

            if (blockName != null && value != null) {
                blockName = blockName.trim();
                if (blockName.isEmpty()) {
                    continue;
                }
                CONFIG_OVERRIDDEN.add(blockName);
                if (value > 0) {
                    VanillaEMC.LOGGER.debug("Setting EMC of {} to {}", blockName, value);
                    setEMCUnchecked(blockName, value);
                } else {
                    removeEMC(blockName);
                }
            }
        }
    }

    private static void removeEMC(String blockName) {
        //remove if defined
        Integer rez = EMC_VALUES.remove(blockName);
        if (rez != null) {
            VanillaEMC.LOGGER.info("Removing EMC value from {}", blockName);
        }
    }

    private static void setEMCUnchecked(
        String blockName,
        Integer value
    ) {
        //has value
        EMC_VALUES.put(blockName, value);
    }

    private static void setEMC(
        String resultId,
        int emcValue
    ) {
        //TODO allow no overrides
        setEMCUnchecked(resultId, emcValue);
    }

    private static void setEMC(HashMap<String, Integer> NEW_EMC_VALUES) {
        //TODO allow no overrides
        EMC_VALUES.putAll(NEW_EMC_VALUES);
    }

    private static boolean tags_loaded = false;
    public static void tagsLoaded(HashMap<String, Integer> NEW_EMC_VALUES) {
        setEMC(NEW_EMC_VALUES);
        tags_loaded = true;

        if (tags_loaded && !RECIPES.isEmpty()) startQuery();
    }

    private static HashMap<String, List<String>> RECIPES = new HashMap<String, List<String>>();
    private static List<String> STONE_CUTTER_LIST = new ArrayList<>();
    public static void recipesLoaded(HashMap<String, List<String>> recipes, List<String> stonecutter) {
        RECIPES = recipes;
        STONE_CUTTER_LIST = stonecutter;

        if (tags_loaded && !RECIPES.isEmpty()) startQuery();
    }

    private static void startQuery() {
        VanillaEMC.LOGGER.info("Searching through " + RECIPES.size() + " recipes!");
        queryRecipes(RECIPES);
    }

    private static List<String> unused = Arrays.asList("minecraft:filled_map", "minecraft:tipped_arrow", "minecraft:debug_stick", "minecraft:small_amethyst_bud", "minecraft:large_amethyst_bud", "minecraft:disc_fragment_5", "minecraft:petrified_oak_slab", "minecraft:suspicious_stew", "minecraft:bundle", "minecraft:enchanted_book", "minecraft:air", "minecraft:ominous_bottle", "minecraft:structure_void", "minecraft:chipped_anvil", "minecraft:firework_star", "minecraft:knowledge_book", "minecraft:light", "minecraft:written_book", "minecraft:damaged_anvil", "minecraft:medium_amethyst_bud");
    static int previousCompletedSize = 0;
    static int loops = 0;
    public static void queryRecipes(HashMap<String, List<String>> RECIPES) {
        loops++;
        for (Map.Entry<String, List<String>> recipe : RECIPES.entrySet()) {
            checkRecipe(recipe);
        }

        // brute force! (i'm sure there's a more optimized way)
        if (COMPLETED.size() != previousCompletedSize) {
            previousCompletedSize = COMPLETED.size();
            queryRecipes(RECIPES);
        } else {
            VanillaEMC.LOGGER.info("Found recipes in " + loops + " loops!");

            List<String> HAS_MULTIPLE = new ArrayList<>();
            RECIPE_ITEM_VALUES.forEach((resultId, emcValues) -> {
                int emcValue = getAverage(emcValues);
                // mostly stonecutter items!
                boolean ignored = resultId.contains("dye") || resultId.contains("copper") || resultId.contains("painting");
                if (emcValues.size() > 1 && !STONE_CUTTER_LIST.contains(resultId) && !HAS_MULTIPLE.contains(resultId) && !ignored) {
                    String blockName = ItemHelper.getName(ItemHelper.getById(resultId));
                    VanillaEMC.LOGGER.info("Found item with multiple different recipes: " + blockName + ". Using average: " + emcValue + " " + emcValues);
                    HAS_MULTIPLE.add(resultId);
                }
                setEMC(resultId, emcValue);

                // add dynamic
                if (resultId.contains("concrete_powder"))
                    setEMC(resultId.substring(0, resultId.indexOf("_powder")), emcValue + 20);
            });
            // if (HAS_MULTIPLE.size() > 0) VanillaEMC.LOGGER.info("FOUND " + (HAS_MULTIPLE.size()) + " ITEMS WITH MULTIPLE DIFFERENT VALUES!");

            // LOG ITEMS WITH MISSING EMC - that does not have a crafting recipe!
            for (String missing : MISSING) {
                if (!COMPLETED.contains(missing) && !unused.contains(missing)) VanillaEMC.LOGGER.info("FOUND ITEM WITH NO RECIPE AND NO EMC: " + ItemHelper.getName(ItemHelper.getById(missing)) + " (" + missing + ")");
            }

            int NOT_FOUND = 0;
            for (RegistryKey<Item> item : Registries.ITEM.getKeys()) {
                String itemId = item.getValue().toString();
                if (!checkItem(itemId)) NOT_FOUND++;
            }

            // VanillaEMC.LOGGER.info("FOUND " + (NOT_FOUND.size()) + " ITEMS WITH NO EMC!");
            VanillaEMC.LOGGER.info("Loaded EMC values for " + EMC_VALUES.size() + " recipes! Could not find value for " + NOT_FOUND + " items.");
        }
    }

    private static List<String> creative_items = Arrays.asList("spawn_egg", "command_block", "bedrock", "barrier", "structure_block", "jigsaw", "spawner", "vault", "end_portal_frame", "budding_amethyst", "reinforced_deepslate");
    private static boolean checkItem(String itemId) {
        // add dynamic (creative items)
        if (ModConfig.CREATIVE_ITEMS) {
            if (itemId.contains("spawn_egg")) {
                setEMC(itemId, 100000);
            }
            return true;
        }

        for (String itemPart : creative_items) {
            if (itemId.contains(itemPart)) return true;
        }

        if (!EMC_VALUES.containsKey(itemId) && !unused.contains(itemId) && !itemId.contains("bucket") && !itemId.contains("potion") && !itemId.contains("infested_") && itemId != "minecraft:air") {
            VanillaEMC.LOGGER.info("No EMC value for item: " + ItemHelper.getName(ItemHelper.getById(itemId)) + " (" + itemId + ")");
            return false;
        }

        return true;
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
    private static void checkRecipe(Map.Entry<String, List<String>> recipe) {
        String id = recipe.getKey();
        if (COMPLETED.contains(id)) return;

        if (EMC_VALUES.containsKey(id.split("__")[0])) {
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
        int extraEMC = Integer.parseInt(parts[2]); // cooking

        // don't allow "children" to change the item they received emc value from
        if (PARENTS.containsKey(resultId)) {
            if (!RECIPE_ITEM_VALUES.containsKey(resultId) || resultId.contains("copper")) return;

            // check if emc values are different
            int previousEMC = RECIPE_ITEM_VALUES.get(resultId).get(0);
            int newEMC = totalInputEMC / resultCount + extraEMC;
            // round up to 1 if below 1
            if (newEMC < 1) newEMC = 1;
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

        totalInputEMC = totalInputEMC / resultCount + extraEMC; // divide value on output item count
        // round up to 1 if below 1
        if (totalInputEMC < 1) totalInputEMC = 1;
        
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
                    if (!recipeKeySearch(itemId) && !MISSING.contains(itemId)) MISSING.add(itemId);
                    return 0;
                }

                if (MISSING.contains(itemId)) MISSING.remove(itemId);
                emcValue = getAverage(RECIPE_ITEM_VALUES.get(itemId));
            }

            totalEmcValue += emcValue;
        }

        return totalEmcValue;
    }

    static private boolean recipeKeySearch(String keyId) {
        for (String key : RECIPES.keySet()){
            if (key.contains(keyId)) return true; // keyId__{outputCount}__{extraEMC}__{craftingIndex}
        }

        return false;
    }
}