package me.sidsam.com.enchanted_mobs.abilities;

import me.sidsam.com.enchanted_mobs.Main;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DivineShield extends Ability {
    private static final Map<UUID, DivineShieldInfo> activeShields = new HashMap<>();

    public DivineShield() {
        super("Divine Shield", 60);
    }

    @Override
    protected void performAbility(LivingEntity caster, LivingEntity target, int level) {
        if (caster == null || caster.isDead()) {
            Main.getPlugin().getLogger().warning("Attempted to activate Divine Shield on null or dead entity");
            return;
        }

        UUID casterId = caster.getUniqueId();
        int duration = 5 + level; // 5 seconds base, increases by 1 second per level

        DivineShieldInfo shieldInfo = new DivineShieldInfo(caster, level);
        activeShields.put(casterId, shieldInfo);

        // Start particle effect
        startShieldParticles(shieldInfo);

        // Deactivate shield after duration
        Main.getPlugin().getServer().getScheduler().runTaskLater(Main.getPlugin(),
                () -> deactivateShield(casterId), duration * 20L);
    }

    private void startShieldParticles(DivineShieldInfo shieldInfo) {
        shieldInfo.particleTask = Main.getPlugin().getServer().getScheduler().runTaskTimer(Main.getPlugin(), () -> {
            LivingEntity entity = shieldInfo.entity;
            if (entity == null || entity.isDead()) {
                assert entity != null;
                deactivateShield(entity.getUniqueId());
                return;
            }

            double radius = 1.5;
            int particles = 40;
            for (int i = 0; i < particles; i++) {
                double angle = 2 * Math.PI * i / particles;
                for (double y = 0; y <= 2; y += 0.5) {
                    double x = radius * Math.cos(angle);
                    double z = radius * Math.sin(angle);
                    entity.getWorld().spawnParticle(
                            Particle.DUST,
                            entity.getLocation().add(x, y, z),
                            1,
                            new Particle.DustOptions(Color.YELLOW, 1)
                    );
                }
            }
        }, 0L, 5L); // Run every 5 ticks (1/4 second)
    }

    private void deactivateShield(UUID entityId) {
        DivineShieldInfo shieldInfo = activeShields.remove(entityId);
        if (shieldInfo != null && shieldInfo.particleTask != null) {
            shieldInfo.particleTask.cancel();
        }
    }

    public static boolean handleDamage(LivingEntity entity, double damage) {
        DivineShieldInfo shieldInfo = activeShields.get(entity.getUniqueId());
        if (shieldInfo != null) {
            // Reflect damage
            double reflectedDamage = damage * (0.2 + (shieldInfo.level * 0.05)); // 20% base, increases by 5% per level
            if (entity.getLastDamageCause() instanceof org.bukkit.event.entity.EntityDamageByEntityEvent damageEvent) {
                if (damageEvent.getDamager() instanceof LivingEntity) {
                    ((LivingEntity) damageEvent.getDamager()).damage(reflectedDamage, entity);
                }
            }
            spawnImpactParticles(entity.getLocation());
            return true; // Damage should be cancelled
        }
        return false; // Damage should proceed normally
    }

    private static void spawnImpactParticles(org.bukkit.Location location) {
        if (location != null && location.getWorld() != null) {
            location.getWorld().spawnParticle(
                    Particle.FLASH,
                    location,
                    10,
                    0.5, 0.5, 0.5,
                    0
            );
        }
    }

    private static class DivineShieldInfo {
        final LivingEntity entity;
        final int level;
        BukkitTask particleTask;

        DivineShieldInfo(LivingEntity entity, int level) {
            this.entity = entity;
            this.level = level;
        }
    }
}