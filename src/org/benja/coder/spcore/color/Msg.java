package org.benja.coder.spcore.color;

import org.bukkit.ChatColor;

public class Msg {

	public static String color(String str) {
		return ChatColor.translateAlternateColorCodes('&', str);
	}
}
