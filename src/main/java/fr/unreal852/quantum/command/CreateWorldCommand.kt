package fr.unreal852.quantum.command

import com.mojang.brigadier.Command
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import fr.unreal852.quantum.Quantum
import fr.unreal852.quantum.QuantumManager.getOrOpenPersistentWorld
import fr.unreal852.quantum.QuantumManager.getWorld
import fr.unreal852.quantum.command.suggestion.DifficultySuggestionProvider
import fr.unreal852.quantum.command.suggestion.WorldsDimensionSuggestionProvider
import fr.unreal852.quantum.utils.CommandArgumentsUtils
import fr.unreal852.quantum.utils.TextUtils
import fr.unreal852.quantum.world.QuantumWorldData
import net.minecraft.command.argument.DimensionArgumentType
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import net.minecraft.world.Difficulty
import net.minecraft.world.World
import net.minecraft.world.dimension.DimensionTypes
import xyz.nucleoid.fantasy.RuntimeWorldConfig
import kotlin.random.Random

class CreateWorldCommand : Command<ServerCommandSource> {
    override fun run(context: CommandContext<ServerCommandSource>): Int {
        if (context.source == null)
            return 0
        else {
            try {

                val server = context.source.server
                val worldName = StringArgumentType.getString(context, "worldName").lowercase()

                if (getWorld(worldName) != null) {
                    context.source.sendError(TextUtils.literal("A world with the same name already exists.", Formatting.WHITE))
                    return 1
                }

                val worldIdentifier = Identifier.of("quantum", worldName)
                val worldConfig = RuntimeWorldConfig()

                val difficulty = CommandArgumentsUtils.getEnumArgument(Difficulty::class.java, context, "worldDifficulty", server.saveProperties.difficulty)
                val dimensionTypeIdentifier = CommandArgumentsUtils.getIdentifierArgument(context, "worldDimension", DimensionTypes.OVERWORLD_ID)
                val seed = CommandArgumentsUtils.getSeedArgument(context, "worldSeed", Random.nextLong())
                var serverWorld = server.getWorld(RegistryKey.of(RegistryKeys.WORLD, dimensionTypeIdentifier))

                if (serverWorld == null) {
                    serverWorld = server.getWorld(World.OVERWORLD)
                    Quantum.LOGGER.warn("No world found for '{}'. Defaulting to minecraft:overworld", dimensionTypeIdentifier.toString())
                }

                worldConfig.setDifficulty(difficulty)
                worldConfig.setDimensionType(serverWorld!!.dimensionEntry)
                worldConfig.setGenerator(serverWorld.chunkManager.chunkGenerator)
                worldConfig.setSeed(seed)

                getOrOpenPersistentWorld(server, QuantumWorldData(worldIdentifier, dimensionTypeIdentifier, worldConfig), true)
                context.source.sendMessage(TextUtils.literal("World '$worldName' created!", Formatting.WHITE))
            } catch (e: Exception) {
                Quantum.LOGGER.error("An error occurred while creating the world.", e)
            }

            return 1
        }
    }

    companion object {
        fun register(dispatcher: CommandDispatcher<ServerCommandSource>) {
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
        }
    }
}

