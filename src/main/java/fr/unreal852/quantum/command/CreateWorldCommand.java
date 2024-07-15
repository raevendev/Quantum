package fr.unreal852.quantum.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;

public class CreateWorldCommand implements Command<ServerCommandSource> {
    public int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        if (context.getSource() == null) {
            return 0;
        } else {
            try {
                var minecraftServer = context.getSource().getServer();
                var worldName = StringArgumentType.getString(context, "worldName").toLowerCase();
                if (QuantumManager.getWorld(worldName) != null) {
                    ((class_2168) context.getSource()).method_9226(TextUtils.literal("A world with the same name already exists.", class_124.field_1061), false);
                    return 1;
                }

                class_2960 worldIdentifier = new class_2960("quantum", worldName);
                RuntimeWorldConfig worldConfig = new RuntimeWorldConfig();
                class_1267 difficulty = (class_1267) CommandArgumentsUtils.getEnumArgument(class_1267.class, context, "worldDifficulty", server.method_30002().method_8407());
                class_2960 dimensionType = CommandArgumentsUtils.getIdentifierArgument(context, "worldDimension", server.method_30002().method_27983().method_29177());
                class_3218 serverWorld = server.method_3847(class_5321.method_29179(class_2378.field_25298, dimensionType));
                if (serverWorld == null) {
                    serverWorld = server.method_30002();
                    Quantum.LOGGER.warn("No world found for '" + dimensionType.toString() + "'. Defaulting to minecraft:overworld");
                }

                worldConfig.setDifficulty(difficulty);
                worldConfig.setDimensionType(serverWorld.method_44013());
                worldConfig.setGenerator(serverWorld.method_14178().method_12129());
                worldConfig.setSeed((new Random()).nextLong());
                QuantumManager.getOrCreateWorld(server, new QuantumWorldConfig(worldIdentifier, dimensionType, worldConfig), true);
                ((class_2168) context.getSource()).method_9226(TextUtils.literal("World '" + worldName + "' created !", class_124.field_1060), false);
            } catch (Exception var9) {
                Quantum.LOGGER.error(var9);
            }

            return 1;
        }
    }
}

