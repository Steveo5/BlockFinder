package com.hotmail.steven.main;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

public class BlockFindSpawnTask extends BukkitRunnable {

	@Override
	public void run() {
		
		for(BlockFind find : BlockFinder.getBlockFinds())
		{
			if(!find.isSpawned()) find.spawn();

			if(!find.getItem().isOnGround())
			{
				Location itemLoc = find.getItem().getLocation().getBlock().getLocation().add(0.5, 0.5, 0.5);
				Entity e = itemLoc.getWorld().dropItem(itemLoc, find.getItemStack());
				e.setVelocity(e.getVelocity().zero());
				find.setItem(e);
			}
		}
		
	}

}
