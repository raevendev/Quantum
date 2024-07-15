package fr.unreal852.quantum.utils;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.argument.ColorArgumentType;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public final class CommandArgumentsUtils {
    public static String getStringArgument(CommandContext<ServerCommandSource> context, String argumentName, String defaultValue) {
        try {
            return StringArgumentType.getString(context, argumentName);
        } catch (IllegalArgumentException e) {
            return defaultValue;
        }
    }

    public static Identifier getIdentifierArgument(CommandContext<ServerCommandSource> context, String argumentName, Identifier defaultValue) {
        try {
            return IdentifierArgumentType.getIdentifier(context, argumentName);
        } catch (IllegalArgumentException e) {
            return defaultValue;
        }
    }

    public static <T extends Enum<T>> T getEnumArgument(Class<T> enumClass, CommandContext<ServerCommandSource> context, String argumentName, T defaultValue) {
        try {
            return Enum.valueOf(enumClass, StringArgumentType.getString(context, argumentName));
        } catch (IllegalArgumentException e) {
            return defaultValue;
        }
    }

    public static Formatting getColorArgument(CommandContext<ServerCommandSource> context, String argumentName, Formatting defaultValue) {
        try {
            return ColorArgumentType.getColor(context, argumentName);
        } catch (IllegalArgumentException e) {
            return defaultValue;
        }
    }
}

