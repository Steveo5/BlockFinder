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

import com.hotmail.steven.main.BlockFind;
import com.hotmail.steven.main.BlockFinder;

public class ItemListener implements Listener {
	
	@EventHandler (priority = EventPriority.HIGH)
	public void onItemPickup(EntityPickupItemEvent evt)
	{
		for(BlockFind find : BlockFinder.getBlockFinds())
		{
			if(find.isSpawned() && evt.getItem().getItemStack().isSimilar(find.getItemStack()))
			{
				evt.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onItemCombine(ItemMergeEvent evt)
	{
		for(BlockFind find : BlockFinder.getBlockFinds())
		{
			if(find.isSpawned() && evt.getEntity().getItemStack().isSimilar(find.getItemStack()))
			{
				evt.setCancelled(true);
			}
		}
	}
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onItemDespawn(ItemDespawnEvent evt)
	{
		for(BlockFind find : BlockFinder.getBlockFinds())
		{
			if(find.isSpawned() && evt.getEntity().getItemStack().isSimilar(find.getItemStack()))
			{
				evt.setCancelled(true);
			}
		}
	}
	
}
