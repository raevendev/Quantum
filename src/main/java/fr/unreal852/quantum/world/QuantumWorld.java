package fr.unreal852.quantum.world;

import fr.unreal852.quantum.world.config.QuantumWorldConfig;
import net.minecraft.server.world.ServerWorld;
import xyz.nucleoid.fantasy.RuntimeWorldHandle;

public class QuantumWorld
{
    private final RuntimeWorldHandle _runtimeWorldHandle;
    private final QuantumWorldConfig _worldConfig;

    public QuantumWorld(RuntimeWorldHandle runtimeWorldHandle, QuantumWorldConfig worldConfig)
    {
        _runtimeWorldHandle = runtimeWorldHandle;
        _worldConfig = worldConfig;
    }

    public RuntimeWorldHandle getRuntimeWorld()
    {
        return _runtimeWorldHandle;
    }

    public ServerWorld getServerWorld()
    {
        return _runtimeWorldHandle.asWorld();
    }

    public QuantumWorldConfig getWorldConfig()
    {
        return _worldConfig;
    }
}