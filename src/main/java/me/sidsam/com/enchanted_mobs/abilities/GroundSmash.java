package me.sidsam.com.enchanted_mobs.abilities;

import me.sidsam.com.enchanted_mobs.Main;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Objects;

public class GroundSmash extends Ability {

    private final double force;
    private final double radius;

    public GroundSmash(int level) {
        super("Ground Smash", 30); // 30-second cooldown
        this.force = 0.5 * level;
        this.radius = 5.0 * level;
    }

    @Override
    protected void performAbility(LivingEntity caster, LivingEntity target, int level) {
        Location casterLocation = caster.getLocation();
        caster.getWorld().getNearbyEntities(casterLocation, this.radius, this.radius, this.radius).stream()
                .filter(e -> e instanceof LivingEntity && !e.equals(caster)) // Only target other entities
                .forEach(entity -> {
                    // Calculate direction
                    Vector direction = entity.getLocation().toVector().subtract(casterLocation.toVector());

                    // Check if the direction vector is not zero
                    if (direction.lengthSquared() > 0) {
                        direction.normalize();
                    } else {
                        // If entities are at the same location, push slightly upwards
                        direction = new Vector(0, 1, 0);
                    }

                    // Apply knockback
                    Vector knockback = direction.multiply(force);

                    // Ensure the knockback vector has finite components
                    knockback.setX(Double.isFinite(knockback.getX()) ? knockback.getX() : 0);
                    knockback.setY(Double.isFinite(knockback.getY()) ? knockback.getY() : 0.5); // Default upward if NaN
                    knockback.setZ(Double.isFinite(knockback.getZ()) ? knockback.getZ() : 0);

                    entity.setVelocity(knockback);
                });

        // Spawn shockwave particles
        spawnShockwaveParticles(caster, this.radius);
    }


    private void spawnShockwaveParticles(LivingEntity caster, double radius) {
        Location start = caster.getLocation();
        int numberOfRays = 36; // Number of lines/rays emanating from the caster

        new BukkitRunnable() {
            double currentRadius = 0;

            @Override
            public void run() {
                if (currentRadius >= radius / 2) {
                    this.cancel(); // Stop once the shockwave reaches the maximum radius
                    return;
                }

                for (int i = 0; i < numberOfRays; i++) {
                    double angle = Math.toRadians((360.0 / numberOfRays) * i);
                    double x = Math.cos(angle) * currentRadius;
                    double z = Math.sin(angle) * currentRadius;

                    Location particleLocation = start.clone().add(x, 0, z);
                    Particle.DustOptions dustOptions = new Particle.DustOptions(Color.WHITE, 1);
                    caster.getWorld().spawnParticle(Particle.DUST, particleLocation, 5, 0.2, 0.2, 0.2, 0.05, dustOptions);

                    caster.getWorld().playSound(particleLocation, Sound.ENTITY_IRON_GOLEM_ATTACK, 1.0f, 1.0f);
                }

                currentRadius += 1; // Expand the shockwave outward
            }
        }.runTaskTimer(Main.getPlugin(), 0L, 5L);
    }
}

