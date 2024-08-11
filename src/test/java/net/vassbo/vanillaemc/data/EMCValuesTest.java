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

    public static final String TEST_CASE = "test:case";
    public static final EMCRecord TEST_CASE_EMC_RECORD = new EMCRecord(TEST_CASE, 4);
    public static final String MINECRAFT_DIRT = "minecraft:dirt";

    class VisibilityModifier
        extends EMCValues {
        public HashMap<String, Integer> EMC_VALUES() {return EMC_VALUES;}

        public Collection<String> CONFIG_OVERRIDDEN() {
            return CONFIG_OVERRIDDEN;
        }

        public void __setEMC(
            String resultId,
            int emcValue
        ) {
            VisibilityModifier.setEMC(resultId, emcValue);
        }

        public static void __setEMC(HashMap<String, Integer> NEW_EMC_VALUES) {
            VisibilityModifier.setEMC(NEW_EMC_VALUES);
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
    void able_to_change_emc_simple_new() {
        test_init_default();

        //that we setup correctly
        assertThat(EMCValues.get(TEST_CASE_EMC_RECORD.getBlockName())).as("setup "+TEST_CASE).isEqualTo(0);

        underTest.__setEMC(TEST_CASE, 100);

        //does not change check
        assertThat(EMCValues.get(TEST_CASE_EMC_RECORD.getBlockName())).as("mutable "+TEST_CASE).isEqualTo(100);


        underTest.__setEMC(TEST_CASE, 1);

        //does not change check
        assertThat(EMCValues.get(TEST_CASE_EMC_RECORD.getBlockName())).as("mutable second pass "+TEST_CASE).isEqualTo(1);
    }

    @Test
    void able_to_change_emc_simple_locked() {
        test_init_overrides();

        //that we setup correctly
        assertThat(EMCValues.get(TEST_CASE_EMC_RECORD.getBlockName())).as("setup "+TEST_CASE).isEqualTo(TEST_CASE_EMC_RECORD.getEmc());

        underTest.__setEMC(TEST_CASE, 100);

        //does not change check
        assertThat(EMCValues.get(TEST_CASE_EMC_RECORD.getBlockName())).as("immutable "+TEST_CASE).isEqualTo(TEST_CASE_EMC_RECORD.getEmc());
    }

    @Test
    void able_to_change_emc_simple_deleted() {
        test_init_overrides_dirt_has_no_emc();

        //that we setup correctly
        assertThat(EMCValues.get(MINECRAFT_DIRT)).as("setup "+TEST_CASE).isEqualTo(0);

        underTest.__setEMC(MINECRAFT_DIRT, 100);

        //does not change check
        assertThat(EMCValues.get(MINECRAFT_DIRT)).as("immutable "+TEST_CASE).isEqualTo(0);
    }


    @Test
    void test_init_overrides() {

        ModConfig.EMC_OVERRIDES = Arrays.asList(
            //Test adding a new item
            TEST_CASE_EMC_RECORD,

            //Test adding an override
            new EMCRecord(MINECRAFT_DIRT, 10)
        );

        ModConfig.MODE = TestConstants.Modes.DEFAULT.getValue();
        EMCValues.init();

        List<EMCRecord> list = new ArrayList<>(common());

        list.add(new EMCRecord(TEST_CASE, 4));
        list.add(new EMCRecord(MINECRAFT_DIRT, 10));

        validateEMCValues(list);
    }

    @Test
    void test_init_overrides_dirt_has_no_emc() {

        ModConfig.EMC_OVERRIDES = Arrays.asList(
            //Test adding a new item
            new EMCRecord(TEST_CASE, 4),

            //Test adding an override
            new EMCRecord(MINECRAFT_DIRT, 0)
        );

        ModConfig.MODE = TestConstants.Modes.DEFAULT.getValue();
        EMCValues.init();

        List<EMCRecord> list = new ArrayList<>(common());

        list.add(new EMCRecord(TEST_CASE, 4));
        //Note, the lack of dirt

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