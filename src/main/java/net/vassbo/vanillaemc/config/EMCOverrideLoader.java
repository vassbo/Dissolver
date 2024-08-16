package net.vassbo.vanillaemc.config;

import net.vassbo.vanillaemc.VanillaEMC;
import net.vassbo.vanillaemc.config.model.ConfigEntry;
import net.vassbo.vanillaemc.data.model.EMCRecord;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class EMCOverrideLoader {
    /**
     * use this prefix to not get married to having
     * > <pre>{mod_name}:{block_name}={number}</pre>
     * be the config entry and risk clashing in the future
     */
    public static final String EMC_PREFIX = "emc:";
    private static final int EMC_PREFIX_LENGTH = EMC_PREFIX.length();
    public static final String SPLITTER = ":";
    public static final String ASSUMED_PREFIX = "minecraft:";

    final HashMap<String, String> config;
    final ModConfigProvider existingConfigs;

    public EMCOverrideLoader(
        HashMap<String, String> config,
        ModConfigProvider existingConfigs
    ) {
        this.config = config;
        this.existingConfigs = existingConfigs;
    }

    private boolean isEMCOverride(Map.Entry<String, String> entry) {
        return entry != null && entry.getKey() != null && entry
            .getKey()
            .startsWith(EMC_PREFIX);
    }

    public List<EMCRecord> load() {
        if(config == null){
            return null;
        }

        final Set<String> existingConfigsKeys;
        if (existingConfigs != null) {
            existingConfigsKeys = existingConfigs
                .getConfigsList()
                .stream()
                .map(ConfigEntry::getProperty)
                .collect(Collectors.toSet());
        } else {
            existingConfigsKeys = new HashSet<>();
        }

        return config
            .entrySet()
            .parallelStream()
            .filter(entry -> !existingConfigsKeys.contains(entry.getKey()) && isEMCOverride(entry))
            .map(EMCOverrideLoader::parseEntry)
            .collect(Collectors.toList());
    }

    public static EMCRecord parseEntry(
        Map.Entry<String, String> entry
    ) {
        try {
            String blockName = parseBlockName(entry);
            int emc = Integer.parseInt(entry.getValue());

            return new EMCRecord(blockName, emc);
        } catch (Exception e) {
            VanillaEMC.LOGGER.error("Could not parse EMC override from \"{}={}\"", entry.getKey(), entry.getValue(), e);
            throw e;
        }
    }

    public static String parseBlockName(Map.Entry<String, String> entry) {
        String blockName = entry
            .getKey()
            .substring(EMC_PREFIX_LENGTH);

        if (blockName.isEmpty()) {
            throw new IllegalArgumentException("Invalid EMC override block name, blank.");
        }

        if (blockName.contains(SPLITTER)) {
            if (blockName.indexOf(SPLITTER) != blockName.lastIndexOf(SPLITTER)) {
                throw new IllegalArgumentException(
                    "Invalid EMC override block name, \"" + blockName + "\", contains too many '" + SPLITTER + "'.");
            }
        } else {
            //correct to minecraft:<blockName>
            blockName = ASSUMED_PREFIX + blockName;
        }

        return blockName;
    }
}
