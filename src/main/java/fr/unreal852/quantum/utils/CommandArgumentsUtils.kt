package fr.unreal852.quantum.utils

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import net.minecraft.command.argument.ColorArgumentType
import net.minecraft.command.argument.IdentifierArgumentType
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier

object CommandArgumentsUtils {

    fun getStringArgument(context: CommandContext<ServerCommandSource>, argumentName: String, defaultValue: String): String {
        return try {
            StringArgumentType.getString(context, argumentName)
        } catch (e: IllegalArgumentException) {
            defaultValue
        }
    }

    fun getIdentifierArgument(context: CommandContext<ServerCommandSource>, argumentName: String, defaultValue: Identifier): Identifier {
        return try {
            IdentifierArgumentType.getIdentifier(context, argumentName)
        } catch (e: IllegalArgumentException) {
            defaultValue
        }
    }

    fun <T : Enum<T>> getEnumArgument(enumClass: Class<T>, context: CommandContext<ServerCommandSource>, argumentName: String, defaultValue: T): T {
        return try {
            enumClass.enumConstants.first { it.name == StringArgumentType.getString(context, argumentName) }
        } catch (e: IllegalArgumentException) {
            defaultValue
        }
    }

    fun getColorArgument(context: CommandContext<ServerCommandSource>, argumentName: String, defaultValue: Formatting): Formatting {
        return try {
            ColorArgumentType.getColor(context, argumentName)
        } catch (e: IllegalArgumentException) {
            defaultValue
        }
    }

    fun getSeedArgument(context: CommandContext<ServerCommandSource>, argumentName: String, defaultValue: Long): Long {
        return try {
            val rawSeed = StringArgumentType.getString(context, argumentName)
            var parsedSeed = ParseUtils.tryParseLong(rawSeed, 0)

            if (parsedSeed != 0L) // The seed was given in a long format we can return it
                return parsedSeed

            // The seed was given in a string format, we have to hash it
            for (i in rawSeed.indices) {
                parsedSeed = 31 * parsedSeed + rawSeed[i].code
            }

            parsedSeed
        } catch (e: IllegalArgumentException) {
            defaultValue
        }
    }
}