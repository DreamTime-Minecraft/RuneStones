package ru.ibusewinner.fundaily.runestones.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;
import ru.ibusewinner.fundaily.runestones.Objects.Rune;
import ru.ibusewinner.fundaily.runestones.Objects.RunePlayer;
import ru.ibusewinner.fundaily.runestones.Objects.Types.Effecting;
import ru.ibusewinner.fundaily.runestones.RuneStone;

import java.util.ArrayList;

public class PlayerDeath implements Listener {
    @EventHandler
    public void onDeath(final PlayerDeathEvent playerDeathEvent) {
        final Player entity = playerDeathEvent.getEntity();
        final RunePlayer runePlayer = RuneStone.getRunePlayer(entity);
        if (runePlayer != null && RuneStone.config.getRealConfig().getBoolean("drop-on-death")) {
            final ArrayList<Rune> list = new ArrayList<>();
            for (final Rune rune : runePlayer.getRunes()) {
                list.add(rune);
                entity.getWorld().dropItemNaturally(entity.getLocation(), rune.asItem());
            }
            for (final Rune rune2 : list) {
                runePlayer.deactivate(rune2, true);
            }
        }
    }

    @EventHandler
    public void onRespawn(final PlayerRespawnEvent playerRespawnEvent) {
        final Player player = playerRespawnEvent.getPlayer();
        new BukkitRunnable() {
            private final /* synthetic */ RunePlayer val$rp = RuneStone.getRunePlayer(player);

            public void run() {
                if (this.val$rp != null) {
                    for (final Rune rune : this.val$rp.getRunes()) {
                        if (rune instanceof Effecting) {
                            ((Effecting)rune).addEffect(player);
                        }
                    }
                }
            }
        }.runTaskLater(RuneStone.plugin, 20L);
    }
}
