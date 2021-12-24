package org.benja.coder.spcore.commands;

import java.util.Optional;

import org.benja.coder.spcore.SPCore;
import org.benja.coder.spcore.color.Msg;
import org.benja.coder.spcore.scoreboard.ScoreboardBuilder;
import org.benja.coder.spcore.xseries.XSound;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class ScoreboardCommand implements CommandExecutor, Listener {

	private final SPCore spc;
	private final Sound sound;
	private final Sound sound1;
	private final Sound sound2;
	private final int vol;
	private final int pitch;
	
	public ScoreboardCommand(SPCore spc) {
		Optional<XSound> xs = XSound.matchXSound(spc.getConfig().getString("options.sounds.value_perm"));
		Optional<XSound> xs1 = XSound.matchXSound(spc.getConfig().getString("options.sounds.value_scoreboard"));
		Optional<XSound> xs2 = XSound.matchXSound(spc.getConfig().getString("options.sounds.value_cooldown"));
		if(xs.isPresent() || xs1.isPresent()) {
			this.sound = xs.get().parseSound();
			this.sound1 = xs1.get().parseSound();
			this.sound2 = xs2.get().parseSound();
		} else {
			this.sound = XSound.ENTITY_ITEM_BREAK.parseSound();
			this.sound1 = XSound.BLOCK_NOTE_BLOCK_XYLOPHONE.parseSound();
			this.sound2 = XSound.BLOCK_NOTE_BLOCK_PLING.parseSound();
		}
		this.vol = spc.getConfig().getInt("options.sounds.vol");
		this.pitch = spc.getConfig().getInt("options.sounds.pitch");
		this.spc = spc;
	}
	
	private Sound getSound() {
		return sound;
	}
	
	private Sound getNextSound() {
		return sound1;
	}
	
	private Sound getOtherNextSound() {
		return sound2;
	}
	
	private int getVol() {
		return vol;
	}
	
	private int getPitch() {
		return pitch;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String la, String[] args) {
		FileConfiguration config = spc.getConfig();
		FileConfiguration lang = spc.getLang();
		if(sender instanceof Player) {
			Player player = (Player) sender;
			if(player.hasPermission("spcore.cmd.scoreboard") || player.isOp()) {
				if(spc.getCooldownsCMD().hasCooldown(player.getUniqueId().toString())) {
					String message = lang.getString("messages.cooldown_cmd");
					message = message.replace("<time>", spc.getCooldownsCMD().getCooldown(player.getUniqueId().toString()) + "");
					if(config.getBoolean("options.sounds.reproduce_sounds")) {
						executeOtherNext(player);
					}
					player.sendMessage(Msg.color(message));
					return true;
				}
				if(ScoreboardBuilder.hasScore(player)) {
					spc.getCooldownsCMD().setCooldown(player.getUniqueId().toString());
					ScoreboardBuilder.removeScore(player);
					player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
					if(config.getBoolean("options.sounds.reproduce_sounds")) {
						executeNext(player);
					}
					if(config.getBoolean("options.titles.show_titles")) {
						String title = lang.getString("messages.command.scoreboard.title_disabled");
						String subtitle = lang.getString("messages.command.scoreboard.subtitle_disabled");
						
						title = Msg.color(title);
						subtitle = Msg.color(subtitle);
						
						int fadeIn = config.getInt("options.titles.fadeIn");
						int showTime = config.getInt("options.titles.showTime");
						int fadeOut = config.getInt("options.titles.fadeOut");
						
						spc.getVc().get().sendTitle(player, title, subtitle, fadeIn, showTime, fadeOut);
					}
					player.sendMessage(Msg.color(lang.getString("messages.command.scoreboard.disabled")));
				} else {
					spc.getCooldownsCMD().setCooldown(player.getUniqueId().toString());
					spc.createScoreboard(player);
					if(config.getBoolean("options.sounds.reproduce_sounds")) {
						executeNext(player);
					}
					if(config.getBoolean("options.titles.show_titles")) {
						String title = lang.getString("messages.command.scoreboard.title_enabled");
						String subtitle = lang.getString("messages.command.scoreboard.subtitle_enabled");
						
						title = Msg.color(title);
						subtitle = Msg.color(subtitle);
						
						int fadeIn = config.getInt("options.titles.fadeIn");
						int showTime = config.getInt("options.titles.showTime");
						int fadeOut = config.getInt("options.titles.fadeOut");
						
						spc.getVc().get().sendTitle(player, title, subtitle, fadeIn, showTime, fadeOut);
					}
					player.sendMessage(Msg.color(lang.getString("messages.command.scoreboard.enabled")));
				}
			} else {
				String message = lang.getString("messages.not_perm");
				message = message.replace("<permission>", "spcore.cmd.scoreboard");
				if(config.getBoolean("options.sounds.reproduce_sounds")) {
					execute(player);
				}
				player.sendMessage(Msg.color(message));
			}
		} else {
			sender.sendMessage(Msg.color(lang.getString("messages.not_console")));
			return false;
		}
		return false;
	}
	
	private void execute(Player player) {
		player.playSound(player.getLocation(), getSound(), getVol(), getPitch());
	}
	
	private void executeNext(Player player) {
		player.playSound(player.getLocation(), getNextSound(), getVol(), getPitch());
	}
	
	private void executeOtherNext(Player player) {
		player.playSound(player.getLocation(), getOtherNextSound(), getVol(), getPitch());
	}
}
