// src/main/java/org/emrage/hideandSeek/Timer.java
package org.emrage.hideandSeek;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.TimeUnit;

public class Timer {

    private final Main plugin;
    private final NamespacedKey key = new NamespacedKey("spectator", "exclude");
    private int timerSeconds;
    private boolean isPaused;
    private double gradientOffset = 0.0;

    public Timer(Main plugin) {
        this.plugin = plugin;
        this.timerSeconds = 0;
        this.isPaused = true;
        startTasks();
    }

    public void startTimer(int seconds) {
        timerSeconds = seconds;
        isPaused = false;
    }

    public void pauseTimer() {
        isPaused = true;
    }

    public void resumeTimer() {
        isPaused = false;
    }

    public int getTimerSeconds() {
        return timerSeconds;
    }

    private void startTasks() {
        // Timer task
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!isPaused && timerSeconds > 0) {
                    timerSeconds--;
                }
                updateActionBar();
            }
        }.runTaskTimer(plugin, 0, 20);

        // Gradient animation task
        new BukkitRunnable() {
            @Override
            public void run() {
                gradientOffset += 0.05;
                if (gradientOffset > 1) {
                    gradientOffset -= 2;
                }
                updateActionBar();
            }
        }.runTaskTimer(plugin, 0, 1);
    }

    private void updateActionBar() {
        MiniMessage miniMessage = MiniMessage.miniMessage();
        if (isPaused || timerSeconds <= 0) {
            Component idleText = miniMessage.deserialize("<gold><italic>Idle</italic></gold>");
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (!player.getPersistentDataContainer().has(key, PersistentDataType.BYTE)) {
                    player.sendActionBar(idleText);
                }
            }
        } else {
            int days = (int) TimeUnit.SECONDS.toDays(timerSeconds);
            int hours = (int) (TimeUnit.SECONDS.toHours(timerSeconds) % 24);
            int minutes = (int) (TimeUnit.SECONDS.toMinutes(timerSeconds) % 60);
            int seconds = (int) (timerSeconds % 60);

            StringBuilder timeString = new StringBuilder();
            if (days > 0) {
                timeString.append(days).append("d, ");
            }
            if (hours > 0 || days > 0) {
                timeString.append(hours).append("h ");
            }
            if (minutes > 0 || hours > 0 || days > 0) {
                timeString.append(minutes).append("min ");
            }
            if (seconds > 0 || (days == 0 && hours == 0 && minutes == 0)) {
                timeString.append(seconds).append("s");
            }

            String gradientTag = "<gradient:#707CF7:#F658CF:" + gradientOffset + "><b>" + timeString.toString().trim() + "</b></gradient>";
            Component timerText = miniMessage.deserialize(gradientTag);

            for (Player player : Bukkit.getOnlinePlayers()) {
                if (!player.getPersistentDataContainer().has(key, PersistentDataType.BYTE)) {
                    player.sendActionBar(timerText);
                }
            }
        }
    }
}