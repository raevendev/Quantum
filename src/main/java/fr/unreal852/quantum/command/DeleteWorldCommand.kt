package fr.unreal852.quantum.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import fr.unreal852.quantum.Quantum;
import fr.unreal852.quantum.QuantumManager;
import fr.unreal852.quantum.utils.TextUtils;
import fr.unreal852.quantum.world.QuantumWorldPersistentState;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Formatting;
import xyz.nucleoid.fantasy.Fantasy;

public class DeleteWorldCommand implements Command<ServerCommandSource>
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
                var worldName = IdentifierArgumentType.getIdentifier(context, "worldName");
                var quantumWorld = QuantumManager.getWorld(worldName);

                if (quantumWorld == null)
                {
                    context.getSource().sendError(TextUtils.literal("The specified world doesn't exists or has not been created using Quantum.", Formatting.RED));
                    return 1;
                }

                var fantasy = Fantasy.get(server);
                if (fantasy.tickDeleteWorld(quantumWorld.getServerWorld()))
                {
                    var state = QuantumWorldPersistentState.getQuantumState(server);
                    state.removeWorldData(quantumWorld.getWorldData());
                    context.getSource().sendMessage(TextUtils.literal("World '" + worldName + "' deleted!", Formatting.GREEN));
                }
            }
            catch (Exception e)
            {
                Quantum.LOGGER.error("An error occurred while creating the world.", e);
            }

            return 1;
        }
    }
}

