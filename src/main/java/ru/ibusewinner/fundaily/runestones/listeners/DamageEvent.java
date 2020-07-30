package ru.ibusewinner.fundaily.runestones.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import ru.ibusewinner.fundaily.runestones.Objects.RunePlayer;
import ru.ibusewinner.fundaily.runestones.RuneStone;

public class DamageEvent implements Listener {
    @EventHandler
    public void onDamage(final EntityDamageEvent entityDamageEvent) {
        if (entityDamageEvent.getEntity() instanceof Player) {
            final Player player = (Player)entityDamageEvent.getEntity();
            if (entityDamageEvent.getCause() == EntityDamageEvent.DamageCause.FALL) {
                final RunePlayer runePlayer = RuneStone.getRunePlayer(player);
                if (runePlayer != null && (runePlayer.getRunes().contains(RuneStone.getRune("Feather Foot")) || runePlayer.getRunes().contains(RuneStone.getRune("Astronaut")))) {
                    entityDamageEvent.setCancelled(true);
                }
            }
        }
    }
}
