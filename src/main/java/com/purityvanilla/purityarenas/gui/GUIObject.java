package com.purityvanilla.purityarenas.gui;

import com.purityvanilla.purityarenas.PurityArenas;
import com.purityvanilla.purityarenas.arenas.Arena;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class GUIObject {
    private String id;
    private String name;
    private String materialType;
    private int amount;
    private List<String> lore;

    public static HashMap<String, GUIObject> guiObjectMap;

    public GUIObject(String name, String materialType, int amount, List<String> lore, String id) {
        this.id = id;
        this.name = name;
        this.materialType = materialType;
        this.amount = amount;
        this.lore = lore;
    }

    public ItemStack createItem() {
        Material material = Material.getMaterial(materialType);
        ItemStack itemStack = new ItemStack(material, amount);

        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(Component.empty().decoration(TextDecoration.ITALIC, false).append(
                LegacyComponentSerializer.legacyAmpersand().deserialize(name)));
        List<Component> lore = new ArrayList<>();
        for (String loreLine : this.lore) {
            lore.add(Component.empty().decoration(TextDecoration.ITALIC, false).append(
                    LegacyComponentSerializer.legacyAmpersand().deserialize(loreLine)));
        }
        meta.lore(lore);

        NamespacedKey pdcKey = new NamespacedKey(PurityArenas.INSTANCE, "gui-object");
        meta.getPersistentDataContainer().set(pdcKey, PersistentDataType.STRING, id);

        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public static void InitGuiObjectMap() {
        HashMap<String, GUIObject> guiObjects = new HashMap<>();

        guiObjects.put("Arena_empty", new GUIObject("&f&l{arenaname}", "IRON_BLOCK", 1,
                new ArrayList<>(Arrays.asList("&ePlayers in arena: {players}/{maxplayers}", "&9Command: /arena {arenaname}")), "Arena_empty"));

        guiObjects.put("Arena_active", new GUIObject("&a&l{arenaname}", "EMERALD_BLOCK", 1,
                new ArrayList<>(Arrays.asList("&ePlayers in arena: {players}/{maxplayers}", "&9Command: /arena {arenaname}")), "Arena_empty"));

        guiObjects.put("Arena_full", new GUIObject("&c&l{arenaname}", "REDSTONE_BLOCK", 1,
                new ArrayList<>(Arrays.asList("&ePlayers in arena: {players}/{maxplayers}", "&9Command: /arena {arenaname}")), "Arena_full"));

        guiObjectMap = guiObjects;
    }

    public static ItemStack SetAssociatedArena(ItemStack itemStack, Arena arena) {
        ItemMeta meta = itemStack.getItemMeta();

        String displayName = LegacyComponentSerializer.legacyAmpersand().serialize(meta.displayName());
        displayName = displayName.replace("{arenaname}", arena.getArenaName());
        meta.displayName(Component.empty().decoration(TextDecoration.ITALIC, false).append(
                LegacyComponentSerializer.legacyAmpersand().deserialize(displayName)));

        List<Component> lore = new ArrayList<>();
        for (Component loreLine : meta.lore()) {
            String loreString = LegacyComponentSerializer.legacyAmpersand().serialize(loreLine);
            loreString = loreString.replace("{players}", Integer.toString(arena.getPlayers().size()));
            loreString = loreString.replace("{arenaname}", arena.getArenaName());
            loreString = loreString.replace("{maxplayers}", Integer.toString(arena.getMaxPlayers()));
            lore.add(Component.empty().decoration(TextDecoration.ITALIC, false).append(
                    LegacyComponentSerializer.legacyAmpersand().deserialize(loreString)));
        }
        meta.lore(lore);

        NamespacedKey pdcKey = new NamespacedKey(PurityArenas.INSTANCE, "arena");
        meta.getPersistentDataContainer().set(pdcKey, PersistentDataType.STRING, arena.getArenaName());

        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public static Arena GetAssociatedArena(ItemStack itemStack) {
        NamespacedKey pdcKey = new NamespacedKey(PurityArenas.INSTANCE, "arena");
        String arenaName = itemStack.getItemMeta().getPersistentDataContainer().get(pdcKey, PersistentDataType.STRING);

        return PurityArenas.getArenaManager().getArenaMap().get(arenaName.toLowerCase());

    }

    public static String GetObjectID(ItemStack item) {
        NamespacedKey pdcKey = new NamespacedKey(PurityArenas.INSTANCE, "gui-object");

        ItemMeta meta = item.getItemMeta();
        return meta.getPersistentDataContainer().get(pdcKey, PersistentDataType.STRING);
    }
}
