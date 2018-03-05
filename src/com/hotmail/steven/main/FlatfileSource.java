package com.hotmail.steven.main;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class FlatfileSource extends DataSource {

    private File cfgFile;
    private YamlConfiguration cfg;

    public FlatfileSource(BlockFinder plugin) {
        super(plugin);

    }

    @Override
    public void reload() {
        cfgFile = new File(plugin.getDataFolder() + File.separator + "finds.yml");

        if(!cfgFile.exists())
        {
            try {
                cfgFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        cfg = YamlConfiguration.loadConfiguration(cfgFile);

        if(!cfg.isConfigurationSection("collected"))
        {
            cfg.createSection("collected");
            save();
        }

        if(!cfg.isConfigurationSection("finds"))
        {
            cfg.createSection("finds");
            save();
        }

        if(!cfg.isList("spawns"))
        {
            cfg.set("spawns", new ArrayList<String>());
            save();
        }

        loadCollected();
        loadFinds();
        loadPossibleSpawns();
    }

    @Override
    public void save() {
        try {
            cfg.save(cfgFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addFind(UUID uuid, String name) {
        List<String> find = getCollected(uuid);
        find.add(name);
        cfg.set("collected." + uuid.toString(), find);
        save();
    }

    @Override
    public void removeCollected(UUID uuid, String name) {
        List<String> finds = getCollected(uuid);
        finds.remove(name);
        cfg.set("collected." + uuid.toString(), finds);
        save();
    }

    @Override
    public List<String> getCollected(UUID uuid) {
        return cfg.getStringList("collected." + uuid.toString());
    }

    @Override
    public HashMap<UUID, List<String>> loadCollected() {
        HashMap<UUID, List<String>> finds = new HashMap<UUID, List<String>>();
        System.out.println("Loading collected");
        if(cfg.isConfigurationSection("collected"))
        {
            for(String strId : cfg.getConfigurationSection("collected").getKeys(false))
            {
                UUID uuid = UUID.fromString(strId);
                List<String> findIds = cfg.getStringList("collected." + strId);
                finds.put(uuid, findIds);
                System.out.println("Loaded " + finds.toString());
            }
        }

        BlockFinder.setFound(finds);
        return finds;
    }

    @Override
    public void addFind(BlockFind find) {
        ConfigurationSection finds = cfg.getConfigurationSection("finds");

        finds.set(find.getName() + ".item", find.getItemStack());

        save();
    }

    @Override
    public void removeFind(String name) {
        cfg.set("finds." + name, null);
        save();
    }

    @Override
    public LinkedList<BlockFind> loadFinds() {
        LinkedList<BlockFind> finds = new LinkedList<BlockFind>();
        System.out.println("Getting dins");
        if(cfg.isConfigurationSection("finds"))
        {
            for (String findName : cfg.getConfigurationSection("finds").getKeys(false))
            {
                System.out.println("Getting find " + findName);
                ConfigurationSection findSection = cfg.getConfigurationSection("finds." + findName);
                ItemStack item = findSection.getItemStack("item");
                finds.add(new BlockFind(findName, item));
            }
        }
        BlockFinder.setBlockFinds(finds);
        return finds;
    }

    @Override
    public void addPossibleSpawn(Location loc) {
        List<String> spawns = cfg.getStringList("spawns");
        spawns.add(loc.getWorld().getUID().toString() + "," + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ());
        cfg.set("spawns", spawns);
        save();
    }

    @Override
    public void removePossibleSpawn(Location loc) {
        List<String> spawns = cfg.getStringList("spawns");
        Iterator<String> spawnItr = spawns.iterator();

        while(spawnItr.hasNext())
        {
            String next = spawnItr.next();
            String[] locData = next.split(",");
            World w = Bukkit.getWorld(UUID.fromString(locData[0]));
            int x = Integer.valueOf(locData[1]);
            int y = Integer.valueOf(locData[2]);
            int z = Integer.valueOf(locData[3]);

            if(w.getUID().equals(loc.getWorld().getUID()) && loc.getBlockX() == x && loc.getBlockY() == y && loc.getBlockZ() == z)
            {
                spawnItr.remove();
            }
        }

        cfg.set("spawns", spawns);
        save();
    }

    @Override
    public LinkedList<Location> loadPossibleSpawns() {
        LinkedList<Location> spawns = new LinkedList<Location>();
        System.out.println("Getting spawns");
        for(String spawnStr : cfg.getStringList("spawns"))
        {
            String[] spawnData = spawnStr.split(",");
            World w = Bukkit.getWorld(UUID.fromString(spawnData[0]));
            int x = Integer.valueOf(spawnData[1]);
            int y = Integer.valueOf(spawnData[2]);
            int z = Integer.valueOf(spawnData[3]);

            Location loc = new Location(w, x, y, z);
            System.out.println(loc.toString());
            spawns.add(loc);
        }

        System.out.println("Returning " + spawns.size());
        BlockFinder.setPossibleSpawns(spawns);
        return spawns;
    }
}
