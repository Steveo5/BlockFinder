package com.hotmail.steven.listener;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Builder;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.entity.Firework;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.inventory.meta.FireworkMeta;

import com.hotmail.steven.main.BlockFinder;

public class ItemListener implements Listener {
	
	@EventHandler
	public void onItemPickup(EntityPickupItemEvent evt)
	{
		if(BlockFinder.hasBlockFind(evt.getItem().getLocation()))
		{
			evt.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onItemCombine(ItemMergeEvent evt)
	{
		if(BlockFinder.hasBlockFind(evt.getTarget().getLocation()))
		{
			evt.setCancelled(true);
		}
	}
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onItemDespawn(ItemDespawnEvent evt)
	{
		if(BlockFinder.hasBlockFind(evt.getLocation()))
		{
			evt.setCancelled(true);
		}
	}
	
}
