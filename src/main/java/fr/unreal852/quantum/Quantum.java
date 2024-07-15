package fr.unreal852.quantum;

import com.mojang.brigadier.arguments.StringArgumentType;
import fr.unreal852.quantum.command.CreateWorldCommand;
import fr.unreal852.quantum.command.suggestion.BlocksSuggestionProvider;
import fr.unreal852.quantum.command.suggestion.DifficultySuggestionProvider;
import fr.unreal852.quantum.command.suggestion.ItemsSuggestionProvider;
import fr.unreal852.quantum.command.suggestion.WorldsDimensionSuggestionProvider;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.command.argument.ColorArgumentType;
import net.minecraft.command.argument.DimensionArgumentType;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.server.command.CommandManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public class Quantum implements ModInitializer {
    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final String MOD_ID = "quantum";
    public static final Logger LOGGER = LoggerFactory.getLogger("quantum");
    public static final Path CONFIG_FOLDER = FabricLoader.getInstance().getConfigDir().resolve("quantum");


    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        LOGGER.info("Hello Fabric world!");

        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            // TODO: load worlds and portals
        });

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            if (environment.integrated || environment.dedicated) {
                dispatcher.register(CommandManager.literal("qt")
                        .then(CommandManager.literal("create")
                                .requires(commandSource -> commandSource.hasPermissionLevel(4))
                                .then(CommandManager.literal("world")
                                        .then(CommandManager.argument("worldName", StringArgumentType.string())
                                                .executes(new CreateWorldCommand()))
                                        .then(CommandManager.argument("worldDifficulty", StringArgumentType.string())
                                                .suggests(new DifficultySuggestionProvider())
                                                .executes(new CreateWorldCommand()))
                                        .then(CommandManager.argument("worldDimension", DimensionArgumentType.dimension())
                                                .suggests(new WorldsDimensionSuggestionProvider())
                                                .executes(new CreateWorldCommand())))));

                dispatcher.register(CommandManager.literal("qt")
                        .then(CommandManager.literal("create")
                                .requires(commandSource -> commandSource.hasPermissionLevel(4))
                                .then(CommandManager.literal("portal")
                                        .then(CommandManager.argument("destination", DimensionArgumentType.dimension())
                                                .suggests(new WorldsDimensionSuggestionProvider())
                                                .then(CommandManager.argument("portalBlock", IdentifierArgumentType.identifier())
                                                        .suggests(new BlocksSuggestionProvider())
                                                        .then(CommandManager.argument("activateWithItem", IdentifierArgumentType.identifier())
                                                                .suggests(new ItemsSuggestionProvider())
                                                                .then(CommandManager.argument("portalColor", ColorArgumentType.color())
                                                                        .executes(new CreatePortalCommand()))))))));

                dispatcher.register(CommandManager.literal("qt")
                        .then(CommandManager.literal("tp")
                                .requires(commandSource -> commandSource.hasPermissionLevel(4))
                                .then(CommandManager.argument("world", DimensionArgumentType.dimension())
                                        .suggests(new WorldsDimensionSuggestionProvider())
                                        .executes(new TeleportWorldCommand()))));
            }
        });
    }
}