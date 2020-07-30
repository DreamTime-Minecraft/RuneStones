package ru.ibusewinner.fundaily.runestones.Runes.Cosmetic;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import ru.ibusewinner.fundaily.runestones.Objects.Rarity;
import ru.ibusewinner.fundaily.runestones.Objects.Rune;
import ru.ibusewinner.fundaily.runestones.Objects.Types.Cosmetic;
import ru.ibusewinner.fundaily.runestones.RuneStone;

public class Aura2 extends Rune implements Cosmetic {
    private boolean run;

    public Aura2(final String s, final String s2, final Rarity rarity) {
        super(s, s2, rarity);
        this.setVersion(1.13);
    }

    @Override
    public void playEffect(final Player player) {
        this.run = true;
        new BukkitRunnable() {
            public void run() {
                if (!Aura2.this.run) {
                    this.cancel();
                }
                final Location add = player.getLocation().add(0.0, 1.0, 0.0);
                if (RuneStone.serverVersion >= 1.13) {
                    add.getWorld().spawnParticle(Particle.PORTAL, add, 30, 0.7, 0.7, 0.7);
                }
            }
        }.runTaskTimer(RuneStone.plugin, 0L, 5L);
    }

    @Override
    public void stop() {
        this.run = false;
    }
}
