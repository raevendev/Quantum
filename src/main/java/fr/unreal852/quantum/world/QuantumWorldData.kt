package fr.unreal852.quantum.world

import fr.unreal852.quantum.utils.Extensions.getIdentifier
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.server.MinecraftServer
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Identifier
import xyz.nucleoid.fantasy.RuntimeWorldConfig

class QuantumWorldData(worldId: Identifier, dimensionId: Identifier, runtimeWorldConfig: RuntimeWorldConfig) {

    var worldId: Identifier = worldId
        private set
    var dimensionId: Identifier = dimensionId
        private set
    var runtimeWorldConfig: RuntimeWorldConfig = runtimeWorldConfig
        private set

    fun writeToNbt(nbt: NbtCompound) {
        nbt.putString(WORLD_KEY, worldId.toString())
        nbt.putString(DIM_KEY, dimensionId.toString())
    }

    companion object {

        const val WORLD_KEY = "worldId"
        const val DIM_KEY = "dimensionId"

        fun fromNbt(nbt: NbtCompound): QuantumWorldData {

            val worldId = nbt.getIdentifier(WORLD_KEY)
            val dimensionId = nbt.getIdentifier(DIM_KEY)

            val quantumWorldData = QuantumWorldData(
                worldId,
                dimensionId,
                RuntimeWorldConfig()
            )
            return quantumWorldData
        }
    }
}
