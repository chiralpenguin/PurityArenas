package com.purityvanilla.purityarenas.listeners;

import com.purityvanilla.purityarenas.gui.GUIWindow;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryHolder;

public class InventoryCloseListener implements Listener {

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        InventoryHolder holder = e.getInventory().getHolder();
        if (holder instanceof GUIWindow) {
            ((GUIWindow) holder).onClose(e);
        }
    }
}

