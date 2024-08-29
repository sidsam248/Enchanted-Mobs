package me.sidsam.com.enchanted_mobs.events;

import me.sidsam.com.enchanted_mobs.abilities.DivineShield;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class MobDamageListener implements Listener {
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event) {
        Entity livingEntity = event.getEntity();

        if (!(livingEntity instanceof Monster)) return;
        if (!livingEntity.hasMetadata("isEnchanted") || !livingEntity.hasMetadata("enchantedLevel")) return;
        if (DivineShield.handleDamage((LivingEntity) livingEntity, event.getDamage())) {
            event.setCancelled(true);
        }
    }
}
