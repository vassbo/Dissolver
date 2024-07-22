package net.vassbo.vanillaemc.command;

import static java.util.stream.Collectors.joining;

import java.util.HashMap;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.vassbo.vanillaemc.data.PlayerData;
import net.vassbo.vanillaemc.data.StateSaverAndLoader;

public class ChangeEMC {
    public static int changeEMC(CommandContext<ServerCommandSource> context, String command) {
        final int value = IntegerArgumentType.getInteger(context, "number");

        PlayerEntity player = context.getSource().getPlayer();
        updateEMCValue(player, command, context, value);

        return 1;
    }

    public static int changeEMCPlayer(CommandContext<ServerCommandSource> context, String command, PlayerEntity player) {
        final int value = IntegerArgumentType.getInteger(context, "number");
        
        updateEMCValue(player, command, context, value);

        return 1;
    }

    public static void updateEMCValue(PlayerEntity player, String key, CommandContext<ServerCommandSource> context, int inputValue) {
        int currentValue = StateSaverAndLoader.getPlayerState(player).emc;

        if (key == "give") {
            currentValue += inputValue;
            ModCommands.feedback(context, "Gave user " + inputValue + " EMC. New value: §6" + currentValue);
        } else if (key == "take") {
            currentValue -= inputValue;
            ModCommands.feedback(context, "Took " + inputValue + " EMC from user. New value: §6" + currentValue);
        } else if (key == "set") {
            currentValue = inputValue;
            ModCommands.feedback(context, "Set user EMC to §6" + currentValue);
        }
        
        StateSaverAndLoader.setPlayerEMC(player, currentValue);
    }

    public static int listUserEMC(CommandContext<ServerCommandSource> context, String command) {
        PlayerEntity player = context.getSource().getPlayer();
        int currentEMC = StateSaverAndLoader.getPlayerState(player).emc;

        ModCommands.feedback(context, "You currently have §6" + currentEMC + "§r EMC!");
        return 1;
    }

    public static int getEMC(CommandContext<ServerCommandSource> context, String command) {
        MinecraftServer server = context.getSource().getServer();
        final String playerName = StringArgumentType.getString(context, "player");
        ServerPlayerEntity player = server.getPlayerManager().getPlayer(playerName);

        if (player == null) {
            ModCommands.feedback(context, "Could not find a player with the name '" + playerName + "'!");
            return -1;
        }

        PlayerData data = StateSaverAndLoader.getFromUuid(server, player.getUuid());

        ModCommands.feedback(context, "EMC of player: §6" + data.emc);
        return 1;
    }

    public static int listEMC(CommandContext<ServerCommandSource> context, String command) {
        MinecraftServer server = context.getSource().getServer();

        HashMap<String, PlayerData> dataList = StateSaverAndLoader.getFullList(server);

        String msg = "§lFull list:§r\n" +
        dataList.entrySet()
        .stream()
        .map(a -> "- " + a.getKey() + ": §6" + a.getValue().emc + "§r")
        .collect(joining("\n"));

        ModCommands.feedback(context, msg);
        return 1;
    }
}
