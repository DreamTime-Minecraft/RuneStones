package ru.ibusewinner.fundaily.runestones.Runes.Effecting;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import ru.ibusewinner.fundaily.runestones.Objects.Rarity;
import ru.ibusewinner.fundaily.runestones.Objects.Rune;
import ru.ibusewinner.fundaily.runestones.Objects.Types.Effecting;

public class Resistance extends Rune implements Effecting {
    public Resistance(final String s, final String s2, final Rarity rarity) {
        super(s, s2, rarity);
    }

    @Override
    public void addEffect(final Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0));
    }

    @Override
    public void removeEffect(final Player player) {
        player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
    }
}
