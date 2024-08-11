package net.vassbo.vanillaemc.data;

import net.vassbo.vanillaemc.TestConstants;
import net.vassbo.vanillaemc.config.ModConfig;
import net.vassbo.vanillaemc.data.model.EMCRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static net.vassbo.vanillaemc.data.EMCExpected.common;
import static org.assertj.core.api.Assertions.assertThat;

class EMCValuesTest {
    class VisibilityModifier
        extends EMCValues {
        public HashMap<String, Integer> EMC_VALUES() {return EMC_VALUES;}

        public Collection<String> CONFIG_OVERRIDDEN() {
            return CONFIG_OVERRIDDEN;
        }

    }

    VisibilityModifier underTest;

    @BeforeEach
    void setUp() {
        underTest = new VisibilityModifier();
        underTest
            .EMC_VALUES()
            .clear();//this is a static on the actual. not gonna mess with that for now

        underTest
            .CONFIG_OVERRIDDEN()
            .clear();//same pattern

        ModConfig.EMC_OVERRIDES = null;
        ModConfig.MODE = null;
    }

    @Test
    void test_init_overrides() {

        ModConfig.EMC_OVERRIDES = Arrays.asList(
            //Test adding a new item
            new EMCRecord("test:case", 4),

            //Test adding an override
            new EMCRecord("minecraft:dirt", 10)
        );

        ModConfig.MODE = TestConstants.Modes.DEFAULT.getValue();
        EMCValues.init();

        List<EMCRecord> list = new ArrayList<>(common());

        list.add(new EMCRecord("test:case", 4));
        list.add(new EMCRecord("minecraft:dirt", 10));

        validateEMCValues(list);
    }

    @Test
    void test_init_overrides_dirt_has_no_emc() {

        ModConfig.EMC_OVERRIDES = Arrays.asList(
            //Test adding a new item
            new EMCRecord("test:case", 4),

            //Test adding an override
            new EMCRecord("minecraft:dirt", 0)
        );

        ModConfig.MODE = TestConstants.Modes.DEFAULT.getValue();
        EMCValues.init();

        List<EMCRecord> list = new ArrayList<>(common());

        list.add(new EMCRecord("test:case", 4));

        validateEMCValues(list);
    }

    @Test
    void test_init_default() {


        ModConfig.MODE = TestConstants.Modes.DEFAULT.getValue();
        EMCValues.init();


        validateEMCValues(EMCExpected.standard());
    }

    @Test
    void test_init_skyblocks() {


        ModConfig.MODE = TestConstants.Modes.MOCK_SKYBLOCK.getValue();
        EMCValues.init();

        validateEMCValues(EMCExpected.skyblocks());
    }


    private void validateEMCValues(List<EMCRecord> expected) {
        Set<String> set = EMCValues.getList();

        assertThat(set).hasSize(expected.size());

        for (EMCRecord emcRecord : expected) {
            assertThat(EMCValues.get(emcRecord.getBlockName()))
                .as("EMC Value of " + emcRecord.getBlockName())
                .isEqualTo(emcRecord.getEmc());
        }
    }

}