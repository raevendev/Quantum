package fr.unreal852.quantum.command.suggestion

import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionProvider
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import net.minecraft.server.command.ServerCommandSource
import java.util.concurrent.CompletableFuture

class WorldsDimensionSuggestionProvider : SuggestionProvider<ServerCommandSource> {
    override fun getSuggestions(context: CommandContext<ServerCommandSource>, builder: SuggestionsBuilder): CompletableFuture<Suggestions> {

        for (world in context.source.worldKeys) {
            builder.suggest(world.value.toString())
        }

        return builder.buildFuture()
    }
}