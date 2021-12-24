package org.benja.coder.spcore.listeners;

import java.util.Optional;

import org.benja.coder.spcore.SPCore;
import org.benja.coder.spcore.color.Msg;
import org.benja.coder.spcore.utility.StringUtil;
import org.benja.coder.spcore.xseries.XSound;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.clip.placeholderapi.PlaceholderAPI;

public class GroupsChatListener implements Listener {

	private final SPCore spc;
	private final Sound sound;
	private final int vol;
	private final int pitch;
	
	public GroupsChatListener(SPCore spc) {
		Optional<XSound> xs = XSound.matchXSound(spc.getConfig().getString("options.sounds.value_cooldown"));
		if(xs.isPresent()) {
			this.sound = xs.get().parseSound();
		} else {
			this.sound = XSound.BLOCK_NOTE_BLOCK_PLING.parseSound();
		}
		this.vol = spc.getConfig().getInt("options.sounds.vol");
		this.pitch = spc.getConfig().getInt("options.sounds.pitch");
		this.spc = spc;
	}
	
	private Sound getSound() {
		return sound;
	}
	
	public int getVol() {
		return vol;
	}

	public int getPitch() {
		return pitch;
	}

	@EventHandler
	public void onChatGroup(AsyncPlayerChatEvent ev) {
		Player player = ev.getPlayer();
		FileConfiguration config = spc.getConfig();
		FileConfiguration lang = spc.getLang();
		if(config.getBoolean("options.chat.active")) {
			if(player.hasPermission("spcore.bypass.chat.cooldown") || player.isOp()) {
				String group = spc.permission.getPrimaryGroup(player);
				if(player.hasPermission("spcore.event.chat.color") || player.isOp()) {
					ev.setMessage(Msg.color(ev.getMessage()));
				}
				String message = "";
				if(config.getConfigurationSection("options.chat.groups." + group) != null) {
					message = config.getString("options.chat.groups." + group + ".format");
				} else {
					message = config.getString("options.chat.default_format");
				}
				
				message = message.replace("<message>", ev.getMessage());
				message = StringUtil.setPlaceholders(message, player);
				message = PlaceholderAPI.setPlaceholders(player, message);
				message = Msg.color(message);
				ev.setFormat(message);
				return;
			} else {
				if(spc.getCooldownsChat().hasCooldown(player.getUniqueId().toString())) {
					ev.setCancelled(true);
					String cooldown = lang.getString("messages.cooldown_chat");
					cooldown = cooldown.replace("<time>", spc.getCooldownsChat().getCooldown(player.getUniqueId().toString()) + "");
					if(config.getBoolean("options.sounds.reproduce_sounds")) {
						execute(player);
					}
					player.sendMessage(Msg.color(cooldown));
					return;
				}
				spc.getCooldownsChat().setCooldown(player.getUniqueId().toString());
				String group = spc.permission.getPrimaryGroup(player);
				if(player.hasPermission("spcore.event.chat.color") || player.isOp()) {
					ev.setMessage(Msg.color(ev.getMessage()));
				}
				String message = "";
				if(config.getConfigurationSection("options.chat.groups." + group) != null) {
					message = config.getString("options.chat.groups." + group + ".format");
				} else {
					message = config.getString("options.chat.default_format");
				}
				
				message = message.replace("<message>", ev.getMessage());
				message = StringUtil.setPlaceholders(message, player);
				message = PlaceholderAPI.setPlaceholders(player, message);
				message = Msg.color(message);
				ev.setFormat(message);
			}
		}
	}
	
	private void execute(Player player) {
		player.playSound(player.getLocation(), getSound(), getVol(), getPitch());
	}
}
