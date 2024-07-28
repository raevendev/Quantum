package fr.unreal852.quantum.world

import net.minecraft.server.world.ServerWorld
import xyz.nucleoid.fantasy.RuntimeWorldHandle

class QuantumWorld(val runtimeWorld: RuntimeWorldHandle, val worldData: QuantumWorldData) {
    val serverWorld: ServerWorld
        get() = runtimeWorld.asWorld()
}