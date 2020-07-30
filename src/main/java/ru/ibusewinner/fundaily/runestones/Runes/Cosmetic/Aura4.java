package ru.ibusewinner.fundaily.runestones.Runes.Cosmetic;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import ru.ibusewinner.fundaily.runestones.Objects.Rarity;
import ru.ibusewinner.fundaily.runestones.Objects.Rune;
import ru.ibusewinner.fundaily.runestones.RuneStone;
import ru.ibusewinner.fundaily.runestones.Objects.Types.Cosmetic;

public class Aura4 extends Rune implements Cosmetic {
    private boolean run;

    public Aura4(final String s, final String s2, final Rarity rarity) {
        super(s, s2, rarity);
        this.setVersion(1.13);
    }

    @Override
    public void playEffect(final Player player) {
        this.run = true;
        new BukkitRunnable() {
            public void run() {
                if (!Aura4.this.run) {
                    this.cancel();
                }
                final Location add = player.getLocation().add(0.0, 1.0, 0.0);
                add.getWorld().spawnParticle(Particle.SNOWBALL, add, 25, 0.7, 0.7, 0.7);
            }
        }.runTaskTimer(RuneStone.plugin, 0L, 5L);
    }

    @Override
    public void stop() {
        this.run = false;
    }
}
