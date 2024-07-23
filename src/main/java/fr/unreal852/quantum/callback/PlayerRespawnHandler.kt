package fr.unreal852.quantum.callback

import fr.unreal852.quantum.Quantum
import fr.unreal852.quantum.utils.Extensions.teleportTo
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents.AfterRespawn
import net.fabricmc.fabric.api.event.player.UseBlockCallback
import net.minecraft.block.SignBlock
import net.minecraft.block.WallSignBlock
import net.minecraft.block.entity.SignBlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.world.World

class PlayerRespawnHandler : AfterRespawn {

    override fun afterRespawn(oldPlayer: ServerPlayerEntity?, newPlayer: ServerPlayerEntity?, alive: Boolean) {
        Quantum.LOGGER.info("RESPAWNED")
    }
}