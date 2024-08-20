package me.sidsam.com.enchanted_mobs.entities;

import me.sidsam.com.enchanted_mobs.abilities.Ability;
import me.sidsam.com.enchanted_mobs.abilities.DarkBolt;
import me.sidsam.com.enchanted_mobs.abilities.LifeDrain;
import me.sidsam.com.enchanted_mobs.abilities.SummonMinions;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;

import java.util.List;

public class Warlock extends EnchantedMob {

    public Warlock() {
        super("Warlock");
    }

    @Override
    public void loop() {
        // Define radii and heights for the top and middle rings
        double topRingRadius = 1.5; // Radius of the top ring
        double middleRingRadius = 1.0; // Radius of the middle ring
        double topRingHeight = 1.2; // Height above the mob's location for the top ring
        double middleRingHeight = 0.6; // Height around the mob's location for the middle ring

        spawnRingParticles(Particle.WITCH, topRingRadius, topRingHeight, 30);
        spawnRingParticles(Particle.PORTAL, middleRingRadius, middleRingHeight, 15);
    }

    private void spawnRingParticles(Particle particleType, double radius, double height, int count) {
        Location center = entity.getLocation();
        World world = center.getWorld();
        if (world == null) return;

        // Top ring
        spawnRing(world, center.clone().add(0, height, 0), radius, count, particleType);

        // Middle ring
        spawnRing(world, center.clone(), radius, count, particleType);
    }

    private void spawnRing(World world, Location center, double radius, int count, Particle particleType) {
        for (int i = 0; i < 360; i += 360 / count) { // Divide the circle into segments
            double radians = Math.toRadians(i);
            double x = radius * Math.cos(radians);
            double z = radius * Math.sin(radians);

            Location particleLocation = center.clone().add(x, 0, z);
            world.spawnParticle(particleType, particleLocation, 1, 0, 0, 0, 0);
        }
    }


    @Override
    protected List<Ability> createAbilities() {
        return List.of(new DarkBolt(), new LifeDrain(), new SummonMinions());
    }
}
