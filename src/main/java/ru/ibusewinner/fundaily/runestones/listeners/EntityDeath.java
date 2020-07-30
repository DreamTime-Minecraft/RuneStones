package ru.ibusewinner.fundaily.runestones.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import ru.ibusewinner.fundaily.runestones.Objects.RunePlayer;
import ru.ibusewinner.fundaily.runestones.RuneStone;

public class EntityDeath implements Listener {
    @EventHandler
    public void onDeath(final EntityDeathEvent entityDeathEvent) {
        final RunePlayer runePlayer = RuneStone.getRunePlayer(entityDeathEvent.getEntity().getKiller());
        if (runePlayer != null && runePlayer.getRunes().contains(RuneStone.getRune("Double Experience"))) {
            entityDeathEvent.setDroppedExp(entityDeathEvent.getDroppedExp() * 2);
        }
    }
}
