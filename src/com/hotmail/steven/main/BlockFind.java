package com.hotmail.steven.main;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.hotmail.steven.main.util.StringUtil;

import me.badbones69.blockparticles.api.BlockParticles;
import me.badbones69.blockparticles.api.ParticleType;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class BlockFind {

	private String name;
	private ItemStack item;
	private Location spawnedLoc = null;
	private Entity spawnedEnt;
	private HashMap<UUID, BossBar> progressBars;
	
	public BlockFind(String name, ItemStack item)
	{
		this.name = name;
		this.item = item;
		item.setAmount(1);
		
		progressBars = new HashMap<UUID, BossBar>();
		//progressBar = Bukkit.createBossBar(StringUtil.color(BlockFinder.getBlockConfig().getString("near.message")), BarColor.PURPLE, BarStyle.SOLID);
	}
	
	public String getName()
	{
		return name;
	}
	
	public ItemStack getItemStack()
	{
		return item;
	}
	
	/**
	 * Selects one of the random spawn locations
	 * to spawn this block find
	 * @return
	 */
	public void spawn()
	{
		List<Location> possibleSpawns = BlockFinder.getPossibleSpawns();
		int random = new Random().nextInt(possibleSpawns.size());
		if(random < 0) return;
		spawnedLoc = possibleSpawns.get(random);
		
		// Set the block to find
		Location itemLoc = spawnedLoc.getBlock().getLocation().add(0.5, 0.5, 0.5);
		Entity e = itemLoc.getWorld().dropItem(itemLoc, getItemStack());
		e.setVelocity(e.getVelocity().zero());
		spawnedEnt = e;
		
		
	}
	
	/**
	 * Create and start displaying a progress bar for a player.
	 * Shows a percentage based on distance to this block find,
	 * thus this method shouldn't be called on far distances due
	 * to calculations in square root
	 * @param p
	 */
	public void showProgressBar(Player p)
	{
		BossBar bar = Bukkit.createBossBar(StringUtil.color(BlockFinder.getBlockConfig().getString("near.message")), BarColor.PURPLE, BarStyle.SOLID);
		bar.addPlayer(p);
		progressBars.put(p.getUniqueId(), bar);
	}
	
	/**
	 * Hides an active progress bar for a player
	 * @param p
	 */
	public void hideProgressBar(Player p)
	{
		if(progressBars.containsKey(p.getUniqueId()))
		{
			BossBar bar = progressBars.get(p.getUniqueId());
			bar.removeAll();
		}
	}
	
	/**
	 * Updates the progress of the player depending how close
	 * they are to the centre of this block find. Should NOT be
	 * called over a long distance due to square root calculations
	 * 
	 * Will throw a NPE if this BlockFind isn't spawned
	 * @param p
	 */
	public void updateProgressBar(Player p)
	{
		double distance = p.getLocation().distance(spawnedLoc);
	}
	
	/**
	 * Check if a player is within a certain distance of this block find
	 * @param p
	 * @param distance
	 * @return
	 */
	public boolean isNear(Location loc, int radius)
	{
		int pX = loc.getBlockX();
		int pY = loc.getBlockY();
		int pZ = loc.getBlockZ();
		
		int x = spawnedLoc.getBlockX();
		int y = spawnedLoc.getBlockY();
		int z = spawnedLoc.getBlockZ();
		// Send message to play if they're near
		if(pX > x - radius && pX < x + radius)
		{
			if(pY > y - radius && pY < y + radius)
			{
				if(pZ > z - radius && pZ < z + radius)
				{
					//p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(StringUtil.color(BlockFinder.getBlockConfig().getString("near.message"))));
					return true;
				}
			}
		}
		
		return false;
	}
	
	public void setLocation(Location loc)
	{
		this.spawnedLoc = loc;
	}
	
	public Location getLocation()
	{
		return spawnedLoc;
	}
	
	public boolean isSpawned()
	{
		return spawnedLoc != null;
	}
	
	public Entity getItem()
	{
		return spawnedEnt;
	}
	
}
