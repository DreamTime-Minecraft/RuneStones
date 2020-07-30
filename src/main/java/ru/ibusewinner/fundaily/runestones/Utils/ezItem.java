package ru.ibusewinner.fundaily.runestones.Utils;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import ru.ibusewinner.fundaily.runestones.RuneStone;

import java.util.ArrayList;
import java.util.List;

public class ezItem {
    private ItemStack item;

    public ezItem(final ItemStack item) {
        this.item = item;
    }

    public ezItem(final XMaterial xMaterial) {
        this.item = new ItemStack(xMaterial.parseMaterial(), 1, (short)xMaterial.data);
    }

    public ezItem(final XMaterial xMaterial, final int n, final short n2) {
        this.item = new ItemStack(xMaterial.parseMaterial(), n, n2);
    }

    public ezItem withDisplayName(final String displayName) {
        final ItemMeta itemMeta = this.item.getItemMeta();
        itemMeta.setDisplayName(displayName);
        this.item.setItemMeta(itemMeta);
        return this;
    }

    public ezItem withAmount(final int amount) {
        this.item.setAmount(amount);
        return this;
    }

    public ezItem withDurability(final short durability) {
        this.item.setDurability(durability);
        return this;
    }

    public ezItem withData(final MaterialData data) {
        this.item.setData(data);
        return this;
    }

    public ezItem withLore(final String s) {
        final ArrayList<String> lore = new ArrayList<String>();
        final ItemMeta itemMeta = this.item.getItemMeta();
        String[] split;
        for (int length = (split = s.split("\n")).length, i = 0; i < length; ++i) {
            lore.add(split[i]);
        }
        itemMeta.setLore(lore);
        this.item.setItemMeta(itemMeta);
        return this;
    }

    public ezItem withLore(final List<String> lore) {
        final ItemMeta itemMeta = this.item.getItemMeta();
        itemMeta.setLore(lore);
        this.item.setItemMeta(itemMeta);
        return this;
    }

    public ezItem withEnchantment(final Enchantment enchantment, final int n) {
        final ItemMeta itemMeta = this.item.getItemMeta();
        itemMeta.addEnchant(enchantment, n, true);
        this.item.setItemMeta(itemMeta);
        return this;
    }

    public ezItem withItemFlag(final ItemFlag itemFlag) {
        final ItemMeta itemMeta = this.item.getItemMeta();
        itemMeta.addItemFlags(itemFlag);
        this.item.setItemMeta(itemMeta);
        return this;
    }

    public ItemStack get() {
        return this.item;
    }

    public static ItemStack getTrue() {
        return new ezItem(XMaterial.LIME_DYE).withDisplayName(" ").get();
    }

    public static ItemStack getFalse() {
        return new ezItem(XMaterial.GRAY_DYE).withDisplayName(" ").get();
    }
}
