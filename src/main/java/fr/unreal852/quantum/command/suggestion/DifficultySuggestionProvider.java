package fr.unreal852.quantum.command.suggestion;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.world.Difficulty;

import java.util.concurrent.CompletableFuture;

public class DifficultySuggestionProvider implements SuggestionProvider<ServerCommandSource> {
    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) {
        Difficulty[] difficulties = Difficulty.values();

        for (Difficulty difficulty : difficulties) {
            builder.suggest(difficulty.name());
        }

        return builder.buildFuture();
    }
}
