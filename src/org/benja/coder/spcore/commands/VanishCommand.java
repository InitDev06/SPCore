package org.benja.coder.spcore.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.benja.coder.spcore.SPCore;
import org.benja.coder.spcore.color.Msg;
import org.benja.coder.spcore.xseries.XSound;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class VanishCommand implements CommandExecutor, Listener {

	private final SPCore spc;
	private final Sound sound;
	private final Sound sound1;
	private final Sound sound2;
	private final int vol;
	private final int pitch;
	private final List<UUID> vanished;
	
	public VanishCommand(SPCore spc) {
		Optional<XSound> xs = XSound.matchXSound(spc.getConfig().getString("options.sounds.value_perm"));
		Optional<XSound> xs1 = XSound.matchXSound(spc.getConfig().getString("options.sounds.value_vanish"));
		Optional<XSound> xs2 = XSound.matchXSound(spc.getConfig().getString("options.sounds.value_cooldown"));
		if(xs.isPresent() || xs1.isPresent() || xs2.isPresent()) {
			this.sound = xs.get().parseSound();
			this.sound1 = xs1.get().parseSound();
			this.sound2 = xs2.get().parseSound();
		} else {
			this.sound = XSound.ENTITY_ITEM_BREAK.parseSound();
			this.sound1 = XSound.ENTITY_PLAYER_ATTACK_SWEEP.parseSound();
			this.sound2 = XSound.BLOCK_NOTE_BLOCK_PLING.parseSound();
		}
		this.vol = spc.getConfig().getInt("options.sounds.vol");
		this.pitch = spc.getConfig().getInt("options.sounds.pitch");
		this.vanished = new ArrayList<>();
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
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String la, String[] args) {
		FileConfiguration config = spc.getConfig();
		FileConfiguration lang = spc.getLang();
		if(sender instanceof Player) {
			Player player = (Player) sender;
			if(player.hasPermission("spcore.cmd.vanish") || player.isOp()) {
				if(vanished.contains(player.getUniqueId())) {
					if(spc.getCooldownsCMD().hasCooldown(player.getUniqueId().toString())) {
						String message = lang.getString("messages.cooldown_cmd");
						message = message.replace("<time>", spc.getCooldownsCMD().getCooldown(player.getUniqueId().toString()) + "");
						if(config.getBoolean("options.sounds.reproduce_sounds")) {
							executeOtherNext(player);
						}
						player.sendMessage(Msg.color(message));
						return true;
					}
					spc.getCooldownsCMD().setCooldown(player.getUniqueId().toString());
					for(Player connected : Bukkit.getOnlinePlayers()) {
						connected.showPlayer(player);
					}
					vanished.remove(player.getUniqueId());
					if(config.getBoolean("options.sounds.reproduce_sounds")) {
						executeNext(player);
					}
					if(config.getBoolean("options.titles.show_titles")) {
						String title = lang.getString("messages.command.vanish.title_disabled");
						String subtitle = lang.getString("messages.command.vanish.subtitle_disabled");
						
						title = Msg.color(title);
						subtitle = Msg.color(subtitle);
						
						int fadeIn = config.getInt("options.titles.fadeIn");
						int showTime = config.getInt("options.titles.showTime");
						int fadeOut = config.getInt("options.titles.fadeOut");
						
						spc.getVc().get().sendTitle(player, title, subtitle, fadeIn, showTime, fadeOut);
					}
					player.sendMessage(Msg.color(lang.getString("messages.command.vanish.disabled")));
					return true;
				} else {
					if(spc.getCooldownsCMD().hasCooldown(player.getUniqueId().toString())) {
						String message = lang.getString("messages.cooldown_cmd");
						message = message.replace("<time>", spc.getCooldownsCMD().getCooldown(player.getUniqueId().toString()) + "");
						if(config.getBoolean("options.sounds.reproduce_sounds")) {
							executeOtherNext(player);
						}
						player.sendMessage(Msg.color(message));
						return true;
					}
					spc.getCooldownsCMD().setCooldown(player.getUniqueId().toString());
					for(Player connected : Bukkit.getOnlinePlayers()) {
						if(!(connected.hasPermission("spcore.bypass.cmd.vanish"))) {
							connected.hidePlayer(player);
						}
					}
					vanished.add(player.getUniqueId());
					if(config.getBoolean("options.sounds.reproduce_sounds")) {
						executeNext(player);
					}
					if(config.getBoolean("options.titles.show_titles")) {
						String title = lang.getString("messages.command.vanish.title_enabled");
						String subtitle = lang.getString("messages.command.vanish.subtitle_enabled");
						
						title = Msg.color(title);
						subtitle = Msg.color(subtitle);
						
						int fadeIn = config.getInt("options.titles.fadeIn");
						int showTime = config.getInt("options.titles.showTime");
						int fadeOut = config.getInt("options.titles.fadeOut");
						
						spc.getVc().get().sendTitle(player, title, subtitle, fadeIn, showTime, fadeOut);
					}
					player.sendMessage(Msg.color(lang.getString("messages.command.vanish.enabled")));
					player.sendMessage(Msg.color(lang.getString("messages.command.vanish.bypass")));
					return false;
				}
			} else {
				String message = lang.getString("messages.not_perm");
				message = message.replace("<permission>", "spcore.cmd.vanish");
				if(config.getBoolean("options.sounds.reproduce_sounds")) {
					execute(player);
				}
				player.sendMessage(Msg.color(message));
				return false;
			}
		} else {
			sender.sendMessage(Msg.color(lang.getString("messages.not_console")));
			return false;
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onQuit(PlayerQuitEvent ev) {
		Player player = ev.getPlayer();
		if(vanished.contains(player.getUniqueId())) {
			for(Player connected : Bukkit.getOnlinePlayers()) {
				connected.showPlayer(player);
			}
			vanished.remove(player.getUniqueId());
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onWorldChange(PlayerChangedWorldEvent ev) {
		Player player = ev.getPlayer();
		if(vanished.contains(player.getUniqueId())) {
			for(Player connected : Bukkit.getOnlinePlayers()) {
				connected.showPlayer(player);
			}
			vanished.remove(player.getUniqueId());
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onDeath(PlayerDeathEvent ev) {
		Player player = (Player) ev.getEntity();
		if(vanished.contains(player.getUniqueId())) {
			for(Player connected : Bukkit.getOnlinePlayers()) {
				connected.showPlayer(player);
			}
			vanished.remove(player.getUniqueId());
		}
	}
	 
	@EventHandler
	public void onInteract(PlayerInteractEvent ev) {
		Player player = ev.getPlayer();
		if(vanished.contains(player.getUniqueId()) && ev.getAction() == Action.RIGHT_CLICK_BLOCK) {
			ev.setCancelled(true);
		}
	}
	
	@EventHandler
	public void on(PlayerRespawnEvent ev) {
		Player player = ev.getPlayer();
		if(vanished.contains(player.getUniqueId())) {
			return;
		}
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
