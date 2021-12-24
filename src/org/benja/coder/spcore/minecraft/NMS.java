package org.benja.coder.spcore.minecraft;

import org.bukkit.entity.Player;

public interface NMS {

	void sendTitle(Player player, String title, String subtitle, int fadeIn, int showTime, int fadeOut);
	
	void sendActionBar(Player player, String text);
	
}
