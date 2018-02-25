package com.hotmail.steven.listener;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.hotmail.steven.main.BlockFinder;
import com.hotmail.steven.main.util.StringUtil;

import net.md_5.bungee.api.ChatColor;

public class SelectorListener implements Listener {

	@EventHandler
	public void onBlockClick(PlayerInteractEvent evt)
	{
		Player p = evt.getPlayer();
		if(evt.getItem() != null && evt.getItem().getType() != Material.AIR && BlockFinder.hasSelector(evt.getItem()))
		{
			Location click = evt.getClickedBlock().getLocation().add(0, 1, 0);
			if(evt.getAction() == Action.LEFT_CLICK_BLOCK)
			{
				// Add possible spawn
				BlockFinder.addPossibleSpawn(click);
				// Send a message
				p.sendMessage(StringUtil.color("&dAdded a possible block find at x" + click.getBlockX() + " y" + click.getBlockY() + " z" + click.getBlockZ()));
				evt.getClickedBlock().setType(Material.WOOL);
			} else if(evt.getAction() == Action.RIGHT_CLICK_BLOCK)
			{
				if(BlockFinder.removePossibleSpawn(click))
				{
					p.sendMessage(StringUtil.color("&dRemoved possible block find"));
				} else
				{
					p.sendMessage(ChatColor.RED + "There is no block find here");
				}
			}
			evt.setCancelled(true);
		}
	}
	
}
