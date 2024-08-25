package me.sidsam.com.enchanted_mobs.abilities;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

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
    }
}

