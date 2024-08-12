package net.vassbo.vanillaemc.config;

import net.vassbo.vanillaemc.config.model.ConfigConstants;
import net.vassbo.vanillaemc.data.model.EMCRecord;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EMCOverrideLoaderTest {

    @Test
    void none_null_case() {
        List<EMCRecord> actual = new EMCOverrideLoader(null, null).load();

        assertThat(actual).isNullOrEmpty();
    }

    @Test
    void none_blank_case() {
        List<EMCRecord> actual = new EMCOverrideLoader(new HashMap<>(), null).load();

        assertThat(actual).isNullOrEmpty();
    }

    @Test
    void none_null_case_alt() {
        List<EMCRecord> actual = new EMCOverrideLoader(null, defaultConfigs()).load();

        assertThat(actual).isNullOrEmpty();
    }

    @Test
    void none_blank_case_alt() {
        List<EMCRecord> actual = new EMCOverrideLoader(new HashMap<>(), defaultConfigs()).load();

        assertThat(actual).isNullOrEmpty();
    }

    @Test
    void no_block_names() {
        HashMap<String, String> config = getDefaultLoadedConfig();

        List<EMCRecord> actual = new EMCOverrideLoader(config, defaultConfigs()).load();

        assertThat(actual).isNullOrEmpty();
    }

    @Test
    void other_fluff() {
        HashMap<String, String> config = getDefaultLoadedConfig();
        config.put("some:config", "123"); //no emc: prefix so is ignored

        List<EMCRecord> actual = new EMCOverrideLoader(config, defaultConfigs()).load();

        assertThat(actual).isNullOrEmpty();
    }

    @Test
    void add_prefix_block_name() {
        HashMap<String, String> config = getDefaultLoadedConfig();
        config.put("emc:testcase", "12");

        List<EMCRecord> actual = new EMCOverrideLoader(config, defaultConfigs()).load();

        assertThat(actual).containsExactly(new EMCRecord("minecraft:testcase", 12));
    }

    @Test
    void add_block_name() {
        HashMap<String, String> config = getDefaultLoadedConfig();
        config.put("emc:test:case", "12345");

        List<EMCRecord> actual = new EMCOverrideLoader(config, defaultConfigs()).load();

        assertThat(actual).containsExactly(new EMCRecord("test:case", 12345));
    }

    @Test
    void illegal_block_name() {
        HashMap<String, String> config = getDefaultLoadedConfig();
        String blockName = "illegal:test:case";
        config.put("emc:" + blockName, "12345");

        assertThatThrownBy(()->{
            new EMCOverrideLoader(config, defaultConfigs()).load();
        }).isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Invalid EMC override block name, \"" + blockName + "\", contains too many ':'.");
    }

    private static HashMap<String, String> getDefaultLoadedConfig() {
        HashMap<String, String> config = new HashMap<>();
        for (ConfigConstants value : ConfigConstants.values()) {
            config.put(
                value.asConfigEntry().getProperty(),
                String.valueOf(value.asConfigEntry().getDefault())
            );
        }
        return config;
    }

    private static ModConfigProvider defaultConfigs() {
        ModConfigProvider configs = new ModConfigProvider();

        for (ConfigConstants value : ConfigConstants.values()) {
            configs.addKeyValuePair(value.asConfigEntry(), value.getComment());
        }

        return configs;
    }
}