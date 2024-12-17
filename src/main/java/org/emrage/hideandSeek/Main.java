// src/main/java/org/emrage/hideandSeek/Main.java
package org.emrage.hideandSeek;

import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private static Main instance;
    private GameManager gameManager;

    @Override
    public void onEnable() {
        instance = this;
        gameManager = new GameManager(this);
        getServer().getPluginManager().registerEvents(new PlayerListener(gameManager), this);
        getServer().getPluginManager().registerEvents(new ServerListener(gameManager), this);
        this.getCommand("mode").setExecutor(new ModeCommand(gameManager));
        this.getCommand("mode").setTabCompleter(new ModeCommand(gameManager));
    }

    @Override
    public void onDisable() {
        gameManager.cleanup();
    }

    public static Main getInstance() {
        return instance;
    }
}