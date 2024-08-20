package me.sidsam.com.enchanted_mobs.entities;

import me.sidsam.com.enchanted_mobs.abilities.Ability;
import me.sidsam.com.enchanted_mobs.abilities.Bloodlust;
import me.sidsam.com.enchanted_mobs.abilities.Frenzy;
import me.sidsam.com.enchanted_mobs.abilities.GroundSmash;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;

import java.util.List;

public class Berserker extends EnchantedMob {

    public Berserker() {
        super("Berserker");
    }

    @Override
    public void loop() {
        double topRingRadius = 1.5; // Radius of the top ring
        double middleRingRadius = 1.0; // Radius of the middle ring
        double topRingHeight = 1.2; // Height above the mob's location for the top ring
        double middleRingHeight = 0.6; // Height around the mob's location for the middle ring

        // Spawn particles in the top ring (DUST)
        spawnRingParticles(Particle.DUST, topRingRadius, topRingHeight, 30, Color.RED);

        // Spawn particles in the middle ring (DAMAGE_INDICATOR)
        spawnRingParticles(Particle.DAMAGE_INDICATOR, middleRingRadius, middleRingHeight, 15, null);
    }

    private void spawnRingParticles(Particle particleType, double radius, double height, int count, Color color) {
        Location center = entity.getLocation();
        World world = center.getWorld();
        if (world == null) return;

        Particle.DustOptions dustOptions = color != null ? new Particle.DustOptions(color, 1) : null;

        // Top ring
        spawnRing(world, center.clone().add(0, height, 0), radius, count, particleType, dustOptions);

        // Middle ring
        spawnRing(world, center.clone(), radius, count, particleType, dustOptions);
    }

    private void spawnRing(World world, Location center, double radius, int count, Particle particleType, Particle.DustOptions dustOptions) {
        for (int i = 0; i < 360; i += 360 / count) { // Divide the circle into segments
            double radians = Math.toRadians(i);
            double x = radius * Math.cos(radians);
            double z = radius * Math.sin(radians);

            Location particleLocation = center.clone().add(x, 0, z);
            if (dustOptions != null) {
                world.spawnParticle(particleType, particleLocation, 1, 0, 0, 0, 0, dustOptions);
            } else {
                world.spawnParticle(particleType, particleLocation, 1, 0, 0, 0, 0);
            }
        }
    }

    @Override
    protected List<Ability> createAbilities() {
        return List.of(new Bloodlust(this.level), new Frenzy(this.level), new GroundSmash(this.level));
    }
}
