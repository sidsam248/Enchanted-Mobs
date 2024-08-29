package me.sidsam.com.enchanted_mobs.abilities;

import me.sidsam.com.enchanted_mobs.Main;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Zombie;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Objects;

public class RaiseUndead extends Ability {

    public RaiseUndead() {
        super("Raise Undead", 30);
    }

    @Override
    protected void performAbility(LivingEntity caster, LivingEntity target, int level) {
        World world = caster.getWorld();
        Location location = caster.getLocation();

        // Summon zombies
        for (int i = 0; i < level; i++) {
            Location spawnLoc = getRandomSpawnLocation(location, 5);
            Objects.requireNonNull(spawnLoc.getWorld()).spawn(spawnLoc, Zombie.class, zombieEntity -> {
                zombieEntity.setMetadata("isMinion", new FixedMetadataValue(Main.getPlugin(), true));
                zombieEntity.setCustomName("Undead ");
            });
        }

        // Summon skeletons
        for (int i = 0; i < level; i++) {
            Location spawnLoc = getRandomSpawnLocation(location, 5);
            Objects.requireNonNull(spawnLoc.getWorld()).spawn(spawnLoc, Skeleton.class, skeletonEntity -> {
                skeletonEntity.setMetadata("isMinion", new FixedMetadataValue(Main.getPlugin(), true));
                skeletonEntity.setCustomName("Skeletal Archer");
            });
        }

        // Visual and sound effects
        world.playSound(location, Sound.ENTITY_WITHER_SPAWN, 1.0f, 0.5f);
    }

    private Location getRandomSpawnLocation(Location center, double radius) {
        double angle = Math.random() * 2 * Math.PI;
        double x = center.getX() + Math.cos(angle) * radius;
        double z = center.getZ() + Math.sin(angle) * radius;
        return new Location(center.getWorld(), x, center.getY(), z);
    }
}
