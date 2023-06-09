package com.purityvanilla.purityarenas;

import it.unimi.dsi.fastutil.Hash;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Config {
    private final YamlConfigurationLoader configLoader;
    private final String FNAME = "plugins/PurityArenas/config.yml";

    private HashMap<String, CommentedConfigurationNode> arenas;
    private HashMap<String, String> messageMap;
    private String spawnWorldKey;
    private int checkArenaInterval;
    private int customVoidHeight;
    private List<String> customVoidWorlds;
    private int duellingCountdown;
    private Boolean verbose;

    public Config() {
        File file = new File(FNAME);
        file.getParentFile().mkdirs();

        if (!file.exists()) {
            try {
                InputStream configStream = getClass().getResourceAsStream("/config.yml");
                Files.copy(configStream, Paths.get(file.toURI()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        configLoader = YamlConfigurationLoader.builder().path(Path.of(FNAME)).build();
        CommentedConfigurationNode root = null;

        try {
            root = configLoader.load();
        } catch (ConfigurateException e) {
            e.printStackTrace();
        }

        this.arenas = new HashMap<>();
        Map<Object, CommentedConfigurationNode> arenaNodes = root.node("arenas").childrenMap();
        for (Map.Entry<Object, CommentedConfigurationNode> arena : arenaNodes.entrySet()) {
            arenas.put(arena.getKey().toString(), arena.getValue());
        }

        this.messageMap = new HashMap<>();
        Map<Object, CommentedConfigurationNode> messages = root.node("messages").childrenMap();
        for (Map.Entry<Object, CommentedConfigurationNode> message : messages.entrySet()) {
            messageMap.put(message.getKey().toString(), message.getValue().getString());
        }

        this.spawnWorldKey = root.node("spawn-world").getString();
        this.checkArenaInterval = root.node("check-arena-interval").getInt();
        this.customVoidHeight = root.node("custom-void-height").getInt();
        try {
            this.customVoidWorlds = root.node("custom-void-worlds").getList(String.class);
        } catch (SerializationException e) {
            e.printStackTrace();
        }
        this.duellingCountdown = root.node("duelling-countdown").getInt();
        this.verbose = root.node("verbose").getBoolean();
    }

    public HashMap<String, CommentedConfigurationNode> getArenas() {
        return arenas;
    }

    public String ArenaFullMessage() {
        return messageMap.get("arena-full");
    }

    public String getSpawnWorldKey() {
        return spawnWorldKey;
    }

    public int getCheckArenaInterval() {
        return checkArenaInterval;
    }

    public int getCustomVoidHeight() {
        return customVoidHeight;
    }

    public List<String> getCustomVoidWorlds() {
        return customVoidWorlds;
    }

    public int getDuellingCountdown() {
        return duellingCountdown;
    }

    public Boolean verbose() {
        return verbose;
    }
}
