package com.purityvanilla.purityarenas;

import com.purityvanilla.purityarenas.arenas.ArenaManager;
import com.purityvanilla.purityarenas.commands.ArenaCommand;
import com.purityvanilla.purityarenas.commands.ReloadCommand;
import com.purityvanilla.purityarenas.gui.GUIObject;
import com.purityvanilla.purityarenas.listeners.InventoryClickListener;
import com.purityvanilla.purityarenas.listeners.InventoryCloseListener;
import com.purityvanilla.purityarenas.tasks.CheckArenas;
import com.purityvanilla.purityarenas.tasks.IncrementTimers;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.logging.Logger;

public class PurityArenas extends JavaPlugin {
    public static PurityArenas INSTANCE;
    private Config config;
    private ArenaManager arenaManager;
    private BukkitTask checkArenasTask;
    private BukkitTask incrementTimersTask;

    @Override
    public void onEnable() {
        INSTANCE = this;
        config = new Config();
        GUIObject.InitGuiObjectMap();

        arenaManager = new ArenaManager();

        checkArenasTask = new CheckArenas().runTaskTimer(this, 20L, Config().getCheckArenaInterval());
        incrementTimersTask = new IncrementTimers().runTaskTimer(this, 20L, 1);

        getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryCloseListener(), this);

        getCommand("arena").setExecutor(new ArenaCommand());
        getCommand("reload").setExecutor(new ReloadCommand());

        arenaManager.ResetAllArenas();
    }

    @Override
    public void onDisable() {

    }

    public void reloadConfig() {
        checkArenasTask.cancel();

        config = new Config();
        arenaManager.loadArenaMap();

        checkArenasTask = new CheckArenas().runTaskTimer(this, 20L, Config().getCheckArenaInterval());
    }

    public static Config Config() {
        return INSTANCE.config;
    }

    public static Logger Logger() {
        return INSTANCE.getLogger();
    }

    public static ArenaManager getArenaManager() {
        return INSTANCE.arenaManager;
    }
}
