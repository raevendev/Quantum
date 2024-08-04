package fr.unreal852.quantum.command

import com.mojang.brigadier.Command
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.context.CommandContext
import fr.unreal852.quantum.Quantum
import fr.unreal852.quantum.command.suggestion.BlocksSuggestionProvider
import fr.unreal852.quantum.state.QuantumStorage
import net.minecraft.command.argument.IdentifierArgumentType
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource

class DeletePortalCommand : Command<ServerCommandSource> {
    override fun run(context: CommandContext<ServerCommandSource>): Int {

        if (context.source == null)
            return 0

        try {

            val portalBlock = IdentifierArgumentType.getIdentifier(context, PORTAL_BLOCK_ARG)
            val quantumStorage = QuantumStorage.getQuantumState(context.source.server)

            if (!quantumStorage.portalExists(portalBlock)) {
                Quantum.LOGGER.error("An error occurred while creating the portal.")
                return 0
            }

            quantumStorage.removePortalData(portalBlock)

        } catch (e: Exception) {
            Quantum.LOGGER.error("An error occurred while creating the world.", e)
        }

        return 1
    }

    companion object {

        private const val PORTAL_BLOCK_ARG = "portalFrameBlock"

        fun register(dispatcher: CommandDispatcher<ServerCommandSource>) {
            dispatcher.register(CommandManager.literal("qt")
                .then(CommandManager.literal("deleteportal")
                    .requires { commandSource: ServerCommandSource -> commandSource.hasPermissionLevel(4) }
                    .then(
                        CommandManager.argument(PORTAL_BLOCK_ARG, IdentifierArgumentType.identifier())
                            .suggests(BlocksSuggestionProvider())
                            .executes(DeletePortalCommand())
                    )
                )
            )
        }
    }
}

