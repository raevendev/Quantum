package fr.unreal852.quantum.command.suggestion;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registries;
import net.minecraft.server.command.ServerCommandSource;

import java.util.concurrent.CompletableFuture;

public class BlocksSuggestionProvider implements SuggestionProvider<ServerCommandSource> {
    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) {

        for (var block : Registries.BLOCK) {
            var blockState = block.getDefaultState();

            if (block != Blocks.AIR) {
                builder.suggest(Registries.BLOCK.getEntry(block).getIdAsString());
            }
        }

        return builder.buildFuture();
    }
}