package fr.unreal852.quantum.world.states

import fr.unreal852.quantum.Quantum
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryWrapper.WrapperLookup
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.world.PersistentState

class QuantumWorldPersistentState : PersistentState() {

    lateinit var worldSpawn: BlockPos
        private set
    var worldSpawnYaw = 0f
        private set
    var worldSpawnPitch = 0f
        private set

    fun setWorldSpawn(worldSpawn: BlockPos, worldSpawnYaw: Float, worldSpawnPitch: Float) {
        this.worldSpawn = worldSpawn
        this.worldSpawnYaw = worldSpawnYaw
        this.worldSpawnPitch = worldSpawnPitch
        markDirty()
    }

    override fun writeNbt(nbt: NbtCompound, registryLookup: WrapperLookup): NbtCompound {
        nbt.putLong(SPAWN_NBT_KEY, worldSpawn.asLong())
        nbt.putFloat(SPAWN_YAW_NBT_KEY, worldSpawnYaw)
        nbt.putFloat(SPAWN_PITCH_NBT_KEY, worldSpawnPitch)
        return nbt
    }

    companion object {

        private const val DATA_KEY = "${Quantum.MOD_ID}:world"
        private const val SPAWN_NBT_KEY = "${Quantum.MOD_ID}:spawnpos"
        private const val SPAWN_YAW_NBT_KEY = "${Quantum.MOD_ID}:spawnposyaw"
        private const val SPAWN_PITCH_NBT_KEY = "${Quantum.MOD_ID}:spawnpospitch"

        private val PersistentStateTypeLoader = Type(
            { QuantumWorldPersistentState() },
            { nbt: NbtCompound, registryLookup: WrapperLookup -> fromNbt(nbt, registryLookup) },
            null
        )

        fun getWorldState(world: ServerWorld): QuantumWorldPersistentState {
            val worldState = world.persistentStateManager.getOrCreate(PersistentStateTypeLoader, DATA_KEY)
            if (!worldState::worldSpawn.isInitialized)
                worldState.worldSpawn = world.spawnPos
            worldState.markDirty()
            return worldState
        }

        @Suppress("UNUSED_PARAMETER")
        fun fromNbt(nbt: NbtCompound, registryLookup: WrapperLookup): QuantumWorldPersistentState {
            val worldState = QuantumWorldPersistentState()
            val spawnPosLong = nbt.getLong(SPAWN_NBT_KEY)
            worldState.worldSpawn = BlockPos.fromLong(spawnPosLong)
            worldState.worldSpawnYaw = nbt.getFloat(SPAWN_YAW_NBT_KEY)
            worldState.worldSpawnPitch = nbt.getFloat(SPAWN_PITCH_NBT_KEY)
            return worldState
        }
    }
}
