// src/main/java/org/emrage/hideandSeek/ServerListener.java
package org.emrage.hideandSeek;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class ServerListener implements Listener {

    private final GameManager gameManager;

    public ServerListener(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        gameManager.handleEntityDamageByEntity(event);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        gameManager.handleBlockBreak(event);
    }
}