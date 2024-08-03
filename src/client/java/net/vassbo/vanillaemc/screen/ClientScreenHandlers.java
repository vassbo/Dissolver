package net.vassbo.vanillaemc.screen;

import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class ClientScreenHandlers {
    public static void registerScreenHandlers() {
        HandledScreens.register(ModScreenHandlers.DISSOLVER_SCREEN_HANDLER_TYPE, DissolverScreen::new);
    }
}