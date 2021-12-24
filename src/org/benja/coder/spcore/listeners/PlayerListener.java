package org.benja.coder.spcore.listeners;

import java.util.List;
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
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class PlayerListener implements Listener {

	private final SPCore spc;
	private final Sound sound;
	private final int vol;
	private final int pitch;
	
	public PlayerListener(SPCore spc) {
		Optional<XSound> xs = XSound.matchXSound(spc.getConfig().getString("options.sounds.value_cmd"));
		if(xs.isPresent()) {
			this.sound = xs.get().parseSound();
		} else {
			this.sound = XSound.ENTITY_PLAYER_DEATH.parseSound();
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
	public void onCmdBlock(PlayerCommandPreprocessEvent ev) {
		FileConfiguration config = spc.getConfig();
		FileConfiguration lang = spc.getLang();
		Player player = ev.getPlayer();
		if(config.getBoolean("options.commands.block")) {
			if(player.hasPermission("spcore.bypass.chat.commands") || player.isOp()) {
				return;
			} else {
				List<String> list = config.getStringList("options.commands.list");
				for(int i = 0 ; i < list.size() ; i++) {
					String list_commands = list.get(i);
					if(ev.getMessage().equalsIgnoreCase(list_commands)) {
						ev.setCancelled(true);
						if(config.getBoolean("options.sounds.reproduce_sounds")) {
							execute(player);
						}
						player.sendMessage(Msg.color(lang.getString("messages.blocked_cmd")));
						String message = lang.getString("messages.cmd_log");
						message = message.replace("<command>", ev.getMessage());
						message = StringUtil.setPlaceholders(message, player);
						for(Player connected : Bukkit.getOnlinePlayers()) {
							if(connected.hasPermission("spcore.log.cmd") || connected.isOp()) {
								connected.sendMessage(Msg.color(message));
							}
						}
					}
				}
			}
		}
	}
	
	private void execute(Player player) {
		player.playSound(player.getLocation(), getSound(), getVol(), getPitch());
	}
}
