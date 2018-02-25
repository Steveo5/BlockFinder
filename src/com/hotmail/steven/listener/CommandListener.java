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

import com.hotmail.steven.main.BlockFind;
import com.hotmail.steven.main.BlockFinder;
import com.hotmail.steven.main.util.StringUtil;

import net.md_5.bungee.api.ChatColor;

public class CommandListener implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// Execute give command
		if(args.length > 0 && args[0].equalsIgnoreCase("give"))
		{
			if(!sender.hasPermission("blockfinder.give"))
			{
				sender.sendMessage(ChatColor.RED + "You have no permission to execute this command");
				return true;
			}
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
		} else if(args.length > 0 && args[0].equalsIgnoreCase("create"))
		{
			if(!sender.hasPermission("blockfinder.create"))
			{
				sender.sendMessage(ChatColor.RED + "You have no permission to execute this command");
				return true;
			}
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
					Player p = (Player)sender;
					ItemStack handItem = p.getInventory().getItemInMainHand().clone();
					if(handItem == null || handItem.getType() == Material.AIR)
					{
						sender.sendMessage(ChatColor.RED + "You must be holding a valid item");
					} else if(BlockFinder.hasBlockFind(args[1]))
					{
						sender.sendMessage(ChatColor.RED + "Block find with this name already exists!");
					} else
					{
						BlockFind find = new BlockFind(args[1], handItem);
						BlockFinder.addBlockFind(find);
						
						p.sendMessage(StringUtil.color("&dCreated new block find that should start spawning shortly"));
					}
				}
			}
			return true;
		} else if(args.length > 0 && args[0].equals("remove"))
		{
			if(!sender.hasPermission("blockfinder.remove"))
			{
				sender.sendMessage(ChatColor.RED + "You have no permission to execute this command");
				return true;
			}
			if(args.length > 2)
			{
				Player p = Bukkit.getPlayer(args[1]);
				if(p != null)
				{
					if(BlockFinder.getFound(p).contains(args[2]))
					{
						BlockFinder.getFound(p).remove(args[2]);
					} else
					{
						sender.sendMessage(ChatColor.RED + "You must enter a valid block find");
					}
				} else
				{
					sender.sendMessage(ChatColor.RED + "You must enter a valid player name");
				}
			} else
			{
				sender.sendMessage(ChatColor.RED + "Usage: /bf remove <player> <find>");
			}
			return true;
		} else if(args.length > 0 && args[0].equals("finds"))
		{
			if(!sender.hasPermission("blockfinder.finds"))
			{
				sender.sendMessage(ChatColor.RED + "You have no permission to execute this command");
				return true;
			}
			StringBuilder builder = new StringBuilder();
			builder.append("&a&lShowing total &e&l" + BlockFinder.getBlockFinds().size() + " &a&lfinds");
			for(BlockFind find : BlockFinder.getBlockFinds())
			{
				builder.append("&6\n- " + find.getName() + (find.isSpawned() ? " (x" + find.getLocation().getBlockX() + " y" + find.getLocation().getBlockY() + " z" + find.getLocation().getBlockZ() + ")" : ""));
			}
			sender.sendMessage(StringUtil.color(builder.toString()));
		} else if(args.length > 0 && args[0].equalsIgnoreCase("spawns"))
		{
			if(!sender.hasPermission("blockfinder.spawns"))
			{
				sender.sendMessage(ChatColor.RED + "You have no permission to execute this command");
				return true;
			}
			StringBuilder builder = new StringBuilder();
			builder.append("&a&lShowing total &e&l" + BlockFinder.getAllPossibleSpawns().size() + " &a&lpossible spawns");
			for(Location loc : BlockFinder.getAllPossibleSpawns())
			{
				builder.append("&6\n- x" + loc.getBlockX() + " y" + loc.getBlockY() + " z" + loc.getBlockZ());
			}
			sender.sendMessage(StringUtil.color(builder.toString()));
			return true;
		} else if(args.length > 0 && args[0].equalsIgnoreCase("collected"))
		{
			Player sendTo = null;
			if(!(sender instanceof Player))
			{
				if(args.length > 1)
				{
					sendTo = Bukkit.getPlayer(args[1]);
					if(sendTo == null)
					{
						sender.sendMessage(ChatColor.RED + "You must enter a valid player name");
						return true;
					}
				} else
				{
					sender.sendMessage(ChatColor.RED + "You must enter a valid player name");
				}
				return true;
			} else
			{
				sendTo = (Player)sender;
			}
			StringBuilder builder = new StringBuilder();
			builder.append("&a&lCollected &e&l" + BlockFinder.getFound(sendTo).size() + " / " + BlockFinder.getBlockFinds().size() + " &a&lpieces");
			for(BlockFind find : BlockFinder.getBlockFinds())
			{
				if(BlockFinder.getFound(sendTo).contains(find.getName()))
				{
					builder.append("\n- &3" + find.getName() + " &e&l(found)");
				} else
				{
					builder.append("\n- &3" + find.getName());
				}
			}
			sender.sendMessage(StringUtil.color(builder.toString()));
			return true;
		} else
		{
			if(!sender.hasPermission("blockfinder.help"))
			{
				sender.sendMessage(ChatColor.RED + "You have no permission to execute this command");
				return true;
			}
			StringBuilder builder = new StringBuilder();
			builder.append("&a&lBlockfinder help");
			builder.append("\n&a/bf give - &egive yourself the selector tool");
			builder.append("\n&a/bf create <name> - &ecreate a new block find with the item in your hand");
			builder.append("\n&a/bf remove <player> <blockfind> - &eremove a blockfind from a player");
			builder.append("\n&a/bf finds - &elist all finds");
			builder.append("\n&a/bf spawns - &elist all possible spawns for finds");
			builder.append("\n&a/bf collected [<player>] - &eshow collected block finds");
			sender.sendMessage(StringUtil.color(builder.toString()));
			return true;
		}
		return false;
	}

}
