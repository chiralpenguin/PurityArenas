package com.purityvanilla.purityarenas.tasks;

import com.purityvanilla.purityarenas.PurityArenas;
import com.purityvanilla.purityarenas.arenas.Arena;
import com.purityvanilla.purityarenas.arenas.ArenaDuel;
import org.bukkit.scheduler.BukkitRunnable;

public class IncrementTimers extends BukkitRunnable {

    @Override
    public void run() {
        for (Arena arena : PurityArenas.getArenaManager().getArenaMap().values()) {
            if (arena instanceof ArenaDuel) {
                ((ArenaDuel) arena).updateTimer();
            }
        }
    }
}
