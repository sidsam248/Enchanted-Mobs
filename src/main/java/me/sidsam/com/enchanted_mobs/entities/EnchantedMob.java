package me.sidsam.com.enchanted_mobs.entities;

import me.sidsam.com.enchanted_mobs.Main;
import me.sidsam.com.enchanted_mobs.abilities.Ability;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockIterator;

import java.util.List;
import java.util.Objects;

public abstract class EnchantedMob {
    private final String name;
    protected LivingEntity entity;
    protected int level;
    protected List<Ability> abilities;
    private BossBar bossBar;
    private final double RANGE = 30.0;
    private long useAbilityNextAt = 0;
    private final long DEFAULT_DELAY = 1000 * 5; // 5 seconds delay

    public EnchantedMob(String name) {
        this.name = name;
    }

    public abstract void loop(); // For unique particles/effects when the mob is spawned

    protected abstract List<Ability> createAbilities(); // Define abilities specific to the mob

    protected void spawnParticles(Particle particle, int count, double offsetX, double offsetY, double offsetZ) {
        World world = entity.getWorld();
        world.spawnParticle(particle, entity.getLocation().add(offsetX, offsetY, offsetZ), count);
    }

    private boolean hasLineOfSight(LivingEntity entity, Player player) {
        if (entity.hasLineOfSight(player)) {
            return true;
        } else {
            // Double-check with a more precise method
            Location entityEyes = entity.getEyeLocation();
            Location playerEyes = player.getEyeLocation();
            BlockIterator blockIterator = new BlockIterator(Objects.requireNonNull(entityEyes.getWorld()),
                    entityEyes.toVector(),
                    playerEyes.toVector().subtract(entityEyes.toVector()),
                    0,
                    (int) entityEyes.distance(playerEyes));

            while (blockIterator.hasNext()) {
                Block block = blockIterator.next();
                if (block.getType().isOccluding()) {
                    return false;
                }
                if (block.getLocation().distanceSquared(playerEyes) <= 1) {
                    return true;
                }
            }
            return true;
        }
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
                .filter(player -> player.getLocation().distance(entity.getLocation()) <= RANGE)
                .filter(player -> hasLineOfSight(entity, player))
                .toList();

        if (playersInRange.isEmpty()) return;

        if(useAbilityNextAt > System.currentTimeMillis()) return;

        // Get the nearest player
        Player nearestPlayer = playersInRange.stream()
                .min((p1, p2) -> Double.compare(p1.getLocation().distance(entity.getLocation()), p2.getLocation().distance(entity.getLocation())))
                .orElse(null);

        // Get all passive abilities
        List<Ability> availablePassiveAbilities = abilities.stream()
                .filter(Ability::isPassive)
                .toList();

        if (!availablePassiveAbilities.isEmpty()) {
            availablePassiveAbilities.forEach(ability -> {
                ability.useAbility(entity, nearestPlayer, level);
            });
        }

        // Select a random ability that is not on cooldown and is not a passive ability
        List<Ability> availableAbilities = abilities.stream()
                .filter(ability -> System.currentTimeMillis() >= ability.getLastUsedTime() + ability.getCooldown() * 1000L)
                .filter(a -> !a.isPassive())
                .toList();

        if (!availableAbilities.isEmpty()) {
            Ability selectedAbility = availableAbilities.get((int) (Math.random() * availableAbilities.size()));
            // Use the selected ability
            selectedAbility.useAbility(entity, nearestPlayer, level);

            useAbilityNextAt = System.currentTimeMillis() + DEFAULT_DELAY;
        }
    }

    public void createBossBar() {
        if (bossBar == null && this.entity != null) {
            bossBar = Bukkit.createBossBar(
                    this.entity.getName(),
                    BarColor.PURPLE,
                    BarStyle.SOLID
            );
        }
        updateBossBar();
    }

    public void updateBossBar() {
        if (bossBar != null && entity != null) {
            double health = entity.getHealth();
            double maxHealth = Objects.requireNonNull(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getBaseValue();
            bossBar.setProgress(Math.max(0, Math.min(health / maxHealth, 1)));

            for (Player player : entity.getWorld().getPlayers()) {
                if (player.getLocation().distance(entity.getLocation()) <= RANGE) {
                    bossBar.addPlayer(player);
                } else {
                    bossBar.removePlayer(player);
                }
            }
        }
    }

    public void removeBossBar() {
        if (bossBar != null) {
            bossBar.removeAll();
            bossBar = null;
        }
    }

    public void startLogic() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (entity == null || entity.isDead() || entity.getHealth() < 1) {
                    removeBossBar();
                    cancel();
                    return;
                }
                loop();
                useAbility();
                updateBossBar();
            }
        }.runTaskTimer(Main.getPlugin(), 0L, 20L); // 1 second intervals
    }

    public void setEntity(LivingEntity entity, int level) {
        entity.setCustomName(name + " - " + level);
        this.entity = entity;
        this.level = level;
        this.abilities = createAbilities();

        AttributeInstance maxHealthAttribute = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (maxHealthAttribute != null) {
            double newMaxHealth = maxHealthAttribute.getBaseValue() + (level * 10);
            maxHealthAttribute.setBaseValue(newMaxHealth);
            entity.setHealth(newMaxHealth);
        }
        createBossBar();
    }

    public LivingEntity getEntity() {
        return this.entity;
    }

    public int getLevel() {
        return this.level;
    }
}