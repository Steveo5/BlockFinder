package com.hotmail.steven.listener;

import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.hotmail.steven.main.BlockFinder;
import com.hotmail.steven.main.util.StringUtil;

import net.md_5.bungee.api.ChatColor;

public class CommandListener implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// Execute give command
		if(args.length > 0 && args[0].equalsIgnoreCase("give"))
		{
			// Player who will receive the selector
			Player receive = null;
			if(args.length > 1)
			{
				if(!(sender instanceof Player))
				{
					sender.sendMessage(ChatColor.RED + "You must be an in-game player to do this");
				} else
				{
					receive = Bukkit.getPlayer(args[1]);
				}
			} else
			{
				receive = (Player)sender;
			}
			
			receive.sendMessage(StringUtil.color("&dYou have received the block selector"));
			receive.getInventory().addItem(BlockFinder.getSelector());
			return true;
		} else if(args.length > 0 && args[0].equalsIgnoreCase("set"))
		{
			if(!(sender instanceof Player))
			{
				sender.sendMessage(ChatColor.RED + "You must be an in-game player to do this");
			} else
			{
				if(args.length < 2) 
				{
					sender.sendMessage(ChatColor.RED + "You must enter a name!");
				} else
				{
					if(BlockFinder.hasBlockFind(args[1]))
					{
						sender.sendMessage(ChatColor.RED + "Block find with this name already exists!");
					} else
					{
						Player p = (Player)sender;

						p.sendMessage(StringUtil.color("&dCreated new block find at your location"));
						ItemStack handItem = p.getInventory().getItemInMainHand().clone();
						handItem.setAmount(1);
						ItemMeta im = handItem.getItemMeta();
						im.setDisplayName(StringUtil.color(args[1]));;
						handItem.setItemMeta(im);
						// Set the block to find
						Location where = p.getTargetBlock((HashSet<Material>) null, 10).getLocation().add(0, 1, 0);
						Location itemLoc = where.getBlock().getLocation().add(0.5, 0.5, 0.5);
						Entity e = itemLoc.getWorld().dropItem(itemLoc, handItem);
						e.setVelocity(e.getVelocity().zero());
						
						BlockFinder.setBlockFind(args[1], p.getInventory().getItemInMainHand(), where.getBlock().getLocation());
					}
				}
			}
			return true;
		}
		return false;
	}

}
