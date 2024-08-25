package me.sidsam.com.enchanted_mobs.abilities;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

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

            // Heal the caster
            if (caster == null || caster.isDead()) return;

            double health = Objects.requireNonNull(caster.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue();
            double newHealth = Math.min(caster.getHealth() + 4, health);
            caster.setHealth(newHealth);
        }
    }
}
