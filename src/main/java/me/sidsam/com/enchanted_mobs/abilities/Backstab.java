package me.sidsam.com.enchanted_mobs.abilities;

import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class Backstab extends Ability {

    public Backstab() {
        super("Backstab", 15, false); // 15 seconds cooldown
    }

    @Override
    protected void performAbility(LivingEntity caster, LivingEntity target, int level) {
        if (target == null) return;

        Vector targetDirection = target.getLocation().getDirection();
        Vector toTarget = target.getLocation().subtract(caster.getLocation()).toVector().normalize();

        if (targetDirection.dot(toTarget) > 0.5) { // Check if caster is behind target
            // Deal extra damage
            double damage = 4 + (level * 2); // Base 4 damage, +2 per level
            target.damage(damage, caster);

            // Apply debuff
            PotionEffectType effectType = Math.random() < 0.5 ? PotionEffectType.POISON : PotionEffectType.WEAKNESS;
            target.addPotionEffect(new PotionEffect(effectType, 100, 1)); // 5 seconds, level 2

            // Particle effect
            target.getWorld().spawnParticle(Particle.DUST,
                    target.getLocation().add(0, 1, 0),
                    30, 0.5, 0.5, 0.5, 0.1,
                    new Particle.DustOptions(Color.RED, 1));
        }
    }
}
