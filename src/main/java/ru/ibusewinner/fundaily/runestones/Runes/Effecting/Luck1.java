package ru.ibusewinner.fundaily.runestones.Runes.Effecting;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import ru.ibusewinner.fundaily.runestones.Objects.Rarity;
import ru.ibusewinner.fundaily.runestones.Objects.Rune;
import ru.ibusewinner.fundaily.runestones.Objects.Types.Effecting;

public class Luck1 extends Rune implements Effecting {
    public Luck1(final String s, final String s2, final Rarity rarity) {
        super(s, s2, rarity);
        this.setVersion(1.9);
    }

    @Override
    public void addEffect(final Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.LUCK, Integer.MAX_VALUE, 0));
    }

    @Override
    public void removeEffect(final Player player) {
        player.removePotionEffect(PotionEffectType.LUCK);
    }
}
