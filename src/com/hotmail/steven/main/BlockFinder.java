package com.hotmail.steven.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
import com.hotmail.steven.main.util.StringUtil;

public class BlockFinder extends JavaPlugin {

	// Item used to physically select blocks
	private static ItemStack selector;
	private static HashMap<Location, ItemStack> blocksList;
	private static BlockFinder plugin;
	private static HashMap<UUID, List<String>> found;
	
	@Override
	public void onEnable()
	{
		plugin = this;
		/**
		 * Create the selector
		 */
		selector = new ItemStack(Material.IRON_AXE);
		ItemMeta im = selector.getItemMeta();
		im.setDisplayName(StringUtil.color("&e&lClick to select"));
		im.setLore(Arrays.asList(new String[] {StringUtil.color("&7Left-click to add"), StringUtil.color("&7Right-click to remove")}));
		
		/**
		 * Register our events
		 */
		this.getServer().getPluginManager().registerEvents(new SelectorListener(), this);
		this.getServer().getPluginManager().registerEvents(new PlayerNearListener(), this);
		this.getServer().getPluginManager().registerEvents(new ItemListener(), this);
		this.getServer().getPluginManager().registerEvents(new BlockFindListener(), this);
		this.getCommand("blockfinder").setExecutor(new CommandListener());
		
		blocksList = new HashMap<Location, ItemStack>();
		found = new HashMap<UUID, List<String>>();
		
		/**
		 * Keep items alive at the location of each block find
		 */
		long interval = (20L * 60) * 3;
		new FindPlaceTask().runTaskTimer(this, interval, interval);
		new ParticleSpawner().runTaskTimer(this, 0L, 10L);
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
	 * Get all the block finds
	 * @return
	 */
	public static HashMap<Location, ItemStack> getBlockFinds()
	{
		return blocksList;
	}
	
	/**
	 * Add a block to be found at a specified location
	 * @param Loc - location to place the block
	 */
	public static void setBlockFind(String name, ItemStack item, Location loc)
	{
		item = item.clone();
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(name);
		item.setItemMeta(im);
		blocksList.put(loc.getBlock().getLocation(), item);
	}
	
	/**
	 * Check if there is a block to be found at a location
	 * @param loc
	 * @return
	 */
	public static boolean hasBlockFind(Location loc)
	{
		Location find = loc.getBlock().getLocation();
		return blocksList.containsKey(find);
	}
	
	/**
	 * Check if there is a block find with a specific name
	 * @param name
	 * @return
	 */
	public static boolean hasBlockFind(String name)
	{
		for(ItemStack im : blocksList.values())
		{
			if(im.getItemMeta().getDisplayName().equalsIgnoreCase(name)) return true;
		}
		
		return false;
	}
	
	/**
	 * Get the actual itemstack for a block to be found
	 * @param loc
	 * @return
	 */
	public static ItemStack getBlockFind(Location loc)
	{
		return blocksList.get(loc);
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
	
	/**
	 * Add a found block for a player
	 * @param uuid
	 * @param name
	 */
	public static void addFound(UUID uuid, String name)
	{
		List<String> alreadyFound = null;
		System.out.println("Addnig " + name);
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
	}
}
