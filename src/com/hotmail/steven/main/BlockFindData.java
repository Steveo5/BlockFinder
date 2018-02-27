package com.hotmail.steven.main;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

public class BlockFindData {

	private static BlockFinder plugin;
	private static File cfgFile;
	private static FileConfiguration cfg;
	
	public static void initialize(BlockFinder plugin)
	{
		BlockFindData.plugin = plugin;
		reload();
	}
	
	public static void reload()
	{
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
		
		if(!cfg.isConfigurationSection("spawns")) 
		{
			cfg.createSection("spawns");
			save();
		}
		
		BlockFinder.setFound(loadCollected());
		BlockFinder.setBlockFinds(getFinds());
		BlockFinder.setPossibleSpawns(getPossibleSpawns());
	}
	
	public static void save()
	{
		try {
			cfg.save(cfgFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Add a block find to a user and save to data store
	 * @param uuid
	 * @param name
	 */
	public static void addFind(UUID uuid, String name)
	{
		List<String> find = getCollected(uuid);
		find.add(name);
		cfg.set("collected" + uuid.toString(), find);
		save();
	}
	
	/**
	 * Remove a block find from a player
	 * @param uuid
	 * @param name
	 */
	public static void removeCollected(UUID uuid, String name)
	{
		List<String> finds = getCollected(uuid);
		finds.remove(name);
		cfg.set("collected." + uuid.toString(), finds);
		save();
	}
	
	public static List<String> getCollected(UUID uuid)
	{
		return cfg.getStringList(uuid.toString());
	}
	
	public static HashMap<UUID, List<String>> loadCollected()
	{
		HashMap<UUID, List<String>> finds = new HashMap<UUID, List<String>>();
		if(cfg.isConfigurationSection("collected"))
		{
			for(String strId : cfg.getConfigurationSection("collected").getKeys(false))
			{
				UUID uuid = UUID.fromString(strId);
				List<String> findIds = cfg.getStringList(strId);
				finds.put(uuid, findIds);
			}
		}
		
		return finds;
	}
	
	public static void addFind(BlockFind find)
	{
		ConfigurationSection finds = cfg.getConfigurationSection("finds");

		finds.set(find.getName() + ".item", find.getItemStack());
		
		save();
	}
	
	public static void removeFind(String name)
	{
		cfg.set("finds." + name, null);
		save();
	}
	
	public static LinkedList<BlockFind> getFinds()
	{
		LinkedList<BlockFind> finds = new LinkedList<BlockFind>();

		if(cfg.isConfigurationSection("finds"))
		{
            for (String findName : cfg.getConfigurationSection("finds").getKeys(false))
            {
                ConfigurationSection findSection = cfg.getConfigurationSection("finds." + findName);
                ItemStack item = findSection.getItemStack("item");
                finds.add(new BlockFind(findName, item));
            }
        }
		return new LinkedList<BlockFind>();
	}
	
	public static void addPossibleSpawn(Location loc)
	{
		List<String> spawns = cfg.getStringList("spawns");
		spawns.add(loc.getWorld().getUID().toString() + "," + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ());
		cfg.set("spawns", spawns);
		save();
	}

	public static void removePossibleSpawn(Location loc)
    {
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

    public static LinkedList<Location> getPossibleSpawns()
    {
        LinkedList<Location> spawns = new LinkedList<Location>();

        return spawns;
    }
}
