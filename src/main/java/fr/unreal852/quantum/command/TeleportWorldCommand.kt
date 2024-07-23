package fr.unreal852.quantum.command

import com.mojang.brigadier.Command
import com.mojang.brigadier.context.CommandContext
import fr.unreal852.quantum.Quantum
import fr.unreal852.quantum.utils.Extensions.teleportTo
import net.minecraft.command.argument.IdentifierArgumentType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text

class TeleportWorldCommand : Command<ServerCommandSource> {
    override fun run(context: CommandContext<ServerCommandSource>): Int {
        if (context.source == null) {
            return 0
        } else {
            try {
                val player: PlayerEntity? = context.source!!.player
                if (player == null || player.world == null) {
                    return 0
                }

                val worldName = IdentifierArgumentType.getIdentifier(context, "world")
                val world = context.source!!.server.getWorld(RegistryKey.of(RegistryKeys.WORLD, worldName))
                if (world == null) {
                    context.source!!.sendError(Text.literal("The specified world '$worldName' doesn't exist."))
                    return 0
                }

                player.teleportTo(world, world.spawnPos.toBottomCenterPos())
            } catch (e: Exception) {
                Quantum.LOGGER.error("An error occurred while teleporting the player.", e)
            }

            return 1
        }
    }
}