package net.vassbo.vanillaemc.data;

import net.minecraft.entity.player.PlayerEntity;

public class SetEMC {
    public static void addEMCValue(PlayerEntity player, int addedValue) {
        int currentValue = StateSaverAndLoader.getPlayerState(player).emc;
        int newValue = currentValue += addedValue;
        
        setEMCValue(player, newValue);
    }

    public static boolean removeEMCValue(PlayerEntity player, int removedValue) {
        int currentValue = StateSaverAndLoader.getPlayerState(player).emc;
        int newValue = currentValue -= removedValue;

        if (newValue < 0) return false;

        setEMCValue(player, newValue);
        return true;
    }

    public static void setEMCValue(PlayerEntity player, int value) {
        StateSaverAndLoader.setPlayerEMC(player, value);
    }
}
