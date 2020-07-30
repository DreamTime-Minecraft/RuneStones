package ru.ibusewinner.fundaily.runestones.Objects;

import org.bukkit.ChatColor;

public enum Rarity {
    LEGENDARY("LEGENDARY", 0, ChatColor.AQUA, 1, 0L, 4, 10000.0),
    EPIC("EPIC", 1, ChatColor.LIGHT_PURPLE, 2, 0L, 8, 5000.0),
    UNIQUE("UNIQUE", 2, ChatColor.YELLOW, 3, 0L, 12, 3000.0),
    NORMAL("NORMAL", 3, ChatColor.GRAY, 4, 0L, 16, 1000.0);

    private ChatColor colour;
    private int shardValue;
    private long forgeDelay;
    private int forgeCost;
    private double suggestedCost;

    private Rarity(final String name, final int ordinal, final ChatColor colour, final int shardValue, final long forgeDelay, final int forgeCost, final double suggestedCost) {
        this.colour = colour;
        this.suggestedCost = suggestedCost;
        this.forgeCost = forgeCost;
        this.forgeDelay = forgeDelay;
        this.shardValue = shardValue;
    }

    public ChatColor getColor() {
        return this.colour;
    }

    public long getForgeDelay() {
        return this.forgeDelay;
    }

    public int getForgeCost() {
        return this.forgeCost;
    }

    public int getShardValue() {
        return this.shardValue;
    }

    public void setShardValue(final int shardValue) {
        this.shardValue = shardValue;
    }

    public void setForgeDelay(final long n) {
        this.forgeDelay = n * 60L * 1000L;
    }

    public void setForgeCost(final int forgeCost) {
        this.forgeCost = forgeCost;
    }

    public double getSuggestedCost() {
        return this.suggestedCost;
    }
}
