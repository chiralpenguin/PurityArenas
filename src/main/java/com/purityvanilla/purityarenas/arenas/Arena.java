package com.purityvanilla.purityarenas.arenas;

import com.purityvanilla.purityarenas.PurityArenas;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.entity.Entity;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.internal.platform.WorldGuardPlatform;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.util.WorldEditRegionConverter;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.spongepowered.configurate.CommentedConfigurationNode;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class Arena {
    protected String arenaName;
    protected World world;
    protected ProtectedRegion region;
    protected int maxPlayers;
    protected boolean resetEnabled;
    protected String schematicName;

    protected List<UUID> players;

    public Arena(String name,  CommentedConfigurationNode node) {
        this.arenaName = name;
        this.world = PurityArenas.INSTANCE.getServer().getWorld(node.node("world-key").getString());
        this.maxPlayers = node.node("max-players").getInt();
        this.resetEnabled = node.node("reset-enabled").getBoolean();
        this.schematicName = node.node("schematic-name").getString();

        String regionKey = node.node("region-key").getString();
        ProtectedRegion region = getRegionFromID(regionKey);
        if (region == null) {
            PurityArenas.Logger().info(String.format("'%s' is an invalid region key! Check your config.yml", regionKey));
        } else {
            if (PurityArenas.Config().verbose()) PurityArenas.Logger().info(String.format("Initialised arena for region: %s", regionKey));
            this.region = region;
        }

        this.players = new ArrayList<>();

    }

    public void unload() {
        this.players = null;
    }

    public void ResetArena() {
        TeleportPlayersForReset();

        Clipboard clipboard = null;
        try {
            File file = new File("plugins/FastAsyncWorldEdit/schematics/" + schematicName + ".schem");
            ClipboardReader reader = ClipboardFormats.findByFile(file).getReader(new FileInputStream(file));
            clipboard = reader.read();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (EditSession editSession = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(this.world))) {
            BlockVector3 minBlock = region.getMinimumPoint();
            BlockVector3 maxBlock = region.getMaximumPoint();

            BlockVector3 center = BlockVector3.at(
                    minBlock.getX() + Math.floorDiv(maxBlock.getX() - minBlock.getX(), 2),
                    minBlock.getY() + Math.floorDiv(maxBlock.getY() - minBlock.getY(), 2),
                    minBlock.getZ() + Math.floorDiv(maxBlock.getZ() - minBlock.getZ(), 2)
            );

            Operation operation = new ClipboardHolder(clipboard)
                    .createPaste(editSession)
                    .to(center)
                    .build();
            Operations.complete(operation);
        } catch (WorldEditException e) {
            e.printStackTrace();
        }

        // TODO Kill all entities in arena at reset
        BukkitAdapter.adapt(this.world).getEntities(WorldEditRegionConverter.convertToRegion(region)).forEach(Entity::remove);
    }

    public void TeleportPlayersForReset() {
        World spawnWorld = PurityArenas.INSTANCE.getServer().getWorld(PurityArenas.Config().getSpawnWorldKey());

        for (UUID playerID : players) {
            PurityArenas.INSTANCE.getServer().getPlayer(playerID).teleport(spawnWorld.getSpawnLocation());
        }
    }

    public void TeleportPlayer(Player player) {

    }

    public void JoinArena(Player player) {

        if (isFull()) {
            player.sendMessage(LegacyComponentSerializer.legacyAmpersand().deserialize(PurityArenas.Config().ArenaFullMessage()));
            return;
        }

        if (players.contains(player.getUniqueId())) {
            return;
        }

        addPlayer(player);
        TeleportPlayer(player);
        if (PurityArenas.Config().verbose()) PurityArenas.Logger().info(String.format("Player %s joined %s", player.getName(), arenaName));
    }

    public void LeaveArena(Player player) {
        removePlayer(player);
        if (PurityArenas.Config().verbose()) PurityArenas.Logger().info(String.format("Player %s left %s", player.getName(), arenaName));
    }

    public ProtectedRegion getRegionFromID(String regionID) {
        WorldGuardPlatform wg = WorldGuard.getInstance().getPlatform();

        return wg.getRegionContainer().get(BukkitAdapter.adapt(this.world)).getRegion(regionID);
    }

    public String getArenaName() {
        return arenaName;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public boolean isFull() {
        return players.size() >= maxPlayers;
    }

    public boolean isResetEnabled() {
        return resetEnabled;
    }

    public ProtectedRegion getRegion() {
        return region;
    }

    public List<Player> getPlayers() {
        List<Player> players = new ArrayList<>();

        for (UUID uuid : this.players) {
            players.add(PurityArenas.INSTANCE.getServer().getPlayer(uuid));
        }
        return players;
    }

    public boolean isPlayerInArena(Player player) {
        return players.contains(player.getUniqueId());
    }

    public void addPlayer(UUID player) {
        players.add(player);
    }

    public void addPlayer(Player player) {
        addPlayer(player.getUniqueId());
    }

    public void removePlayer(UUID player) {
        players.remove(player);
    }

    public void removePlayer(Player player) {
        removePlayer(player.getUniqueId());
    }

    public enum TYPE {
        FFA,
        DUEL
    }
}
