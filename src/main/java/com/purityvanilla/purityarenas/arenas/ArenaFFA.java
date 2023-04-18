package com.purityvanilla.purityarenas.arenas;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.spongepowered.configurate.CommentedConfigurationNode;


public class ArenaFFA extends Arena {
    protected Location spawnLocation;

    public ArenaFFA(String name, CommentedConfigurationNode node) {
        super(name, node);

        String coordString = node.node("spawn-location").getString();

        String[] coordList = coordString.split(",");
        Float[] spawnCoords = new Float[3];

        for (int i = 0; i < 3; i++) {
            spawnCoords[i] = Float.valueOf(coordList[i]);
        }

        spawnLocation = new Location(world, spawnCoords[0], spawnCoords[1], spawnCoords[2]);
    }

    @Override
    public void TeleportPlayer(Player player) {
        player.teleport(spawnLocation);
    }

}
