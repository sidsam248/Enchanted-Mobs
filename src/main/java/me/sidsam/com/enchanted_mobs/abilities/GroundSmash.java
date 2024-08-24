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
        super("Ground Smash", 20); // 20-second cooldown
        this.force = 0.5 * level;
        this.radius = 5.0 * level;
    }

    @Override
    protected void performAbility(LivingEntity caster, LivingEntity target, int level) {
        Location casterLocation = caster.getLocation();
        caster.getWorld().getNearbyEntities(casterLocation, this.radius, this.radius, this.radius).stream()
                .filter(e -> e instanceof LivingEntity && !e.equals(caster)) // Only target other entities
                .forEach(entity -> {
                    // Apply knockback
                    Vector direction = entity.getLocation().toVector().subtract(casterLocation.toVector()).normalize();
                    entity.setVelocity(direction.multiply(force));
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

                    // Optional: Play sound at the edge of the shockwave
                    caster.getWorld().playSound(particleLocation, Sound.ENTITY_IRON_GOLEM_ATTACK, 1.0f, 1.0f);
                }

                currentRadius += 1; // Expand the shockwave outward
            }
        }.runTaskTimer(Main.getPlugin(), 0L, 5L);
    }
}

