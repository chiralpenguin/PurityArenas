package com.purityvanilla.purityarenas.tasks;

import com.purityvanilla.purityarenas.PurityArenas;
import com.purityvanilla.purityarenas.arenas.Arena;
import com.purityvanilla.purityarenas.arenas.ArenaManager;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class CheckArenas extends BukkitRunnable {

    @Override
    public void run() {
        ArenaManager arenaManager = PurityArenas.getArenaManager();

        for (Player player : PurityArenas.INSTANCE.getServer().getOnlinePlayers()) {
            Location playerLoc = player.getLocation();
            BlockVector3 playerBlock = BlockVector3.at(playerLoc.getBlockX(), playerLoc.getBlockY(), playerLoc.getBlockZ());

            for (Arena arena : arenaManager.getArenaMap().values()) {
                ProtectedRegion region = arena.getRegion();

                if (region.contains(playerBlock) && !arena.isPlayerInArena(player)) {
                    if (PurityArenas.Config().verbose()) PurityArenas.Logger().info(String.format("Tracked player %s in arena %s", player.getName(), arena.getArenaName()));
                    arena.addPlayer(player);
                    continue;
                }

                if (!region.contains(playerBlock) && arena.isPlayerInArena(player)) {
                    if (PurityArenas.Config().verbose()) PurityArenas.Logger().info(String.format("Un-tracked player %s in arena %s", player.getName(), arena.getArenaName()));
                    arena.LeaveArena(player);
                    continue;
                }
            }
        }
    }

}
