package net.vassbo.vanillaemc.config;

import net.fabricmc.loader.api.FabricLoader;
import net.vassbo.vanillaemc.TestConstants;
import net.vassbo.vanillaemc.config.model.ConfigEntry;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class ModConfigTest {
    class VisibilityModifier
        extends ModConfig {

        ModConfigProvider configs() {
            return VisibilityModifier.configs;
        }

    }

    VisibilityModifier underTest;

    @BeforeEach
    void setUp() {
        underTest = new VisibilityModifier();
    }

    @Test
    void init_fresh() throws IOException {
        TestConstants.Configs.Directories directory = TestConstants.Configs.Directories.EMPTY;
        defaultsTest(directory, true);

        checkValues(directory);

    }

    @Test
    void init_existing_defaults() throws IOException {
        TestConstants.Configs.Directories directory = TestConstants.Configs.Directories.DEFAULT;
        defaultsTest(directory, false);

        checkValues(directory);

    }


    @Test
    void init_existing_inverts() throws IOException {
        TestConstants.Configs.Directories directory = TestConstants.Configs.Directories.INVERTED;
        defaultsTest(directory, false);

        checkValues(directory);
    }

    private void checkValues(TestConstants.Configs.Directories directory) {
//todo
    }

    private void defaultsTest(TestConstants.Configs.Directories directory,
                              boolean shouldDeleteExistingConfig
    ) throws IOException {
        try (MockedStatic<FabricLoader> staticMock = mockFabricConfigDirectory(directory, shouldDeleteExistingConfig);) {

            ModConfigProvider expected = new ModConfigProvider();
            expected.addKeyValuePair(new ConfigEntry<>("emc_on_hud", false),
                "Display current EMC on HUD (top left corner)"
            );
            // WIP not added (Note: Turning this on will disable redstone integration.)

            expected.addKeyValuePair(new ConfigEntry<>("private_emc", false),
                "Should each player have their own EMC storage?"
            );
            expected.addKeyValuePair(new ConfigEntry<>("creative_items", false), "Should creative items have EMC?");

            expected.addKeyValuePair(
                new ConfigEntry<>("difficulty", "hard"),
                "easy | normal | hard - Changes crafting recipe for Dissolver block."
            );

            expected.addKeyValuePair(new ConfigEntry<>("mode", "default"),
                "default | skyblock - Changes some EMC values."
            );

            ModConfig.init();

            ModConfigProvider actual = underTest.configs();

            assertThat(actual.getConfigsList()).containsAll(expected.getConfigsList());
        }

        verifyDefaultFile(directory);
    }

    private static void verifyDefaultFile(TestConstants.Configs.Directories directory) throws IOException {
        Configuration dirActual = getMyConfigLocation(directory);

        assertThat(Files.readAllLines(dirActual.config.toPath(), Charset.defaultCharset())).containsAll(
            directory.getExpectedConfig());
    }

    private static MockedStatic<FabricLoader> mockFabricConfigDirectory(
        TestConstants.Configs.Directories directories,
        boolean shouldDelete
    ) {
        return mockFabric(setUpFabricMock(directories, shouldDelete));
    }

    private static MockedStatic<FabricLoader> mockFabric(FabricLoader mockFabric) {
        MockedStatic<FabricLoader> staticMock = Mockito.mockStatic(FabricLoader.class);
        staticMock
            .when(FabricLoader::getInstance)
            .thenReturn(mockFabric);
        return staticMock;
    }

    private static FabricLoader setUpFabricMock(TestConstants.Configs.Directories directories,
                                                boolean shouldDelete
    ) {
        Configuration myConfigLocation = getMyConfigLocation(directories);

        Path path = myConfigLocation
            .file()
            .toPath();

        if (shouldDelete &&  myConfigLocation
            .config()
            .exists()) {
            assertThat(myConfigLocation
                .config()
                .delete())
                .as("Empty folder should be empty")
                .isTrue();
        }

        FabricLoader mockFabric = Mockito.mock(FabricLoader.class);
        when(mockFabric.getConfigDir()).thenReturn(path);
        return mockFabric;
    }

    private static @NotNull Configuration getMyConfigLocation(TestConstants.Configs.Directories directories) {
        File file = new File(directories.getValue());
        File config = new File(file, TestConstants.Configs.FILE_NAME);
        Configuration myConfigLocation = new Configuration(file, config);
        return myConfigLocation;
    }

    private record Configuration(File file, File config) {}
}