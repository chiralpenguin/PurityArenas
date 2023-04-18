package com.purityvanilla.purityarenas.commands;

import com.purityvanilla.purityarenas.PurityArenas;
import com.purityvanilla.purityarenas.arenas.Arena;
import com.purityvanilla.purityarenas.arenas.ArenaDuel;
import com.purityvanilla.purityarenas.gui.ArenaGUI;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ArenaCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage("§cOnly a player may use this command!");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            new ArenaGUI(player).openGUI(player);
            return true;
        }

        String subcommand = args[0].toLowerCase();
        switch (subcommand) {
            case "reset": {
                if (!player.hasPermission("purityarenas.arena.reset")) {
                    sender.sendMessage(Component.text("You do not have permission for that command!"));
                    return true;
                }

                if (args.length < 2) {
                    sender.sendMessage(Component.text("Please specify an arena."));
                    return true;
                }

                Arena arena = PurityArenas.getArenaManager().getArenaMap().get(args[1].toLowerCase());
                if (arena == null) {
                    sender.sendMessage(Component.text("That arena does not exist! Define it in config.yml"));
                    return true;
                }

                arena.ResetArena();
                return true;
            }

            case "lock": {
                if (!player.hasPermission("purityarenas.arena.lock")) {
                    sender.sendMessage(Component.text("You do not have permission for that command!"));
                    return true;
                }

                ArenaDuel arena = PurityArenas.getArenaManager().ArenaFromOrganiser(player);
                if (arena == null) {
                    sender.sendMessage(Component.text("You are not the organiser of any duelling arena!"));
                    return true;
                }

                arena.toggleLock();
                if (arena.isLocked()) {
                    sender.sendMessage(Component.text("Your duelling arena has been locked!"));
                }
                else {
                    sender.sendMessage(Component.text("Your duelling arena has been unlocked!"));
                }

                return true;
            }

            default:
                // Join arena here
        }

        return false;
    }

}
