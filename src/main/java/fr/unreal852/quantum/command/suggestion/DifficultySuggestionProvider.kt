package fr.unreal852.quantum.command.suggestion

import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionProvider
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.world.Difficulty
import java.util.concurrent.CompletableFuture

class DifficultySuggestionProvider : SuggestionProvider<ServerCommandSource?> {
    override fun getSuggestions(context: CommandContext<ServerCommandSource?>, builder: SuggestionsBuilder): CompletableFuture<Suggestions> {
        val difficulties = Difficulty.entries.toTypedArray()

        for (difficulty in difficulties) {
            builder.suggest(difficulty.name)
        }

        return builder.buildFuture()
    }
}
