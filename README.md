# Enchanted Mobs Plugin

## Overview

Enchanted Mobs is a custom Minecraft plugin designed to add RPG-style mobs with unique abilities and particle effects. The plugin introduces six different classes of enchanted mobs, each with its own set of skills that challenge players in combat. This plugin is perfect for adding a layer of difficulty and excitement to your Minecraft server by introducing specialized mobs that require strategy to defeat.

## Features

- **Six Unique Mob Classes**: 
  - **Warlock (Mage)**
  - **Berserker (Melee)**
  - **Pyromancer (AOE/Mage)**
  - **Necromancer (Mage/Summoner)**
  - **Paladin (Melee/Support)**
  - **Assassin (Melee)**

- **10% Chance of Transformation**: Every time a regular mob spawns (e.g., Zombie, Skeleton), it has a 10% chance to turn into one of the custom enchanted mobs.

- **Custom Abilities**: Each enchanted mob comes with a set of abilities and custom particle effects that enhance gameplay.

## Class Details and Abilities

### 1. **Warlock (Mage)**
   - **Abilities**:
     - **Dark Bolt**: Fires a wither skull or fireball at the player that inflicts Wither or Blindness.
     - **Summon Minions**: Temporarily summons 2-3 smaller minions (Zombies or Skeletons) to assist in battle.
     - **Life Drain**: Reduces the player’s health by 2 hearts and heals the Warlock.
   - **Particle Effects**: Purple/black swirling particles with Ender particles.

### 2. **Berserker (Melee)**
   - **Abilities**:
     - **Frenzy**: Increases attack speed and damage for a short time, with each attack dealing extra knockback.
     - **Ground Smash**: Slams the ground, creating a shockwave that knocks back nearby players.
     - **Bloodlust**: Gains a temporary strength buff whenever it lands a hit.
   - **Particle Effects**: Red swirls and blood splatter particles.

### 3. **Pyromancer (AOE/Mage)**
   - **Abilities**:
     - **Fire Nova**: Sends out a ring of fire that sets everything in a 5-block radius on fire.
     - **Flame Burst**: Launches a burst of fireballs in all directions.
     - **Blaze Shield**: Creates a shield of fire that damages and knocks back players who get too close.
   - **Particle Effects**: Flame and smoke particles, with occasional spark particles.

### 4. **Necromancer (Mage/Summoner)**
   - **Abilities**:
     - **Soul Harvest**: Steals health from the player and stores it in floating soul orbs around the Necromancer, which heal it when consumed.
     - **Raise Undead**: Summons a horde of Zombies or Skeletons from the ground.
     - **Death Mark**: Marks the player, causing them to take extra damage for a short period.
   - **Particle Effects**: Greenish smoke with floating souls (white particles).

### 5. **Paladin (Melee/Support)**
   - **Abilities**:
     - **Holy Smite**: Strikes the ground with lightning, dealing area damage and healing the Paladin.
     - **Divine Shield**: Temporarily becomes immune to damage and reflects a portion of melee damage back to attackers.
     - **Radiance**: Passively heals the Paladin and nearby allies over time.
   - **Particle Effects**: Golden light particles with white sparkles.

### 6. **Assassin (Melee)**
   - **Abilities**:
     - **Shadow Step**: Teleports behind the player for a quick ambush.
     - **Backstab**: Deals extra damage from behind, inflicting Poison or Weakness on the player.
     - **Vanish**: Temporarily goes invisible and reappears for a surprise attack.
   - **Particle Effects**: Black smoke particles with dark gray wisps.

## Installation

1. Download the plugin JAR file and place it in your server’s `plugins` folder.
2. Start your server. The plugin will automatically generate the required configuration files.

## Configuration

- **Mob Transformation Chance**: By default, mobs have a 20% chance to transform into an enchanted mob when they spawn. This value can be adjusted in the configuration file generated by the plugin.

## Usage

- The plugin runs automatically and requires no manual interaction.
- You can customize the spawn rate, abilities, and other aspects of the enchanted mobs by modifying the configuration file.

## Contributing

If you’d like to contribute to the plugin, feel free to fork the repository, create a branch, and submit a pull request.

## License

This plugin is licensed under the MIT License.
