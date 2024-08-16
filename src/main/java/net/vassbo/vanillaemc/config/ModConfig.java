package net.vassbo.vanillaemc.config;

import net.vassbo.vanillaemc.VanillaEMC;
import net.vassbo.vanillaemc.config.model.ConfigConstants;
import net.vassbo.vanillaemc.data.model.EMCRecord;

import java.util.List;

public class ModConfig {
    public static SimpleConfig CONFIG;
    protected static ModConfigProvider configs;

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
        for (ConfigConstants value : ConfigConstants.values()) {
            configs.addKeyValuePair(value.asConfigEntry(), value.getComment());
        }
    }

    private static void assignConfigs() {
        EMC_ON_HUD = CONFIG.getOrDefault("emc_on_hud", false);
        PRIVATE_EMC = CONFIG.getOrDefault("private_emc", false);
        CREATIVE_ITEMS = CONFIG.getOrDefault("creative_items", false);
        DIFFICULTY = CONFIG.getOrDefault("difficulty", "hard");
        MODE = CONFIG.getOrDefault("mode", "default");

        EMC_OVERRIDES = new EMCOverrideLoader(CONFIG.getConfig(), configs).load();
    }
}