package fr.unreal852.quantum.command

import com.mojang.brigadier.Command
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.context.CommandContext
import fr.unreal852.quantum.Quantum
import fr.unreal852.quantum.utils.TextUtils
import net.minecraft.block.SignBlock
import net.minecraft.block.WallSignBlock
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Formatting
import net.minecraft.world.RaycastContext

class SetTeleportSignCommand : Command<ServerCommandSource> {
    override fun run(context: CommandContext<ServerCommandSource>): Int {
        if (context.source == null) {
            return 0
        } else {
            try {
                val player = context.source.player
                if (player == null || player.world == null) {
                    return 0
                }

                val world = player.world

                if (world is ServerWorld) {

                    val raycastContext = RaycastContext(
                        player.eyePos,
                        player.eyePos.add(player.rotationVecClient.multiply(200.0)),
                        RaycastContext.ShapeType.OUTLINE,
                        RaycastContext.FluidHandling.NONE,
                        player
                    )

                    val hitResult = world.raycast(raycastContext)
                    val blockState = world.getBlockState(hitResult.blockPos)
                    Quantum.LOGGER.info(blockState.block.name.toString())
                    if (blockState.block !is SignBlock && blockState.block !is WallSignBlock) {
                        context.source.sendMessage(TextUtils.literal("You must look at a sign block", Formatting.RED))
                        return 0
                    }

                }

            } catch (e: Exception) {
                Quantum.LOGGER.error("An error occurred while teleporting the player.", e)
            }

            return 1
        }
    }

    companion object {
        fun register(dispatcher: CommandDispatcher<ServerCommandSource>) {
            dispatcher.register(
                CommandManager.literal("qt")
                    .requires { commandSource: ServerCommandSource -> commandSource.hasPermissionLevel(4) }
                    .then(
                        CommandManager.literal("setdestination")
                            .then(
                                CommandManager.argument("spawnRadius", IntegerArgumentType.integer(0))
                                    .executes(SetTeleportSignCommand())
                            )
                            .executes(SetTeleportSignCommand())
                    ))
        }
    }
}