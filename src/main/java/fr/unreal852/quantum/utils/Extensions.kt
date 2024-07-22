package fr.unreal852.quantum.utils

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Identifier
import net.minecraft.util.math.Vec3d
import net.minecraft.world.TeleportTarget

object Extensions {

    fun NbtCompound.getIdentifier(key: String): Identifier {
        return Identifier.of(this.getString(key))
    }

    fun PlayerEntity.teleportTo(targetWorld: ServerWorld, pos: Vec3d) {
        val teleportTarget = TeleportTarget(targetWorld, pos, Vec3d.ZERO, 0f, 0f, false) {}
        this.teleportTo(teleportTarget)
    }
}