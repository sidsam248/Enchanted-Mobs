package me.sidsam.com.enchanted_mobs.abilities;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Frenzy extends Ability {

    private final double speedBoost; // Velocity multiplier for movement
    private final int duration; // Duration in seconds

    public Frenzy(int level) {
        super("Frenzy", 30);
        this.speedBoost = 2 * level;
        this.duration = 5 * 20 * level;
    }

    @Override
    protected void performAbility(LivingEntity caster, LivingEntity target, int level) {
        caster.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, duration, (int) speedBoost));
    }
}
