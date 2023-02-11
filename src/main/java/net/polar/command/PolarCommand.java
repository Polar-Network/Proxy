package net.polar.command;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import net.polar.Proxy;
import net.polar.data.Rank;
import net.polar.utils.Utils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public abstract class PolarCommand implements SimpleCommand {

    private final Rank rankRequired;
    private final String name;
    private final String[] aliases;

    private final Map<Integer, List<String>> args = new HashMap<>();

    public PolarCommand(@NotNull Rank rank, @NotNull String name, @Nullable String... aliases) {
        this.rankRequired = rank;
        this.name = name;
        this.aliases = aliases;
    }

    public PolarCommand(@NotNull String name, @Nullable String... aliases) {
        this.rankRequired = Rank.MEMBER;
        this.name = name;
        this.aliases = aliases;
    }

    public abstract void run(CommandSource source, String[] args);

    @Override
    public void execute(Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();
        for (int i = 0; i < args.length; i++) {
            args[i] = args[i].toLowerCase();
            int index = i + 1;
            List<String> suggestions = this.args.get(index);
            if (suggestions != null && !suggestions.contains(args[i])) {
                source.sendMessage(Utils.color("<red>Invalid argument at index " + index + "!"));
                return;
            }
        }
        run(source, args);
    }

    @Override
    public boolean hasPermission(Invocation invocation) {
        CommandSource source = invocation.source();
        if (source instanceof Player player) {
            return Proxy.getInstance().getDatabase().fromCache(player).getRank().isInheriting(rankRequired);
        }
        return true;
    }

    @Override
    public List<String> suggest(Invocation invocation) {
        int length = invocation.arguments().length + 1;
        return args.getOrDefault(length, List.of());
    }

    public Rank getRankRequired() {
        return rankRequired;
    }

    public void addArgument(int index, List<String> arguments) {
        args.put(index, arguments);
    }

    public void addArgument(int index, String... arguments) {
        args.put(index, List.of(arguments));
    }

    public String getName() {
        return name;
    }

    public String[] getAliases() {
        if (aliases == null) return new String[0];
        return aliases;
    }

}
