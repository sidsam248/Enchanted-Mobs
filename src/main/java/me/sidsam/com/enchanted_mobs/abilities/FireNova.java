package me.sidsam.com.enchanted_mobs.abilities;

import me.sidsam.com.enchanted_mobs.Main;
import org.bukkit.*;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;

public class FireNova extends Ability {

    private final double radius;

    public FireNova(int level) {
        super("Fire Nova", 15); // 15-second cooldown
        this.radius = 5 * level; // 5 blocks per level
    }

    @Override
    protected void performAbility(LivingEntity caster, LivingEntity target, int level) {
        Location casterLocation = caster.getLocation();
        int count = caster.getWorld().getNearbyEntities(casterLocation, this.radius, this.radius, this.radius).size();

        // Retrieve nearby entities
        caster.getWorld().getNearbyEntities(casterLocation, this.radius, this.radius, this.radius).stream()
                .filter(e -> e instanceof LivingEntity && !e.equals(caster)) // Only target other entities
                .forEach(entity -> {
                    entity.setFireTicks(20 * level); // Set the target on fire for level * 1 seconds
                    ((LivingEntity) entity).damage(2.0 * level, caster); // Apply damage
                });
        caster.getWorld().playSound(casterLocation, Sound.ENTITY_BLAZE_SHOOT, 1.0f, 1.0f);
        spawnFireNovaParticles(casterLocation, this.radius);
    }

    private void spawnFireNovaParticles(Location center, double radius) {
        // Custom particle effect to show a ring of fire expanding outward
        World world = center.getWorld();
        if (world == null) return;

        new BukkitRunnable() {
            private double currentRadius = 0;
            private int steps = 20; // Number of rings to create

            @Override
            public void run() {
                if (currentRadius > radius) {
                    this.cancel(); // Stop when the maximum radius is reached
                    return;
                }

                for (int i = 0; i < 360; i += 10) { // Create a ring by spawning particles in a circle
                    double radians = Math.toRadians(i);
                    double x = center.getX() + currentRadius * Math.cos(radians);
                    double z = center.getZ() + currentRadius * Math.sin(radians);
                    world.spawnParticle(Particle.FLAME, new Location(world, x, center.getY(), z), 1, 0, 0, 0, 0);
                }

                currentRadius += (radius / steps); // Increase the radius for the next step
            }
        }.runTaskTimer(Main.getPlugin(), 0L, 1L);
    }
}

