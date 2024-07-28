package fr.unreal852.quantum.command

import com.mojang.brigadier.Command
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.context.CommandContext
import fr.unreal852.quantum.Quantum
import fr.unreal852.quantum.QuantumManager
import fr.unreal852.quantum.command.suggestion.WorldsDimensionSuggestionProvider
import net.minecraft.command.argument.DimensionArgumentType
import net.minecraft.command.argument.IdentifierArgumentType
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text

class DeleteWorldCommand : Command<ServerCommandSource> {
    override fun run(context: CommandContext<ServerCommandSource>): Int {

        if (context.source == null)
            return 0

        try {

            val worldName = IdentifierArgumentType.getIdentifier(context, WORLD_NAME_ARG)

            if (!QuantumManager.worldExists(worldName)) {
                context.source.sendError(Text.translatable("quantum.text.cmd.world.notexists.unspecified"))
                return 0
            }

            if (QuantumManager.deleteWorld(worldName)) {
                context.source.sendMessage(Text.translatable("quantum.text.cmd.world.deleted", worldName.toString()))
            }
        } catch (e: Exception) {
            Quantum.LOGGER.error("An error occurred while deleting the world.", e)
        }

        return 1
    }

    companion object {

        private const val WORLD_NAME_ARG = "worldName"

        fun register(dispatcher: CommandDispatcher<ServerCommandSource>) {
            dispatcher.register(CommandManager.literal("qt")
                .then(CommandManager.literal("deleteworld")
                    .requires { commandSource: ServerCommandSource -> commandSource.hasPermissionLevel(4) }
                    .then(
                        CommandManager.argument(WORLD_NAME_ARG, DimensionArgumentType.dimension())
                            .suggests(WorldsDimensionSuggestionProvider())
                            .executes(DeleteWorldCommand())

                    )
                )
            )
        }
    }
}

