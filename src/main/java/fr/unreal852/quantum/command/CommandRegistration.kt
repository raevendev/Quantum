package fr.unreal852.quantum.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import fr.unreal852.quantum.command.suggestion.DifficultySuggestionProvider;
import fr.unreal852.quantum.command.suggestion.WorldsDimensionSuggestionProvider;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.DimensionArgumentType;
import net.minecraft.server.command.CommandManager;

public final class CommandRegistration
{
    public static void RegisterCommands()
    {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
        {
            if (!environment.integrated && !environment.dedicated)
                return;

            dispatcher.register(CommandManager.literal("qt")
                    .then(CommandManager.literal("create")
                            .requires(commandSource -> commandSource.hasPermissionLevel(4))
                            .then(CommandManager.literal("world")
                                    .then(CommandManager.argument("worldName", StringArgumentType.string())
                                            .then(CommandManager.argument("worldDifficulty", StringArgumentType.string())
                                                    .suggests(new DifficultySuggestionProvider())
                                                    .then(CommandManager.argument("worldDimension", DimensionArgumentType.dimension())
                                                            .suggests(new WorldsDimensionSuggestionProvider())
                                                            .then(CommandManager.argument("worldSeed", StringArgumentType.string())
                                                                    .executes(new CreateWorldCommand())
                                                            )
                                                    )
                                                    .executes(new CreateWorldCommand())
                                            )
                                    )
                            )
                    ));

            dispatcher.register(CommandManager.literal("qt")
                    .then(CommandManager.literal("delete")
                            .requires(commandSource -> commandSource.hasPermissionLevel(4))
                            .then(CommandManager.literal("world")
                                    .then(CommandManager.argument("worldName", DimensionArgumentType.dimension())
                                            .suggests(new WorldsDimensionSuggestionProvider())
                                            .executes(new DeleteWorldCommand())
                                    )
                            )
                    )
            );

            dispatcher.register(CommandManager.literal("qt")
                    .then(CommandManager.literal("tp")
                            .requires(commandSource -> commandSource.hasPermissionLevel(4))
                            .then(CommandManager.argument("world", DimensionArgumentType.dimension())
                                    .suggests(new WorldsDimensionSuggestionProvider())
                                    .executes(new TeleportWorldCommand()))));

//            dispatcher.register(CommandManager.literal("qt")
//                    .then(CommandManager.literal("create")
//                            .requires(commandSource -> commandSource.hasPermissionLevel(4))
//                            .then(CommandManager.literal("portal")
//                                    .then(CommandManager.argument("destination", DimensionArgumentType.dimension())
//                                            .suggests(new WorldsDimensionSuggestionProvider())
//                                            .then(CommandManager.argument("portalBlock", IdentifierArgumentType.identifier())
//                                                    .suggests(new BlocksSuggestionProvider())
//                                                    .then(CommandManager.argument("activateWithItem", IdentifierArgumentType.identifier())
//                                                            .suggests(new ItemsSuggestionProvider())
//                                                            .then(CommandManager.argument("portalColor", ColorArgumentType.color())
//                                                                    .executes(new CreatePortalCommand()))))))));
        });
    }
}
