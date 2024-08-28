package me.sidsam.com.enchanted_mobs.events;

import me.sidsam.com.enchanted_mobs.Main;
import me.sidsam.com.enchanted_mobs.entities.mobs.EnchantedMob;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class MobSpawnListener implements Listener {

    @EventHandler
    public void onMobSpawn(CreatureSpawnEvent event) {
        try {
            LivingEntity entity = event.getEntity();

            if (!(entity instanceof Monster)) return;

            if (entity.hasMetadata("isMinion")) {
                return;
            }
            // Add a 10% chance to enchant the mob
            if (Math.random() > 0.1) return;

            int randomMobClassIndex = (int) (Math.random() * Main.getEnchantedMobClasses().size()); // Random mob class from list
            int level = (int) (Math.random() * 5) + 1; // Random level from 1-5

            // Create a new instance of the selected enchanted mob class
            EnchantedMob enchantedMob;
            try {
                Class<? extends EnchantedMob> mobClass = Main.getEnchantedMobClasses().get(randomMobClassIndex);
                enchantedMob = mobClass.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                Bukkit.getLogger().warning(e.getMessage());
                return;
            }

            entity.setMetadata("isEnchanted", new FixedMetadataValue(Main.getPlugin(), true));
            entity.setMetadata("enchantedLevel", new FixedMetadataValue(Main.getPlugin(), level));
            enchantedMob.setEntity(entity, level);
            enchantedMob.startLogic();

        } catch (Exception e) {
            Bukkit.getLogger().warning(e.getMessage());
        }
    }
}