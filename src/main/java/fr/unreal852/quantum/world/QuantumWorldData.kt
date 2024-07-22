package fr.unreal852.quantum.world

import net.minecraft.nbt.NbtCompound
import net.minecraft.util.Identifier
import xyz.nucleoid.fantasy.RuntimeWorldConfig

class QuantumWorldData {

    private var enabled = true

    lateinit var worldId: Identifier
        private set
    lateinit var dimensionId: Identifier
        private set
    lateinit var runtimeWorldConfig: RuntimeWorldConfig
        private set

    constructor()

    constructor(worldId: Identifier, dimensionId: Identifier, runtimeWorldConfig: RuntimeWorldConfig) {
        this.worldId = worldId
        this.dimensionId = dimensionId
        this.runtimeWorldConfig = runtimeWorldConfig
    }

    fun isEnabled(): Boolean {
        return enabled
    }

    fun setEnabled(enabled: Boolean) {
        this.enabled = enabled
    }

    fun writeToNbt(nbt: NbtCompound) {
        nbt.putBoolean("enabled", enabled)
        nbt.putString("worldId", worldId.toString())
        nbt.putString("dimensionId", dimensionId.toString())
    }

    companion object {
        @JvmStatic
        fun fromNbt(nbt: NbtCompound): QuantumWorldData {
            val quantumWorldData = QuantumWorldData()
            quantumWorldData.enabled = nbt.getBoolean("enabled")
            quantumWorldData.worldId = Identifier.of((nbt.getString("worldId")))
            quantumWorldData.dimensionId = Identifier.of((nbt.getString("dimensionId")))
            return quantumWorldData
        }
    }
}
