package org.benja.coder.spcore.listeners;

import java.util.Optional;

import org.benja.coder.spcore.SPCore;
import org.benja.coder.spcore.color.Msg;
import org.benja.coder.spcore.utility.StringUtil;
import org.benja.coder.spcore.xseries.XSound;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.clip.placeholderapi.PlaceholderAPI;

public class GroupsJoinListener implements Listener {

	private final SPCore spc;
	private final Sound sound;
	private final int vol;
	private final int pitch;
	
	public GroupsJoinListener(SPCore spc) {
		Optional<XSound> xs = XSound.matchXSound(spc.getConfig().getString("options.sounds.value_join"));
		if(xs.isPresent()) {
			this.sound = xs.get().parseSound();
		} else {
			this.sound = XSound.UI_BUTTON_CLICK.parseSound();
		}
		this.vol = spc.getConfig().getInt("options.sounds.vol");
		this.pitch = spc.getConfig().getInt("options.sounds.pitch");
		this.spc = spc;
	}
	
	private Sound getSound() {
		return sound;
	}
	
	private int getVol() {
		return vol;
	}
	
	private int getPitch() {
		return pitch;
	}
	
	@EventHandler
	public void onJoinGroup(PlayerJoinEvent ev) {
		Player player = ev.getPlayer();
		FileConfiguration config = spc.getConfig();
		if(config.getBoolean("options.announce_entry")) {
			String group = spc.permission.getPrimaryGroup(player);
			String message = "";
			if(config.getConfigurationSection("options.join.groups." + group) != null) {
				message = config.getString("options.join.groups." + group + ".message");
			} else {
				message = config.getString("options.join.default_message");
			}
			
			message = StringUtil.setPlaceholders(message, player);
			message = PlaceholderAPI.setPlaceholders(player, message);
			message = Msg.color(message);
			
			if(config.getBoolean("options.sounds.reproduce_sounds") && player.hasPermission("spcore.event.join.sound")) {
				for(Player connected : Bukkit.getOnlinePlayers()) {
					execute(connected);
				}
			}
			
			ev.setJoinMessage(message);
		} else {
			ev.setJoinMessage(null);
		}
	}
	
	private void execute(Player player) {
		player.playSound(player.getLocation(), getSound(), getVol(), getPitch());
	}
}
