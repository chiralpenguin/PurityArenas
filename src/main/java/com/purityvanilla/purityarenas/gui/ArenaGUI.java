package com.purityvanilla.purityarenas.gui;

import com.purityvanilla.purityarenas.PurityArenas;
import com.purityvanilla.purityarenas.arenas.Arena;
import com.purityvanilla.purityarenas.arenas.ArenaFFA;
import com.purityvanilla.purityarenas.util.BannerHelper;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ArenaGUI extends GUIWindow {

    public ArenaGUI(Player player) {
        invTitle = "&c&l&nPurity Arenas";
        createInventory(54);

        ListFFAArenas();
    }

    public void ListFFAArenas() {
        List<Arena> ffaArenas = new ArrayList<>();
        for (Arena arena : PurityArenas.getArenaManager().getArenaMap().values()) {
            if (arena instanceof ArenaFFA) {
                ffaArenas.add(arena);
            }
        }

        HashMap<String, GUIObject> guiObjects = GUIObject.guiObjectMap;
        for (int j = 0; j < 2; j ++) {
            for (int i = 0; i < 7; i ++) {
                int slot = i + j * 9 + 10;
                int arenaIndex = i + j * 6;

                if (ffaArenas.size() < arenaIndex + 1) {
                    continue;
                }

                Arena arena = ffaArenas.get(arenaIndex);
                ItemStack arenaItem;

                if (arena.getPlayers().size() == 0) {
                    arenaItem = guiObjects.get("Arena_empty").createItem();
                }
                else if (arena.getPlayers().size() == arena.getMaxPlayers()) {
                    arenaItem = guiObjects.get("Arena_full").createItem();
                } else {
                    arenaItem = guiObjects.get("Arena_active").createItem();
                }

                arenaItem = GUIObject.SetAssociatedArena(arenaItem, arena);
                inventory.setItem(slot, arenaItem);

            }
        }


        for (int i = 3; i < 5; i ++) {
            inventory.setItem(i, BannerHelper.LetterBanner('F', Material.RED_BANNER, DyeColor.RED, DyeColor.WHITE));
        }
        inventory.setItem(5, BannerHelper.LetterBanner('A', Material.RED_BANNER, DyeColor.RED, DyeColor.WHITE));

        inventory.setItem(29, BannerHelper.LetterBanner('O', Material.BLUE_BANNER, DyeColor.BLUE, DyeColor.WHITE));
        inventory.setItem(30, BannerHelper.LetterBanner('T', Material.BLUE_BANNER, DyeColor.BLUE, DyeColor.WHITE));
        inventory.setItem(31, BannerHelper.LetterBanner('H', Material.BLUE_BANNER, DyeColor.BLUE, DyeColor.WHITE));
        inventory.setItem(32, BannerHelper.LetterBanner('E', Material.BLUE_BANNER, DyeColor.BLUE, DyeColor.WHITE));
        inventory.setItem(33, BannerHelper.LetterBanner('R', Material.BLUE_BANNER, DyeColor.BLUE, DyeColor.WHITE));

    }

    @Override
    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);

        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null) {
            return;
        }

        String objectId = GUIObject.GetObjectID(clickedItem);
        if (objectId == null) {
            return;
        }

        Player player = (Player) event.getWhoClicked();

        if (objectId.equals("Arena_empty") || objectId.equals("Arena_active") || objectId.equals("Arena_full")) {
            Arena arena = GUIObject.GetAssociatedArena(event.getCurrentItem());

            arena.JoinArena(player);
        }

    }

}
