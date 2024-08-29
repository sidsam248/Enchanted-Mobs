package me.sidsam.com.enchanted_mobs.abilities;

import me.sidsam.com.enchanted_mobs.Main;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public class DeathMark extends Ability {
    private final int duration;

    public DeathMark(int level) {
        super("Death mark", 45);
        this.duration = 20 * 2 * level;
    }

    @Override
    protected void performAbility(LivingEntity caster, LivingEntity target, int level) {
        if (target == null) return;

        // Apply the death mark effect
        applyDeathMark(caster, target, level);

        // Visual and sound effects
        World world = target.getWorld();
        Location location = target.getLocation();
        world.playSound(location, Sound.ENTITY_ELDER_GUARDIAN_CURSE, 1.0f, 1.5f);

        // Start the dark stacked rings particle effect
        startDarkRingEffect(caster, target, level);
    }

    private void applyDeathMark(LivingEntity caster, LivingEntity target, int level) {

        new BukkitRunnable() {
            int ticksPassed = 0;
            @Override
            public void run() {
                if (target == null || target.isDead() || caster.isDead() || ticksPassed > duration) {
                    cancel(); // Stop the effect if the duration ends or the target dies
                    return;
                }
                target.damage(0.5 * level, caster);
                ticksPassed += 20;
            }
        }.runTaskTimer(Main.getPlugin(), 0L, 20L);
    }

    private void startDarkRingEffect(LivingEntity caster, LivingEntity target, int level) {
        new BukkitRunnable() {
            int ticksPassed = 0;

            @Override
            public void run() {
                if (target == null || target.isDead() || caster.isDead() || ticksPassed > duration) {
                    cancel(); // Stop the effect if the duration ends or the target dies
                    return;
                }

                Location targetLocation = target.getLocation();

                // Stack multiple rings on top of each other based on the level
                double ringSpacing = 0.4; // Vertical distance between stacked rings
                int points = 20; // Number of particles in each ring
                for (int ring = 0; ring < level; ring++) {
                    double yOffset = ring * ringSpacing;
                    for (int i = 0; i < points; i++) {
                        double angle = 2 * Math.PI * i / points;
                        double x = 1.5 * Math.cos(angle); // Fixed radius for all rings
                        double z = 1.5 * Math.sin(angle);
                        Location particleLocation = targetLocation.clone().add(x, yOffset, z);
                        target.getWorld().spawnParticle(Particle.SMOKE, particleLocation, 1, 0, 0, 0, 0);
                    }
                }

                ticksPassed += 2;
            }
        }.runTaskTimer(Main.getPlugin(), 0L, 2L); // Runs every 2 ticks for smooth movement
    }
}
