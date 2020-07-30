package ru.ibusewinner.fundaily.runestones.Runes.Cosmetic;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import ru.ibusewinner.fundaily.runestones.Objects.Rarity;
import ru.ibusewinner.fundaily.runestones.Objects.Rune;
import ru.ibusewinner.fundaily.runestones.RuneStone;
import ru.ibusewinner.fundaily.runestones.Objects.Types.Cosmetic;

public class Helix2 extends Rune implements Cosmetic {
    private boolean run;

    public Helix2(final String s, final String s2, final Rarity rarity) {
        super(s, s2, rarity);
        this.setVersion(1.13);
    }

    @Override
    public void playEffect(final Player player) {
        this.run = true;
        new BukkitRunnable() {
            double t = 0.0;
            double r = 0.8;

            public void run() {
                if (!Helix2.this.run) {
                    this.cancel();
                    return;
                }
                final Location add = player.getLocation().add(0.0, 2.0, 0.0);
                this.t += 0.19634954084936207;
                final double n = this.r * Math.cos(this.t);
                final double n2 = Math.sin(this.t) * this.r * 0.2;
                final double n3 = Math.sin(this.t) * this.r;
                add.add(n, n2, n3);
                add.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, add, 2, 0.0, 0.0, 0.0, 0.0);
                add.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, add, 2, 0.0, 0.0, 0.0, 0.0);
                add.subtract(n, n2, n3);
                if (this.t > 18.84955592153876) {
                    this.t = 0.0;
                }
            }
        }.runTaskTimer(RuneStone.plugin, 0L, 1L);
    }

    @Override
    public void stop() {
        this.run = false;
    }
}
