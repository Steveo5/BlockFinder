package com.hotmail.steven.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

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
		
		BlockFinder.setFound(loadFinds());
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
		List<String> find = getFinds(uuid);
		find.add(name);
		cfg.set(uuid.toString(), find);
		save();
	}
	
	/**
	 * Remove a block find from a player
	 * @param uuid
	 * @param name
	 */
	public static void removeFind(UUID uuid, String name)
	{
		List<String> finds = getFinds(uuid);
		finds.remove(name);
		cfg.set(uuid.toString(), finds);
		save();
	}
	
	public static List<String> getFinds(UUID uuid)
	{
		return cfg.getStringList(uuid.toString());
	}
	
	public static HashMap<UUID, List<String>> loadFinds()
	{
		HashMap<UUID, List<String>> finds = new HashMap<UUID, List<String>>();
		
		for(String strId : cfg.getKeys(false))
		{
			UUID uuid = UUID.fromString(strId);
			List<String> findIds = cfg.getStringList(strId);
			finds.put(uuid, findIds);
		}
		
		return finds;
	}
}
