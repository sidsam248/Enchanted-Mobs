package me.sidsam.com.enchanted_mobs.abilities;

import me.sidsam.com.enchanted_mobs.Main;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class Bloodlust extends Ability {

    private final int strengthLevel;
    private final long duration;

    public Bloodlust(int level) {
        super("Bloodlust", 30);
        this.strengthLevel = level;
        this.duration = 20L * 15 * level;
    }

    @Override
    protected void performAbility(LivingEntity caster, LivingEntity target, int level) {
        caster.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH, (int) duration, strengthLevel));

        // Play activation sound
        caster.getWorld().playSound(caster.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0f, 1.5f);

        // Initial particle burst
        caster.getWorld().spawnParticle(Particle.FLAME, caster.getLocation().add(0, 1, 0), 50, 0.5, 0.5, 0.5, 0.1);

        // Continuous particle effect
        new BukkitRunnable() {
            int ticks = 0;
            @Override
            public void run() {
                if (ticks >= duration || !caster.isValid()) {
                    this.cancel();
                    return;
                }

                Location location = caster.getLocation().add(0, 1, 0);
                double radius = 1;
                for (int i = 0; i < 10; i++) {
                    double angle = 2 * Math.PI * i / 10;
                    double x = radius * Math.cos(angle);
                    double z = radius * Math.sin(angle);
                    location.add(x, 0, z);
                    caster.getWorld().spawnParticle(Particle.DUST, location, 1, new Particle.DustOptions(Color.RED, 1));
                    location.subtract(x, 0, z);
                }

                if (ticks % 20 == 0) { // Every second
                    caster.getWorld().playSound(caster.getLocation(), Sound.BLOCK_FIRE_AMBIENT, 0.5f, 1.0f);
                }

                ticks++;
            }
        }.runTaskTimer(Main.getPlugin(), 0L, 1L);
    }
}