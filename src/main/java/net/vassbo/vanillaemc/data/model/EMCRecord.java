package net.vassbo.vanillaemc.data.model;


import java.util.Objects;

//consider converting to a record class if defs java21+
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

    @Override
    public String toString() {
        return "new EMCRecord(\"" + blockName + "\", " + emc + ");";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        EMCRecord emcRecord = (EMCRecord) o;
        return Objects.equals(blockName, emcRecord.blockName)
            && Objects.equals(emc, emcRecord.emc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(blockName, emc);
    }
}
