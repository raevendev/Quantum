﻿@file:Suppress("unused")

package fr.unreal852.quantum.utils

import com.mojang.brigadier.arguments.IntegerArgumentType
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

    fun getIntArgument(context: CommandContext<ServerCommandSource>, argumentName: String, defaultValue: Int): Int {
        return try {
            IntegerArgumentType.getInteger(context, argumentName)
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

    fun <T : Enum<T>> getEnumArgument(context: CommandContext<ServerCommandSource>, argumentName: String, enumClass: Class<T>, defaultValue: T): T {
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
            val parsedSeed = ParseUtils.tryParseLong(rawSeed)

            if (parsedSeed != null) // The seed was given in a long format we can return it
                return parsedSeed

            var hashedSeed = 0L

            // The seed was given in a string format, we have to hash it
            for (i in rawSeed.indices) {
                hashedSeed = 31 * hashedSeed + rawSeed[i].code
            }

            hashedSeed
        } catch (e: IllegalArgumentException) {
            defaultValue
        }
    }
}