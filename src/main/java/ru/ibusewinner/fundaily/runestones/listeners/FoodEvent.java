package ru.ibusewinner.fundaily.runestones.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import ru.ibusewinner.fundaily.runestones.Objects.RunePlayer;
import ru.ibusewinner.fundaily.runestones.RuneStone;

public class FoodEvent implements Listener {
    @EventHandler
    public void onFoodChange(final FoodLevelChangeEvent foodLevelChangeEvent) {
        if (foodLevelChangeEvent.getEntity() instanceof Player) {
            final RunePlayer runePlayer = RuneStone.getRunePlayer((Player)foodLevelChangeEvent.getEntity());
            if (runePlayer != null && runePlayer.getRunes().contains(RuneStone.getRune("Saturation"))) {
                foodLevelChangeEvent.setCancelled(true);
            }
        }
    }
}
