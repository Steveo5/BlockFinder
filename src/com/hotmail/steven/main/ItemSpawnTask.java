package com.hotmail.steven.main;

import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class ItemSpawnTask extends BukkitRunnable {

	@Override
	public void run() {
		
		for(BlockFind find : BlockFinder.getBlockFinds())
		{
			if(find.getLocation().getChunk().isLoaded())
			{
				if(find.isSpawned())
				{
					find.getItem().remove();
				}
			}
		}
		
	}

}
