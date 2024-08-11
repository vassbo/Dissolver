package net.vassbo.vanillaemc.data.model;


public class EMCRecord {
    final String blockName;
    final Integer emc;

    public EMCRecord(
        String blockName,
        Integer emc
    ) {
        this.blockName = blockName;
        this.emc = emc;
    }

    public String getBlockName() {
        return blockName;
    }

    public Integer getEmc() {
        return emc;
    }
}
