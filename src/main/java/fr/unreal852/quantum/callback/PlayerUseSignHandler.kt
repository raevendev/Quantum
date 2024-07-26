package fr.unreal852.quantum.callback

import fr.unreal852.quantum.utils.Extensions.teleportTo
import fr.unreal852.quantum.utils.Extensions.teleportToWorld
import net.fabricmc.fabric.api.event.player.UseBlockCallback
import net.minecraft.block.SignBlock
import net.minecraft.block.WallSignBlock
import net.minecraft.block.entity.SignBlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.world.World

class PlayerUseSignHandler : UseBlockCallback {
    override fun interact(player: PlayerEntity, world: World, hand: Hand, hitResult: BlockHitResult): ActionResult {

        if (world.isClient || player.isSpectator || player.isSneaking)
            return ActionResult.PASS

        val blockState = world.getBlockState(hitResult.blockPos)
        val block = blockState.block

        if (block !is SignBlock && block !is WallSignBlock)
            return ActionResult.PASS

        val signEntity = world.getBlockEntity(hitResult.blockPos)

        if (signEntity is SignBlockEntity) {

            val firstLine = signEntity.backText.getMessage(0, false).string
            if (!firstLine.equals("teleport", true))
                return ActionResult.PASS
            val secondLine = signEntity.backText.getMessage(1, false).string
            val thirdLine = signEntity.backText.getMessage(2, false).string
            if (secondLine.isNullOrEmpty() && thirdLine.isNullOrEmpty())
                return ActionResult.PASS
            val identifier = Identifier.of(secondLine, thirdLine)
            val targetWorld = world.server?.getWorld(RegistryKey.of(RegistryKeys.WORLD, identifier))

            if (targetWorld is ServerWorld) {
                player.teleportToWorld(targetWorld)
                return ActionResult.SUCCESS
            }
        }

        return ActionResult.PASS
    }
}