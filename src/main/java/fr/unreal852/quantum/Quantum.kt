package fr.unreal852.quantum

import fr.unreal852.quantum.callback.PlayerRespawnHandler
import fr.unreal852.quantum.callback.PlayerUseSignHandler
import fr.unreal852.quantum.command.CommandRegistration
import fr.unreal852.quantum.utils.Extensions.setDimensionAndGenerator
import fr.unreal852.quantum.world.QuantumWorld
import fr.unreal852.quantum.world.QuantumWorldData
import fr.unreal852.quantum.world.state.QuantumStorage
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents.ServerStarted
import net.fabricmc.fabric.api.event.player.UseBlockCallback
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.server.MinecraftServer
import net.minecraft.util.Identifier
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import xyz.nucleoid.fantasy.Fantasy
import java.nio.file.Path
import java.util.concurrent.ConcurrentHashMap

object Quantum : ModInitializer {

    const val MOD_ID = "quantum"
    val LOGGER: Logger = LoggerFactory.getLogger(MOD_ID)
    val CONFIG_FOLDER: Path = FabricLoader.getInstance().configDir.resolve(MOD_ID)
    private val WORLDS: MutableMap<Identifier, QuantumWorld> = ConcurrentHashMap()

    override fun onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        CommandRegistration.registerCommands()

        ServerLifecycleEvents.SERVER_STARTED.register(ServerStarted { server: MinecraftServer ->

            loadExistingWorlds(server)
        })

        UseBlockCallback.EVENT.register(PlayerUseSignHandler())
        ServerPlayerEvents.AFTER_RESPAWN.register(PlayerRespawnHandler())
    }

    fun worldExists(identifier: Identifier): Boolean {
        return WORLDS.containsKey(identifier)
    }

    fun getOrCreateWorld(server: MinecraftServer, worldData: QuantumWorldData, saveToDisk: Boolean): QuantumWorld? {
        if (WORLDS.containsKey(worldData.worldId))
            return WORLDS[worldData.worldId]

        val fantasy = Fantasy.get(server)
        val runtimeWorldConfig = worldData.runtimeWorldConfig.setDimensionAndGenerator(server, worldData) // Important, set dim and chunk generator
        val runtimeWorldHandle = fantasy.getOrOpenPersistentWorld(worldData.worldId, runtimeWorldConfig)

        val world = QuantumWorld(runtimeWorldHandle, worldData)
        WORLDS[worldData.worldId] = world

        if (saveToDisk)
            QuantumStorage.getQuantumState(server).addWorldData(worldData)

        return world
    }

    fun deleteWorld(identifier: Identifier): Boolean {
        val world = WORLDS.getOrDefault(identifier, null) ?: return false
        val server = world.serverWorld.server
        val fantasy = Fantasy.get(world.serverWorld.server)

        if (!fantasy.tickDeleteWorld(world.serverWorld))
            return false

        val state = QuantumStorage.getQuantumState(server)
        state.removeWorldData(world.worldData)
        WORLDS.remove(identifier)

        return true
    }

    fun loadExistingWorlds(server: MinecraftServer) {
        val state = QuantumStorage.getQuantumState(server)

        for (world in state.getWorlds()) {
            getOrCreateWorld(server, world, false)
            LOGGER.info("Found world '{}', loading it.", world.worldId)
        }
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
//fun test() {
//    val registryManager: DynamicRegistryManager = server.getCombinedDynamicRegistries().getCombinedRegistryManager()
//    val dimensionRegistry: Registry<DimensionOptions> = registryManager.get(RegistryKeys.DIMENSION)
//
//    // iterate all registered dimensions
//    for (key in dimensionsRegistry.getKeys()) {
//        val id = key.value
//
//        // filter the dimension by mod namespace
//        if (id.namespace == "target_mod_id") {
//            // do something with it, e.g. get the world
//            val wKey = RegistryKey.of(RegistryKeys.WORLD, id)
//            val world: ServerWorld = server.getWorld(wKey)
//        }
//    }
//}