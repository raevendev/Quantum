package fr.unreal852.quantum.command

import com.mojang.brigadier.Command
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.context.CommandContext
import fr.unreal852.quantum.Quantum
import fr.unreal852.quantum.command.suggestion.BlocksSuggestionProvider
import fr.unreal852.quantum.command.suggestion.WorldsDimensionSuggestionProvider
import net.kyrptonaught.customportalapi.api.CustomPortalBuilder
import net.minecraft.command.argument.DimensionArgumentType
import net.minecraft.command.argument.IdentifierArgumentType
import net.minecraft.item.Items
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource

class CreatePortalCommand : Command<ServerCommandSource> {
    override fun run(context: CommandContext<ServerCommandSource>): Int {

        if (context.source == null)
            return 0

        try {

            val portalBlock = IdentifierArgumentType.getIdentifier(context, PORTAL_BLOCK_ARG)
            val destinationWorld = IdentifierArgumentType.getIdentifier(context, PORTAL_DESTINATION_ARG)

            CustomPortalBuilder.beginPortal()
                .frameBlock(portalBlock)
                .lightWithItem(Items.DIAMOND)
                .destDimID(destinationWorld)
                .tintColor(255, 0, 0)
                .registerPortal()

        } catch (e: Exception) {
            Quantum.LOGGER.error("An error occurred while creating the world.", e)
        }

        return 1
    }

    companion object {

        private const val PORTAL_BLOCK_ARG = "portalFrameBlock"
        private const val PORTAL_DESTINATION_ARG = "destinationWorld"

        fun register(dispatcher: CommandDispatcher<ServerCommandSource>) {
            dispatcher.register(CommandManager.literal("qt")
                .then(CommandManager.literal("createportal")
                    .requires { commandSource: ServerCommandSource -> commandSource.hasPermissionLevel(4) }
                    .then(
                        CommandManager.argument(PORTAL_BLOCK_ARG, IdentifierArgumentType.identifier())
                            .suggests(BlocksSuggestionProvider())
                            .executes(CreatePortalCommand())
                            .then(
                                CommandManager.argument(PORTAL_DESTINATION_ARG, DimensionArgumentType.dimension())
                                    .suggests(WorldsDimensionSuggestionProvider())
                                    .executes(CreatePortalCommand())
                            )
                    )
                )
            )
        }
    }
}

