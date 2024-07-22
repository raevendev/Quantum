package fr.unreal852.quantum.callback

import fr.unreal852.quantum.utils.Extensions.teleportTo
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

            val text = signEntity.backText.getMessage(0, false).string
            val identifier = Identifier.of(text)
            val targetWorld = world.server?.getWorld(RegistryKey.of(RegistryKeys.WORLD, identifier))

            if (targetWorld is ServerWorld) {
                player.teleportTo(targetWorld, targetWorld.spawnPos.toBottomCenterPos())
                return ActionResult.SUCCESS
            }
        }

        return ActionResult.PASS
    }
}