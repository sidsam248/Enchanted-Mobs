package me.sidsam.com.enchanted_mobs.abilities;

import me.sidsam.com.enchanted_mobs.Main;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public class SoulHarvest extends Ability {
    private int healingOrbs = 0;

    public SoulHarvest() {
        super("Soul Harvest", 30); // 30-second cooldown
    }

    @Override
    public void performAbility(LivingEntity caster, LivingEntity target, int level) {
        // Calculate damage and create an orb
        double damage = level * 2.0;
        ((Player) target).damage(damage);
        healingOrbs++;

        // Start spawning orbs if not already started
        if (healingOrbs > 0) {
            Bukkit.getLogger().info("We have Soulharvest! " + healingOrbs);
            startOrbSpawning(caster, level);
        }
    }

    private void startOrbSpawning(LivingEntity caster, int level) {
        World world = caster.getWorld();

        new BukkitRunnable() {
            @Override
            public void run() {
                if (caster.isDead() || healingOrbs <= 0) {
                    cancel();
                    return;
                }

                // Heal caster if health is below 25%
                if (caster.getHealth() <= Objects.requireNonNull(caster.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue() * 0.25) {
                    double healAmount = 2.0 * level;
                    caster.setHealth(Math.min(caster.getHealth() + healAmount, Objects.requireNonNull(caster.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue()));
                    healingOrbs -= 1;
                    if (healingOrbs <= 0) {
                        cancel();
                        return;
                    }
                }

                Location casterLocation = caster.getLocation();

                Bukkit.getLogger().info("We render Soulharvest! " + healingOrbs);

                // Spawn a smaller thick blob for each orb
                for (int i = 0; i < healingOrbs; i++) {
                    double angle = (360.0 / healingOrbs) * i;
                    double radius = 0.6; // Reduced radius for spacing
                    double x = Math.cos(Math.toRadians(angle)) * radius;
                    double z = Math.sin(Math.toRadians(angle)) * radius;

                    Location orbLocation = casterLocation.clone().add(x, 3.5, z);

                    // Create a smaller, denser blob
                    for (int j = 0; j < 15; j++) { // Reduced particle count for smaller size
                        double offsetX = (Math.random() - 0.5) * 0.1; // Reduced offset for smaller size
                        double offsetY = (Math.random() - 0.5) * 0.1;
                        double offsetZ = (Math.random() - 0.5) * 0.1;
                        Location particleLoc = orbLocation.clone().add(offsetX, offsetY, offsetZ);
                        world.spawnParticle(Particle.ENCHANT, particleLoc, 0, 0, 1, 0, 1); // Green color
                    }

                    // Add a subtle glowing outline
                    world.spawnParticle(Particle.END_ROD, orbLocation, 5, 0.05, 0.05, 0.05, 0.01);
                }
            }
        }.runTaskTimer(Main.getPlugin(), 0L, 1L); // Run every tick for smooth animation
    }
}