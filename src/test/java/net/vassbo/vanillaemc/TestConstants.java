package net.vassbo.vanillaemc;

import java.util.Arrays;

public class TestConstants {
    public enum Modes {
        DEFAULT("default"), MOCK_SKYBLOCK("MockskyblockMode");

        final String value;

        Modes(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public class Configs {
        public final static String FILE_NAME = "vanillaemc.properties";


        public enum Directories {
            EMPTY("src/test/resources/config/empty",
                Arrays.asList("emc_on_hud=false # Display current EMC on HUD (top left corner) [default: false]",
                    "private_emc=false # Should each player have their own EMC storage? [default: false]",
                    "creative_items=false # Should creative items have EMC? [default: false]",
                    "difficulty=hard # easy | normal | hard - Changes crafting recipe for Dissolver block. [default: hard]",
                    "mode=default # default | skyblock - Changes some EMC values. [default: default]"
                )),

            /** default file */
            DEFAULT("src/test/resources/config/defaults",
                Arrays.asList("emc_on_hud=false # Display current EMC on HUD (top left corner) [default: false]",
                    "private_emc=false # Should each player have their own EMC storage? [default: false]",
                    "creative_items=false # Should creative items have EMC? [default: false]",
                    "difficulty=hard # easy | normal | hard - Changes crafting recipe for Dissolver block. [default: hard]",
                    "mode=default # default | skyblock - Changes some EMC values. [default: default]"
                )),

            /** all the booleans are flipped */
            INVERTED("src/test/resources/config/inverted",
                Arrays.asList("emc_on_hud=true # Display current EMC on HUD (top left corner) [default: false]",
                    "private_emc=true # Should each player have their own EMC storage? [default: false]",
                    "creative_items=true # Should creative items have EMC? [default: false]",
                    "difficulty=easy # easy | normal | hard - Changes crafting recipe for Dissolver block. [default: hard]",
                    "mode=skyblock # default | skyblock - Changes some EMC values. [default: default]"
                ));

            final String value;
            final Iterable<String> expectedConfig;

            Directories(String value,
                        Iterable<String> expectedConfig
            ) {
                this.value = value;
                this.expectedConfig = expectedConfig;
            }

            public String getValue() {
                return value;
            }

            public Iterable<String> getExpectedConfig() {
                return expectedConfig;
            }
        }

    }
}
