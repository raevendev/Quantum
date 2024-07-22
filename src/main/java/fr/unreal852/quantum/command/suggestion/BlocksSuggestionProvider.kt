package fr.unreal852.quantum.command.suggestion

import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionProvider
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import net.minecraft.block.Blocks
import net.minecraft.registry.Registries
import net.minecraft.server.command.ServerCommandSource
import java.util.concurrent.CompletableFuture

class BlocksSuggestionProvider : SuggestionProvider<ServerCommandSource?> {
    override fun getSuggestions(context: CommandContext<ServerCommandSource?>, builder: SuggestionsBuilder): CompletableFuture<Suggestions> {
        for (block in Registries.BLOCK) {
            val blockState = block.defaultState

            if (block !== Blocks.AIR) {
                builder.suggest(Registries.BLOCK.getEntry(block).idAsString)
            }
        }

        return builder.buildFuture()
    }
}