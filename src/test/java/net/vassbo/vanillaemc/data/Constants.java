package net.vassbo.vanillaemc.data;

public class Constants {
    enum Modes {
        DEFAULT("default"),
        MOCK_SKYBLOCK("MockskyblockMode");

        final String value;

        Modes(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
