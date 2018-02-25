package com.hotmail.steven.main;

import org.bukkit.scheduler.BukkitRunnable;

public class BlockFindSpawnTask extends BukkitRunnable {

	@Override
	public void run() {
		
		for(BlockFind find : BlockFinder.getBlockFinds())
		{
			if(!find.isSpawned()) find.spawn();
		}
		
	}

}
