package fr.unreal852.quantum.command.suggestion

import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionProvider
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import fr.unreal852.quantum.utils.Extensions.isIn
import net.minecraft.block.BlockWithEntity
import net.minecraft.block.PlantBlock
import net.minecraft.registry.Registries
import net.minecraft.registry.tag.BlockTags
import net.minecraft.server.command.ServerCommandSource
import java.util.concurrent.CompletableFuture

class BlocksSuggestionProvider : SuggestionProvider<ServerCommandSource> {

    override fun getSuggestions(context: CommandContext<ServerCommandSource>, builder: SuggestionsBuilder): CompletableFuture<Suggestions> {
        for (block in Registries.BLOCK) {
            val blockState = block.defaultState

            if (block is BlockWithEntity || block is PlantBlock)
                continue

            if (blockState.isIn(
                    BlockTags.AIR, BlockTags.DOORS, BlockTags.BUTTONS, BlockTags.ALL_SIGNS, BlockTags.SLABS, BlockTags.STAIRS,
                    BlockTags.BEDS, BlockTags.BANNERS, BlockTags.RAILS, BlockTags.TRAPDOORS,
                    BlockTags.PRESSURE_PLATES, BlockTags.FENCES, BlockTags.FENCE_GATES, BlockTags.SAPLINGS, BlockTags.WOOL_CARPETS
                )
            )
                continue

            builder.suggest(blockState.registryEntry.idAsString)
        }

        return builder.buildFuture()
    }
}