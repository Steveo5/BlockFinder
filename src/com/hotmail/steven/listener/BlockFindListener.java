package com.hotmail.steven.listener;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.FireworkEffect.Builder;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

import com.hotmail.steven.main.BlockFinder;

import net.md_5.bungee.api.ChatColor;

public class BlockFindListener implements Listener {
	
	@EventHandler
	public void onItempickup(EntityPickupItemEvent evt)
	{
		System.out.println(evt.getItem().getItemStack().getItemMeta().getDisplayName());
		if(evt.getEntity() instanceof Player && BlockFinder.hasBlockFind(evt.getEntity().getLocation()))
		{
			Player p = (Player)evt.getEntity();
			// Get the already found blocks
			List<String> found = BlockFinder.getFound(p);
			if(!found.contains(evt.getItem().getItemStack().getItemMeta().getDisplayName()))
			{
				// Add found to the database
				BlockFinder.addFound(p.getUniqueId(), evt.getItem().getItemStack().getItemMeta().getDisplayName());
				// Get the entities location
				Location loc = evt.getEntity().getLocation();
				// Spawn a pretty firework
				Firework f = (Firework) loc.getWorld().spawn(loc, Firework.class);
				FireworkMeta fm = f.getFireworkMeta();
				Builder fe = FireworkEffect.builder();
				fe.flicker(true).with(Type.BALL).withColor(Color.AQUA).withFade(Color.RED);
				fm.addEffect(fe.build());
				f.setFireworkMeta(fm);
				// Get the blocks that can be found
				HashMap<Location, ItemStack> blockFinds = BlockFinder.getBlockFinds();

				p.sendTitle(ChatColor.GOLD + "Found block!", ChatColor.BLUE + "You have found " + (found.size() == 0 ? 1 : found.size()) +  " / " + blockFinds.size(), 10, 70, 20);
				
				if(found.size() >= blockFinds.size())
				{
					
				}
			}
		}
	}

}
