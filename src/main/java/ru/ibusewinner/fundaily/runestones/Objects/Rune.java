package ru.ibusewinner.fundaily.runestones.Objects;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import ru.ibusewinner.fundaily.runestones.Objects.Types.Cosmetic;
import ru.ibusewinner.fundaily.runestones.Objects.Types.Custom;
import ru.ibusewinner.fundaily.runestones.Objects.Types.Effecting;
import ru.ibusewinner.fundaily.runestones.RuneStone;
import ru.ibusewinner.fundaily.runestones.Utils.XMaterial;
import ru.ibusewinner.fundaily.runestones.Utils.ezItem;

import java.util.ArrayList;
import java.util.List;

public class Rune {
    public static List<Rune> runes;
    private String name;
    private String lore;
    private Rarity rarity;
    private double cost;
    private String customName;
    private double version;

    static {
        Rune.runes = new ArrayList<>();
    }

    public Rune(final String s, final String lore, final Rarity rarity) {
        this.cost = -1.0;
        this.name = s;
        this.lore = lore;
        this.rarity = rarity;
        this.customName = s;
        this.version = 1.8;
        Rune.runes.add(this);
    }

    public void activate(final Player player) {
        if (this instanceof Effecting) {
            ((Effecting)this).addEffect(player);
        }
        if (this instanceof Custom) {
            ((Custom)this).effect(player);
        }
        if (this instanceof Cosmetic) {
            ((Cosmetic)this).playEffect(player);
        }
    }

    public void deactivate(final Player player) {
        if (this instanceof Custom) {
            ((Custom)this).remove(player);
        }
        if (this instanceof Effecting) {
            ((Effecting)this).removeEffect(player);
        }
        if (this instanceof Cosmetic) {
            ((Cosmetic)this).stop();
        }
    }

    public ItemStack asItem() {
        return new ezItem(XMaterial.EMERALD)
                .withDisplayName(RuneStone.Format("&a" + this.getCustomName() + " Артефакт"))
                .withLore(RuneStone.Format("\n&7" + this.lore.replaceAll("\n", "\n&7") + "\n\n" + this.rarity.getColor() + this.rarity.toString()))
                .withEnchantment(Enchantment.LUCK, 1)
                .withItemFlag(ItemFlag.HIDE_ENCHANTS)
                .get();
    }

    public void setCustomName(final String customName) {
        this.customName = customName;
    }

    public void setRarity(final Rarity rarity) {
        this.rarity = rarity;
    }

    public String getRealName() {
        return this.name;
    }

    public String getCustomName() {
        return this.customName;
    }

    public String getLore() {
        return this.lore;
    }

    public void setLore(final String lore) {
        this.lore = lore;
    }

    public Rarity getRarity() {
        return this.rarity;
    }

    public double getCost() {
        return this.cost;
    }

    public void setCost(final double cost) {
        this.cost = cost;
    }

    public double getVersion() {
        return this.version;
    }

    public void setVersion(final double version) {
        this.version = version;
    }

    public static List<Rune> getAllRunes() {
        return Rune.runes;
    }
}
