package net.vassbo.vanillaemc.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.OptionalDouble;
import java.util.Set;

import org.spongepowered.asm.mixin.injection.struct.InjectorGroupInfo.Map;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.vassbo.vanillaemc.VanillaEMC;
import net.vassbo.vanillaemc.helpers.ItemHelper;

public class EMCValues {
    private static final boolean INCLUDE_CREATIVE_ITEMS = false;
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

        int DIRT = 1;
        int GRASS = 2;
        int LEAVES = 5;
        int BAMBOO = 1; // this * 9 should be dividable by 2
        int COBBLESTONE = 2; // should be at least 2 (because of slabs)
        int DEEPSLATE = 4;
        int COPPER = 216;
        int IRON = 225; // dividable by 9
        int GOLD = 1350; // dividable by 9
        int DIAMOND = 4200;

        // nature
        EMC_VALUES.put("minecraft:cobblestone", COBBLESTONE);
        EMC_VALUES.put("minecraft:dirt", DIRT);
        EMC_VALUES.put("minecraft:dirt_path", DIRT);
        EMC_VALUES.put("minecraft:farmland", DIRT);
        EMC_VALUES.put("minecraft:gravel", COBBLESTONE);
        EMC_VALUES.put("minecraft:suspicious_gravel", COBBLESTONE);
        EMC_VALUES.put("minecraft:flint", DEEPSLATE);
        EMC_VALUES.put("minecraft:bamboo", BAMBOO);
        EMC_VALUES.put("minecraft:grass_block", GRASS);
        EMC_VALUES.put("minecraft:moss_block", GRASS);
        EMC_VALUES.put("minecraft:raw_iron", IRON - 10); // a bit less (10) than the ingot
        EMC_VALUES.put("minecraft:raw_gold", GOLD - 10); // a bit less (10) than the ingot
        EMC_VALUES.put("minecraft:raw_copper", COPPER - 10); // a bit less (10) than the ingot
        EMC_VALUES.put("minecraft:deepslate", DEEPSLATE);
        EMC_VALUES.put("minecraft:cobbled_deepslate", COBBLESTONE); // has to be same as cobblestone
        EMC_VALUES.put("minecraft:blackstone", COBBLESTONE); // has to be same as cobblestone
        EMC_VALUES.put("minecraft:gilded_blackstone", GOLD); // should be same as gold
        EMC_VALUES.put("minecraft:tuff", DEEPSLATE);
        EMC_VALUES.put("minecraft:mud", DIRT);
        EMC_VALUES.put("minecraft:clay_ball", LEAVES);
        EMC_VALUES.put("minecraft:short_grass", DIRT);
        EMC_VALUES.put("minecraft:tall_grass", DIRT);
        EMC_VALUES.put("minecraft:fern", GRASS);
        EMC_VALUES.put("minecraft:large_fern", GRASS);
        EMC_VALUES.put("minecraft:azalea", LEAVES);
        EMC_VALUES.put("minecraft:mycelium", LEAVES);
        EMC_VALUES.put("minecraft:podzol", GRASS);
        EMC_VALUES.put("minecraft:ice", GRASS);
        EMC_VALUES.put("minecraft:vine", GRASS); // should be same as moss block!
        EMC_VALUES.put("minecraft:glow_lichen", GRASS);
        EMC_VALUES.put("minecraft:lily_pad", GRASS);
        EMC_VALUES.put("minecraft:big_dripleaf", GRASS + 2);
        EMC_VALUES.put("minecraft:small_dripleaf", GRASS + 2);
        EMC_VALUES.put("minecraft:mangrove_roots", LEAVES);
        EMC_VALUES.put("minecraft:sugar_cane", LEAVES * 5); // honey bottle / 3
        EMC_VALUES.put("minecraft:pointed_dripstone", LEAVES);
        EMC_VALUES.put("minecraft:hanging_roots", DIRT);
        EMC_VALUES.put("minecraft:rooted_dirt", LEAVES);
        EMC_VALUES.put("minecraft:cactus", LEAVES);
        EMC_VALUES.put("minecraft:dead_bush", DIRT);
        EMC_VALUES.put("minecraft:snowball", DIRT);
        EMC_VALUES.put("minecraft:stripped_bamboo_block", BAMBOO * 9); // should be the same as bamboo_block
        EMC_VALUES.put("minecraft:pitcher_pod", GRASS);
        EMC_VALUES.put("minecraft:torchflower_seeds", GRASS);
        EMC_VALUES.put("minecraft:honeycomb", LEAVES * 10); // this should not be more than 3, because of copper waxing: ((({copper_block_emc}[[+3]])*3+{redstone+blaze_rod})/4)[[+3]]
        EMC_VALUES.put("minecraft:honey_bottle", LEAVES * 15); // sugar cane * 3
        EMC_VALUES.put("minecraft:bee_nest", 174); // same as beehive
        EMC_VALUES.put("minecraft:goat_horn", 1200);
        EMC_VALUES.put("minecraft:frogspawn", 60);
        EMC_VALUES.put("minecraft:turtle_egg", 70);
        EMC_VALUES.put("minecraft:sniffer_egg", 80);
        // froglight
        EMC_VALUES.put("minecraft:shroomlight", 80);
        EMC_VALUES.put("minecraft:verdant_froglight", 400);
        EMC_VALUES.put("minecraft:ochre_froglight", 400);
        EMC_VALUES.put("minecraft:pearlescent_froglight", 400);
        // mushroom
        EMC_VALUES.put("minecraft:brown_mushroom", 5);
        EMC_VALUES.put("minecraft:red_mushroom", 5);
        EMC_VALUES.put("minecraft:brown_mushroom_block", 20);
        EMC_VALUES.put("minecraft:red_mushroom_block", 20);
        EMC_VALUES.put("minecraft:mushroom_stem", 20);
        // ancient
        EMC_VALUES.put("minecraft:sculk", 600);
        EMC_VALUES.put("minecraft:sculk_sensor", 1400);
        EMC_VALUES.put("minecraft:sculk_shrieker", 2800);
        EMC_VALUES.put("minecraft:sculk_catalyst", 800);
        EMC_VALUES.put("minecraft:sculk_vein", 400);
        EMC_VALUES.put("minecraft:echo_shard", 1800);
        // amethyst
        EMC_VALUES.put("minecraft:amethyst_cluster", 4800);
        EMC_VALUES.put("minecraft:amethyst_shard", 1800);
        EMC_VALUES.put("minecraft:calcite", 600);
        // ocean
        EMC_VALUES.put("minecraft:prismarine_shard", 160);
        EMC_VALUES.put("minecraft:prismarine_crystals", 400);
        EMC_VALUES.put("minecraft:nautilus_shell", 200);
        EMC_VALUES.put("minecraft:kelp", GRASS);
        EMC_VALUES.put("minecraft:sea_pickle", LEAVES);
        EMC_VALUES.put("minecraft:wet_sponge", 90);
        EMC_VALUES.put("minecraft:heart_of_the_sea", 500);
        EMC_VALUES.put("minecraft:seagrass", GRASS);
        // loot
        EMC_VALUES.put("minecraft:string", 12);
        EMC_VALUES.put("minecraft:cobweb", 12);
        EMC_VALUES.put("minecraft:gunpowder", 56);
        EMC_VALUES.put("minecraft:turtle_scute", 180);
        EMC_VALUES.put("minecraft:armadillo_scute", 180);
        EMC_VALUES.put("minecraft:spider_eye", 25);
        EMC_VALUES.put("minecraft:breeze_rod", 90);
        EMC_VALUES.put("minecraft:rabbit_hide", 20); // this * 4 = leather
        EMC_VALUES.put("minecraft:blaze_rod", 60);
        EMC_VALUES.put("minecraft:ender_pearl", 80);
        EMC_VALUES.put("minecraft:ink_sac", 25);
        EMC_VALUES.put("minecraft:glow_ink_sac", 120);
        EMC_VALUES.put("minecraft:feather", 8);
        EMC_VALUES.put("minecraft:egg", 12);
        EMC_VALUES.put("minecraft:ghast_tear", 220);
        EMC_VALUES.put("minecraft:magma_cream", 150);
        EMC_VALUES.put("minecraft:bone", 27);
        EMC_VALUES.put("minecraft:slime_ball", 30);
        EMC_VALUES.put("minecraft:rotten_flesh", DIRT);
        EMC_VALUES.put("minecraft:rabbit_foot", 22);
        EMC_VALUES.put("minecraft:phantom_membrane", 150);
        // crops
        EMC_VALUES.put("minecraft:wheat_seeds", GRASS);
        EMC_VALUES.put("minecraft:wheat", 20);
        EMC_VALUES.put("minecraft:beetroot_seeds", GRASS);
        EMC_VALUES.put("minecraft:beetroot", 20);
        EMC_VALUES.put("minecraft:poisonous_potato", DIRT);
        EMC_VALUES.put("minecraft:potato", 24);
        EMC_VALUES.put("minecraft:carrot", 24);
        EMC_VALUES.put("minecraft:pumpkin", 36); // divisable by 4
        EMC_VALUES.put("minecraft:carved_pumpkin", 40);
        EMC_VALUES.put("minecraft:melon_slice", 4);
        EMC_VALUES.put("minecraft:cocoa_beans", 12);
        // food
        EMC_VALUES.put("minecraft:mutton", 40);
        EMC_VALUES.put("minecraft:porkchop", 50);
        EMC_VALUES.put("minecraft:beef", 50);
        EMC_VALUES.put("minecraft:chicken", 40);
        EMC_VALUES.put("minecraft:rabbit", 40);
        EMC_VALUES.put("minecraft:apple", 30);
        EMC_VALUES.put("minecraft:cod", 35);
        EMC_VALUES.put("minecraft:salmon", 35);
        EMC_VALUES.put("minecraft:tropical_fish", 35);
        EMC_VALUES.put("minecraft:pufferfish", 38);
        EMC_VALUES.put("minecraft:sweet_berries", 18);
        EMC_VALUES.put("minecraft:glow_berries", 24);
        // buckets
        int BUCKET = IRON * 3;
        EMC_VALUES.put("minecraft:water_bucket", BUCKET + 20);
        EMC_VALUES.put("minecraft:lava_bucket", BUCKET + 200);
        EMC_VALUES.put("minecraft:milk_bucket", BUCKET + 50);
        EMC_VALUES.put("minecraft:powder_snow_bucket", BUCKET + 80);
        // nether
        int ANCIENT_DEBRIS = 800;
        EMC_VALUES.put("minecraft:ancient_debris", ANCIENT_DEBRIS);
        EMC_VALUES.put("minecraft:obsidian", 140);
        EMC_VALUES.put("minecraft:crying_obsidian", 140);
        EMC_VALUES.put("minecraft:glowstone_dust", 38);
        EMC_VALUES.put("minecraft:nether_wart", 40);
        EMC_VALUES.put("minecraft:basalt", DEEPSLATE);
        EMC_VALUES.put("minecraft:nether_quartz_ore", 120);
        EMC_VALUES.put("minecraft:netherrack", DIRT);
        EMC_VALUES.put("minecraft:soul_sand", 3);
        EMC_VALUES.put("minecraft:soul_soil", 3); // should be same as soul sand
        EMC_VALUES.put("minecraft:warped_fungus", GRASS);
        EMC_VALUES.put("minecraft:crimson_fungus", GRASS);
        EMC_VALUES.put("minecraft:warped_roots", GRASS);
        EMC_VALUES.put("minecraft:nether_sprouts", GRASS);
        EMC_VALUES.put("minecraft:crimson_nylium", GRASS);
        EMC_VALUES.put("minecraft:crimson_roots", GRASS);
        EMC_VALUES.put("minecraft:twisting_vines", GRASS);
        EMC_VALUES.put("minecraft:weeping_vines", GRASS);
        EMC_VALUES.put("minecraft:warped_nylium", GRASS);
        EMC_VALUES.put("minecraft:warped_wart_block", LEAVES);
        // end
        int SHULKER_SHELL = 1000;
        EMC_VALUES.put("minecraft:chorus_fruit", 50);
        EMC_VALUES.put("minecraft:chorus_plant", 5); // same as chorus flower ?
        EMC_VALUES.put("minecraft:shulker_shell", SHULKER_SHELL);
        EMC_VALUES.put("minecraft:end_stone", 12);
        EMC_VALUES.put("minecraft:dragon_breath", 150);
        // heads
        EMC_VALUES.put("minecraft:dragon_head", 1500); // should be same as elytra (asme rarity)
        EMC_VALUES.put("minecraft:wither_skeleton_skull", 4000);
        EMC_VALUES.put("minecraft:creeper_head", 10000);
        EMC_VALUES.put("minecraft:piglin_head", 10000);
        EMC_VALUES.put("minecraft:skeleton_skull", 10000);
        EMC_VALUES.put("minecraft:zombie_head", 10000);
        EMC_VALUES.put("minecraft:player_head", 12000);
        // armor
        int CHAINMAIL = (int)(IRON * 0.8); // CUSTOM VALUE
        EMC_VALUES.put("minecraft:chainmail_helmet", CHAINMAIL * 5);
        EMC_VALUES.put("minecraft:chainmail_chestplate", CHAINMAIL * 8);
        EMC_VALUES.put("minecraft:chainmail_leggings", CHAINMAIL * 7);
        EMC_VALUES.put("minecraft:chainmail_boots", CHAINMAIL * 4);
        EMC_VALUES.put("minecraft:diamond_horse_armor", (int)(DIAMOND * 1.2));
        EMC_VALUES.put("minecraft:golden_horse_armor", (int)(GOLD * 1.2));
        EMC_VALUES.put("minecraft:iron_horse_armor", (int)(IRON * 1.2));
        int NETHERITE_INGOT = (int)(ANCIENT_DEBRIS * 4.455);
        EMC_VALUES.put("minecraft:netherite_sword", NETHERITE_INGOT + (DIAMOND * 2) + 2);
        EMC_VALUES.put("minecraft:netherite_pickaxe", NETHERITE_INGOT + (DIAMOND * 3) + (2 * 2));
        EMC_VALUES.put("minecraft:netherite_axe", NETHERITE_INGOT + (DIAMOND * 3) + (2 * 2));
        EMC_VALUES.put("minecraft:netherite_shovel", NETHERITE_INGOT + DIAMOND + (2 * 2));
        EMC_VALUES.put("minecraft:netherite_hoe", NETHERITE_INGOT + (DIAMOND * 2) + (2 * 2));
        EMC_VALUES.put("minecraft:netherite_helmet", NETHERITE_INGOT + (DIAMOND * 5));
        EMC_VALUES.put("minecraft:netherite_chestplate", NETHERITE_INGOT + (DIAMOND * 8));
        EMC_VALUES.put("minecraft:netherite_leggings", NETHERITE_INGOT + (DIAMOND * 7));
        EMC_VALUES.put("minecraft:netherite_boots", NETHERITE_INGOT + (DIAMOND * 4));
        // special
        EMC_VALUES.put("minecraft:dragon_egg", 180000);
        EMC_VALUES.put("minecraft:nether_star", 120000);
        EMC_VALUES.put("minecraft:enchanted_golden_apple", 50000);
        EMC_VALUES.put("minecraft:heavy_core", 2200);
        EMC_VALUES.put("minecraft:experience_bottle", 600);
        EMC_VALUES.put("minecraft:trident", 3000);
        EMC_VALUES.put("minecraft:saddle", 250);
        EMC_VALUES.put("minecraft:name_tag", 250);
        EMC_VALUES.put("minecraft:bell", 800);
        EMC_VALUES.put("minecraft:elytra", 1500);
        EMC_VALUES.put("minecraft:trial_key", 1200);
        EMC_VALUES.put("minecraft:ominous_trial_key", 1300);
        EMC_VALUES.put("minecraft:totem_of_undying", 60000);

