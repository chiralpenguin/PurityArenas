package com.purityvanilla.purityarenas.arenas;

import com.purityvanilla.purityarenas.PurityArenas;
import io.leangen.geantyref.TypeToken;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ArenaDuel extends Arena {
    protected List<Location> spawnLocations;
    protected int teamLimit;

    private HashMap<Integer, List<UUID>> teams;
    private boolean locked;
    private int ticksToStart;

    public ArenaDuel(String name, CommentedConfigurationNode node) {
        super(name, node);

        List<String> coordStringList = null;
        try {
            coordStringList = node.node("team-spawn-locations").getList(new TypeToken<>(){});
        } catch (SerializationException e) {
            e.printStackTrace();
        }

        spawnLocations = new ArrayList<>();
        for (String coordString : coordStringList) {
            String[] coordList = coordString.split(",");
            Float[] coords = new Float[3];

            for (int i = 0; i < 3; i++) {
                coords[i] = Float.valueOf(coordList[i]);
            }

            this.spawnLocations.add(new Location(world, coords[0], coords[1], coords[2]));
        }

        teams = new HashMap<>();
        for (int i = 1; i <= spawnLocations.size(); i ++) {
            teams.put(i, new ArrayList<>());
        }

        teamLimit = node.node("team-limit").getInt();

        locked = false;
        ticksToStart = PurityArenas.Config().getDuellingCountdown();
    }

    public boolean isLocked() {
        return locked;
    }

    public boolean inProgress() {
        return ticksToStart > 0;
    }

    public void toggleLock() {
        locked = !locked;
    }

    public void updateTimer() {
        if (players.size() > 0) {
            this.ticksToStart -= 1;

            // TODO Update players with messages here
        }
    }

    @Override
    public void TeleportPlayer(Player player) {
        // TODO Logic that teleports a player to the correct spawn location depending on which team they are in
    }

    @Override
    public void LeaveArena(Player player) {
        super.LeaveArena(player);

        if (players.size() == 0) {
            ResetArena();
            ticksToStart = PurityArenas.Config().getDuellingCountdown();
        }
    }

    @Override
    public void removePlayer(UUID player) {
        players.remove(player);

        teams.forEach((team, list) -> list.remove(player));
    }
}
