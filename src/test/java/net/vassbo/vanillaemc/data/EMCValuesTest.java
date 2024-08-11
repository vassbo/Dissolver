package net.vassbo.vanillaemc.data;

import net.vassbo.vanillaemc.config.ModConfig;
import net.vassbo.vanillaemc.data.model.EMCRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class EMCValuesTest {
    class VisibilityModifier
        extends EMCValues {
        public HashMap<String, Integer> EMC_VALUES() {return EMC_VALUES;}
    }

    VisibilityModifier underTest;

    @BeforeEach
    void setUp() {
        underTest = new VisibilityModifier();
        underTest
            .EMC_VALUES()
            .clear();//this is a static on the actual. not gonna mess with that for now
    }

    @Test
    void test_init_default() {


        ModConfig.MODE = "default";
        EMCValues.init();


        validateEMCValues(EMCExpected.standard());
    }

    @Test
    void test_init_skyblocks() {


        ModConfig.MODE = Constants.Modes.MOCK_SKYBLOCK.getValue();
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