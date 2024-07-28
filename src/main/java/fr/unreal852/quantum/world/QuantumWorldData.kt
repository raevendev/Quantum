package fr.unreal852.quantum.world

import fr.unreal852.quantum.utils.Extensions.getIdentifier
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.Identifier
import xyz.nucleoid.fantasy.RuntimeWorldConfig

class QuantumWorldData(worldId: Identifier, dimensionId: Identifier, runtimeWorldConfig: RuntimeWorldConfig?) {

    var enabled = true

    var worldId: Identifier = worldId
        private set
    var dimensionId: Identifier = dimensionId
        private set
    var runtimeWorldConfig: RuntimeWorldConfig? = runtimeWorldConfig
        private set

    fun writeToNbt(nbt: NbtCompound) {
        nbt.putBoolean("enabled", enabled)
        nbt.putString("worldId", worldId.toString())
        nbt.putString("dimensionId", dimensionId.toString())
    }

    companion object {

        fun fromNbt(nbt: NbtCompound): QuantumWorldData {
            val quantumWorldData = QuantumWorldData(
                nbt.getIdentifier("worldId"),
                nbt.getIdentifier("dimensionId"), null
            )
            quantumWorldData.enabled = nbt.getBoolean("enabled")
            return quantumWorldData
        }
    }
}
