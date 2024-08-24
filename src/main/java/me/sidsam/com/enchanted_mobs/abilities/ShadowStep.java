package me.sidsam.com.enchanted_mobs.abilities;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

public class ShadowStep extends Ability {

    public ShadowStep() {
        super("Shadow Step", 10, false); // 10 seconds cooldown
    }

    @Override
    protected void performAbility(LivingEntity caster, LivingEntity target, int level) {
        if (target == null) return;

        Location targetLocation = target.getLocation();
        Vector direction = targetLocation.getDirection().multiply(-1);
        Location teleportLocation = targetLocation.add(direction.multiply(2));

        // Particle effect at the starting position
        caster.getWorld().spawnParticle(Particle.SMOKE, caster.getLocation(), 50, 0.5, 1, 0.5, 0.05);

        // Teleport
        caster.teleport(teleportLocation);

        // Particle effect at the ending position
        for (int i = 0; i < 20; i++) {
            Vector offset = new Vector(
                    Math.random() - 0.5,
                    Math.random() - 0.5,
                    Math.random() - 0.5
            ).normalize().multiply(0.5);
            caster.getWorld().spawnParticle(Particle.DUST,
                    teleportLocation.add(offset),
                    1, 0, 0, 0, 0,
                    new Particle.DustOptions(Color.BLACK, 1));
        }
    }
}