        // creative
        if (INCLUDE_CREATIVE_ITEMS) {
            EMC_VALUES.put("minecraft:bedrock", 1000000);
            EMC_VALUES.put("minecraft:barrier", 1000000);
            EMC_VALUES.put("minecraft:end_portal_frame", 800000);
            EMC_VALUES.put("minecraft:reinforced_deepslate", 100000);
            EMC_VALUES.put("minecraft:structure_block", 400000);
            EMC_VALUES.put("minecraft:jigsaw", 400000);
            EMC_VALUES.put("minecraft:command_block", 500000);
            EMC_VALUES.put("minecraft:repeating_command_block", 500000);
            EMC_VALUES.put("minecraft:chain_command_block", 500000);
            EMC_VALUES.put("minecraft:command_block_minecart", 500000 + 2250);
            EMC_VALUES.put("minecraft:vault", 400000);
            EMC_VALUES.put("minecraft:trial_spawner", 400000);
            EMC_VALUES.put("minecraft:spawner", 400000);
            EMC_VALUES.put("minecraft:budding_amethyst", 8000);
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
        // banner pattern
        // EMC_VALUES.put("minecraft:flow_banner_pattern", 300);
        // EMC_VALUES.put("minecraft:guster_banner_pattern", 300);
        // EMC_VALUES.put("minecraft:piglin_banner_pattern", 300);
        // EMC_VALUES.put("minecraft:globe_banner_pattern", 300);
        
        // BY TAGS (https://mcreator.net/wiki/minecraft-item-tags-list)
        // nature
        int LOGS = 16; // bamboo planks (BAMBOO * 9 / 2) * 4 (all planks should be same EMC)
        int FLOWERS = 8; // at least 8 so dyed glass is more expensive
        EMC_TAG_VALUES.put("minecraft:stone", COBBLESTONE * 2);
        EMC_TAG_VALUES.put("minecraft:logs", LOGS); // dividable by 4
        EMC_TAG_VALUES.put("minecraft:sand", COBBLESTONE);
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
        // EMC_VALUES.put("minecraft:chest", LOGS * 2);
        EMC_VALUES.put("minecraft:charcoal", COAL); // same as coal because it should be the same emc!
        // WIP THESE DON'T WORK::
        // EMC_VALUES.put("minecraft:shield", IRON + (LOGS / 4 * 6)); // this recipe did was not found
        // EMC_VALUES.put("minecraft:crossbow", 704); // this recipe did was not found (IRON+(STICK*2)+(STRING*2)+TRIPWIRE_HOOK)

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
                if (state != "") EMC_VALUES.put("minecraft:" + state + "copper" + (type != "block" ? "_" + type : ""), value / (type == "door" ? 3 : type == "trapdoor" ? 2 : 1));
                // EMC_VALUES.put("minecraft:waxed_" + state + "copper" + (type != "block" || state == "" ? "_" + type : ""), value + 12);
            }
        }

        // coral
        List<String> coral_blocks = Arrays.asList("tube", "brain", "bubble", "fire", "horn");
        List<String> coral_types = Arrays.asList("", "_block", "_fan");
        int CORAL = 4;
        for (String block : coral_blocks) {
            for (String type : coral_types) {
                EMC_VALUES.put("minecraft:" + block + "_coral" + type, CORAL * (type == "_block" ? 2 : 1));
                EMC_VALUES.put("minecraft:dead_" + block + "_coral" + type, CORAL / (type == "_block" ? 1 : 2));
            }
        }
        
        // saplings
        List<String> saplings = Arrays.asList("oak", "spruce", "birch", "jungle", "acacia", "dark_oak", "cherry");
        for (String sapling : saplings) {
            EMC_VALUES.put("minecraft:" + sapling + "_sapling", 3);
        }

        // shulker boxes & dyes
        int SHULKER_BOX = LOGS * 2 + (SHULKER_SHELL * 2);
        List<String> colors = Arrays.asList("black", "blue", "brown", "cyan", "gray", "green", "light_blue", "light_gray", "lime", "magenta", "orange", "pink", "purple", "red", "white", "yellow");
        for (String color : colors) {
            EMC_VALUES.put("minecraft:" + color + "_shulker_box", SHULKER_BOX);
            EMC_VALUES.put("minecraft:" + color + "_dye", FLOWERS);
        }
        // EMC_TAG_VALUES.put("minecraft:dyes", FLOWERS); // all should be same because of all the recipes using dyes

        // music discs
        int MUSIC_DISC = 8000;
        List<String> discs = Arrays.asList("mall", "chirp", "wait", "strad", "creator", "far", "cat", "ward", "blocks", "otherside", "relic", "11", "mellohi", "pigstep", "13", "stal", "creator_music_box", "precipice", "5");
        for (String disc : discs) {
            EMC_VALUES.put("minecraft:music_disc_" + disc, MUSIC_DISC);
        }

        // pottery sherds
        int POTTERY_SHERD = 2500;
        List<String> sherds = Arrays.asList("brewer", "blade", "burn", "archer", "arms_up", "shelter", "howl", "heartbreak", "scrape", "guster", "explorer", "miner", "plenty", "heart", "skull", "flow", "sheaf", "danger", "prize", "friend", "mourner", "angler", "snort");
        for (String sherd : sherds) {
            EMC_VALUES.put("minecraft:" + sherd + "_pottery_sherd", POTTERY_SHERD);
        }

        // smithing templates (change based on rarity?)
        int SMITHING_TEMPLATE = 9200;
        EMC_VALUES.put("minecraft:netherite_upgrade_smithing_template", SMITHING_TEMPLATE + 500);
        List<String> smithing_templates = Arrays.asList("wayfinder", "wild", "coast", "shaper", "snout", "sentry", "spire", "raiser", "tide", "vex", "ward", "bolt", "silence", "eye", "dune", "host", "rib", "flow");
        for (String template : smithing_templates) {
            EMC_VALUES.put("minecraft:" + template + "_armor_trim_smithing_template", SMITHING_TEMPLATE);
        }

        // EXTRA: minecraft:light?
        // states: minecraft:large_amethyst_bud, minecraft:small_amethyst_bud
        // minecraft:damaged_anvil, minecraft:chipped_anvil, minecraft:written_book
        // minecraft:tipped_arrow
        // helpers: minecraft:knowledge_book, minecraft:debug_stick, minecraft:structure_void
        // past: minecraft:petrified_oak_slab
        // future: minecraft:bundle
    }

    private static boolean tags_loaded = false;
    public static void tagsLoaded(HashMap<String, Integer> NEW_EMC_VALUES) {
        EMC_VALUES.putAll(NEW_EMC_VALUES);
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
                EMC_VALUES.put(resultId, emcValue);
                
                // add dynamic
                if (resultId.contains("concrete_powder")) EMC_VALUES.put(resultId.substring(0, resultId.indexOf("_powder")), emcValue + 20);
            });
            // if (HAS_MULTIPLE.size() > 0) VanillaEMC.LOGGER.info("FOUND " + (HAS_MULTIPLE.size()) + " ITEMS WITH MULTIPLE DIFFERENT VALUES!");

            // LOG ITEMS WITH MISSING EMC - that does not have a crafting recipe!
            List<String> unused = Arrays.asList("minecraft:filled_map", "minecraft:tipped_arrow", "minecraft:debug_stick", "minecraft:small_amethyst_bud", "minecraft:large_amethyst_bud", "minecraft:disc_fragment_5");
            for (String missing : MISSING) {
                if (!COMPLETED.contains(missing) && !unused.contains(missing)) VanillaEMC.LOGGER.info("FOUND ITEM WITH NO RECIPE AND NO EMC: " + ItemHelper.getName(ItemHelper.getById(missing)) + " (" +missing+ ")");
            }

            int NOT_FOUND = 0;
            List<String> creative_items = Arrays.asList("spawn_egg", "command_block", "bedrock", "barrier");
            for (RegistryKey<Item> item : Registries.ITEM.getKeys()) {
                String itemId = item.getValue().toString();

                // add dynamic (creative items)
                if (INCLUDE_CREATIVE_ITEMS) {
                    if (itemId.contains("spawn_egg")) {
                        EMC_VALUES.put(itemId, 100000);
                    }
                    return;
                }

                for (String itemPart : creative_items) {
                    if (itemId.contains(itemPart)) return;
                }

                if (!EMC_VALUES.containsKey(itemId) && !unused.contains(itemId) && !itemId.contains("bucket") && !itemId.contains("potion") && !itemId.contains("banner") && !itemId.contains("infested_") && itemId != "minecraft:air") {
                    NOT_FOUND++;
                    VanillaEMC.LOGGER.info("No EMC value for item: " + ItemHelper.getName(ItemHelper.getById(itemId)) + " (" + itemId + ")");
                }
            }

            // VanillaEMC.LOGGER.info("FOUND " + (NOT_FOUND.size()) + " ITEMS WITH NO EMC!");
            VanillaEMC.LOGGER.info("Loaded EMC values for " + EMC_VALUES.size() + " recipes! Could not find value for " + NOT_FOUND + " items.");
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
    private static void checkRecipe(Map.Entry<String, List<String>> recipe) {
        String id = recipe.getKey();
        if (COMPLETED.contains(id)) {
            // // WIP move if useful (only needed by copper)
            // // double check value (average might have changed)
            // String[] parts = id.split("__");
            // String resultId = parts[0];
            // int resultCount = Integer.parseInt(parts[1]);
            // int extraEMC = Integer.parseInt(parts[2]); // cooking
            // if (EMC_VALUES.containsKey(resultId)) return;
            // if (RECIPE_ITEM_VALUES.containsKey(resultId) && RECIPE_ITEM_VALUES.get(resultId).size() > 1) {
            //     List<String> ingredients = recipe.getValue();
            //     int totalInputEMC = combineEMC(ingredients);
            //     List<Integer> values = new ArrayList<>();
            //     if (RECIPE_ITEM_VALUES.containsKey(resultId)) values = RECIPE_ITEM_VALUES.get(resultId);
            //     List<Integer> oldValues = new ArrayList<Integer>(values);
            //     totalInputEMC = totalInputEMC / resultCount + extraEMC; // divide value on output item count
            //     if (totalInputEMC < 1) totalInputEMC = 1; // round up to 1
            //     if (values.contains(totalInputEMC)) return; // same value
            //     values.add(totalInputEMC);
            //     VanillaEMC.LOGGER.info("CHANGED ITEM EMC OF ITEM: " + resultId + " FROM:" + oldValues + ". TO: " + values);
            //     RECIPE_ITEM_VALUES.put(resultId, values);
            //     previousCompletedSize--;
            // }
            return;
        }

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