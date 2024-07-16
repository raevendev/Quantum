package fr.unreal852.quantum;

import fr.unreal852.quantum.world.QuantumWorld;
import fr.unreal852.quantum.world.QuantumWorldData;
import fr.unreal852.quantum.world.QuantumWorldPersistentState;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameRules;
import xyz.nucleoid.fantasy.Fantasy;
import xyz.nucleoid.fantasy.RuntimeWorldConfig;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class QuantumManager
{
    private static final Map<Identifier, QuantumWorld> WORLDS = new ConcurrentHashMap<>();

    public static QuantumWorld getWorld(Identifier identifier)
    {
        return WORLDS.get(identifier);
    }

    public static QuantumWorld getWorld(String worldName)
    {
        return getWorld(Identifier.of("quantum", worldName));
    }

    public static QuantumWorld getOrOpenPersistentWorld(MinecraftServer server, QuantumWorldData worldData, boolean saveToDisk)
    {
        if (WORLDS.containsKey(worldData.getWorldId()))
            return WORLDS.get(worldData.getWorldId());

        var fantasy = Fantasy.get(server);
        var runtimeWorldConfig = getOrCreateRuntimeWorldConfig(server, worldData);
        var runtimeWorldHandle = fantasy.getOrOpenPersistentWorld(worldData.getWorldId(), runtimeWorldConfig);

        // TODO: CustomPortalsMod.dims.put(worldConfig.getWorldId(), runtimeWorldHandle.getRegistryKey());

        QuantumWorld world = new QuantumWorld(runtimeWorldHandle, worldData);
        WORLDS.put(worldData.getWorldId(), world);

        if (saveToDisk)
            QuantumWorldPersistentState.getQuantumState(server).addWorldData(worldData);

        return world;
    }

    public static RuntimeWorldConfig getOrCreateRuntimeWorldConfig(MinecraftServer server, QuantumWorldData worldData)
    {
        if (worldData.getRuntimeWorldConfig() != null)
            return worldData.getRuntimeWorldConfig();

        var runtimeWorldConfig = new RuntimeWorldConfig();
        var serverWorld = server.getWorld(RegistryKey.of(RegistryKeys.WORLD, worldData.getDimensionId()));

        if (serverWorld != null)
        {
            runtimeWorldConfig
                    .setDimensionType(serverWorld.getDimensionEntry())
                    .setGenerator(serverWorld.getChunkManager().getChunkGenerator());
        }

        if (runtimeWorldConfig.getGenerator() == null)
        {
            runtimeWorldConfig.setGenerator(server.getOverworld().getChunkManager().getChunkGenerator());
            Quantum.LOGGER.warn("The config has no generator, setting the generator to the default one.");
        }

        runtimeWorldConfig.setGameRule(GameRules.DO_DAYLIGHT_CYCLE, true);

        return runtimeWorldConfig;
    }

    public static void loadExistingWorlds(MinecraftServer server)
    {
        var state = QuantumWorldPersistentState.getQuantumState(server);

        for (var world : state.getWorlds())
        {
            if (!world.isEnabled())
                continue;
            getOrOpenPersistentWorld(server, world, false);
            Quantum.LOGGER.info("Found enabled world '{}', loading it.", world.getWorldId());
        }
    }

    // Old Portal code. =====================================
    //    public static PortalLink createPortal(MinecraftServer server, QuantumWorldPortalConfig portalConfig, boolean saveToDisk) {
    //        class_1792 item = (class_1792)class_2378.field_11142.method_10223(portalConfig.getPortalIgniteItemId());
    //        CustomPortalBuilder portalBuilder = CustomPortalBuilder.beginPortal().destDimID(portalConfig.getDestinationId()).frameBlock(portalConfig.getPortalBlockId()).tintColor(portalConfig.getPortalColor());
    //        if (item == class_1802.field_8705) {
    //            portalBuilder.lightWithWater();
    //        } else if (item == class_1802.field_8187) {
    //            portalBuilder.lightWithFluid(class_3612.field_15908);
    //        } else if (item != class_1802.field_8884) {
    //            portalBuilder.lightWithItem(item);
    //        }
    //
    //        PortalLink portalLink = portalBuilder.registerPortal();
    //        PortalRegistrySync.syncLinkToAllPlayers(portalLink, server);
    //        if (saveToDisk) {
    //            FilesUtils.writeObjectToJsonFile((Path)PORTAL_FOLDER.resolve(UUID.randomUUID() + ".json"), portalConfig);
    //        }
    //
    //        return portalLink;
    //    }

    //    public static void loadPortals(MinecraftServer server) {
    //        try {
    //            File directory = PORTAL_FOLDER.toFile();
    //            if (!directory.exists()) {
    //                return;
    //            }
    //
    //            File[] var2 = FilesUtils.listFiles(directory);
    //            int var3 = var2.length;
    //
    //            for (int var4 = 0; var4 < var3; ++var4) {
    //                File file = var2[var4];
    //                if (file.isDirectory()) {
    //                    Quantum.LOGGER.error("The specified file is a directory ! It should be a file");
    //                } else {
    //                    QuantumWorldPortalConfig config = (QuantumWorldPortalConfig) FilesUtils.readObjectFromJsonFile(QuantumWorldPortalConfig.class, file);
    //                    if (config == null) {
    //                        Quantum.LOGGER.warn("Failed to load portal '" + file.getName() + "'");
    //                    } else if (config.isEnabled()) {
    //                        createPortal(server, config, false);
    //                        Quantum.LOGGER.info("Portal '" + file.getName() + "' loaded !");
    //                    }
    //                }
    //            }
    //        } catch (Exception var7) {
    //            Quantum.LOGGER.error(var7);
    //            var7.printStackTrace();
    //        }
    //
    //    }
}