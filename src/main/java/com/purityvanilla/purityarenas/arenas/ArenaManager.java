package com.purityvanilla.purityarenas.arenas;

import com.purityvanilla.purityarenas.PurityArenas;
import org.bukkit.entity.Player;
import org.spongepowered.configurate.CommentedConfigurationNode;

import java.util.HashMap;
import java.util.Map;

public class ArenaManager {
    private HashMap<String, Arena> arenaMap;

    public ArenaManager() {
        loadArenaMap();
    }

    public void loadArenaMap() {
        arenaMap = new HashMap<>();

        for (Map.Entry<String, CommentedConfigurationNode> arenaEntry : PurityArenas.Config().getArenas().entrySet()) {
            Arena.TYPE arenaType = Arena.TYPE.valueOf(arenaEntry.getValue().node("arena-type").getString());

            switch (arenaType) {
                case FFA -> arenaMap.put(arenaEntry.getKey().toLowerCase(), new ArenaFFA(arenaEntry.getKey(), arenaEntry.getValue()));

                case DUEL -> arenaMap.put(arenaEntry.getKey().toLowerCase(), new ArenaDuel(arenaEntry.getKey(), arenaEntry.getValue()));
            }
        }

    }

    public HashMap<String, Arena> getArenaMap() {
        return arenaMap;
    }

    public void ResetAllArenas() {
        arenaMap.forEach((name, arena) -> {
            if (arena.isResetEnabled()) {
                if (PurityArenas.Config().verbose()) PurityArenas.Logger().info(String.format("Resetting arena %s...", name));
                arena.ResetArena();
            }
        });
     }

     public ArenaDuel ArenaFromOrganiser(Player player) {
        for (Arena arena : arenaMap.values()) {
            if (arena instanceof ArenaDuel && player.getUniqueId().equals(arena.getPlayers().get(0))) {
                return (ArenaDuel) arena;
            }
        }
         return null;
     }
}
