package ru.ibusewinner.fundaily.runestones.Objects;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import ru.ibusewinner.fundaily.runestones.RuneStone;
import ru.ibusewinner.fundaily.runestones.Utils.FileManager;
import ru.ibusewinner.fundaily.runestones.Utils.RuneConfig;
import ru.ibusewinner.fundaily.runestones.commands.RuneGUI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class RunePlayer {
    public static List<RunePlayer> players;
    private Player p;
    private List<Rune> runes;
    private HashMap<Rune, Integer> runeBag;
    private HashMap<Rarity, Integer> shards;
    private HashMap<Rune, Long> forging;
    private Inventory runeBagInv;

    static {
        RunePlayer.players = new ArrayList<>();
    }

    public RunePlayer(final Player p) {
        this.p = p;
        this.runes = new ArrayList<>();
        this.shards = new HashMap<>();
        this.forging = new HashMap<>();
        this.runeBag = new HashMap<>();
        this.runeBagInv = Bukkit.createInventory(null, this.getRuneBagLevel() * 9, RuneStone.Format(RuneStone.msg.getString("Rune-Bag-Name").replaceAll("%level%", String.valueOf(this.getRuneBagLevel()))));
        this.loadData();
        RunePlayer.players.add(this);
        for (Rune rune : this.runes) {
            rune.activate(p);
        }
    }

    public int getShards(final Rarity key) {
        return this.shards.get(key);
    }

    public HashMap<Rune, Long> getForging() {
        return this.forging;
    }

    public boolean isForged(final Rune rune) {
        return this.forging.containsKey(rune) && RuneStone.hasPassed(rune.getRarity().getForgeDelay(), this.forging.get(rune));
    }

    public long getTimeLeft(final Rune rune) {
        if (this.forging.containsKey(rune)) {
            return this.forging.get(rune) + rune.getRarity().getForgeDelay() - System.currentTimeMillis();
        }
        return -1L;
    }

    public void forge(final Rune key) {
        this.removeShards(key.getRarity(), key.getRarity().getForgeCost());
        final long currentTimeMillis = System.currentTimeMillis();
        this.forging.put(key, currentTimeMillis);
        final RuneConfig config = FileManager.getConfig("playerdata.yml");
        assert config != null;
        final List<String> stringList = config.getStringList(this.p.getUniqueId() + ".forging");
        stringList.add(key.getRealName() + ":" + currentTimeMillis);
        config.set(this.p.getUniqueId() + ".forging", stringList);
        try {
            config.save();
        }catch (IOException ex) { }
    }

    public void collectForge(final Rune key) {
        if (this.isForged(key)) {
            if (this.p.getInventory().firstEmpty() == -1) {
                this.p.sendMessage(RuneStone.Format(RuneStone.msg.getString("Full-Inventory")));
                return;
            }
            this.p.getInventory().addItem(key.asItem());
            this.forging.remove(key);
            final RuneConfig config = FileManager.getConfig("playerdata.yml");
            final ArrayList<Rune> list = new ArrayList<>(this.forging.keySet());
            final ArrayList<String> list2 = new ArrayList<>();
            for (int i = 0; i < list.size(); ++i) {
                list2.add(list.get(i).getRealName() + ":" + this.forging.get(list.get(i)).toString());
            }
            assert config != null;
            config.set(this.p.getUniqueId() + ".forging", list2);
            try {
                config.save();
            }catch (IOException ex) { }
        }
    }

    public void addShards(final Rarity key, final int n) {
        this.shards.put(key, this.shards.get(key) + n);
        final RuneConfig config = FileManager.getConfig("playerdata.yml");
        config.set(this.p.getUniqueId() + ".shards." + key.toString().toLowerCase(), this.shards.get(key));
        try {
            config.save();
        }catch (IOException ex) { }
    }

    public void removeShards(final Rarity key, final int n) {
        this.shards.put(key, this.shards.get(key) - n);
        final RuneConfig config = FileManager.getConfig("playerdata.yml");
        config.set(this.p.getUniqueId() + ".shards." + key.toString().toLowerCase(), this.shards.get(key));
        try {
            config.save();
        }catch (IOException ex) { }
    }

    public void activate(final Rune rune, final ItemStack itemStack, final boolean b) {
        if (!this.p.hasPermission("runestones.rune.*") && !this.p.hasPermission("runestones.rune." + rune.getRealName().replaceAll(" ", ""))) {
            this.p.sendMessage(RuneStone.Format(RuneStone.msg.getString("NoPermission")));
            return;
        }
        if (RuneStone.isHigherVersion(rune.getVersion(), RuneStone.serverVersion)) {
            this.p.sendMessage(RuneStone.Format(RuneStone.msg.getString("Invalid-Version").replaceAll("%version%", String.valueOf(RuneStone.serverVersion))));
            return;
        }
        if (this.getRunes().size() >= this.getSlots()) {
            this.p.sendMessage(RuneStone.Format(RuneStone.msg.getString("No-More-Slots")));
            return;
        }
        if (!RuneStone.databaseEnabled) {
            rune.activate(this.p);
            this.runes.add(rune);
            final RuneConfig config = FileManager.getConfig("playerdata.yml");
            final ArrayList<String> list = new ArrayList<>();
            final Iterator<Rune> iterator = this.runes.iterator();
            while (iterator.hasNext()) {
                list.add(iterator.next().getRealName());
            }
            assert config != null;
            config.set(this.p.getUniqueId().toString() + ".runes", list);
            try {
                config.save();
            }catch (IOException ex) { }
        }
        this.p.sendMessage(RuneStone.Format(RuneStone.msg.getString("Activate-Messsage").replaceAll("%rune%", rune.getCustomName())));
        if (itemStack.getAmount() != 1) {
            itemStack.setAmount(itemStack.getAmount() - 1);
        }
        else {
            this.p.getInventory().remove(itemStack);
        }
        this.p.updateInventory();
        if (b) {
            RuneGUI.openRuneGUI(this);
        }
    }

    public void deactivate(final Rune rune, final boolean b) {
        if (!b) {
            this.p.getInventory().addItem(rune.asItem());
            this.p.updateInventory();
        }
        if (!RuneStone.databaseEnabled) {
            rune.deactivate(this.p);
            this.runes.remove(rune);
            final RuneConfig config = FileManager.getConfig("playerdata.yml");
            final ArrayList<String> list = new ArrayList<String>();
            final Iterator<Rune> iterator = this.runes.iterator();
            while (iterator.hasNext()) {
                list.add(iterator.next().getRealName());
            }
            config.set(this.p.getUniqueId().toString() + ".runes", list);
            try {
                config.save();
            }catch (IOException ex) { }
            if (!b) {
                this.p.sendMessage(RuneStone.Format(RuneStone.msg.getString("Deactivate-Messsage").replaceAll("%rune%", rune.getCustomName())));
            }
            RuneGUI.openRuneGUI(this);
        }
    }

    public boolean isActive(final Rune rune) {
        return this.runes.contains(rune);
    }

    public void loadData() {
        if (!RuneStone.databaseEnabled) {
            final RuneConfig config = FileManager.getConfig("playerdata.yml");
            final String string = this.p.getUniqueId().toString();
            assert config != null;
            if (this.hasData()) {
                for (String value : config.getStringList(string + ".runes")) {
                    this.runes.add(RuneStone.getRune(value));
                }
                for (final String s : config.getStringList(string + ".runebag")) {
                    this.runeBag.put(RuneStone.getRune(s.substring(0, s.indexOf(":"))), Integer.parseInt(s.substring(s.indexOf(":") + 1)));
                }
                this.shards.put(Rarity.NORMAL, config.getInt(string + ".shards.normal"));
                this.shards.put(Rarity.UNIQUE, config.getInt(string + ".shards.unique"));
                this.shards.put(Rarity.EPIC, config.getInt(string + ".shards.epic"));
                this.shards.put(Rarity.LEGENDARY, config.getInt(string + ".shards.legendary"));
                for (final String s2 : config.getStringList(string + ".forging")) {
                    this.forging.put(RuneStone.getRune(s2.substring(0, s2.indexOf(":"))), Long.parseLong(s2.substring(s2.indexOf(":") + 1)));
                }
            }
            else {
                config.set(string + ".runes", new ArrayList());
                config.set(string + ".runebag", new ArrayList());
                config.set(string + ".shards.normal", 0);
                config.set(string + ".shards.unique", 0);
                config.set(string + ".shards.epic", 0);
                config.set(string + ".shards.legendary", 0);
                config.set(string + ".forging", new ArrayList());
                try {
                    config.save();
                }catch (IOException ex) { }
                this.shards.put(Rarity.NORMAL, 0);
                this.shards.put(Rarity.UNIQUE, 0);
                this.shards.put(Rarity.EPIC, 0);
                this.shards.put(Rarity.LEGENDARY, 0);
            }
        }
    }

    public boolean hasData() {
        return !RuneStone.databaseEnabled && FileManager.getConfig("playerdata.yml").getRealConfig().contains(this.p.getUniqueId().toString());
    }

    public Player getPlayer() {
        return this.p;
    }

    public List<Rune> getRunes() {
        return this.runes;
    }

    public void sendMessage(final String s) {
        this.p.sendMessage(RuneStone.Format(s));
    }

    public int getSlots() {
        int n = 1;
        for (int i = 2; i <= 8; ++i) {
            if (this.p.hasPermission("runestones.slots." + i)) {
                n = i;
            }
        }
        return n;
    }

    public int getForgeSlots() {
        int n = 1;
        for (int i = 2; i <= 3; ++i) {
            if (this.p.hasPermission("runestones.forgeslots." + i)) {
                ++n;
            }
        }
        return n;
    }

    public int getRuneBagLevel() {
        int n = 1;
        for (int i = 2; i <= 3; ++i) {
            if (this.p.hasPermission("runestones.runebag." + i)) {
                ++n;
            }
        }
        return n;
    }

    public void saveRuneBag(final Inventory inventory) {
        this.runeBag.clear();
        final RuneConfig config = FileManager.getConfig("playerdata.yml");
        final ArrayList<String> list = new ArrayList<>();
        ItemStack[] contents;
        for (int length = (contents = inventory.getContents()).length, i = 0; i < length; ++i) {
            final ItemStack itemStack = contents[i];
            if (itemStack != null && itemStack.getType() == Material.EMERALD && itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName()) {
                final Rune rune = RuneStone.getRune(ChatColor.stripColor(itemStack.getItemMeta().getDisplayName().substring(0, itemStack.getItemMeta().getDisplayName().length() - 9)));
                if (rune != null) {
                    if (this.runeBag.containsKey(rune)) {
                        list.remove(rune.getRealName() + ":" + this.runeBag.get(rune));
                        this.runeBag.put(rune, itemStack.getAmount() + this.runeBag.get(rune));
                    }
                    else {
                        this.runeBag.put(rune, itemStack.getAmount());
                    }
                    list.add(rune.getRealName() + ":" + this.runeBag.get(rune));
                }
            }
        }
        config.set(this.p.getUniqueId() + ".runebag", list);
        try {
            config.save();
        }catch (IOException ex) { }
    }

    public void openRuneBag() {
        this.runeBagInv.clear();
        for (final Rune key : this.runeBag.keySet()) {
            for (int i = 0; i < this.runeBag.get(key); ++i) {
                ItemStack item = key.asItem();
                item.addUnsafeEnchantment(Enchantment.LUCK, 1);
                item.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                this.runeBagInv.addItem(item);
            }
        }
        this.p.openInventory(this.runeBagInv);
    }

    public HashMap<Rune, Integer> getRuneBag() {
        return this.runeBag;
    }

    public Inventory getRuneBagInv() {
        return this.runeBagInv;
    }
}
