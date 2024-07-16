package fr.unreal852.quantum;

import fr.unreal852.quantum.command.CommandRegistration;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

import static fr.unreal852.quantum.QuantumManager.loadExistingWorlds;

public class Quantum implements ModInitializer
{
    // Mappings
    // class_3218 = ServerWorld
    // class_2960 = Identifier
    // class_2961 = Identifier.Serializer

    // TODO: Cleanup & Refactor Project
    // TODO: Try to switch to Kotlin

    public static final String MOD_ID = "quantum";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final Path CONFIG_FOLDER = FabricLoader.getInstance().getConfigDir().resolve(MOD_ID);

    @Override
    public void onInitialize()
    {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        CommandRegistration.RegisterCommands();

        ServerLifecycleEvents.SERVER_STARTED.register(server ->
        {
            // TODO: load portals
            loadExistingWorlds(server);
        });
    }

}