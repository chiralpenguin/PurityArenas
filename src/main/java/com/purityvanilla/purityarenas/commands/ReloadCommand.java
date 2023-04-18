package com.purityvanilla.purityarenas.commands;

import com.purityvanilla.purityarenas.PurityArenas;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        sender.sendMessage("\247cReloading PurityArenas...");
        PurityArenas.INSTANCE.reloadConfig();
        sender.sendMessage("\247cPurityArenas config reloaded!");
        return true;
    }
}
