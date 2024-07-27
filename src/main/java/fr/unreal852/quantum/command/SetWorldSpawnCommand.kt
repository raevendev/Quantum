package fr.unreal852.quantum.command

import com.mojang.brigadier.Command
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.context.CommandContext
import fr.unreal852.quantum.Quantum
import fr.unreal852.quantum.utils.CommandArgumentsUtils
import fr.unreal852.quantum.utils.Extensions.setCustomSpawnPos
import fr.unreal852.quantum.utils.TextUtils
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Formatting
import net.minecraft.world.GameRules

class SetWorldSpawnCommand : Command<ServerCommandSource> {
    override fun run(context: CommandContext<ServerCommandSource>): Int {
        if (context.source == null) {
            return 0
        }

        try {
            val player = context.source.player
            if (player == null || player.world == null) {
                return 0
            }

            val world = player.world

            if (world is ServerWorld) {
                val radius = CommandArgumentsUtils.getIntArgument(context, "spawnRadius", -1)

                if (radius >= 0)
                    world.gameRules.get(GameRules.SPAWN_RADIUS).set(radius, context.source.server)

                world.setCustomSpawnPos(player.pos, player.yaw, player.pitch)

                player.sendMessage(
                    TextUtils.literal(
                        "World spawn set successful -> x=${player.x}, y=${player.y}, z=${player.z}\nAngle: ${player.spawnAngle}°",
                        Formatting.GREEN
                    )
                )
            }

        } catch (e: Exception) {
            Quantum.LOGGER.error("An error occurred while setting the world spawn.", e)
        }

        return 1
    }

    companion object {
        fun register(dispatcher: CommandDispatcher<ServerCommandSource>) {
            dispatcher.register(
                CommandManager.literal("qt")
                    .requires { commandSource: ServerCommandSource -> commandSource.hasPermissionLevel(4) }
                    .then(
                        CommandManager.literal("setSpawn")
                            .then(
                                CommandManager.argument("spawnRadius", IntegerArgumentType.integer(0)).executes(SetWorldSpawnCommand())
                            )
                            .executes(SetWorldSpawnCommand())
                    ))
        }
    }
}