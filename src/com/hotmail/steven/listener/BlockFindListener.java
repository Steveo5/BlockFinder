package com.hotmail.steven.listener;

import java.util.HashMap;
import java.util.List;

import com.hotmail.steven.main.Settings;
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
			StringBuilder major = new StringBuilder();
			major.append(Settings.REWARD_FIND_TITLE_MAJOR.getString().isEmpty() ? "" : Settings.REWARD_FIND_TITLE_MAJOR.getString());
			StringBuilder minor = new  StringBuilder();
			minor.append(Settings.REWARD_FIND_TITLE_MINOR.getString().isEmpty() ? "" : Settings.REWARD_FIND_TITLE_MINOR.getString()
					.replaceAll("%collected%",String.valueOf(found.size() == 0 ? 1 : found.size()))
					.replaceAll("%finds%", String.valueOf(blockFinds.size())));


			player.sendTitle(StringUtil.color(major.toString()), StringUtil.color(minor.toString()), 10, 70, 20);

			if(!Settings.REWARD_FIND_BROADCAST.getStringList().isEmpty()) {
				for(String broadcast : Settings.REWARD_FIND_BROADCAST.getStringList()) {
					Bukkit.broadcastMessage(StringUtil.color(broadcast.replaceAll("%player%", player.getName())));
				}
			}
			ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
			for(String command : Settings.REWARD_FIND_COMMANDS.getStringList()) {
				Bukkit.dispatchCommand(console, "/" + command.replaceAll("%player%", player.getName()));
			}
		}
		
		return true;
	}
	
	public void onBlockFindAll(Player player)
	{
		// Get the blocks that can be found
		List<BlockFind> blockFinds = BlockFinder.getBlockFinds();
		List<String> found = BlockFinder.getFound(player);

		StringBuilder major = new StringBuilder();
		major.append(Settings.REWARD_FINDALL_TITLE_MAJOR.getString().isEmpty() ? "" : Settings.REWARD_FINDALL_TITLE_MAJOR.getString());
		StringBuilder minor = new  StringBuilder();
		minor.append(Settings.REWARD_FINDALL_TITLE_MINOR.getString().isEmpty() ? "" : Settings.REWARD_FINDALL_TITLE_MINOR.getString()
				.replaceAll("%collected%",String.valueOf(found.size() == 0 ? 1 : found.size()))
				.replaceAll("%finds%", String.valueOf(blockFinds.size())));


		player.sendTitle(StringUtil.color(major.toString()), StringUtil.color(minor.toString()), 10, 70, 20);
		;
		if(!Settings.REWARD_FINDALL_BROADCAST.getStringList().isEmpty()) {
			for(String broadcast : Settings.REWARD_FINDALL_BROADCAST.getStringList()) {
				Bukkit.broadcastMessage(StringUtil.color(broadcast.replaceAll("%player%", player.getName())));
			}
		}
		ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
		for(String command : Settings.REWARD_FINDALL_COMMANDS.getStringList()) {
			Bukkit.dispatchCommand(console, "/" + command.replaceAll("%player%", player.getName()));
		}
	}

}
