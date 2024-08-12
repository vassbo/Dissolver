package net.vassbo.vanillaemc.config.model;

public enum ConfigConstants {
    EMC_ON_HUD(
        new ConfigEntry<>("emc_on_hud", false),
        "Display current EMC on HUD (top left corner)"
    ),

    PRIVATE_EMC(
        new ConfigEntry<>("private_emc", false),
        "Should each player have their own EMC storage?"
        //todo maybe? ...
        // + "Note: Turning this on will disable redstone integration."
    ),

    CREATIVE_ITEMS(
        new ConfigEntry<>("creative_items", false),
        "Should creative items have EMC?"
    ),

    DIFFICULTY(
        new ConfigEntry<>("difficulty", "hard"),
        "easy | normal | hard - Changes crafting recipe for Dissolver block."
    ),

    MODE(
        new ConfigEntry<>("mode", "default"),
        "default | skyblock - Changes some EMC values."
    );

    ;

    final ConfigEntry<?> configEntry;
    final String comment;

    <T> ConfigConstants(
        ConfigEntry<T> configEntry,
        String comment
    ) {
        this.configEntry = configEntry;
        this.comment = comment;
    }

    public ConfigEntry<?> asConfigEntry(){
        return configEntry;
    }

    public String getComment() {
        return comment;
    }
}
