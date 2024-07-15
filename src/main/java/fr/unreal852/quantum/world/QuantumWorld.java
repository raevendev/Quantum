package fr.unreal852.quantum.world;

import fr.unreal852.quantum.world.config.QuantumWorldConfig;
import net.minecraft.server.world.ServerWorld;
import xyz.nucleoid.fantasy.RuntimeWorldHandle;

public class QuantumWorld {

    private final RuntimeWorldHandle _runtimeWorldHandle;
    private final QuantumWorldConfig _worldConfig;

    public QuantumWorld(RuntimeWorldHandle runtimeWorldHandle, QuantumWorldConfig worldConfig) {
        this._runtimeWorldHandle = runtimeWorldHandle;
        this._worldConfig = worldConfig;
    }

    public RuntimeWorldHandle getRuntimeWorld() {
        return this._runtimeWorldHandle;
    }

    public ServerWorld getServerWorld() {
        return this._runtimeWorldHandle.asWorld();
    }

    public QuantumWorldConfig getWorldConfig() {
        return this._worldConfig;
    }
}