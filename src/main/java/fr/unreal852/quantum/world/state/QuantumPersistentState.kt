package fr.unreal852.quantum.world.state

import fr.unreal852.quantum.Quantum
import fr.unreal852.quantum.world.QuantumWorldData
import fr.unreal852.quantum.world.QuantumWorldData.Companion.fromNbt
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtList
import net.minecraft.registry.RegistryWrapper.WrapperLookup
import net.minecraft.server.MinecraftServer
import net.minecraft.world.PersistentState

class QuantumPersistentState : PersistentState() {

    private val worlds: MutableList<QuantumWorldData> = ArrayList()

    fun getWorlds(): List<QuantumWorldData> {
        return worlds
    }

    fun addWorldData(quantumWorldData: QuantumWorldData) {
        worlds.add(quantumWorldData)
        markDirty()
    }

    fun removeWorldData(quantumWorldData: QuantumWorldData) {
        worlds.remove(quantumWorldData)
        markDirty()
    }

    override fun writeNbt(nbt: NbtCompound, registryLookup: WrapperLookup): NbtCompound {
        val worldsNbtList = NbtList()
        for (entry in worlds) {
            val entryNbt = NbtCompound()
            entry.writeToNbt(entryNbt)
            worldsNbtList.add(entryNbt)
        }
        nbt.put(WORLDS_NBT_KEY, worldsNbtList)
        return nbt
    }

    companion object {

        private const val DATA_KEY = Quantum.MOD_ID
        private const val WORLDS_NBT_KEY = "${Quantum.MOD_ID}:worlds"

        private val PersistentStateTypeLoader = Type(
            { QuantumPersistentState() },
            { nbt: NbtCompound, registryLookup: WrapperLookup -> fromNbt(nbt, registryLookup) },
            null
        )

        fun getQuantumState(server: MinecraftServer): QuantumPersistentState {
            val stateManager = server.overworld.persistentStateManager
            val quantumState = stateManager.getOrCreate(PersistentStateTypeLoader, DATA_KEY)
            quantumState.markDirty()
            return quantumState
        }

        @Suppress("UNUSED_PARAMETER")
        fun fromNbt(nbt: NbtCompound, registryLookup: WrapperLookup): QuantumPersistentState {
            val quantumPersistentState = QuantumPersistentState()
            val worldsNbtList = nbt.getList(WORLDS_NBT_KEY, 10) // 10 is the NbtCompound type
            for (i in worldsNbtList.indices) {
                val entryNbt = worldsNbtList.getCompound(i)
                quantumPersistentState.worlds.add(fromNbt(entryNbt))
            }
            return quantumPersistentState
        }
    }
}
