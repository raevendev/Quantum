package fr.unreal852.quantum.world;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import xyz.nucleoid.fantasy.RuntimeWorldConfig;

public class QuantumWorldData
{
    private boolean enabled = true;
    private Identifier worldId;
    private Identifier dimensionId;
    private RuntimeWorldConfig runtimeWorldConfig;

    public QuantumWorldData()
    {
    }

    public QuantumWorldData(Identifier worldId, Identifier dimensionId, RuntimeWorldConfig runtimeWorldConfig)
    {
        this.worldId = worldId;
        this.dimensionId = dimensionId;
        this.runtimeWorldConfig = runtimeWorldConfig;
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    public Identifier getWorldId()
    {
        return worldId;
    }

    public Identifier getDimensionId()
    {
        return dimensionId;
    }

    public RuntimeWorldConfig getRuntimeWorldConfig()
    {
        return runtimeWorldConfig;
    }

    public void writeToNbt(NbtCompound nbt)
    {
        nbt.putBoolean("enabled", enabled);
        nbt.putString("worldId", worldId.toString());
        nbt.putString("dimensionId", dimensionId.toString());
    }

    public static QuantumWorldData fromNbt(NbtCompound nbt)
    {
        var quantumWorldData = new QuantumWorldData();
        quantumWorldData.enabled = nbt.getBoolean("enabled");
        quantumWorldData.worldId = Identifier.of((nbt.getString("worldId")));
        quantumWorldData.dimensionId = Identifier.of((nbt.getString("dimensionId")));
        return quantumWorldData;
    }
}
