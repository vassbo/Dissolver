package net.vassbo.vanillaemc.screen;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.vassbo.vanillaemc.VanillaEMC;

public class ModScreenHandlers {
    private static final ScreenHandlerType<MagicScreenHandler> MAGIC_SCREEN = new ScreenHandlerType<>((syncId, playerInventory) -> new MagicScreenHandler(syncId, playerInventory), FeatureFlags.VANILLA_FEATURES);
    
    public static final ScreenHandlerType<MagicScreenHandler> MAGIC_SCREEN_HANDLER = Registry.register(Registries.SCREEN_HANDLER, Identifier.of(VanillaEMC.MOD_ID, "magic_screen_handler"), MAGIC_SCREEN);

    public static void registerScreenHandlers() {
    }
}
