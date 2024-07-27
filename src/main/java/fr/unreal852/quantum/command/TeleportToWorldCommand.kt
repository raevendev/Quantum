package fr.unreal852.quantum.command

import com.mojang.brigadier.Command
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.context.CommandContext
import fr.unreal852.quantum.Quantum
import fr.unreal852.quantum.command.suggestion.WorldsDimensionSuggestionProvider
import fr.unreal852.quantum.utils.Extensions.teleportToWorld
import net.minecraft.command.argument.DimensionArgumentType
import net.minecraft.command.argument.IdentifierArgumentType
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text

class TeleportToWorldCommand : Command<ServerCommandSource> {

    override fun run(context: CommandContext<ServerCommandSource>): Int {

        if (context.source == null) {
            return 0
        }

        try {
            val player = context.source.player
            if (player == null || player.world == null) {
                return 0
            }

            val worldName = IdentifierArgumentType.getIdentifier(context, "world")
            val world = context.source.server.getWorld(RegistryKey.of(RegistryKeys.WORLD, worldName))
            if (world == null) {
                context.source.sendError(Text.literal("The specified world '$worldName' doesn't exist."))
                return 0
            }

            player.teleportToWorld(world)
        } catch (e: Exception) {
            Quantum.LOGGER.error("An error occurred while teleporting the player.", e)
        }

        return 1
    }

    companion object {
        fun register(dispatcher: CommandDispatcher<ServerCommandSource>) {
            dispatcher.register(CommandManager.literal("qt")
                .then(CommandManager.literal("tp")
                    .requires { commandSource: ServerCommandSource -> commandSource.hasPermissionLevel(4) }
                    .then(
                        CommandManager.argument("world", DimensionArgumentType.dimension())
                            .suggests(WorldsDimensionSuggestionProvider())
                            .executes(TeleportToWorldCommand())
                    )))
        }
    }
}