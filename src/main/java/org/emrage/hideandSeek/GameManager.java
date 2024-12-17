// src/main/java/org/emrage/hideandSeek/GameManager.java
package org.emrage.hideandSeek;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class GameManager {

    private final Main plugin;
    private Player seeker;
    private boolean gameStarted = false;
    private Timer hideTimer;
    private Timer seekTimer;
    private boolean modeLoaded = false;
    private Location joinLocation = new Location(Bukkit.getWorld("world"), -750, 66, 449); // Example location

    public GameManager(Main plugin) {
        this.plugin = plugin;
        hideTimer = new Timer(plugin);
        seekTimer = new Timer(plugin);
    }

    public void cleanup() {
        // Cleanup logic
    }

    public void startGame(String seekerName) {
        gameStarted = true;
        seeker = Bukkit.getPlayer(seekerName);
        if (seeker == null) {
            plugin.getLogger().warning("Seeker not found!");
            return;
        }

        // Teleport the seeker to a specific location
        Location seekerLocation = new Location(Bukkit.getWorld("world"), -750, 66, 449); // Example location
        seeker.teleport(seekerLocation);

        // Apply blindness and slowness effects to the seeker for 4 minutes
        seeker.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 4 * 60 * 20, 255, false, false));
        seeker.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 4 * 60 * 20, 255, false, false));

        // Set all players to adventure mode and hide their nametags
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.setGameMode(GameMode.ADVENTURE);
            if (!player.equals(seeker)) {
                player.setDisplayName(ChatColor.RESET + "Hider");
            }
        }

        // Start the hide timer
        hideTimer.startTimer(4 * 60); // 4 minutes

        // Schedule the start of the seek timer
        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.broadcastMessage(ChatColor.GOLD + "The hide time is over! The seeker is now hunting!");
                startSeekTimer();
            }
        }.runTaskLater(plugin, 4 * 60 * 20L); // 4 minutes delay
    }

    public void loadStreamerMode() {
        modeLoaded = true;
        Bukkit.broadcastMessage(ChatColor.GREEN + "Streamer mode loaded! All players will be teleported to the starting location.");
    }

    public void handlePlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.setPlayerListHeaderFooter(
                ChatColor.GOLD + "TwitchBattle 2024",
                ChatColor.GREEN + "HideandSeek"
        );
        player.sendMessage(ChatColor.GREEN + "Welcome to the server, " + player.getName() + "!");
        if (modeLoaded) {
            player.teleport(joinLocation);
        }
    }

    public void handlePlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Bukkit.broadcastMessage(ChatColor.RED + player.getName() + " has left the game.");
    }

    public void handleEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player && gameStarted) {
            Player damager = (Player) event.getDamager();
            Player damaged = (Player) event.getEntity();

            if (damager.equals(seeker)) {
                damaged.kickPlayer(ChatColor.RED + "You have been found by the seeker!");
                damaged.getWorld().strikeLightningEffect(damaged.getLocation());
                Bukkit.broadcastMessage(ChatColor.RED + damaged.getName() + " has been found by the seeker!");
            }
        }
    }

    public void handleBlockBreak(BlockBreakEvent event) {
        if (event.getPlayer().getDisplayName().equals("HeyTobi4k")) {
            event.setCancelled(true);
        }
    }

    private void startSeekTimer() {
        // Give the seeker a sword
        ItemStack sword = new ItemStack(Material.IRON_SWORD);
        seeker.getInventory().addItem(sword);

        // Start the seek timer
        seekTimer.startTimer(20 * 60); // 20 minutes
    }
}