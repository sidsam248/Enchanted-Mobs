package me.sidsam.com.enchanted_mobs.entities;

import me.sidsam.com.enchanted_mobs.Main;
import me.sidsam.com.enchanted_mobs.abilities.Ability;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public abstract class EnchantedMob {
    private final String name;
    protected LivingEntity entity;
    protected int level;
    protected List<Ability> abilities;

    public EnchantedMob(String name) {
        this.name = name;
    }

    public abstract void loop(); // For unique particles/effects when the mob is spawned

    protected abstract List<Ability> createAbilities(); // Define abilities specific to the mob

    protected void spawnParticles(Particle particle, int count, double offsetX, double offsetY, double offsetZ) {
        World world = entity.getWorld();
        world.spawnParticle(particle, entity.getLocation().add(offsetX, offsetY, offsetZ), count);
    }

    protected void spawnParticles(Particle particle, int count, double offsetX, double offsetY, double offsetZ, Color color) {
        World world = entity.getWorld();

        if (particle != Particle.ENTITY_EFFECT) {
            Particle.DustOptions dustOptions = new Particle.DustOptions(color, 1);
            world.spawnParticle(particle, entity.getLocation().add(offsetX, offsetY, offsetZ), count, 0, 0, 0, 0, dustOptions);
        } else {
            world.spawnParticle(particle, entity.getLocation().add(offsetX, offsetY, offsetZ), count, 0, 0, 0, 0, color);
        }
    }

    public void useAbility() {
        List<Player> playersInRange = entity.getWorld().getEntitiesByClass(Player.class).stream()
                .filter(player -> player.getLocation().distance(entity.getLocation()) <= 30)
                .toList();

        if (playersInRange.isEmpty()) return;

        // Get the nearest player
        Player nearestPlayer = playersInRange.stream()
                .min((p1, p2) -> Double.compare(p1.getLocation().distance(entity.getLocation()), p2.getLocation().distance(entity.getLocation())))
                .orElse(null);

        // Select a random ability that is not on cooldown
        List<Ability> availableAbilities = abilities.stream()
                .filter(a -> System.currentTimeMillis() >= a.getLastUsedTime() + a.getCooldown() * 1000L)
                .toList();

        if (!availableAbilities.isEmpty()) {
            Ability selectedAbility = availableAbilities.get((int) (Math.random() * availableAbilities.size()));
            // Use the selected ability
            selectedAbility.useAbility(entity, nearestPlayer, level);
        }
    }

    public void startLogic() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (entity == null || entity.isDead()) {
                    cancel();
                    return;
                }
                loop();
                useAbility();
            }
        }.runTaskTimer(Main.getPlugin(), 0L, 20L); // 1 second intervals
    }

    public void setEntity(LivingEntity entity, int level) {
        entity.setCustomName(name + " - " + level);
        this.entity = entity;
        this.level = level;
        this.abilities = createAbilities();
        AttributeInstance attribute = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        assert attribute != null;
        attribute.setBaseValue(20 + (level * 10));
    }

    public LivingEntity getEntity() {
        return this.entity;
    }

    public int getLevel() {
        return this.level;
    }
}