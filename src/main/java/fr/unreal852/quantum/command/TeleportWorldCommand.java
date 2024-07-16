package fr.unreal852.quantum.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import fr.unreal852.quantum.Quantum;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class TeleportWorldCommand implements Command<ServerCommandSource> {
    @Override
    public int run(CommandContext<ServerCommandSource> context) {
        if (context.getSource() == null) {
            return 0;
        } else {
            try {
                PlayerEntity player = context.getSource().getPlayer();
                if (player == null || player.getWorld() == null) {
                    return 0;
                }

                var worldName = IdentifierArgumentType.getIdentifier(context, "world");
                var world = context.getSource().getServer().getWorld(RegistryKey.of(RegistryKeys.WORLD, worldName));
                if (world == null) {
                    context.getSource().sendError(Text.literal("The specified world '" + worldName + "' doesn't exist."));
                    return 0;
                }

                BlockPos pos = world.getSpawnPos();
                player.teleport(world, pos.getX(), pos.getY(), pos.getZ(), PositionFlag.VALUES, world.getSpawnAngle(), 0.0F);
            } catch (Exception e) {
                Quantum.LOGGER.error("An error occurred while teleporting the player.", e);
            }

            return 1;
        }
    }
}