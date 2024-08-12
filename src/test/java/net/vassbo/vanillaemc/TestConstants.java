package net.vassbo.vanillaemc;

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
            EMPTY("src/test/resources/config/empty"),

            /** default file */
            DEFAULT("src/test/resources/config/defaults"),

            /** all the booleans are flipped */
            INVERTED("src/test/resources/config/inverted");

            final String value;

            Directories(String value) {
                this.value = value;
            }

            public String getValue() {
                return value;
            }
        }

    }
}
