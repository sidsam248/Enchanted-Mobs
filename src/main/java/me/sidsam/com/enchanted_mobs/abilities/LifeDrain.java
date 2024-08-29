package me.sidsam.com.enchanted_mobs.abilities;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.attribute.Attribute;
import org.bukkit.util.Vector;

import java.util.Objects;

public class LifeDrain extends Ability {

    public LifeDrain() {
        super("Life Drain", 20); // 20-second cooldown
    }

    @Override
    protected void performAbility(LivingEntity caster, LivingEntity target, int level) {
        if (target instanceof Player player) {
            // Deal damage to the player
            player.damage(4); // 2 hearts

            // Visual effect: Red particles from target to caster
            drawParticleLine(target.getLocation(), caster.getLocation(), Particle.DUST, Color.RED, 50);

            // Sound effect at target's location
            target.getWorld().playSound(target.getLocation(), Sound.ENTITY_WITHER_HURT, 1.0f, 1.0f);

            // Heal the caster
            if (caster.isDead()) return;

            double health = caster.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
            double newHealth = Math.min(caster.getHealth() + 4, health);
            caster.setHealth(newHealth);

            // Visual effect: Green particles around caster
            caster.getWorld().spawnParticle(Particle.HAPPY_VILLAGER, caster.getLocation().add(0, 1, 0), 20, 0.5, 0.5, 0.5, 0);

            // Sound effect at caster's location
            caster.getWorld().playSound(caster.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 2.0f);
        }
    }

    private void drawParticleLine(Location start, Location end, Particle particle, Color color, int particleCount) {
        double distance = start.distance(end);
        Vector direction = end.toVector().subtract(start.toVector()).normalize();

        for (int i = 0; i < particleCount; i++) {
            double t = i / (double) particleCount;
            Vector position = start.toVector().add(direction.clone().multiply(distance * t));
            Objects.requireNonNull(start.getWorld()).spawnParticle(particle, position.toLocation(start.getWorld()), 1, new Particle.DustOptions(color, 1));
        }
    }
}