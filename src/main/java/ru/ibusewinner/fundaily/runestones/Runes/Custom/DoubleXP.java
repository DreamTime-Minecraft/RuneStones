package ru.ibusewinner.fundaily.runestones.Runes.Custom;

import org.bukkit.entity.Player;
import ru.ibusewinner.fundaily.runestones.Objects.Rarity;
import ru.ibusewinner.fundaily.runestones.Objects.Rune;
import ru.ibusewinner.fundaily.runestones.Objects.Types.Custom;

public class DoubleXP extends Rune implements Custom {
    public DoubleXP(final String s, final String s2, final Rarity rarity) {
        super(s, s2, rarity);
    }

    @Override
    public void effect(final Player player) {
    }

    @Override
    public void remove(final Player player) {
    }
}
