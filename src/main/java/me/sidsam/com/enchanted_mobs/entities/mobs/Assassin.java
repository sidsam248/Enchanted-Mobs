package me.sidsam.com.enchanted_mobs.entities.mobs;

import me.sidsam.com.enchanted_mobs.abilities.Ability;
import me.sidsam.com.enchanted_mobs.abilities.Backstab;
import me.sidsam.com.enchanted_mobs.abilities.ShadowStep;
import me.sidsam.com.enchanted_mobs.abilities.Vanish;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Random;

public class Assassin extends EnchantedMob {
    private int tick = 0;
    private final Random random = new Random();
    private final Vector[] miragePositions = new Vector[3];

    public Assassin() {
        super("Assassin");
        for (int i = 0; i < miragePositions.length; i++) {
            miragePositions[i] = new Vector(random.nextDouble() - 0.5, 0, random.nextDouble() - 0.5).normalize().multiply(2);
        }
    }

    @Override
    public void loop() {
        tick++;

        if (tick > 40)
            tick = 0;

        // Assassin's Veil
        createAssassinVeil();

        // Mirage Illusions
        createMirageIllusions();
    }

    private void createAssassinVeil() {
        double height = 2.0;
        for (double y = 0; y <= height; y += 0.2) {
            double radius = 0.8 * Math.sin((Math.PI * y) / height);
            for (int i = 0; i < 8; i++) {
                double angle = 2 * Math.PI * i / 8 + (tick * 0.05);
                double x = radius * Math.cos(angle);
                double z = radius * Math.sin(angle);
                Color color = Color.fromRGB(
                        (int)(20 + 20 * Math.sin(tick * 0.1 + y)),
                        0,
                        (int)(20 + 20 * Math.cos(tick * 0.1 + y))
                );
                spawnParticles(Particle.DUST, 1, x, y, z, color);
            }
        }
    }

    private void createMirageIllusions() {
        for (int i = 0; i < miragePositions.length; i++) {
            miragePositions[i] = new Vector(random.nextDouble() - 0.5, 0, random.nextDouble() - 0.5).normalize().multiply(2);
        }

        for (Vector pos : miragePositions) {
            for (int i = 0; i < 5; i++) {
                double y = random.nextDouble() * 2;
                spawnParticles(Particle.DUST, 1, pos.getX(), y, pos.getZ(), Color.GRAY);
            }
        }
    }

    @Override
    protected List<Ability> createAbilities() {
        return List.of(new ShadowStep(), new Backstab(), new Vanish());
    }
}