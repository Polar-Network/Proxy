package net.polar.data;

import org.jetbrains.annotations.NotNull;

public enum Rank {

    OWNER("#C495FF", "#CA58FF", "<BOLD>OWNER", 1074084929123651644L),
    ADMINISTRATOR("#B60000", "#FF7F00", "<bold>ADMINISTRATOR", 1074085081100058685L),
    MODERATOR("#FB72B4", "#9543FD", "<bold>MODERATOR", 1074086504751693824L),
    GRANDMASTER("#B9C52B", "#78FF49", "<bold>GRANDMASTER", 1074085173651570761L),
    LEGEND("#1B69FF", "#B4FBFF", "<bold>LEGEND", 1074085582365523988L),
    ELITE("#C623FF", "#FB41FF", "<bold>ELITE", 1074085680407396433L),
    VIP("#FF4E98", "#FF4E98", "<bold>VIP", 1074085770303910071L),
    MEMBER("#958282", "#958282", "<bold>MEMBER", 1074085810934140958L)
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
