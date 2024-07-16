package fr.unreal852.quantum.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import fr.unreal852.quantum.Quantum;
import fr.unreal852.quantum.QuantumManager;
import fr.unreal852.quantum.utils.CommandArgumentsUtils;
import fr.unreal852.quantum.utils.TextUtils;
import fr.unreal852.quantum.world.QuantumWorldData;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionTypes;
import xyz.nucleoid.fantasy.RuntimeWorldConfig;

import java.util.Random;

public class CreateWorldCommand implements Command<ServerCommandSource>
{
    @Override
    public int run(CommandContext<ServerCommandSource> context)
    {
        if (context.getSource() == null)
            return 0;
        else
        {
            try
            {
                var server = context.getSource().getServer();
                var worldName = StringArgumentType.getString(context, "worldName").toLowerCase();

                if (QuantumManager.getWorld(worldName) != null)
                {
                    context.getSource().sendError(TextUtils.literal("A world with the same name already exists.", Formatting.WHITE));
                    return 1;
                }

                var worldIdentifier = Identifier.of("quantum", worldName);
                var worldConfig = new RuntimeWorldConfig();

                var difficulty = CommandArgumentsUtils.getEnumArgument(Difficulty.class, context, "worldDifficulty", server.getSaveProperties().getDifficulty());
                var dimensionTypeIdentifier = CommandArgumentsUtils.getIdentifierArgument(context, "worldDimension", DimensionTypes.OVERWORLD_ID);
                var seed = CommandArgumentsUtils.getSeedArgument(context, "worldSeed", (new Random()).nextLong());
                var serverWorld = server.getWorld(RegistryKey.of(RegistryKeys.WORLD, dimensionTypeIdentifier));

                if (serverWorld == null)
                {
                    serverWorld = server.getWorld(World.OVERWORLD);
                    Quantum.LOGGER.warn("No world found for '{}'. Defaulting to minecraft:overworld", dimensionTypeIdentifier.toString());
                }

                worldConfig.setDifficulty(difficulty);
                worldConfig.setDimensionType(serverWorld.getDimensionEntry());
                worldConfig.setGenerator(serverWorld.getChunkManager().getChunkGenerator());
                worldConfig.setSeed(seed);

                QuantumManager.getOrOpenPersistentWorld(server, new QuantumWorldData(worldIdentifier, dimensionTypeIdentifier, worldConfig), true);
                context.getSource().sendMessage(TextUtils.literal("World '" + worldName + "' created!", Formatting.WHITE));
            }
            catch (Exception e)
            {
                Quantum.LOGGER.error("An error occurred while creating the world.", e);
            }

            return 1;
        }
    }
}

