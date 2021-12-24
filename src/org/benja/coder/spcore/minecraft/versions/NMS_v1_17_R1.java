package org.benja.coder.spcore.minecraft.versions;

import org.benja.coder.spcore.minecraft.NMS;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;

public class NMS_v1_17_R1 implements NMS {

	@Override
	public void sendTitle(Player player, String title, String subtitle, int fadeIn, int showTime, int fadeOut) {
		player.sendTitle(title, subtitle, fadeIn, showTime, fadeOut);
	}
	
	@Override
	public void sendActionBar(Player player, String text) {
		player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new ComponentBuilder(text).create());
	}
}
