package net.polar.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public final class Utils {

    private static final MiniMessage MM = MiniMessage.miniMessage();
    private Utils() {}

    public static @NotNull Component color(@NotNull String text) {
        return MM.deserialize(text);
    }

    public static @NotNull List<Component> color(@NotNull List<String> text) {
        return text.stream().map(MM::deserialize).toList();
    }

    public static @NotNull String generateRandomString(int size) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < size; i++) {
            int index = (int) (characters.length() * Math.random());
            builder.append(characters.charAt(index));
        }
        return builder.toString();
    }

}
