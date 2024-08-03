package net.vassbo.vanillaemc.screen;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.vassbo.vanillaemc.VanillaEMC;

public class ModScreenHandlers {
    private static final ScreenHandlerType<DissolverScreenHandler> DISSOLVER_SCREEN = new ScreenHandlerType<>((syncId, playerInventory) -> new DissolverScreenHandler(syncId, playerInventory), FeatureFlags.VANILLA_FEATURES);
    
    public static final ScreenHandlerType<DissolverScreenHandler> DISSOLVER_SCREEN_HANDLER_TYPE = Registry.register(Registries.SCREEN_HANDLER, Identifier.of(VanillaEMC.MOD_ID, "dissolver_screen_handler"), DISSOLVER_SCREEN);

    public static void init() {
    }
}
