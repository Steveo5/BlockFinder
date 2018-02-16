package com.hotmail.steven.main;

import java.util.Random;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.scheduler.BukkitRunnable;

import net.minecraft.server.v1_12_R1.PacketPlayOutWorldParticles;

public class ParticleSpawner extends BukkitRunnable {

	@Override
	public void run() {
		
		for(Location loc : BlockFinder.getBlockFinds().keySet())
		{
			if(loc.getChunk().isLoaded())
			{
		        loc.getWorld().playEffect(loc, Effect.MOBSPAWNER_FLAMES, 3);
			}
		}
		
	}

}
