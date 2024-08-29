package me.sidsam.com.enchanted_mobs.abilities;

import me.sidsam.com.enchanted_mobs.Main;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Zombie;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.Objects;

public class SummonMinions extends Ability {

    public SummonMinions() {
        super("Summon Minions", 30);
    }

    @Override
    protected void performAbility(LivingEntity caster, LivingEntity target, int level) {
        Location casterLocation = caster.getLocation();

        for (int i = 0; i < 2 + (int) (Math.random() * 2 * level); i++) {
            Objects.requireNonNull(casterLocation.getWorld()).spawn(casterLocation, Zombie.class, zombieEntity -> {
                zombieEntity.setMetadata("isMinion", new FixedMetadataValue(Main.getPlugin(), true));
                zombieEntity.setBaby();
                zombieEntity.setCustomName("Minion");
            });
        }
    }
}