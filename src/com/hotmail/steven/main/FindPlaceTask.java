package com.hotmail.steven.main;

import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class FindPlaceTask extends BukkitRunnable {

	@Override
	public void run() {
		
		for(Entry<Location, ItemStack> entry : BlockFinder.getBlockFinds().entrySet())
		{
			if(entry.getKey().getChunk().isLoaded())
			{
				Location itemLoc = entry.getKey().getBlock().getLocation().add(0.5, 0.5, 0.5);
				Entity e = itemLoc.getWorld().dropItem(itemLoc, entry.getValue());
				e.setVelocity(e.getVelocity().zero());
			}
		}
		
	}

}
