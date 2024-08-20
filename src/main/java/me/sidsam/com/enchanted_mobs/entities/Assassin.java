package me.sidsam.com.enchanted_mobs.entities;

import me.sidsam.com.enchanted_mobs.abilities.Ability;
import org.bukkit.entity.LivingEntity;
import org.bukkit.Particle;

import java.util.List;

public class Assassin extends EnchantedMob {

    public Assassin() {
        super("Assassin");
    }

    @Override
    public void loop() {
        // Spawn black smoke and dark gray wisps
        spawnParticles(Particle.SMOKE, 30, 0.5, 1.5, 0.5);
        spawnParticles(Particle.SQUID_INK, 20, 0.5, 1.5, 0.5);
    }

    @Override
    protected List<Ability> createAbilities() {
        return List.of();
    }
}