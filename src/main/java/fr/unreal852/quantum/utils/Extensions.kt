@file:Suppress("unused")

package fr.unreal852.quantum.utils

import fr.unreal852.quantum.world.states.QuantumWorldPersistentState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.server.MinecraftServer
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.TeleportTarget
import net.minecraft.world.World

object Extensions {

    fun NbtCompound.getIdentifier(key: String): Identifier {
        return Identifier.of(this.getString(key))
    }

    fun PlayerEntity.teleportToWorld(targetWorld: ServerWorld) {
        val worldState = QuantumWorldPersistentState.getWorldState(targetWorld)
        val teleportTarget = TeleportTarget(
            targetWorld,
            if (targetWorld.registryKey == World.OVERWORLD) targetWorld.spawnPos.toBottomCenterPos()
            else worldState.worldSpawn.toBottomCenterPos(),
            Vec3d.ZERO, worldState.worldSpawnYaw, worldState.worldSpawnPitch, false
        ) {}
        this.teleportTo(teleportTarget)
    }

    fun PlayerEntity.teleportTo(targetWorld: ServerWorld, pos: Vec3d) {
        val teleportTarget = TeleportTarget(targetWorld, pos, Vec3d.ZERO, 0f, 0f, false) {}
        this.teleportTo(teleportTarget)
    }

    fun PlayerEntity.teleportTo(targetWorld: ServerWorld, pos: Vec3d, yaw: Float, pitch: Float) {
        val teleportTarget = TeleportTarget(targetWorld, pos, Vec3d.ZERO, yaw, pitch, false) {}
        this.teleportTo(teleportTarget)
    }

    fun MinecraftServer.getWorldByIdentifier(identifier: Identifier): ServerWorld? {
        return this.getWorld(RegistryKey.of(RegistryKeys.WORLD, identifier))
    }

    fun ServerWorld.setCustomSpawnPos(pos: BlockPos, yaw: Float, pitch: Float) {
        if (this.registryKey == World.OVERWORLD) { // For the overworld lets use the default ones
            this.setSpawnPos(pos, yaw)
            return
        }
        val worldState = QuantumWorldPersistentState.getWorldState(this)
        worldState.setWorldSpawn(pos, yaw, pitch)
    }
}