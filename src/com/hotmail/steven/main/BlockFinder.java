package com.hotmail.steven.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import com.hotmail.steven.listener.BlockFindListener;
import com.hotmail.steven.listener.CommandListener;
import com.hotmail.steven.listener.ItemListener;
import com.hotmail.steven.listener.PlayerNearListener;
import com.hotmail.steven.listener.SelectorListener;
import com.hotmail.steven.main.util.StringUtil;

public class BlockFinder extends JavaPlugin {

	// Item used to physically select blocks
	private static ItemStack selector;
	private static HashMap<Location, ItemStack> blocksList;
	private static BlockFinder plugin;
	private static HashMap<UUID, List<String>> found;
	
	private static LinkedList<Location> possibleSpawns;
	private static LinkedList<BlockFind> blockList;

	private static DataSource dataSource;

	@Override
	public void onEnable()
	{
		plugin = this;
		this.saveDefaultConfig();
		loadSettings();
		/**
		 * Create the selector
		 */
		selector = new ItemStack(Material.IRON_AXE);
		ItemMeta im = selector.getItemMeta();
		im.setDisplayName(StringUtil.color("&e&lClick to select"));
		im.setLore(Arrays.asList(new String[] {StringUtil.color("&7Left-click to add"), StringUtil.color("&7Right-click to remove")}));
		selector.setItemMeta(im);
		/**
		 * Register our events
		 */
		this.getServer().getPluginManager().registerEvents(new SelectorListener(), this);
		this.getServer().getPluginManager().registerEvents(new PlayerNearListener(), this);
		this.getServer().getPluginManager().registerEvents(new ItemListener(), this);
		this.getServer().getPluginManager().registerEvents(new BlockFindListener(), this);
		this.getCommand("blockfinder").setExecutor(new CommandListener(this));
		
		blocksList = new HashMap<Location, ItemStack>();
		found = new HashMap<UUID, List<String>>();
		possibleSpawns = new LinkedList<Location>();
		blockList = new LinkedList<BlockFind>();

		if(Settings.MYSQL_ENABLED.getBoolean())
		{
			dataSource = new MysqlSource(this);
		} else
		{
			dataSource = new FlatfileSource(this);
		}
		
		/**
		 * Keep items alive at the location of each block find
		 */
		long interval = (20L * 30);
		new ItemSpawnTask().runTaskTimer(this, interval, interval);
		new ParticleSpawner().runTaskTimer(this, 0L, 10L);
		new BlockFindSpawnTask().runTaskTimer(this, 0L, 20L);
	}

	/**
	 * Load the settings from config file, can be also used to reload
	 * the values from config (also reloads config)
	 */
	public void loadSettings()
	{
		this.reloadConfig();
		for(Settings setting : Settings.values())
		{
			setting.setValue(getConfig().get(setting.getNode()));
		}
	}

	/**
	 * Gets the main storage component
	 * @return
	 */
	public static DataSource getDataSource()
	{
		return dataSource;
	}
	
	/**
	 * Get the physical block selector
	 * @return
	 */
	public static ItemStack getSelector()
	{
		return selector;
	}
	
	/**
	 * Gets whether an item is the block selector tool
	 * @param item
	 * @return
	 */
	public static boolean hasSelector(ItemStack item)
	{
		if(item.hasItemMeta() && item.getItemMeta().hasDisplayName())
		{
			return item.getItemMeta().getDisplayName().equals(selector.getItemMeta().getDisplayName());
		}
		
		return false;
	}
	
	/**
	 * Add a block find that will get automatically spawned
	 * at a possible block find location
	 * @param find
	 */
	public static void addBlockFind(BlockFind find)
	{
		blockList.add(find);
		getDataSource().addFind(find);
	}
	
	public static boolean hasBlockFind(String name)
	{
		for(BlockFind find : blockList)
		{
			if(find.getName().equalsIgnoreCase(name))
			{
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Used internally when config data is loaded
	 * @param finds
	 */
	protected static void setBlockFinds(LinkedList<BlockFind> finds)
	{
		BlockFinder.blockList = finds;

		System.out.println("Finds " + blockList.size());
	}
	
	/**
	 * Remove a block find
	 * @param name
	 */
	public static void removeBlockFind(String name)
	{
		for(BlockFind find : blockList)
		{
			if(find.getName().equalsIgnoreCase(name))
			{
				blockList.remove(find);
				getDataSource().removeFind(name);
				return;
			}
		}
	}
	
	public static List<BlockFind> getBlockFinds()
	{

		return blockList;
	}
	
	/**
	 * Get the file configuration
	 * @return
	 */
	public static FileConfiguration getBlockConfig()
	{
		return plugin.getConfig();
	}
	
	/**
	 * Get all the found blocks for a player
	 * @param uuid
	 * @return
	 */
	public static List<String> getFound(UUID uuid)
	{
		if(found.containsKey(uuid)) 
		{
			return found.get(uuid);
		}
		
		return new ArrayList<String>();
	}
	
	/**
	 * Get all the blocks found for a player
	 * @param player
	 * @return
	 */
	public static List<String> getFound(Player player)
	{
		return getFound(player.getUniqueId());
	}
	
	// Internally sets the list of block finds. Used when BlockFindData.reload() is called
	static void setFound(HashMap<UUID, List<String>> found)
	{

		BlockFinder.found = found;
		System.out.println("Found " + found.size());
	}
	
	/**
	 * Add a found block for a player
	 * @param uuid
	 * @param name
	 */
	public static void addFound(UUID uuid, String name)
	{
		List<String> alreadyFound = null;
		if(found.containsKey(uuid))
		{
			alreadyFound = found.get(uuid);
			alreadyFound.add(name);
			found.put(uuid, alreadyFound);
		} else
		{
			alreadyFound = new ArrayList<String>();
			alreadyFound.add(name);
			found.put(uuid, alreadyFound);
		}
		
		getDataSource().addFind(uuid, name);
	}
	
	public static void removeFound(UUID uuid, String name)
	{
		found.remove(uuid);
		
		
		getDataSource().removeCollected(uuid, name);
	}
	
	/**
	 * Add a possible spawn location
	 * @param loc
	 */
	public static void addPossibleSpawn(Location loc)
	{

		possibleSpawns.add(loc);
		getDataSource().addPossibleSpawn(loc);
	}
	
	/**
	 * Get locations where spawns are a possibility that
	 * aren't taken by other block finds
	 * @return
	 */
	public static List<Location> getPossibleSpawns()
	{
		List<Location> possible = new ArrayList<Location>();
		// Loop all block finds
		Outer:
		for(Location loc : getAllPossibleSpawns())
		{
			for(BlockFind find : blockList)
			{
				if(find.isSpawned() && loc.equals(find.getLocation()))
				{
					continue Outer;
				}
			}
			possible.add(loc);
		}
		return possible;
	}
	
	/**
	 * Gets all the locations including ones where
	 * a block find is already spawned
	 * @return
	 */
	public static List<Location> getAllPossibleSpawns()
	{
		return possibleSpawns;
	}
	
	/**
	 * Remove a possible spawn location
	 * @param loc
	 * @return
	 */
	public static boolean removePossibleSpawn(Location loc)
	{
		if(possibleSpawns.contains(loc))
		{
			possibleSpawns.remove(loc);
			getDataSource().removePossibleSpawn(loc);
			return true;
		}
		
		return false;
	}

	protected static void setPossibleSpawns(LinkedList<Location> spawns)
	{
		BlockFinder.possibleSpawns = spawns;
		System.out.println("Setting spawns to " + possibleSpawns.size());
	}
}
