package com.hotmail.steven.listener;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.FireworkEffect.Builder;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

import com.hotmail.steven.main.BlockFind;
import com.hotmail.steven.main.BlockFinder;
import com.hotmail.steven.main.util.StringUtil;

import net.md_5.bungee.api.ChatColor;

public class BlockFindListener implements Listener {
	

	@EventHandler
	public void onMoveBlock(PlayerMoveEvent evt)
	{
		if(evt.getFrom().getBlockX() != evt.getTo().getBlockX()
				|| evt.getFrom().getBlockY() != evt.getTo().getBlockY()
				|| evt.getFrom().getBlockZ() != evt.getTo().getBlockZ())
		{
			for(BlockFind find : BlockFinder.getBlockFinds())
			{
				if(find.isSpawned() && find.getLocation().getBlock().equals(evt.getTo().getBlock()))
				{
					onBlockFind(find, evt.getPlayer());
				}
			}
		}
	}
	
	public boolean onBlockFind(BlockFind find, Player player)
	{
		
		List<String> found = BlockFinder.getFound(player);
		
		if(found.contains(find.getName())) return false;
		find.setLocation(null);
		find.getItem().remove();
		// Add found to the database
		BlockFinder.addFound(player.getUniqueId(), find.getName());
		// Get the entities location
		Location loc = player.getLocation();
		// Spawn a pretty firework
		Firework f = (Firework) loc.getWorld().spawn(loc, Firework.class);
		FireworkMeta fm = f.getFireworkMeta();
		Builder fe = FireworkEffect.builder();
		fe.flicker(true).with(Type.BALL).withColor(Color.AQUA).withFade(Color.RED);
		fm.addEffect(fe.build());
		f.setFireworkMeta(fm);
		// Get the blocks that can be found
		List<BlockFind> blockFinds = BlockFinder.getBlockFinds();

		if(found.size() >= blockFinds.size())
		{
			onBlockFindAll(player);
		} else
		{
			player.sendTitle(ChatColor.GOLD + "Found block!", ChatColor.BLUE + "You have found " + (found.size() == 0 ? 1 : found.size()) +  " / " + blockFinds.size(), 10, 70, 20);
			Bukkit.broadcastMessage(StringUtil.color("&d&l" + player.getName() + " is one step closer to getting a new kit!"));
		}
		
		return true;
	}
	
	public void onBlockFindAll(Player player)
	{
		player.sendTitle(ChatColor.GOLD + "Found block!", ChatColor.BLUE + "You have gained a new kit!", 10, 70, 20);
		
		ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
		String command = "/" + BlockFinder.getBlockConfig().getString("rewards.find-all.command").replaceAll("%player%", player.getName());
		Bukkit.dispatchCommand(console, command);
	}

}
