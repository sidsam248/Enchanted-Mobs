package me.sidsam.com.enchanted_mobs.abilities;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FlameBurst extends Ability {

    private final int fireballCount;
    private final double radius;

    public FlameBurst(int level) {
        super("Flame Burst", 30); // 20-second cooldown
        this.fireballCount = level; // Number of fireballs
        this.radius = 15.0 * level; // radius
    }

    @Override
    public void performAbility(LivingEntity caster, LivingEntity target, int level) {
        Location casterLocation = caster.getLocation();
        World world = caster.getWorld();

        // Get nearby entities
        List<LivingEntity> nearbyEntities = caster.getWorld().getNearbyEntities(casterLocation, radius, radius, radius).stream()
                .filter(e -> e instanceof LivingEntity && !e.equals(caster)) // Exclude caster
                .map(e -> (LivingEntity) e)
                .collect(Collectors.toList());

        // Shuffle the list to randomize the fireball targets
        Collections.shuffle(nearbyEntities);

        // Number of fireballs to spawn
        int fireballCount = Math.min(nearbyEntities.size(), this.fireballCount); // Limit to the number of entities or a maximum value

        for (int i = 0; i < fireballCount; i++) {
            if (i >= nearbyEntities.size()) break;

            LivingEntity targetEntity = nearbyEntities.get(i);
            Location fireballLocation = casterLocation.clone().add(0, 1, 0);

            Fireball fireball = world.spawn(fireballLocation, Fireball.class);
            fireball.setShooter(caster);
            fireball.setDirection(targetEntity.getLocation().toVector().subtract(fireballLocation.toVector()).normalize());
        }
        caster.getWorld().playSound(casterLocation, Sound.ENTITY_BLAZE_SHOOT, 1.0f, 1.0f);
    }
}
