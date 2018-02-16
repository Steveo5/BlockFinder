package com.hotmail.steven.main;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class SelectorListener implements Listener {

	@EventHandler
	public void onBlockClick(PlayerInteractEvent evt)
	{
		Player p = evt.getPlayer();
		if(evt.getItem() != null && evt.getItem().getType() != Material.AIR && BlockFinder.hasSelector(evt.getItem()))
		{
			if(evt.getAction() == Action.LEFT_CLICK_BLOCK)
			{
				p.sendMessage("addingblck");
			} else  if(evt.getAction() == Action.RIGHT_CLICK_BLOCK)
			{
				p.sendMessage("removing block");
			}
		}
	}
	
}
