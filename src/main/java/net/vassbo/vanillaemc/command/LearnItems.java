package net.vassbo.vanillaemc.command;

import com.mojang.brigadier.context.CommandContext;

import net.minecraft.command.argument.ItemStackArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.server.command.ServerCommandSource;
import net.vassbo.vanillaemc.helpers.EMCHelper;

public class LearnItems {
    public static int everything(CommandContext<ServerCommandSource> context, String command) {
        PlayerEntity player = context.getSource().getPlayer();

        EMCHelper.learnAllItems(player);
        
        ModCommands.feedback(context, "Learned all items!");
        return 1;
    }

    public static int everythingPlayer(CommandContext<ServerCommandSource> context, String command, PlayerEntity player) {
        EMCHelper.learnAllItems(player);
        
        ModCommands.feedback(context, "Learned all items!");
        return 1;
    }

    public static int forget(CommandContext<ServerCommandSource> context, String command) {
        PlayerEntity player = context.getSource().getPlayer();

        EMCHelper.forgetAllItems(player);
        
        ModCommands.feedback(context, "Forgot all items ever learned!");
        return 1;
    }

    public static int forgetPlayer(CommandContext<ServerCommandSource> context, String command, PlayerEntity player) {
        EMCHelper.forgetAllItems(player);
        
        ModCommands.feedback(context, "Forgot all items ever learned!");
        return 1;
    }

    public static int add(CommandContext<ServerCommandSource> context, String command) {
        PlayerEntity player = context.getSource().getPlayer();
        EMCHelper.learnItem(player, getItemId(context));

        ModCommands.feedback(context, "Learned " + getItemName(context) + "!");
        return 1;
    }

    public static int addPlayer(CommandContext<ServerCommandSource> context, String command, PlayerEntity player) {
        EMCHelper.learnItem(player, getItemId(context));

        ModCommands.feedback(context, "Learned " + getItemName(context) + "!");
        return 1;
    }

    public static int remove(CommandContext<ServerCommandSource> context, String command) {
        PlayerEntity player = context.getSource().getPlayer();
        EMCHelper.forgetItem(player, getItemId(context));

        ModCommands.feedback(context, "Forgot " + getItemName(context) + "!");
        return 1;
    }

    public static int removePlayer(CommandContext<ServerCommandSource> context, String command, PlayerEntity player) {
        EMCHelper.forgetItem(player, getItemId(context));
        
        ModCommands.feedback(context, "Forgot " + getItemName(context) + "!");
        return 1;
    }

    // HELPERS

    private static String getItemId(CommandContext<ServerCommandSource> context) {
        final Item item = ItemStackArgumentType.getItemStackArgument(context, "item").getItem();
        return item.toString();
    }

    private static String getItemName(CommandContext<ServerCommandSource> context) {
        final Item item = ItemStackArgumentType.getItemStackArgument(context, "item").getItem();
        return item.getName().getString();
    }
}
