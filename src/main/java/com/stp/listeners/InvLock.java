package com.stp.listeners;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class InvLock implements Listener {

    private final Set<String> lockedTitles = new HashSet<>();

    public InvLock(String... titles) {
        for (String title : titles) {
            lockedTitles.add(title.replace("&", "").replace("§", "").toLowerCase());
        }
    }

    public void addLockedTitle(String title) {
        lockedTitles.add(title.replace("&", "").replace("§", "").toLowerCase());
    }

    public void removeLockedTitle(String title) {
        lockedTitles.remove(title.replace("&", "").replace("§", "").toLowerCase());
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Inventory inv = e.getInventory();
        String invTitle = inv.getTitle().replace("&", "").replace("§", "").toLowerCase();
        for (String locked : lockedTitles) {
            if (invTitle.contains(locked)) {
                e.setCancelled(true);
                break;
            }
        }
    }
}