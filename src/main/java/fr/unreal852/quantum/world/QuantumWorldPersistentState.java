package fr.unreal852.quantum.world;

import fr.unreal852.quantum.Quantum;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.PersistentState;

import java.util.ArrayList;
import java.util.List;

public class QuantumWorldPersistentState extends PersistentState
{
    private static final String DATA_KEY = Quantum.MOD_ID;
    private static final String WORLDS_NBT_KEY = Quantum.MOD_ID + ":worlds";
    private static final Type<QuantumWorldPersistentState> PersistentStateTypeLoader = new Type<>(
            QuantumWorldPersistentState::new, // If there's no 'QuantumWorldPersistentState' yet create one
            QuantumWorldPersistentState::fromNbt, // If there is a 'QuantumWorldPersistentState' NBT, parse it with 'createFromNbt'
            null // Supposed to be an 'DataFixTypes' enum, but we can just pass null
    );

    private final List<QuantumWorldData> worlds = new ArrayList<>();

    public List<QuantumWorldData> getWorlds()
    {
        return worlds;
    }

    public void addWorldData(QuantumWorldData quantumWorldData)
    {
        worlds.add(quantumWorldData);
        markDirty();
    }

    public void removeWorldData(QuantumWorldData quantumWorldData)
    {
        worlds.remove(quantumWorldData);
        markDirty();
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup)
    {
        var worldsNbtList = new NbtList();
        for (var entry : worlds)
        {
            var entryNbt = new NbtCompound();
            entry.writeToNbt(entryNbt);
            worldsNbtList.add(entryNbt);
        }
        nbt.put(WORLDS_NBT_KEY, worldsNbtList);
        return nbt;
    }

    public static QuantumWorldPersistentState getQuantumState(MinecraftServer server)
    {
        var stateManager = server.getOverworld().getPersistentStateManager();
        var quantumState = stateManager.getOrCreate(PersistentStateTypeLoader, DATA_KEY);
        quantumState.markDirty();
        return quantumState;
    }

    public static QuantumWorldPersistentState fromNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup)
    {
        var quantumWorldPersistentState = new QuantumWorldPersistentState();
        var worldsNbtList = nbt.getList(WORLDS_NBT_KEY, 10); // 10 is the NbtCompound type
        for (int i = 0; i < worldsNbtList.size(); i++)
        {
            var entryNbt = worldsNbtList.getCompound(i);
            quantumWorldPersistentState.worlds.add(QuantumWorldData.fromNbt(entryNbt));
        }
        return quantumWorldPersistentState;
    }
}
