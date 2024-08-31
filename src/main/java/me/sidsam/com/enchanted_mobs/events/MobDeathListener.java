package me.sidsam.com.enchanted_mobs.events;

import me.sidsam.com.enchanted_mobs.entities.loot.Loot;
import me.sidsam.com.enchanted_mobs.entities.loot.LootEnchantment;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Random;

public class MobDeathListener implements Listener {

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        Entity entity = event.getEntity();

        if (!(entity instanceof Monster)) return;
        if (!entity.hasMetadata("isEnchanted") || !entity.hasMetadata("enchantedLevel")) return;

        int level = entity.getMetadata("enchantedLevel").getFirst().asInt();

        Bukkit.getLogger().info("Enchanted mob has died");

        // Filter items by level range
        List<Loot> validItems = Loot.lootDrops.stream()
                .filter(item -> level >= item.getMinLevel() && level <= item.getMaxLevel())
                .toList();

        if (validItems.isEmpty()) return;

        // Select a random item
        Loot randomItem = validItems.get(new Random().nextInt(validItems.size()));

        // Create the item stack
        ItemStack itemStack = new ItemStack(randomItem.getMaterial(), randomItem.getQuantity());
        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(randomItem.getName());
            meta.setLore(randomItem.getLore());
            for (LootEnchantment enchantment : randomItem.getEnchantments()) {
                meta.addEnchant(enchantment.enchantment(), enchantment.level(), true);
            }
            itemStack.setItemMeta(meta);
        }

        // Drop the item
        entity.getWorld().dropItemNaturally(entity.getLocation(), itemStack);
    }
}
