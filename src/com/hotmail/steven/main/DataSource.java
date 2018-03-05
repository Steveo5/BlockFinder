package com.hotmail.steven.main;

import org.bukkit.Location;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public abstract class DataSource {

    protected BlockFinder plugin;

    public DataSource(BlockFinder plugin)
    {
        this.plugin = plugin;

        reload();
    }

    public abstract void reload();
    public abstract void save();
    public abstract void addFind(UUID uuid, String name);
    public abstract void removeCollected(UUID uuid, String name);
    public abstract List<String> getCollected(UUID uuid);
    public abstract HashMap<UUID, List<String>> loadCollected();
    public abstract void addFind(BlockFind find);
    public abstract void removeFind(String name);
    public abstract LinkedList<BlockFind> loadFinds();
    public abstract void addPossibleSpawn(Location loc);
    public abstract void removePossibleSpawn(Location loc);
    public abstract LinkedList<Location> loadPossibleSpawns();

}
