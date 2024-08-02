package fr.unreal852.quantum.callback

import fr.unreal852.quantum.state.QuantumWorldStorage
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents.AfterRespawn
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket
import net.minecraft.network.packet.s2c.play.PositionFlag
import net.minecraft.server.network.ServerPlayerEntity

class PlayerRespawnHandler : AfterRespawn {

    // A Minecraft bug ignore the world spawn angle, this fix it for now
    // https://bugs.mojang.com/browse/MC-200092

    override fun afterRespawn(oldPlayer: ServerPlayerEntity, newPlayer: ServerPlayerEntity, alive: Boolean) {

        val worldState = QuantumWorldStorage.getWorldState(newPlayer.serverWorld)

        val playerPositionPacket = PlayerPositionLookS2CPacket(
            worldState.worldSpawnPos.x, worldState.worldSpawnPos.y, worldState.worldSpawnPos.z,
            worldState.worldSpawnAngle.x, worldState.worldSpawnAngle.y, PositionFlag.ROT, 0
        )

        newPlayer.networkHandler.sendPacket(playerPositionPacket)
    }
}