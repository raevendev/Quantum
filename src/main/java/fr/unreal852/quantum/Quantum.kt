package fr.unreal852.quantum

import fr.unreal852.quantum.callback.PlayerUseSignHandler
import fr.unreal852.quantum.command.CommandRegistration
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents.ServerStarted
import net.fabricmc.fabric.api.event.player.UseBlockCallback
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.block.SignBlock
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.server.MinecraftServer
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.world.World
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.file.Path

class Quantum : ModInitializer {
    override fun onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        CommandRegistration.registerCommands()

        ServerLifecycleEvents.SERVER_STARTED.register(ServerStarted { server: MinecraftServer? ->
            // TODO: load portals
            if (server != null) {
                QuantumManager.loadExistingWorlds(server)
            }
        })

        UseBlockCallback.EVENT.register(PlayerUseSignHandler())
    }

    companion object {

        const val MOD_ID: String = "quantum"

        val LOGGER: Logger = LoggerFactory.getLogger(MOD_ID)
        val CONFIG_FOLDER: Path = FabricLoader.getInstance().configDir.resolve(MOD_ID)
    }
}