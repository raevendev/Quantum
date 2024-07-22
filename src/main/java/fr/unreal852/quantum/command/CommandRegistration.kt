package fr.unreal852.quantum.command

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import fr.unreal852.quantum.command.suggestion.DifficultySuggestionProvider
import fr.unreal852.quantum.command.suggestion.WorldsDimensionSuggestionProvider
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.command.CommandRegistryAccess
import net.minecraft.command.argument.DimensionArgumentType
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.CommandManager.RegistrationEnvironment
import net.minecraft.server.command.ServerCommandSource

object CommandRegistration {
    fun registerCommands() {
        CommandRegistrationCallback.EVENT.register(CommandRegistrationCallback { dispatcher: CommandDispatcher<ServerCommandSource?>,
                                                                                 registryAccess: CommandRegistryAccess?,
                                                                                 environment: RegistrationEnvironment ->
            if (!environment.integrated && !environment.dedicated) return@CommandRegistrationCallback
            dispatcher.register(CommandManager.literal("qt")
                .then(CommandManager.literal("create")
                    .requires { commandSource: ServerCommandSource -> commandSource.hasPermissionLevel(4) }
                    .then(
                        CommandManager.literal("world")
                            .then(
                                CommandManager.argument("worldName", StringArgumentType.string())
                                    .then(
                                        CommandManager.argument("worldDifficulty", StringArgumentType.string())
                                            .suggests(DifficultySuggestionProvider())
                                            .then(
                                                CommandManager.argument(
                                                    "worldDimension",
                                                    DimensionArgumentType.dimension()
                                                )
                                                    .suggests(WorldsDimensionSuggestionProvider())
                                                    .then(
                                                        CommandManager.argument(
                                                            "worldSeed",
                                                            StringArgumentType.string()
                                                        )
                                                            .executes(CreateWorldCommand())
                                                    )
                                            )
                                            .executes(CreateWorldCommand())
                                    )
                            )
                    )
                ))

            dispatcher.register(CommandManager.literal("qt")
                .then(CommandManager.literal("delete")
                    .requires { commandSource: ServerCommandSource -> commandSource.hasPermissionLevel(4) }
                    .then(
                        CommandManager.literal("world")
                            .then(
                                CommandManager.argument("worldName", DimensionArgumentType.dimension())
                                    .suggests(WorldsDimensionSuggestionProvider())
                                    .executes(DeleteWorldCommand())
                            )
                    )
                )
            )
            dispatcher.register(CommandManager.literal("qt")
                .then(CommandManager.literal("tp")
                    .requires { commandSource: ServerCommandSource -> commandSource.hasPermissionLevel(4) }
                    .then(
                        CommandManager.argument("world", DimensionArgumentType.dimension())
                            .suggests(WorldsDimensionSuggestionProvider())
                            .executes(TeleportWorldCommand())
                    )))
        })
    }
}
