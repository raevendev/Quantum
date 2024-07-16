package fr.unreal852.quantum.world.config;

import com.google.gson.annotations.Expose;
import fr.unreal852.quantum.Quantum;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameRules;
import xyz.nucleoid.fantasy.RuntimeWorldConfig;

public class QuantumWorldConfig
{
    @Expose
    private boolean _enabled = true;
    @Expose
    private long _seed;
    @Expose
    private Identifier _worldId;
    @Expose
    private Identifier _dimensionId;
    @Expose
    private Difficulty _difficulty;
    private RuntimeWorldConfig _runtimeWorldConfig;

    public QuantumWorldConfig()
    {
    }

    public QuantumWorldConfig(Identifier worldId, Identifier dimensionId, RuntimeWorldConfig worldConfig)
    {
        _worldId = worldId;
        _dimensionId = dimensionId;
        _runtimeWorldConfig = worldConfig;
        if (_runtimeWorldConfig != null)
        {
            _difficulty = _runtimeWorldConfig.getDifficulty();
            _seed = _runtimeWorldConfig.getSeed();
        }
    }

    public Identifier getWorldId()
    {
        return _worldId;
    }

    public Identifier getDimensionId()
    {
        return _dimensionId;
    }

    public RuntimeWorldConfig getOrCreateRuntimeWorldConfig(MinecraftServer server)
    {
        if (_runtimeWorldConfig == null)
        {
            _runtimeWorldConfig = new RuntimeWorldConfig();
            var world = server.getWorld(RegistryKey.of(RegistryKeys.WORLD, _dimensionId));
            if (world != null)
            {
                _runtimeWorldConfig.setDimensionType(world.getDimensionEntry());
                _runtimeWorldConfig.setGenerator(world.getChunkManager().getChunkGenerator());
            }

            if (_runtimeWorldConfig.getGenerator() == null)
            {
                _runtimeWorldConfig.setGenerator(server.getOverworld().getChunkManager().getChunkGenerator());
                Quantum.LOGGER.warn("The config has no generator, setting the generator to the default one.");
            }

            _runtimeWorldConfig.setDifficulty(_difficulty);
            _runtimeWorldConfig.setSeed(_seed);
            _runtimeWorldConfig.setGameRule(GameRules.DO_DAYLIGHT_CYCLE, true);
        }
        return this._runtimeWorldConfig;
    }
}