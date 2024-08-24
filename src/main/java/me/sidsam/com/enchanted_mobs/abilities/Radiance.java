package me.sidsam.com.enchanted_mobs.abilities;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;
import me.sidsam.com.enchanted_mobs.Main;

import java.util.Objects;

public class Radiance extends Ability {

    private boolean started = false;

    public Radiance() {
        super("Radiance", 0, true);
    }

    @Override
    protected void performAbility(LivingEntity caster, LivingEntity target, int level) {
        if (started) return;
        startPassiveEffect(caster, target, level);
        started = true;
    }

    public void startPassiveEffect(LivingEntity caster, LivingEntity target, int level) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (caster.isDead()) {
                    this.cancel();
                    return;
                }

                healNearbyAllies(caster, target, level);
            }
        }.runTaskTimer(Main.getPlugin(), 0L, 20L * 5); // Run every 5 seconds
    }

    private void healNearbyAllies(LivingEntity caster, LivingEntity target, int level) {
        double healAmount = 1 + (level * 0.5); // Base heal 1, increases by 0.5 per level
        double radius = 5 + (level * 0.5); // Base radius 5, increases by 0.5 per level

        for (Entity entity : caster.getNearbyEntities(radius, radius, radius)) {
            if (entity instanceof LivingEntity livingEntity && !livingEntity.equals(caster) && !livingEntity.equals(target)) {
                healEntity(livingEntity, healAmount);
            }
        }

        // Heal the caster and create splash effect
        healEntity(caster, healAmount);
        createHealingSplashEffect(caster.getLocation());
    }

    private void healEntity(LivingEntity entity, double healAmount) {
        double maxHealth = Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue();
        entity.setHealth(Math.min(entity.getHealth() + healAmount, maxHealth));
    }

    private void createHealingSplashEffect(Location location) {
        // Splash effect
        Objects.requireNonNull(location.getWorld()).spawnParticle(
                Particle.ENTITY_EFFECT,
                location.add(0, 1, 0), // Adjust height to be around the entity's center
                50, // Number of particles
                0.5, 0.5, 0.5, // Spread
                Color.FUCHSIA // Pink color for healing
        );

        // Additional particles for a more potion-like effect
        location.getWorld().spawnParticle(
                Particle.INSTANT_EFFECT,
                location,
                20,
                0.5, 0.5, 0.5,
                0.1
        );
    }
}