package me.sidsam.com.enchanted_mobs.abilities;

import me.sidsam.com.enchanted_mobs.Main;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

public class BlazeShield extends Ability {

    private final double radius;
    private final double damage;
    private final double knockback;
    private final long duration;

    public BlazeShield(int level) {
        super("Blaze Shield", 30); // 30-second cooldown
        this.radius = 1.0 * level; // Radius of the shield
        this.damage = 4.0 * level; // Damage per tick
        this.knockback = 0.5 * level; // Knockback strength
        this.duration = 20L * 5 * level; // Duration of the shield in ticks (5 seconds per level)
    }

    @Override
    protected void performAbility(LivingEntity caster, LivingEntity target, int level) {
        Location casterLocation = caster.getLocation();
        World world = caster.getWorld();

        // Start the shield effect
        BukkitTask runnable = new BukkitRunnable() {
            @Override
            public void run() {
                if (caster.isDead()) {
                    cancel(); // Stop if the caster is dead
                    return;
                }

                // Check for nearby entities
                caster.getWorld().getNearbyEntities(casterLocation, radius, radius, radius).stream()
                        .filter(e -> e instanceof Player && !e.equals(caster)) // Only players and not the caster
                        .forEach(entity -> {
                            Location entityLocation = entity.getLocation();
                            if (casterLocation.distance(entityLocation) <= radius) {
                                // Apply damage and knockback
                                ((Player) entity).damage(damage, caster);
                                Vector direction = entityLocation.toVector().subtract(casterLocation.toVector()).normalize();
                                entity.setVelocity(direction.multiply(knockback));

                                // Optional: Play sound or create visual effect for Blaze Shield
                                world.playSound(entityLocation, Sound.ENTITY_BLAZE_AMBIENT, 1.0f, 1.0f);
                            }
                        });

                // Create a particle effect that follows the caster
                spawnBlazeShieldParticles(caster);
            }
        }.runTaskTimer(Main.getPlugin(), 0L, 20L); // Run every second

        // Stop the shield effect after the duration
        new BukkitRunnable() {
            @Override
            public void run() {
                // Code to stop the shield effect
                runnable.cancel(); // Stop the task
            }
        }.runTaskLater(Main.getPlugin(), this.duration); // Schedule to stop after the duration
    }

    private void spawnBlazeShieldParticles(LivingEntity caster) {
        Location casterLocation = caster.getLocation();
        World world = caster.getWorld();

        for (int i = 0; i < 360; i += 10) {
            double radians = Math.toRadians(i);
            double x = casterLocation.getX() + radius * Math.cos(radians);
            double z = casterLocation.getZ() + radius * Math.sin(radians);
            Location particleLocation = new Location(world, x, casterLocation.getY() + 1, z);

            world.spawnParticle(Particle.FLAME, particleLocation, 5, 0, 0, 0, 0.1);
        }
    }
}

