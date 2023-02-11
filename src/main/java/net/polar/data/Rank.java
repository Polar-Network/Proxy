package net.polar.data;

import org.jetbrains.annotations.NotNull;

public enum Rank {

    OWNER("#C495FF", "#CA58FF", "<BOLD>OWNER", 0l),
    ADMINISTRATOR("#B60000", "#FF7F00", "<bold>ADMINISTRATOR", 0l),
    GRANDMASTER("#B9C52B", "#78FF49", "<bold>GRANDMASTER", 0l),
    LEGEND("#1B69FF", "#B4FBFF", "<bold>LEGEND", 0l),
    ELITE("#C623FF", "#FB41FF", "<bold>ELITE", 0l),
    VIP("#FF4E98", "#FF4E98", "<bold>VIP", 0l),
    MEMBER("#958282", "#958282", "<bold>MEMBER", 0l)
    ;

    private final String prefix;
    private final int weight;
    private final long discordId;

    private final String colorStart;
    private final String colorEnd;


    Rank(String colorStart, String colorEnd, String prefix, long discordId) {
        this.colorStart = colorStart;
        this.colorEnd = colorEnd;
        this.prefix = prefix;
        this.weight = ordinal();
        this.discordId = discordId;
    }


    public boolean isInheriting(@NotNull Rank other) {
        return weight <= other.weight;
    }

    public String surroundString(String string) {
        return "<gradient:" + colorStart + ":" + colorEnd + ">" + string + "</gradient>";
    }

    public String surroundPrefix() {
        return surroundString(prefix);
    }


    public String getPrefix() {
        return prefix;
    }

    public int getWeight() {
        return weight;
    }

    public long getDiscordId() {
        return discordId;
    }

    public static @NotNull Rank getHigherPriority(Rank rank1, Rank rank2) {
        if (rank1.getWeight() < rank2.getWeight()) {
            return rank1;
        } else {
            return rank2;
        }
    }

    public static @NotNull Rank getHigherPriority(Rank... ranks) {
        Rank highest = ranks[0];
        for (Rank rank : ranks) {
            if (rank.getWeight() < highest.getWeight()) {
                highest = rank;
            }
        }
        return highest;
    }

}
