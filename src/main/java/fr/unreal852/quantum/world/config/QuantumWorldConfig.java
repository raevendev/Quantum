package fr.unreal852.quantum.world.config;

import com.google.gson.annotations.Expose;
import fr.unreal852.quantum.Quantum;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.world.Difficulty;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import xyz.nucleoid.fantasy.RuntimeWorldConfig;

public class QuantumWorldConfig {
    @Expose
    private boolean enabled = true;
    @Expose
    private Identifier worldId;
    @Expose
    private Identifier dimensionId;
    @Expose
    private long seed;
    @Expose
    private Difficulty difficulty;
    private RuntimeWorldConfig _runtimeWorldConfig;

    public QuantumWorldConfig() {
    }

    public QuantumWorldConfig(Identifier worldId, Identifier dimensionId, RuntimeWorldConfig worldConfig) {
        this.worldId = worldId;
        this.dimensionId = dimensionId;
        this._runtimeWorldConfig = worldConfig;
        if (this._runtimeWorldConfig != null) {
            this.difficulty = this._runtimeWorldConfig.getDifficulty();
            this.seed = this._runtimeWorldConfig.getSeed();
        }
    }

    public Identifier getWorldId() {
        return this.worldId;
    }

    public Identifier getDimensionId() {
        return this.dimensionId;
    }

    public RuntimeWorldConfig getOrCreateRuntimeWorldConfig(MinecraftServer server) {
        if (this._runtimeWorldConfig != null) {
            return this._runtimeWorldConfig;
        } else {
            this._runtimeWorldConfig = new RuntimeWorldConfig();
            var world = server.getWorld(RegistryKey.of(Registries.WORLD_KEY, this.dimensionId));
            server.getWorlds()
            server.getWorld(RegistryKey.of(Registries.REGISTRIES.))
            var world = server.method_3847(class_5321.method_29179(class_2378.field_25298, this.dimensionId));
            if (world != null) {
                this._runtimeWorldConfig.setDimensionType(world.getDimension());
                this._runtimeWorldConfig.setGenerator(world.getChunkManager().getChunkGenerator().getGeneratorOptions());
            }

            if (this._runtimeWorldConfig.getGenerator() == null) {
                this._runtimeWorldConfig.setGenerator(server.getOverworld().getChunkManager().getChunkGenerator());
                Quantum.LOGGER.warn("The config has no generator, setting the generator to the default one.");
            }

            this._runtimeWorldConfig.setDifficulty(this.difficulty);
            this._runtimeWorldConfig.setSeed(this.seed);
            this._runtimeWorldConfig.setGameRule(GameRules.DO_DAYLIGHT_CYCLE, true);
            return this._runtimeWorldConfig;
        }
    }
}