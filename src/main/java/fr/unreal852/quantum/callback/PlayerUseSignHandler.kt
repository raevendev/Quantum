package fr.unreal852.quantum.callback

import fr.unreal852.quantum.Quantum.Companion.LOGGER
import net.fabricmc.fabric.api.event.player.UseBlockCallback
import net.minecraft.block.SignBlock
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.world.World

class PlayerUseSignHandler : UseBlockCallback {
    override fun interact(player: PlayerEntity?, world: World?, hand: Hand?, hitResult: BlockHitResult?): ActionResult {

        if (world?.isClient == null || player == null || hitResult == null)
            return ActionResult.PASS
        if (world.isClient || player.isSpectator || player.isSneaking)
            return ActionResult.PASS

        val blockState = world.getBlockState(hitResult.blockPos)
        val block = blockState.block

        if (block is SignBlock) {
            LOGGER.info("Sign block found.")
            return ActionResult.SUCCESS
        }
        return ActionResult.PASS
    }
}