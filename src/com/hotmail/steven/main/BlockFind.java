package com.hotmail.steven.main;

import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import me.badbones69.blockparticles.api.BlockParticles;
import me.badbones69.blockparticles.api.ParticleType;

public class BlockFind {

	private String name;
	private ItemStack item;
	private Location spawnedLoc = null;
	private Entity spawnedEnt;
	
	public BlockFind(String name, ItemStack item)
	{
		this.name = name;
		this.item = item;
		item.setAmount(1);
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
