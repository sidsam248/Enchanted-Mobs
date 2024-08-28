package me.sidsam.com.enchanted_mobs.entities.mobs;

import me.sidsam.com.enchanted_mobs.abilities.Ability;
import me.sidsam.com.enchanted_mobs.abilities.BlazeShield;
import me.sidsam.com.enchanted_mobs.abilities.FireNova;
import me.sidsam.com.enchanted_mobs.abilities.FlameBurst;
import org.bukkit.Location;
import org.bukkit.Particle;

import java.util.List;

public class Pyromancer extends EnchantedMob {

    public Pyromancer() {
        super("Pyromancer");
    }

    @Override
    public void loop() {
        Location center = this.entity.getLocation().add(0, 2.5, 0); // Adjust the Y value to place the halo above the mob’s head
        double radius = 0.5; // Radius of the halo
        int points = 20; // Number of particles in the halo

        for (int i = 0; i < points; i++) {
            double angle = 2 * Math.PI * i / points; // Calculate the angle for each particle
            double x = Math.cos(angle) * radius;
            double z = Math.sin(angle) * radius;

            Location particleLocation = center.clone().add(x, 0, z);
            this.entity.getWorld().spawnParticle(Particle.LARGE_SMOKE, particleLocation, 1, 0, 0, 0, 0); // Spawn the particle
        }
    }

    @Override
    protected List<Ability> createAbilities() {
        return List.of(new FireNova(level), new FlameBurst(level), new BlazeShield(level));
    }
}