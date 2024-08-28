package me.sidsam.com.enchanted_mobs.entities.loot;

import me.sidsam.com.enchanted_mobs.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Loot {
    private final String name;
    private final Material material;
    private final int quantity;
    private final List<String> lore;
    private final List<LootEnchantment> enchantments;
    private final int minLevel;
    private final int maxLevel;

    public static List<Loot> lootDrops = new ArrayList<>();

    public Loot(String name, Material material, int quantity, List<String> lore, List<LootEnchantment> enchantments, int minLevel, int maxLevel) {
        this.name = name;
        this.material = material;
        this.quantity = quantity;
        this.lore = lore;
        this.enchantments = enchantments;
        this.minLevel = minLevel;
        this.maxLevel = maxLevel;
    }

    public String getName() {
        return this.name;
    }

    public Material getMaterial() {
        return this.material;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public List<String> getLore() {
        return this.lore;
    }

    public List<LootEnchantment> getEnchantments() {
        return this.enchantments;
    }

    public int getMinLevel() {
        return this.minLevel;
    }

    public int getMaxLevel() {
        return this.maxLevel;
    }

    public static void loadLootDrops() {
        List<Map<String, Object>> lootDropsList = (List<Map<String, Object>>) Main.getPlugin().getConfig().getList("lootDrops");

        if (lootDropsList != null) {
            Bukkit.getLogger().info("[Enchanted Mob] Starting loading of drops");
            for (Map<String, Object> itemMap : lootDropsList) {
                String name = (String) itemMap.get("name");
                Bukkit.getLogger().info("[Enchanted Mob] Loaded loot drop: " + name);

                Material material = Material.valueOf((String) itemMap.get("material"));
                int quantity = (int) itemMap.get("quantity");
                List<String> lore = (List<String>) itemMap.get("lore");

                List<LootEnchantment> enchantments = new ArrayList<>();
                List<Map<String, Object>> enchantmentsList = (List<Map<String, Object>>) itemMap.get("enchantments");
                if (enchantmentsList != null) {
                    for (Map<String, Object> enchantmentMap : enchantmentsList) {
                        Enchantment enchantment = Enchantment.getByName((String) enchantmentMap.get("type"));
                        int level = (int) enchantmentMap.get("level");
                        enchantments.add(new LootEnchantment(enchantment, level));
                    }
                }

                int minLevel = (int) itemMap.get("minLevel");
                int maxLevel = (int) itemMap.get("maxLevel");

                lootDrops.add(new Loot(name, material, quantity, lore, enchantments, minLevel, maxLevel));
            }
        } else {
            Bukkit.getLogger().warning("LootDrops list is missing or null.");
        }
    }
}