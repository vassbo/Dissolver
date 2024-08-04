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
import net.minecraft.text.Text;
import net.vassbo.vanillaemc.config.ModConfig;
import net.vassbo.vanillaemc.data.PlayerData;
import net.vassbo.vanillaemc.data.StateSaverAndLoader;
import net.vassbo.vanillaemc.helpers.EMCHelper;

public class ChangeEMC {
    public static int changeEMC(CommandContext<ServerCommandSource> context, String command) {
        final int value = IntegerArgumentType.getInteger(context, "number");

        PlayerEntity player = context.getSource().getPlayer();
        updateEMCValue(player, command, context, value);

        return 1;
    }

    public static int changeEMCPlayer(CommandContext<ServerCommandSource> context, String command, PlayerEntity player) {
        if (!ModConfig.PRIVATE_EMC) {
            ModCommands.feedback(context, Text.translatable("command.feedback.shared_data").getString());
            return 1;
        }

        final int value = IntegerArgumentType.getInteger(context, "number");
        
        updateEMCValue(player, command, context, value);

        return 1;
    }

    public static void updateEMCValue(PlayerEntity player, String key, CommandContext<ServerCommandSource> context, int inputValue) {
        int currentValue = EMCHelper.getEMCValue(player);

        if (key == "give") {
            currentValue += inputValue;
            ModCommands.feedback(context, Text.translatable("command.feedback.update.give", inputValue).getString() + currentValue);
        } else if (key == "take") {
            currentValue -= inputValue;
            ModCommands.feedback(context, Text.translatable("command.feedback.update.take", inputValue).getString() + currentValue);
        } else if (key == "set") {
            currentValue = inputValue;
            ModCommands.feedback(context, Text.translatable("command.feedback.update.set", currentValue).getString());
        }
        
        EMCHelper.setEMCValue(player, currentValue);
    }

    public static int listUserEMC(CommandContext<ServerCommandSource> context, String command) {
        if (!ModConfig.PRIVATE_EMC) {
            ModCommands.feedback(context, Text.translatable("command.feedback.shared_data").getString());
            return 1;
        }

        PlayerEntity player = context.getSource().getPlayer();
        int currentEMC = EMCHelper.getEMCValue(player);

        ModCommands.feedback(context, Text.translatable("command.feedback.list.user", currentEMC).getString());
        return 1;
    }

    public static int getEMC(CommandContext<ServerCommandSource> context, String command) {
        if (!ModConfig.PRIVATE_EMC) {
            ModCommands.feedback(context, Text.translatable("command.feedback.shared_data").getString());
            return 1;
        }

        MinecraftServer server = context.getSource().getServer();
        final String playerName = StringArgumentType.getString(context, "player");
        ServerPlayerEntity player = server.getPlayerManager().getPlayer(playerName);
        
        if (!ModConfig.PRIVATE_EMC) {
            ModCommands.feedback(context, Text.translatable("command.feedback.shared_data").getString());
            return 1;
        }

        PlayerData data = StateSaverAndLoader.getFromUuid(server, player.getUuid());

        ModCommands.feedback(context, Text.translatable("command.feedback.get", data.EMC).getString());
        return 1;
    }

    public static int listEMC(CommandContext<ServerCommandSource> context, String command) {
        if (!ModConfig.PRIVATE_EMC) {
            ModCommands.feedback(context, Text.translatable("command.feedback.shared_data").getString());
            return 1;
        }

        MinecraftServer server = context.getSource().getServer();
        HashMap<String, PlayerData> dataList = StateSaverAndLoader.getFullList(server);

        String msg = Text.translatable("command.feedback.list", dataList.size()).getString() + "§r\n" +
        dataList.entrySet()
        .stream()
        .map(a -> "- " + a.getKey() + ": §6" + a.getValue().EMC + "§r")
        .collect(joining("\n"));

        ModCommands.feedback(context, msg);
        return 1;
    }
}
