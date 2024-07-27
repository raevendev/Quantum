package fr.unreal852.quantum.world.states

import fr.unreal852.quantum.Quantum
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryWrapper.WrapperLookup
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.Vec2f
import net.minecraft.util.math.Vec3d
import net.minecraft.world.PersistentState

class QuantumWorldPersistentState : PersistentState() {

    lateinit var worldSpawnPos: Vec3d
        private set
    var worldSpawnAngle = Vec2f(0.0f, 0.0f) // X is yaw, Y is pitch
        private set

    fun setWorldSpawn(worldSpawn: Vec3d, worldSpawnYaw: Float, worldSpawnPitch: Float) {
        this.worldSpawnPos = worldSpawn
        this.worldSpawnAngle = Vec2f(worldSpawnYaw, worldSpawnPitch)
        markDirty()
    }

    override fun writeNbt(nbt: NbtCompound, registryLookup: WrapperLookup): NbtCompound {
        // write spawn pos
        nbt.putDouble(SPAWN_POS_X_NBT_KEY, worldSpawnPos.x)
        nbt.putDouble(SPAWN_POS_Y_NBT_KEY, worldSpawnPos.y)
        nbt.putDouble(SPAWN_POS_Z_NBT_KEY, worldSpawnPos.z)

        // write spawn angle
        nbt.putFloat(SPAWN_POS_YAW_NBT_KEY, worldSpawnAngle.x)
        nbt.putFloat(SPAWN_POS_PITCH_NBT_KEY, worldSpawnAngle.y)
        return nbt
    }

    companion object {

        private const val DATA_KEY = "${Quantum.MOD_ID}_world"
        private const val SPAWN_POS_X_NBT_KEY = "${Quantum.MOD_ID}:spawnposx"
        private const val SPAWN_POS_Y_NBT_KEY = "${Quantum.MOD_ID}:spawnposy"
        private const val SPAWN_POS_Z_NBT_KEY = "${Quantum.MOD_ID}:spawnposz"
        private const val SPAWN_POS_YAW_NBT_KEY = "${Quantum.MOD_ID}:spawnposyaw"
        private const val SPAWN_POS_PITCH_NBT_KEY = "${Quantum.MOD_ID}:spawnpospitch"

        private val PersistentStateTypeLoader = Type(
            { QuantumWorldPersistentState() },
            { nbt: NbtCompound, registryLookup: WrapperLookup -> fromNbt(nbt, registryLookup) },
            null
        )

        fun getWorldState(world: ServerWorld): QuantumWorldPersistentState {
            val worldState = world.persistentStateManager.getOrCreate(PersistentStateTypeLoader, DATA_KEY)
            if (!worldState::worldSpawnPos.isInitialized)
                worldState.worldSpawnPos = world.spawnPos.toBottomCenterPos()
            worldState.markDirty()
            return worldState
        }

        @Suppress("UNUSED_PARAMETER")
        fun fromNbt(nbt: NbtCompound, registryLookup: WrapperLookup): QuantumWorldPersistentState {
            val worldState = QuantumWorldPersistentState()

            // get spawn pos
            val spawnPosX = nbt.getDouble(SPAWN_POS_X_NBT_KEY)
            val spawnPosY = nbt.getDouble(SPAWN_POS_Y_NBT_KEY)
            val spawnPosZ = nbt.getDouble(SPAWN_POS_Z_NBT_KEY)

            // get spawn angle
            val spawnPosYaw = nbt.getFloat(SPAWN_POS_YAW_NBT_KEY)
            val spawnPosPitch = nbt.getFloat(SPAWN_POS_PITCH_NBT_KEY)

            worldState.worldSpawnPos = Vec3d(spawnPosX, spawnPosY, spawnPosZ)
            worldState.worldSpawnAngle = Vec2f(spawnPosYaw, spawnPosPitch)
            return worldState
        }
    }
}
