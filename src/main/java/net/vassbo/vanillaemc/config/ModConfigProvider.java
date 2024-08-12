package net.vassbo.vanillaemc.config;


import net.vassbo.vanillaemc.config.model.ConfigEntry;

import java.util.ArrayList;
import java.util.List;

public class ModConfigProvider implements SimpleConfig.DefaultConfig {

    private String configContents = "";

    public List<ConfigEntry<?>> getConfigsList() {
        return configsList;
    }

    private final List<ConfigEntry<?>> configsList = new ArrayList<>();

    public void addKeyValuePair(ConfigEntry<?> keyValuePair, String comment) {
        configsList.add(keyValuePair);
        configContents += keyValuePair.getProperty() + "=" + keyValuePair.getDefault() + " # "
                + comment + " [default: " + keyValuePair.getDefault() + "]\n";
    }

    @Override
    public String get(String namespace) {
        return configContents;
    }
}