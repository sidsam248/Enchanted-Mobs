package me.sidsam.com.enchanted_mobs.abilities;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;

public abstract class Ability {
    private final String name;
    private final int cooldown; // in seconds
    private long lastUsedTime;
    private boolean isPassive = false;

    public Ability(String name, int cooldown) {
        this(name, cooldown, false);
    }

    public Ability(String name, int cooldown, boolean isPassive) {
        this.name = name;
        this.cooldown = cooldown;
        this.lastUsedTime = 0;
        this.isPassive = isPassive;
    }

    public void useAbility(LivingEntity caster, LivingEntity target, int level) {
        long currentTime = System.currentTimeMillis();
        if (currentTime < lastUsedTime + cooldown * 1000L) {
            return; // Cooldown has not expired
        }

        // Perform ability logic
        performAbility(caster, target, level);

        // Update last used time
        lastUsedTime = currentTime;
    }

    protected abstract void performAbility(LivingEntity caster, LivingEntity target, int level);

    public long getLastUsedTime() {
        return lastUsedTime;
    }

    public int getCooldown() {
        return cooldown;
    }

    public String getName() {
        return name;
    }

    public boolean isPassive() {
        return isPassive;
    }
}
