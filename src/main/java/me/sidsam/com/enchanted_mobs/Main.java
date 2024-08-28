package me.sidsam.com.enchanted_mobs;

import me.sidsam.com.enchanted_mobs.entities.loot.Loot;
import me.sidsam.com.enchanted_mobs.entities.mobs.*;
import me.sidsam.com.enchanted_mobs.events.MobDamageListener;
import me.sidsam.com.enchanted_mobs.events.MobDeathListener;
import me.sidsam.com.enchanted_mobs.events.MobSpawnListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class Main extends JavaPlugin {

    static List<Class<? extends EnchantedMob>> enchantedMobClasses = new ArrayList<>();
    static JavaPlugin instance;

    @Override
    public void onEnable() {
        instance = this;
        // Save the default config file if it doesn't exist
        this.saveDefaultConfig();
        this.reloadConfig();
        initializeMobClasses();
        initializeListeners();
        Loot.loadLootDrops();
    }

    @Override
    public void onDisable() {
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("emob")) return false;
        if (args.length < 1) return false;
        if (args[0].equalsIgnoreCase("reload")) {
            reloadConfig();
            Loot.loadLootDrops();
            sender.sendMessage(ChatColor.GREEN + "Enchanted Mob Loot Drops Reloaded!");
            return true;
        }
        return false;
    }

    public static List<Class<? extends EnchantedMob>> getEnchantedMobClasses() {
        return Main.enchantedMobClasses;
    }

    public static JavaPlugin getPlugin() {
        return Main.instance;
    }

    private void initializeMobClasses() {
        enchantedMobClasses.add(Assassin.class);
        enchantedMobClasses.add(Paladin.class);
        enchantedMobClasses.add(Berserker.class);
        enchantedMobClasses.add(Necromancer.class);
        enchantedMobClasses.add(Pyromancer.class);
        enchantedMobClasses.add(Warlock.class);
    }

    private void initializeListeners() {
        getServer().getPluginManager().registerEvents(new MobSpawnListener(), this);
        getServer().getPluginManager().registerEvents(new MobDamageListener(), this);
        getServer().getPluginManager().registerEvents(new MobDeathListener(), this);
    }
}
