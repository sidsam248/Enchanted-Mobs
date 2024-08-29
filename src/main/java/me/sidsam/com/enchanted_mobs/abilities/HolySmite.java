package me.sidsam.com.enchanted_mobs.abilities;

import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.Objects;

public class HolySmite extends Ability {
    public HolySmite() {
        super("Holy Smite", 30); // 30 seconds cooldown
    }

    @Override
    protected void performAbility(LivingEntity caster, LivingEntity target, int level) {
        Location strikeLocation = target.getLocation();

        // Strike lightning
        caster.getWorld().strikeLightningEffect(strikeLocation);

        // Deal area damage
        double damage = 2 + (level * 2); // Base damage 2, increases by 2 per level
        double radius = 5 + (level * 0.5); // Base radius 5, increases by 0.5 per level
        for (Entity entity : caster.getNearbyEntities(radius, radius, radius)) {
            if (entity instanceof LivingEntity && entity != caster) {
                ((LivingEntity) entity).damage(damage, caster);
            }
        }

        // Heal caster
        double healing = 5 + (level * 1.5); // Base healing 5, increases by 1.5 per level
        double maxHealth = Objects.requireNonNull(caster.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue();
        caster.setHealth(Math.min(caster.getHealth() + healing, maxHealth));
    }
}
