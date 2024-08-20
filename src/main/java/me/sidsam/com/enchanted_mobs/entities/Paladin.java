package me.sidsam.com.enchanted_mobs.entities;

import me.sidsam.com.enchanted_mobs.abilities.Ability;
import org.bukkit.entity.LivingEntity;
import org.bukkit.Particle;

import java.util.List;

public class Paladin extends EnchantedMob {

    public Paladin() {
        super("Paladin");
    }

    @Override
    public void loop() {
        // Spawn golden light and white sparkles
        spawnParticles(Particle.TOTEM_OF_UNDYING, 30, 0.5, 1.5, 0.5);
        spawnParticles(Particle.END_ROD, 20, 0.5, 1.5, 0.5);
    }

    @Override
    protected List<Ability> createAbilities() {
        return List.of();
    }
}