package me.sidsam.com.enchanted_mobs.entities;

import me.sidsam.com.enchanted_mobs.abilities.Ability;
import me.sidsam.com.enchanted_mobs.abilities.DivineShield;
import me.sidsam.com.enchanted_mobs.abilities.HolySmite;
import me.sidsam.com.enchanted_mobs.abilities.Radiance;
import org.bukkit.Color;
import org.bukkit.entity.LivingEntity;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

import java.util.List;

public class Paladin extends EnchantedMob {

    private long tick = 0;

    public Paladin() {
        super("Paladin");
    }

    @Override
    public void loop() {
        tick++;

        // Constant holy aura
        createHolyAura();

        // Rotating halo
        createHalo();

        // Subtle body glow
        createBodyGlow();
    }

    private void createHolyAura() {
        double radius = 1.0;
        int particles = 30;
        for (int i = 0; i < particles; i++) {
            double angle = 2 * Math.PI * i / particles;
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            double y = Math.sin(tick * 0.05 + angle * 2) * 0.1; // Subtle wavy effect
            spawnParticles(Particle.DUST, 1, x, y + 1, z, Color.fromRGB(255, 255, 200)); // Soft yellow
        }
    }

    private void createHalo() {
        double radius = 0.4;
        int particles = 20;
        double yOffset = 2.0; // Position above head
        double rotation = tick * 0.05; // Slow rotation
        for (int i = 0; i < particles; i++) {
            double angle = 2 * Math.PI * i / particles + rotation;
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            spawnParticles(Particle.DUST, 1, x, yOffset, z, Color.WHITE);
        }
    }

    private void createBodyGlow() {
        double height = 2.0;
        int particles = 5;
        for (int i = 0; i < particles; i++) {
            double y = Math.random() * height;
            double angle = Math.random() * 2 * Math.PI;
            double radius = 0.6; // Slightly larger than mob's body
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            spawnParticles(Particle.DUST, 1, x, y, z, Color.YELLOW); // Very soft yellow
        }
    }

    @Override
    protected List<Ability> createAbilities() {
        return List.of(new HolySmite(), new DivineShield(), new Radiance());
    }
}