package fr.unreal852.quantum.command

import com.mojang.brigadier.Command
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.context.CommandContext
import fr.unreal852.quantum.Quantum
import fr.unreal852.quantum.QuantumManager.getWorld
import fr.unreal852.quantum.command.suggestion.WorldsDimensionSuggestionProvider
import fr.unreal852.quantum.utils.TextUtils
import fr.unreal852.quantum.world.states.QuantumPersistentState
import net.minecraft.command.argument.DimensionArgumentType
import net.minecraft.command.argument.IdentifierArgumentType
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.util.Formatting
import xyz.nucleoid.fantasy.Fantasy

class DeleteWorldCommand : Command<ServerCommandSource> {
    override fun run(context: CommandContext<ServerCommandSource>): Int {
        if (context.source == null) return 0
        else {
            try {
                val server = context.source!!.server
                val worldName = IdentifierArgumentType.getIdentifier(context, "worldName")
                val quantumWorld = getWorld(worldName)

                if (quantumWorld == null) {
                    context.source!!.sendError(
                        TextUtils.literal("The specified world doesn't exists or has not been created using Quantum.", Formatting.RED))
                    return 1
                }

                val fantasy = Fantasy.get(server)
                if (fantasy.tickDeleteWorld(quantumWorld.serverWorld)) {
                    val state = QuantumPersistentState.getQuantumState(server)
                    state.removeWorldData(quantumWorld.worldData)
                    context.source!!.sendMessage(TextUtils.literal("World '$worldName' deleted!", Formatting.GREEN))
                }
            } catch (e: Exception) {
                Quantum.LOGGER.error("An error occurred while creating the world.", e)
            }

            return 1
        }
    }

    companion object {
        fun register(dispatcher: CommandDispatcher<ServerCommandSource>) {
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
        }
    }
}

