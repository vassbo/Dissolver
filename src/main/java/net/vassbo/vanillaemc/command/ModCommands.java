package net.vassbo.vanillaemc.command;

// import static com.mojang.brigadier.arguments.StringArgumentType.getString;
// import static com.mojang.brigadier.arguments.StringArgumentType.word;
import static net.minecraft.server.command.CommandManager.argument;
// import static net.minecraft.server.command.CommandManager.*;
import static net.minecraft.server.command.CommandManager.literal;

import java.util.ArrayList;
import java.util.List;
import java.util.Collection;
import java.util.UUID;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.UuidArgumentType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.vassbo.vanillaemc.VanillaEMC;
import net.vassbo.vanillaemc.data.PlayerData;
import net.vassbo.vanillaemc.data.StateSaverAndLoader;

public class ModCommands {
    // HELPERS

    private static void registerCommand(LiteralArgumentBuilder<ServerCommandSource> command) {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(command));
    }

    // move executes inside arg
    // private static void createCustomCommand(String command, ArgumentBuilder<ServerCommandSource, ?> arg, CommandMethodInterface func) {
    //     registerCommand(
    //         createRootCommand(command)
    //         .then(arg)
    //         .executes(context -> {
    //             VanillaEMC.LOGGER.info("Executed command: " + command);
    //             return func.execute(context, command);
    //         })
    //     );
    // }

    private static LiteralArgumentBuilder<ServerCommandSource> createRootCommand(String command) {
        return literal(command).requires(source -> source.hasPermissionLevel(2)); // requires OP
    }

    private static LiteralArgumentBuilder<ServerCommandSource> createSubCommand(String command, String argId, ArgumentType<?> argType, CommandMethodInterface func) {
        return literal(command).then(argument(argId, argType).executes((context) -> executeCommand(context, command, func)));
    }
    
    static void feedback(CommandContext<ServerCommandSource> context, String msg) {
        context.getSource().sendFeedback(() -> Text.literal(msg), false);
    }

    // EXECUTES

    private interface CommandMethodInterface {
        int execute(CommandContext<ServerCommandSource> context, String command);
    }

    private static int executeCommand(CommandContext<ServerCommandSource> context, String command, CommandMethodInterface func) {
        VanillaEMC.LOGGER.info("Executed command: " + command);
        return func.execute(context, command);
    }

    // private static int executeSubCommand(CommandContext<ServerCommandSource> context, String command, CommandMethodInterface func) {
    //     VanillaEMC.LOGGER.info("Executed subcommand: " + command);
    //     return func.execute(context, command);
    // }

    private static int LOCAL_EMC = 0;
    private static int changeEMC(CommandContext<ServerCommandSource> context, String command) {
        final int value = IntegerArgumentType.getInteger(context, "number");
    
        // not an int
        // feedback(context, "Please add a number value");
        // if (value != (int)value) return -1;

        if (command == "give") {
            LOCAL_EMC += value;
            feedback(context, "Gave user " + value + " EMC. New value: §6" + LOCAL_EMC);
        } else if (command == "take") {
            LOCAL_EMC -= value;
            feedback(context, "Took " + value + " EMC from user. New value: §6" + LOCAL_EMC);
        } else if (command == "set") {
            LOCAL_EMC = value;
            feedback(context, "Set user EMC to §6" + LOCAL_EMC);
        } else return -1;

        // feedback(context, "\"%s × %s = %s\".formatted(value, value, result)");
        return 1;
    }

    private static int playerEMC(CommandContext<ServerCommandSource> context, String command) {
        feedback(context, "You currently have §6" + LOCAL_EMC + "§r EMC!");
        return 1;
    }

    private static int getEMC(CommandContext<ServerCommandSource> context, String command) {
        MinecraftServer server = context.getSource().getServer();
        final UUID value = UuidArgumentType.getUuid(context, "player");

        PlayerData data = StateSaverAndLoader.getFromUuid(server, value);

        feedback(context, "EMC of player: §6" + data.emc);
        return 1;
    }

    private static int listEMC(CommandContext<ServerCommandSource> context, String command) {
        // MinecraftServer server = context.getSource().getPlayer().getWorld().getServer();
        MinecraftServer server = context.getSource().getServer();

        Collection<PlayerData> dataList = StateSaverAndLoader.getFullList(server);
        List<Integer> emcList = new ArrayList<>();
        dataList.forEach((data) -> emcList.add(data.emc));

        feedback(context, "Full list:\n " + emcList.toString());
        return 1;
    }


    // INITIALIZE

    public static void registerCommands() {
        // createCustomCommand("emctest", ModCommands::changeEMC);

        registerCommand(
            createRootCommand("emc")
            .executes((context) -> executeCommand(context, "emc", ModCommands::playerEMC))
            .then(createSubCommand("give", "number", IntegerArgumentType.integer(), ModCommands::changeEMC))
            .then(createSubCommand("take", "number", IntegerArgumentType.integer(), ModCommands::changeEMC))
            .then(createSubCommand("set", "number", IntegerArgumentType.integer(), ModCommands::changeEMC))
            .then(createSubCommand("get", "player", UuidArgumentType.uuid(), ModCommands::getEMC))
        );

        registerCommand(
            createRootCommand("emclist")
            .executes((context) -> executeCommand(context, "emclist", ModCommands::listEMC))
        );
    }
}
