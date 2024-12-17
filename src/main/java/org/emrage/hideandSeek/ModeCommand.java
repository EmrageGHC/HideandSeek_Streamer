// src/main/java/org/emrage/hideandSeek/ModeCommand.java
package org.emrage.hideandSeek;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ModeCommand implements CommandExecutor, TabCompleter {

    private final GameManager gameManager;

    public ModeCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 2 && args[0].equalsIgnoreCase("load") && args[1].equalsIgnoreCase("streamer")) {
            gameManager.loadStreamerMode();
            return true;
        } else if (args.length == 3 && args[0].equalsIgnoreCase("start") && args[1].equalsIgnoreCase("hideandseek")) {
            gameManager.startGame(args[2]);
            return true;
        } else {
            sender.sendMessage("Usage: /mode load streamer or /mode start hideandseek <seekerName>");
            return false;
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            return List.of("load", "start");
        } else if (args.length == 2 && args[0].equalsIgnoreCase("load")) {
            return List.of("streamer");
        } else if (args.length == 2 && args[0].equalsIgnoreCase("start")) {
            return List.of("hideandseek");
        }
        return null;
    }
}