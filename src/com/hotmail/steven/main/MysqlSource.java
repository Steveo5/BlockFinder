package com.hotmail.steven.main;

import org.bukkit.Location;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class MysqlSource extends DataSource {


    public MysqlSource(BlockFinder plugin) {
        super(plugin);
    }

    @Override
    public void reload() {

    }

    @Override
    public void save() {

    }

    @Override
    public void addFind(UUID uuid, String name) {

    }

    @Override
    public void removeCollected(UUID uuid, String name) {

    }

    @Override
    public List<String> getCollected(UUID uuid) {
        return null;
    }

    @Override
    public HashMap<UUID, List<String>> loadCollected() {
        return null;
    }

    @Override
    public void addFind(BlockFind find) {

    }

    @Override
    public void removeFind(String name) {

    }

    @Override
    public LinkedList<BlockFind> loadFinds() {
        return null;
    }

    @Override
    public void addPossibleSpawn(Location loc) {

    }

    @Override
    public void removePossibleSpawn(Location loc) {

    }

    @Override
    public LinkedList<Location> loadPossibleSpawns() {
        return null;
    }
}
