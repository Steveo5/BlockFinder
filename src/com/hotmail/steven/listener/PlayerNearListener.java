package com.hotmail.steven.listener;

import org.bukkit.Location;
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
			Location blockLoc = find.getLocation();
			int x = blockLoc.getBlockX();
			int y = blockLoc.getBlockY();
			int z = blockLoc.getBlockZ();
			// Send message to play if they're near
			if(pX > x - radius && pX < x + radius)
			{
				if(pY > y - radius && pY < y + radius)
				{
					if(pZ > z - radius && pZ < z + radius)
					{
						p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(StringUtil.color(BlockFinder.getBlockConfig().getString("near.message"))));
					}
				}
			}
			
		}
	}
	
}
