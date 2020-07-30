package ru.ibusewinner.fundaily.runestones.Runes.Custom;

import org.bukkit.entity.Player;
import ru.ibusewinner.fundaily.runestones.Objects.Rarity;
import ru.ibusewinner.fundaily.runestones.Objects.Rune;
import ru.ibusewinner.fundaily.runestones.Objects.Types.Custom;

public class Flight extends Rune implements Custom {
    public Flight(final String s, final String s2, final Rarity rarity) {
        super(s, s2, rarity);
    }

    @Override
    public void effect(final Player player) {
        player.setAllowFlight(true);
    }

    @Override
    public void remove(final Player player) {
        player.setAllowFlight(false);
        player.setFlying(false);
    }
}
