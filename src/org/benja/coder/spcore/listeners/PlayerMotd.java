package org.benja.coder.spcore.listeners;

import org.benja.coder.spcore.SPCore;
import org.benja.coder.spcore.color.Msg;
import org.benja.coder.spcore.utility.StringUtil;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.clip.placeholderapi.PlaceholderAPI;

public class PlayerMotd implements Listener {

	private final SPCore spc;
	
	public PlayerMotd(SPCore spc) {
		this.spc = spc;
	}
	
	@EventHandler
	public void onJoinMotd(PlayerJoinEvent ev) {
		Player player = ev.getPlayer();
		FileConfiguration config = spc.getConfig();
		if(config.getBoolean("options.motd_message")) {
			String message = config.getString("options.motd");
			String[] motd = message.split("\\n");
			for(int i = 0 ; i < motd.length ; i++) {
				String message_motd = motd[i];
				message_motd = Msg.color(message_motd);
				message_motd = StringUtil.setPlaceholders(message_motd, player);
				message_motd = PlaceholderAPI.setPlaceholders(player, message_motd);
				player.sendMessage(message_motd);
			}
		}
	}
}
