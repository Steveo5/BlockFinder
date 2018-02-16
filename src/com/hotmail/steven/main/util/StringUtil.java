package com.hotmail.steven.main.util;

import org.bukkit.ChatColor;

public class StringUtil {

	/**
	 * Translate standard chat colors on a string
	 * @param str
	 * @return
	 */
	public static String color(String str)
	{
		return ChatColor.translateAlternateColorCodes('&', str);
	}
	
}
