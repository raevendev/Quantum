package fr.unreal852.quantum;

import fr.unreal852.quantum.utils.FilesUtils;
import fr.unreal852.quantum.world.QuantumWorld;
import fr.unreal852.quantum.world.config.QuantumWorldConfig;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import xyz.nucleoid.fantasy.Fantasy;
import xyz.nucleoid.fantasy.RuntimeWorldHandle;

import java.io.File;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class QuantumManager
{
    private static final Map<Identifier, QuantumWorld> WORLDS = new ConcurrentHashMap<>();
    public static final Path WORLDS_FOLDER;
    public static final Path PORTAL_FOLDER;

    public static QuantumWorld getWorld(Identifier identifier)
    {
        return WORLDS.get(identifier);
    }

    public static QuantumWorld getWorld(String worldName)
    {
        return getWorld(Identifier.of("quantum", worldName));
    }

    public static QuantumWorld getOrCreateWorld(MinecraftServer server, QuantumWorldConfig worldConfig, boolean saveToDisk)
    {
        Fantasy fantasy = Fantasy.get(server);
        RuntimeWorldHandle runtimeWorldHandle = fantasy.getOrOpenPersistentWorld(worldConfig.getWorldId(), worldConfig.getOrCreateRuntimeWorldConfig(server));
        // TODO: CustomPortalsMod.dims.put(worldConfig.getWorldId(), runtimeWorldHandle.getRegistryKey());
        QuantumWorld world = new QuantumWorld(runtimeWorldHandle, worldConfig);
        WORLDS.put(worldConfig.getWorldId(), world);
        if (saveToDisk)
        {
            // TODO: make this better
            FilesUtils.writeObjectToJsonFile((Path) WORLDS_FOLDER.resolve(worldConfig.getWorldId().toString().replace(':', '_') + ".json"), worldConfig);
        }

        return world;
    }

    public static void loadExistingWorlds(MinecraftServer server)
    {
        try
        {
            File directory = WORLDS_FOLDER.toFile();
            if (!directory.exists())
            {
                return;
            }

            File[] var2 = FilesUtils.listFiles(directory);
            int var3 = var2.length;

            for (int var4 = 0; var4 < var3; ++var4)
            {
                File file = var2[var4];
                if (file.isDirectory())
                {
                    Quantum.LOGGER.error("The specified file is a directory ! It should be a file");
                }
                else
                {
                    QuantumWorldConfig config = (QuantumWorldConfig) FilesUtils.readObjectFromJsonFile(QuantumWorldConfig.class, file);
                    if (config != null && !config.getWorldId().getNamespace().contains("minecraft"))
                    {
                        getOrCreateWorld(server, config, false);
                        Quantum.LOGGER.info("World '" + config.getWorldId().toString() + "' loaded !");
                    }
                    else
                    {
                        Quantum.LOGGER.warn("Failed to load world '" + file.getName() + "'");
                    }
                }
            }
        }
        catch (Exception var7)
        {
            Quantum.LOGGER.error(var7.getMessage());
        }

    }

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

    static
    {
        WORLDS_FOLDER = Quantum.CONFIG_FOLDER.resolve("worlds");
        PORTAL_FOLDER = Quantum.CONFIG_FOLDER.resolve("portals");
    }
}