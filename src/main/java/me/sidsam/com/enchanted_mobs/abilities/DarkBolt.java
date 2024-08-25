package me.sidsam.com.enchanted_mobs.abilities;

import org.bukkit.Location;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.WitherSkull;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class DarkBolt extends Ability {

    public DarkBolt() {
        super("Dark Bolt", 15);
    }

    @Override
    protected void performAbility(LivingEntity caster, LivingEntity target, int level) {
        Location casterLocation = caster.getLocation();
        Location targetLocation = target.getLocation();

        // Calculate direction from caster to target
        Vector direction = targetLocation.toVector().subtract(casterLocation.toVector()).normalize();

        if (Math.random() > 0.5) {
            Fireball fireball = caster.getWorld().spawn(casterLocation.add(direction.multiply(1.5)), Fireball.class);
            fireball.setShooter(caster);
            fireball.setDirection(direction.multiply(1.5 * level));
        } else {
            WitherSkull witherSkull = caster.getWorld().spawn(casterLocation.add(direction.multiply(1.5)), WitherSkull.class);
            witherSkull.setShooter(caster);
            witherSkull.setDirection(direction.multiply(1.5 * level));
        }

        // Apply effect to the target (if it's a player)
        target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 5 * level, 1));
    }
}
