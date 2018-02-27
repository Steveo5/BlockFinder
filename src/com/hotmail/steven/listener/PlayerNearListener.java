package com.hotmail.steven.listener;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import com.hotmail.steven.main.BlockFind;
import com.hotmail.steven.main.BlockFinder;
import com.hotmail.steven.main.util.StringUtil;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class PlayerNearListener implements Listener {

	int radius = 0;
	
	public PlayerNearListener()
	{
		radius = BlockFinder.getBlockConfig().getInt("near.radius");
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent evt)
	{
		if(evt.getFrom().getBlockX() != evt.getTo().getBlockX()
				|| evt.getFrom().getBlockY() != evt.getTo().getBlockY()
				|| evt.getFrom().getBlockZ() != evt.getTo().getBlockZ())
		{
			onMoveBlock(evt);
		}
	}
	
	public void onMoveBlock(PlayerMoveEvent evt)
	{
		Player p = evt.getPlayer();
		int pX = p.getLocation().getBlockX();
		int pY = p.getLocation().getBlockY();
		int pZ = p.getLocation().getBlockZ();
		
		// Loop possible block finds
		for(BlockFind find : BlockFinder.getBlockFinds())
		{
			if(!find.isSpawned()) continue;
			// Player is leaving radius
			if(!find.isNear(evt.getTo(), 10) && find.isNear(evt.getFrom(), 10))
			{
				find.hideProgressBar(p);
			// Player is entering the radius	
			} else if(find.isNear(evt.getTo(), 10) && find.isNear(evt.getFrom(), 10))
			{
				if(find.isSpawned())
				{
					find.showProgressBar(p);
				}
			}
			
			if(find.isNear(p.getLocation(), 10))
			{
				find.updateProgressBar(p, 10);
			}
			
		}
	}
	
}
