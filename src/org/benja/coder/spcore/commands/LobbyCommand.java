package org.benja.coder.spcore.commands;

import java.util.Optional;

import org.benja.coder.spcore.SPCore;
import org.benja.coder.spcore.color.Msg;
import org.benja.coder.spcore.xseries.XSound;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class LobbyCommand implements CommandExecutor, Listener {

	private final SPCore spc;
	private final Sound sound;
	private final Sound sound1;
	private final int vol;
	private final int pitch;
	
	public LobbyCommand(SPCore spc) {
		Optional<XSound> xs = XSound.matchXSound(spc.getConfig().getString("options.sounds.value_perm"));
		Optional<XSound> xs1 = XSound.matchXSound(spc.getConfig().getString("options.sounds.value_cooldown"));
		if(xs.isPresent() || xs1.isPresent()) {
			this.sound = xs.get().parseSound();
			this.sound1 = xs1.get().parseSound();
		} else {
			this.sound = XSound.ENTITY_ITEM_BREAK.parseSound();
			this.sound1 = XSound.BLOCK_NOTE_BLOCK_PLING.parseSound();
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
		FileConfiguration data = spc.getData();
		if(sender instanceof Player) {
			Player player = (Player) sender;
			if(player.hasPermission("spcore.cmd.lobby") || player.isOp()) {
				if(spc.getCooldownsCMD().hasCooldown(player.getUniqueId().toString())) {
					String message = lang.getString("messages.cooldown_cmd");
					message = message.replace("<time>", spc.getCooldownsCMD().getCooldown(player.getUniqueId().toString()) + "");
					if(config.getBoolean("options.sounds.reproduce_sounds")) {
						executeNext(player);
					}
					player.sendMessage(Msg.color(message));
					return true;
				}
				spc.getCooldownsCMD().setCooldown(player.getUniqueId().toString());
				
				double x = Double.valueOf(data.getInt("lobby.x"));
				double y = Double.valueOf(data.getInt("lobby.y"));
				double z = Double.valueOf(data.getInt("lobby.z"));
				
				float yaw = Float.valueOf(data.getInt("lobby.yaw"));
				float pitch = Float.valueOf(data.getInt("lobby.pitch"));
				
				World world = Bukkit.getServer().getWorld(data.getString("lobby.world"));
				if(world != null) {
					Location location = new Location(world, x, y, z, yaw, pitch);
					location.subtract(0.5D, 0.0D, 0.5D);
					
					player.teleport(location);
					player.sendMessage(Msg.color(lang.getString("messages.command.lobby.teleport")));
				} else {
					player.sendMessage(Msg.color(lang.getString("messages.command.lobby.unknown")));
				}
				return true;
			} else {
				String message = lang.getString("messages.not_perm");
				message = message.replace("<permission>", "spcore.cmd.lobby");
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
	
	@EventHandler
	public void onJoin(PlayerJoinEvent ev) {
		FileConfiguration config = spc.getConfig();
		FileConfiguration data = spc.getData();
		Player player = ev.getPlayer();
		double x = Double.valueOf(data.getInt("lobby.x"));
		double y = Double.valueOf(data.getInt("lobby.y"));
		double z = Double.valueOf(data.getInt("lobby.z"));
			
		float yaw = Float.valueOf(data.getInt("lobby.yaw"));
		float pitch = Float.valueOf(data.getInt("lobby.pitch"));
			
		World world = Bukkit.getServer().getWorld(data.getString("lobby.world"));
		if(config.getBoolean("options.teleport_to_lobby") && world != null) {
			Location location = new Location(world, x, y, z, yaw, pitch);
			location.subtract(0.5D, 0.0D, 0.5D);
			
			player.teleport(location);
		}
	}
	
	private void execute(Player player) {
		player.playSound(player.getLocation(), getSound(), getVol(), getPitch());
	}
	
	private void executeNext(Player player) {
		player.playSound(player.getLocation(), getNextSound(), getVol(), getPitch());
	}
}
