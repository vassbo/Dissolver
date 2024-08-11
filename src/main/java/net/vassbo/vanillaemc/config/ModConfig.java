package net.vassbo.vanillaemc.config;

import com.mojang.datafixers.util.Pair;

import net.vassbo.vanillaemc.VanillaEMC;
import net.vassbo.vanillaemc.data.model.EMCRecord;

import java.util.List;

public class ModConfig {
    public static SimpleConfig CONFIG;
    private static ModConfigProvider configs;

    public static boolean EMC_ON_HUD;
    public static boolean PRIVATE_EMC;
    public static boolean CREATIVE_ITEMS;
    public static String DIFFICULTY;
    public static String MODE;
    public static List<EMCRecord> EMC_OVERRIDES;

    public static void init() {
        configs = new ModConfigProvider();
        createConfigs();

        CONFIG = SimpleConfig.of(VanillaEMC.MOD_ID).provider(configs).request();

        assignConfigs();
    }

    private static void createConfigs() {
        configs.addKeyValuePair(new Pair<>("emc_on_hud", false), "Display current EMC on HUD (top left corner)");
        // WIP not added (Note: Turning this on will disable redstone integration.)
        configs.addKeyValuePair(new Pair<>("private_emc", false), "Should each player have their own EMC storage?");
        configs.addKeyValuePair(new Pair<>("creative_items", false), "Should creative items have EMC?");
        configs.addKeyValuePair(new Pair<>("difficulty", "hard"), "easy | normal | hard - Changes crafting recipe for Dissolver block.");
        configs.addKeyValuePair(new Pair<>("mode", "default"), "default | skyblock - Changes some EMC values.");
    }

    private static void assignConfigs() {
        EMC_ON_HUD = CONFIG.getOrDefault("emc_on_hud", false);
        PRIVATE_EMC = CONFIG.getOrDefault("private_emc", false);
        CREATIVE_ITEMS = CONFIG.getOrDefault("creative_items", false);
        DIFFICULTY = CONFIG.getOrDefault("difficulty", "hard");
        MODE = CONFIG.getOrDefault("mode", "default");
    }
}