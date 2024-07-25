package net.vassbo.vanillaemc.command;

// import static com.mojang.brigadier.arguments.StringArgumentType.getString;
// import static com.mojang.brigadier.arguments.StringArgumentType.word;
import static net.minecraft.server.command.CommandManager.argument;
// import static net.minecraft.server.command.CommandManager.*;
import static net.minecraft.server.command.CommandManager.literal;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.ItemStackArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.vassbo.vanillaemc.VanillaEMC;

public class ModCommands {
    private static void registerCommand(LiteralArgumentBuilder<ServerCommandSource> command) {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(command));
    }

    // private static void createCustomCommand(String command, CommandMethodInterface func) {
    //     registerCommand(
    //         createRootCommand(command)
    //         .executes(context -> {
    //             VanillaEMC.LOGGER.info("Executed command: " + command);
    //             return func.execute(context, command);
    //         })
    //     );
    // }

    private static LiteralArgumentBuilder<ServerCommandSource> createRootCommand(String command) {
        return literal(command).requires(source -> source.hasPermissionLevel(2)); // requires OP
    }

    private static LiteralArgumentBuilder<ServerCommandSource> createCommandWithPlayerArg(String command, ArgumentType<?> argType, CommandMethodInterface func, CommandMethodPlayerInterface playerFunc) {
        return literal(command).executes((context) -> executeCommand(context, command, func)).then(argument("player", StringArgumentType.string()).executes((context) -> executePlayerCommand(context, command, playerFunc)));
    }

    private static LiteralArgumentBuilder<ServerCommandSource> createSubCommand(String command, String argId, ArgumentType<?> argType, CommandMethodInterface func) {
        return literal(command).then(argument(argId, argType).executes((context) -> executeCommand(context, command, func)));
    }

    private static LiteralArgumentBuilder<ServerCommandSource> createSubCommandWithPlayerArg(String command, String argId, ArgumentType<?> argType, CommandMethodInterface func, CommandMethodPlayerInterface playerFunc) {
        return literal(command).then(argument(argId, argType).executes((context) -> executeCommand(context, command, func)).then(argument("player", StringArgumentType.string()).executes((context) -> executePlayerCommand(context, command, playerFunc))));
    }
    
    // special example: msg = "\"%s × %s = %s\".formatted(value, value, result)"
    public static void feedback(CommandContext<ServerCommandSource> context, String msg) {
        context.getSource().sendFeedback(() -> Text.literal(msg), false);
    }
    
    // HELPERS
    
    public static PlayerEntity playerFromName(CommandContext<ServerCommandSource> context, String playerName) {
        return context.getSource().getServer().getPlayerManager().getPlayer(playerName);
    }

    // EXECUTE

    private interface CommandMethodInterface {
        int execute(CommandContext<ServerCommandSource> context, String command);
    }

    private static int executeCommand(CommandContext<ServerCommandSource> context, String command, CommandMethodInterface func) {
        VanillaEMC.LOGGER.info("Executed command: " + command);
        return func.execute(context, command);
    }

    private interface CommandMethodPlayerInterface {
        int execute(CommandContext<ServerCommandSource> context, String command, PlayerEntity player);
    }

    private static int executePlayerCommand(CommandContext<ServerCommandSource> context, String command, CommandMethodPlayerInterface func) {
        final String playerName = StringArgumentType.getString(context, "player");
        PlayerEntity player = playerFromName(context, playerName);

        if (player == null) {
            feedback(context, "Could not find a player with the name '" + playerName + "'!");
            return -1;
        }

        VanillaEMC.LOGGER.info("Executed command to player '" + playerName + "': " + command);
        return func.execute(context, command, player);
    }

    // private static int executeSubCommand(CommandContext<ServerCommandSource> context, String command, CommandMethodInterface func) {
    //     VanillaEMC.LOGGER.info("Executed subcommand: " + command);
    //     return func.execute(context, command);
    // }

    // INITIALIZE

    public static void registerCommands() {
        // createCustomCommand("emctest", ModCommands::changeEMC);

        registerCommand(
            createRootCommand("emc")
            .executes((context) -> executeCommand(context, "emc", ChangeEMC::listUserEMC))
            .then(literal("list").executes((context) -> executeCommand(context, "list", ChangeEMC::listEMC)))
            .then(createSubCommand("get", "player", StringArgumentType.string(), ChangeEMC::getEMC))
            .then(createSubCommandWithPlayerArg("give", "number", IntegerArgumentType.integer(), ChangeEMC::changeEMC, ChangeEMC::changeEMCPlayer))
            .then(createSubCommandWithPlayerArg("take", "number", IntegerArgumentType.integer(), ChangeEMC::changeEMC, ChangeEMC::changeEMCPlayer))
            .then(createSubCommandWithPlayerArg("set", "number", IntegerArgumentType.integer(), ChangeEMC::changeEMC, ChangeEMC::changeEMCPlayer))
        );

        // LEARNED (unlock all, learn specific items, unlearn)
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(
            createRootCommand("learn")
            .then(createCommandWithPlayerArg("everything", ItemStackArgumentType.itemStack(registryAccess), LearnItems::everything, LearnItems::everythingPlayer))
            .then(createCommandWithPlayerArg("forgetall", ItemStackArgumentType.itemStack(registryAccess), LearnItems::forget, LearnItems::forgetPlayer))
            .then(createSubCommandWithPlayerArg("add", "item", ItemStackArgumentType.itemStack(registryAccess), LearnItems::add, LearnItems::addPlayer))
            .then(createSubCommandWithPlayerArg("remove", "item", ItemStackArgumentType.itemStack(registryAccess), LearnItems::remove, LearnItems::removePlayer))
        ));

        // createCustomCommand("openmagic", (context, command) -> {
        //     PlayerEntity player = context.getSource().getPlayer();
        //     Vec3d playerPos = player.getPos();
        //     playerPos.y = 265;
        //     BlockPos pos = new BlockPos((int)playerPos.x, 256, (int)playerPos.y);
        //     BlockState state = new MagicBlock(null).getDefaultState();
        //     player.getWorld().setBlockState(pos, state);
            
        //     MagicBlockEntity entity = player.getWorld().getBlockState(pos).getBlockEntity(player.getWorld(), ModBlockEntities.MAGIC_BLOCK_ENTITY);
        //     player.openHandledScreen(entity);

        //     return 1;
        // });
    }
}
