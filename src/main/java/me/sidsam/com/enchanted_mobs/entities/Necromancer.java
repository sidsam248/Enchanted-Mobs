package me.sidsam.com.enchanted_mobs.entities;

import me.sidsam.com.enchanted_mobs.abilities.Ability;
import me.sidsam.com.enchanted_mobs.abilities.DeathMark;
import me.sidsam.com.enchanted_mobs.abilities.RaiseUndead;
import me.sidsam.com.enchanted_mobs.abilities.SoulHarvest;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;

import java.util.List;

public class Necromancer extends EnchantedMob {

    public Necromancer() {
        super("Necromancer");
    }

    @Override
    public void loop() {
        summonSouls();
    }

    private void summonSouls() {
        Location location = this.entity.getLocation();
        World world = location.getWorld();
        if (world == null) return;

        // Rising souls
        for (int i = 0; i < 5; i++) {
            double x = (Math.random() - 0.5) * 3;
            double z = (Math.random() - 0.5) * 3;
            for (double y = 0; y < 2; y += 0.1) {
                Location soulLocation = location.clone().add(x, y, z);
                world.spawnParticle(Particle.SOUL, soulLocation, 1, 0, 0, 0, 0.02);
            }
        }
    }


    @Override
    protected List<Ability> createAbilities() {
        return List.of(new RaiseUndead(), new SoulHarvest(), new DeathMark(level));
    }
}
