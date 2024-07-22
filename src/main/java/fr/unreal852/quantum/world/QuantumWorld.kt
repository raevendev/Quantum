package fr.unreal852.quantum.world;

import net.minecraft.server.world.ServerWorld;
import xyz.nucleoid.fantasy.RuntimeWorldHandle;

public class QuantumWorld
{
    private final RuntimeWorldHandle _runtimeWorldHandle;
    private final QuantumWorldData _worldData;

    public QuantumWorld(RuntimeWorldHandle runtimeWorldHandle, QuantumWorldData worldConfig)
    {
        _runtimeWorldHandle = runtimeWorldHandle;
        _worldData = worldConfig;
    }

    public RuntimeWorldHandle getRuntimeWorld()
    {
        return _runtimeWorldHandle;
    }

    public ServerWorld getServerWorld()
    {
        return _runtimeWorldHandle.asWorld();
    }


    public QuantumWorldData getWorldData()
    {
        return _worldData;
    }
}