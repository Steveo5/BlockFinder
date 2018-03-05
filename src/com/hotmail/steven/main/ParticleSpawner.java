package com.hotmail.steven.main;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ParticleSpawner extends BukkitRunnable {

	@Override
	public void run() {
		
		for(BlockFind find : BlockFinder.getBlockFinds())
		{
			if(find.isSpawned() && find.getLocation().getChunk().isLoaded())
			{
		        for(Player player : Bukkit.getOnlinePlayers())
		        {
		        	if(player.getWorld().equals(find.getLocation().getWorld()))
		        	{
		        		if(BlockFinder.getFound(player).contains(find.getName()))
		        		{
		        			player.spawnParticle(Settings.PARTICLE_TYPE_FOUND.getParticle(), find.getLocation().clone().add(0.5, 0.5, 0.5), 20);
		        		} else
		        		{
		        			player.spawnParticle(Settings.PARTICLE_TYPE.getParticle(), find.getLocation().clone().add(0.5, 0.5, 0.5), 3);
		        		}
		        		
		        	}
		        }
			}
		}
		
	}

}
