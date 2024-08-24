package me.sidsam.com.enchanted_mobs;

import me.sidsam.com.enchanted_mobs.entities.*;
import me.sidsam.com.enchanted_mobs.events.MobDamageListener;
import me.sidsam.com.enchanted_mobs.events.MobSpawnListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class Main extends JavaPlugin {

    static List<Class<? extends EnchantedMob>> enchantedMobClasses = new ArrayList<>();
    static JavaPlugin instance;

    @Override
    public void onEnable() {
        instance = this;
        initializeMobClasses();
        initializeListeners();
    }

    @Override
    public void onDisable() {
    }

    private void initializeMobClasses() {
        // Yet to finish
//        enchantedMobClasses.add(Assassin.class);
        enchantedMobClasses.add(Paladin.class);

        // Finished
//        enchantedMobClasses.add(Berserker.class);
//        enchantedMobClasses.add(Necromancer.class);
//        enchantedMobClasses.add(Pyromancer.class);
//        enchantedMobClasses.add(Warlock.class);
    }

    private void initializeListeners() {
        getServer().getPluginManager().registerEvents(new MobSpawnListener(), this);
        getServer().getPluginManager().registerEvents(new MobDamageListener(), this);
    }

    public static List<Class<? extends EnchantedMob>> getEnchantedMobClasses() {
        return Main.enchantedMobClasses;
    }

    public static JavaPlugin getPlugin() {
        return Main.instance;
    }
}
