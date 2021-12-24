package org.benja.coder.spcore.utility;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.benja.coder.spcore.SPCore;
import org.benja.coder.spcore.color.Msg;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

public class StringUtil {

	private static final SPCore spc = SPCore.getPlugin(SPCore.class);
	
	// public static void sendTitle(Player player, String title, String subtitle, int fadeIn, int showTime, int fadeOut) {
		// send title method
	// }
	
	// public static void sendActionBar(Player player, String text) {
	    // send actionbar method
    // }

	public static String setPlaceholders(String text, Player player) {
		if(player == null) {
			return "";
		}
		
		if(text.contains("<player>")) {
			text = text.replace("<player>", player.getName());
		}
		
		if(text.contains("<player_display_name>")) {
			text = text.replace("<player_display_name>", player.getDisplayName());
		}
		
		if(text.contains("<player_level>")) {
			text = text.replace("<player_level>", player.getLevel() + "");
		}
		
		if(text.contains("<player_exp>")) {
			text = text.replace("<player_exp>", player.getTotalExperience() + "");
		}
		
		if(text.contains("<player_world>")) {
			text = text.replace("<player_world>", player.getWorld().getName());
		}
		
		if(text.contains("<player_location>")) {
			Location location = player.getLocation();
			text = text.replace("<player_location>", location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ());
		}
		
		if(text.contains("<player_kills>")) {
			text = text.replace("<player_kills>", player.getStatistic(Statistic.PLAYER_KILLS) + "");
		}
		
		if(text.contains("<player_group>")) {
			text = text.replace("<player_group>", Msg.color(spc.permission.getPrimaryGroup(player)));
		}
		
		if(text.contains("<player_group_prefix>")) {
			text = text.replace("<player_group_prefix>", Msg.color(spc.chat.getPlayerPrefix(player)));
		}
		
		if(text.contains("<player_group_suffix>")) {
			text = text.replace("<player_group_suffix>", Msg.color(spc.chat.getPlayerSuffix(player)));
		}
	
		if(text.contains("<server_time>")) {
			Date now = new Date();
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			text = text.replace("<server_time>", format.format(now));
		}
		
		if(text.contains("<connected>")) {
			text = text.replace("<connected>", Bukkit.getOnlinePlayers().size() + "");
		}
		
		if(text.contains("<max_connected>")) {
			text = text.replace("<max_connected>", Bukkit.getMaxPlayers() + "");
		}
		return text;
	}
}
