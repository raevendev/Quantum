package fr.unreal852.quantum.callback

import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents.AfterRespawn
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket
import net.minecraft.network.packet.s2c.play.PositionFlag
import net.minecraft.server.network.ServerPlayerEntity

class PlayerRespawnHandler : AfterRespawn {

    // A Minecraft bug ignore the world spawn angle, this fix it for now
    // https://bugs.mojang.com/browse/MC-200092

    override fun afterRespawn(oldPlayer: ServerPlayerEntity, newPlayer: ServerPlayerEntity, alive: Boolean) {

        val playerPositionPacket = PlayerPositionLookS2CPacket(
            newPlayer.x, newPlayer.y, newPlayer.z,
            newPlayer.world.spawnAngle, 0f, PositionFlag.ROT, 0
        )

        newPlayer.networkHandler.sendPacket(playerPositionPacket)
    }
}