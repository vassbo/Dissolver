package net.vassbo.vanillaemc.command;

import com.mojang.brigadier.context.CommandContext;

import net.minecraft.command.argument.ItemStackArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.vassbo.vanillaemc.helpers.EMCHelper;

public class LearnItems {
    public static int everything(CommandContext<ServerCommandSource> context, String command) {
        PlayerEntity player = context.getSource().getPlayer();

        EMCHelper.learnAllItems(player);
        
        ModCommands.feedback(context, Text.translatable("command.feedback.memory.fill").getString());
        return 1;
    }

    public static int everythingPlayer(CommandContext<ServerCommandSource> context, String command, PlayerEntity player) {
        EMCHelper.learnAllItems(player);
        
        ModCommands.feedback(context, Text.translatable("command.feedback.memory.fill").getString());
        return 1;
    }

    public static int forget(CommandContext<ServerCommandSource> context, String command) {
        PlayerEntity player = context.getSource().getPlayer();

        EMCHelper.forgetAllItems(player);
        
        ModCommands.feedback(context, Text.translatable("command.feedback.memory.forget").getString());
        return 1;
    }

    public static int forgetPlayer(CommandContext<ServerCommandSource> context, String command, PlayerEntity player) {
        EMCHelper.forgetAllItems(player);
        
        ModCommands.feedback(context, Text.translatable("command.feedback.memory.forget").getString());
        return 1;
    }

    public static int add(CommandContext<ServerCommandSource> context, String command) {
        PlayerEntity player = context.getSource().getPlayer();
        boolean learned = EMCHelper.learnItem(player, getItemId(context));

        if (learned) ModCommands.feedback(context, Text.translatable("command.feedback.memory.add", getItemName(context)).getString());
        else ModCommands.feedback(context, Text.translatable("command.feedback.memory.add.fail").getString());

        return 1;
    }

    public static int addPlayer(CommandContext<ServerCommandSource> context, String command, PlayerEntity player) {
        boolean learned = EMCHelper.learnItem(player, getItemId(context));

        if (learned) ModCommands.feedback(context, Text.translatable("command.feedback.memory.add", getItemName(context)).getString());
        else ModCommands.feedback(context, Text.translatable("command.feedback.memory.add.fail").getString());

        return 1;
    }

    public static int remove(CommandContext<ServerCommandSource> context, String command) {
        PlayerEntity player = context.getSource().getPlayer();
        boolean removed = EMCHelper.forgetItem(player, getItemId(context));

        if (removed) ModCommands.feedback(context, Text.translatable("command.feedback.memory.remove", getItemName(context)).getString());
        else ModCommands.feedback(context, Text.translatable("command.feedback.memory.remove.fail").getString());

        return 1;
    }

    public static int removePlayer(CommandContext<ServerCommandSource> context, String command, PlayerEntity player) {
        boolean removed = EMCHelper.forgetItem(player, getItemId(context));
        
        if (removed) ModCommands.feedback(context, Text.translatable("command.feedback.memory.remove", getItemName(context)).getString());
        else ModCommands.feedback(context, Text.translatable("command.feedback.memory.remove.fail").getString());

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
