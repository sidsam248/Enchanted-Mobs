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
import org.bukkit.util.Vector;

public class Frenzy extends Ability {

    private final double speedBoost;
    private final int duration;

    public Frenzy(int level) {
        super("Frenzy", 30);
        this.speedBoost = 2 * level;
        this.duration = 5 * 20 * level;
    }

    @Override
    protected void performAbility(LivingEntity caster, LivingEntity target, int level) {
        caster.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, duration, (int) speedBoost));

        // Activation sound
        caster.getWorld().playSound(caster.getLocation(), Sound.ENTITY_ILLUSIONER_CAST_SPELL, 1.0f, 1.5f);

        // Initial particle burst
        caster.getWorld().spawnParticle(Particle.WITCH, caster.getLocation().add(0, 1, 0), 50, 0.5, 0.5, 0.5, 0.1);

        // Continuous effect
        new BukkitRunnable() {
            int ticks = 0;
            @Override
            public void run() {
                if (ticks >= duration || !caster.isValid()) {
                    this.cancel();
                    return;
                }

                // Spiral particle effect
                Location location = caster.getLocation().add(0, 1, 0);
                double radius = 0.5;
                double y = 0;
                for (int i = 0; i < 2; i++) {
                    double angle = 6.283185 * i / 2 + ticks * 0.5;
                    double x = radius * Math.cos(angle);
                    double z = radius * Math.sin(angle);
                    location.add(x, y, z);
                    caster.getWorld().spawnParticle(Particle.DUST, location, 1,
                            new Particle.DustOptions(Color.fromRGB(173, 216, 230), 1)); // Light blue color
                    location.subtract(x, y, z);
                    y += 0.1;
                }

                // Trail effect
                Vector direction = caster.getLocation().getDirection().normalize().multiply(0.2);
                Location trailLoc = caster.getLocation().add(0, 0.5, 0).subtract(direction);
                caster.getWorld().spawnParticle(Particle.CLOUD, trailLoc, 5, 0.1, 0.1, 0.1, 0);

                // Periodic sound effect
                if (ticks % 20 == 0) { // Every second
                    caster.getWorld().playSound(caster.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 0.5f, 2.0f);
                }

                ticks++;
            }
        }.runTaskTimer(Main.getPlugin(), 0L, 1L);
    }
}