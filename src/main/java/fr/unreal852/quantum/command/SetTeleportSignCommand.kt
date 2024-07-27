package fr.unreal852.quantum.command

import com.mojang.brigadier.Command
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.context.CommandContext
import fr.unreal852.quantum.Quantum
import fr.unreal852.quantum.command.suggestion.WorldsDimensionSuggestionProvider
import fr.unreal852.quantum.utils.TextUtils
import net.minecraft.block.SignBlock
import net.minecraft.block.WallSignBlock
import net.minecraft.block.entity.SignBlockEntity
import net.minecraft.block.entity.SignText
import net.minecraft.command.argument.DimensionArgumentType
import net.minecraft.command.argument.IdentifierArgumentType
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text
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
                        player.eyePos.add(player.rotationVecClient.multiply(20.0)),
                        RaycastContext.ShapeType.OUTLINE,
                        RaycastContext.FluidHandling.NONE,
                        player
                    )

                    val hitResult = world.raycast(raycastContext)
                    val blockState = world.getBlockState(hitResult.blockPos)

                    if (blockState.block !is SignBlock && blockState.block !is WallSignBlock) {
                        context.source.sendMessage(TextUtils.literal("You must look at a sign block", Formatting.RED))
                        return 0
                    }

                    val worldName = IdentifierArgumentType.getIdentifier(context, "world")
                    val world = context.source.server.getWorld(RegistryKey.of(RegistryKeys.WORLD, worldName))

                    if (world == null) {
                        context.source.sendMessage(TextUtils.literal("No world found with id '$worldName'", Formatting.RED))
                        return 0
                    }

                    val signEntity = world.getBlockEntity(hitResult.blockPos) as SignBlockEntity? ?: return 0

                    signEntity.changeText({
                        SignText()
                            .withMessage(0, Text.literal("teleport"))
                            .withMessage(1, Text.literal(worldName.namespace))
                            .withMessage(2, Text.literal(worldName.path))
                    }, false)
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
                                CommandManager.argument("world", DimensionArgumentType.dimension())
                                    .suggests(WorldsDimensionSuggestionProvider())
                                    .executes(SetTeleportSignCommand())
                            )
                            .executes(SetTeleportSignCommand())
                    ))
        }
    }
}