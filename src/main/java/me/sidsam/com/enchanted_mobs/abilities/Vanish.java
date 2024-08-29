package me.sidsam.com.enchanted_mobs.abilities;

import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import me.sidsam.com.enchanted_mobs.Main;

public class Vanish extends Ability {

    public Vanish() {
        super("Vanish", 20); // 20 seconds cooldown
    }

    @Override
    protected void performAbility(LivingEntity caster, LivingEntity target, int level) {
        // Disappear effect
        caster.getWorld().spawnParticle(Particle.LARGE_SMOKE, caster.getLocation().add(0, 1, 0), 50, 0.5, 1, 0.5, 0.05);

        // Apply invisibility
        caster.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 20 * 3 * level, 0));

        // Reappear after
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!caster.isDead()) {
                    // Reappear effect
                    caster.getWorld().spawnParticle(Particle.DUST,
                            caster.getLocation().add(0, 1, 0),
                            50, 0.5, 1, 0.5, 0.1,
                            new Particle.DustOptions(Color.PURPLE, 1));

                    // Deal damage to nearby entities
                    caster.getNearbyEntities(3, 3, 3).stream()
                            .filter(e -> e instanceof LivingEntity && e != caster)
                            .forEach(e -> ((LivingEntity) e).damage(3 + level, caster));
                }
            }
        }.runTaskLater(Main.getPlugin(), 20L * 3 * level);
    }
}