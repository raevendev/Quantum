package fr.unreal852.quantum.command.suggestion

import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionProvider
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import net.minecraft.item.BlockItem
import net.minecraft.item.Items
import net.minecraft.registry.Registries
import net.minecraft.server.command.ServerCommandSource
import java.util.concurrent.CompletableFuture

class ItemsSuggestionProvider : SuggestionProvider<ServerCommandSource?> {
    override fun getSuggestions(context: CommandContext<ServerCommandSource?>, builder: SuggestionsBuilder): CompletableFuture<Suggestions> {
        for (item in Registries.ITEM) {
            if (item != Items.AIR && item !is BlockItem) {
                builder.suggest(item.toString())
            }
        }

        return builder.buildFuture()
    }
}
