package org.benja.coder.spcore.commands;

import java.util.Optional;

import org.benja.coder.spcore.SPCore;
import org.benja.coder.spcore.color.Msg;
import org.benja.coder.spcore.xseries.XSound;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class MainCommand implements CommandExecutor {

	private final SPCore spc;
	private final Sound sound;
	private final Sound sound1;
	private final Sound sound2;
	private final int vol;
	private final int pitch;
	
	public MainCommand(SPCore spc) {
		Optional<XSound> xs = XSound.matchXSound(spc.getConfig().getString("options.sounds.value_perm"));
		Optional<XSound> xs1 = XSound.matchXSound(spc.getConfig().getString("options.sounds.value_reload"));
		Optional<XSound> xs2 = XSound.matchXSound(spc.getConfig().getString("options.sounds.value_cooldown"));
		if(xs.isPresent() || xs1.isPresent()) {
			this.sound = xs.get().parseSound();
			this.sound1 = xs1.get().parseSound();
			this.sound2 = xs2.get().parseSound();
		} else {
			this.sound = XSound.ENTITY_ITEM_BREAK.parseSound();
			this.sound1 = XSound.ENTITY_ENDER_DRAGON_FLAP.parseSound();
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
		String prefix = lang.getString("messages.prefix");
		if(sender instanceof Player) {
			Player player = (Player) sender;
			if(args.length == 0) {
				player.sendMessage(Msg.color(prefix + "Running on version &e" + spc.getVersionOnRunning() + " &7by &aiAlqjDV&7."));
				return true;
			} 
			
			if(args[0].equalsIgnoreCase("list")) {
				if(player.hasPermission("spcore.cmd.list") || player.isOp()) {
					String list = lang.getString("messages.list_cmds");
					String[] cmds = list.split("\\n");
					for(int i = 0 ; i < cmds.length ; i++) {
						String list_cmds = cmds[i];
						list_cmds = Msg.color(list_cmds);
						player.sendMessage(list_cmds);
					}
				} else {
					String message = lang.getString("messages.not_perm");
					message = message.replace("<permission>", "spcore.cmd.list");
					player.sendMessage(Msg.color(prefix + message));
				}
				return true;
			}
			
			if(args[0].equalsIgnoreCase("reload")) {
				if(player.hasPermission("spcore.cmd.reload") || player.isOp()) {
					if(args.length == 1) {
						player.sendMessage(Msg.color(prefix + lang.getString("messages.reload_usage")));
						return true;
					}
					
					if(args[1].equalsIgnoreCase("config")) {
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
						long RELOAD = System.currentTimeMillis();
						spc.reload("config");
						String message = lang.getString("messages.reload_config");
						message = message.replace("<time>", (System.currentTimeMillis() - RELOAD) + "");
						if(config.getBoolean("options.sounds.reproduce_sounds")) {
							executeNext(player);
						}
						player.sendMessage(Msg.color(prefix + message));
						return true;
					}
					
					if(args[1].equalsIgnoreCase("data")) {
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
						long RELOAD = System.currentTimeMillis();
						spc.reload("data");
						String message = lang.getString("messages.reload_data");
						message = message.replace("<time>", (System.currentTimeMillis() - RELOAD) + "");
						if(config.getBoolean("options.sounds.reproduce_sounds")) {
							executeNext(player);
						}
						player.sendMessage(Msg.color(prefix + message));
						return true;
					}
					
					if(args[1].equalsIgnoreCase("lang")) {
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
						long RELOAD = System.currentTimeMillis();
						spc.reload("lang");
						String message = lang.getString("messages.reload_lang");
						message = message.replace("<time>", (System.currentTimeMillis() - RELOAD) + "");
						if(config.getBoolean("options.sounds.reproduce_sounds")) {
							executeNext(player);
						}
						player.sendMessage(Msg.color(prefix + message));
						return true;
					}
					
					player.sendMessage(Msg.color(prefix + lang.getString("messages.not_file")));
					return false;
					
				} else {
					String message = lang.getString("messages.not_perm");
					message = message.replace("<permission>", "spcore.cmd.list");
					if(config.getBoolean("options.sounds.reproduce_sounds")) {
						execute(player);
					}
					player.sendMessage(Msg.color(prefix + message));
				}
				return true;
			}
			
			player.sendMessage(Msg.color(prefix + lang.getString("messages.not_command")));
			return false;
			
		} else {
			if(args.length == 0) {
				sender.sendMessage(Msg.color(prefix + "Running on version &e" + spc.getVersionOnRunning() + " &7by &aiAlqjDV&7."));
				return true;
			} 
			
			if(args[0].equalsIgnoreCase("list")) {
				if(sender.hasPermission("spcore.cmd.list") || sender.isOp()) {
					String list = lang.getString("messages.list_cmds");
					String[] cmds = list.split("\\n");
					for(int i = 0 ; i < cmds.length ; i++) {
						String list_cmds = cmds[i];
						list_cmds = Msg.color(list_cmds);
						sender.sendMessage(list_cmds);
					}
				} else {
					String message = lang.getString("messages.not_perm");
					message = message.replace("<permission>", "spcore.cmd.list");
					sender.sendMessage(Msg.color(prefix + message));
				}
				return true;
			}
			
			if(args[0].equalsIgnoreCase("reload")) {
				if(sender.hasPermission("spcore.cmd.reload") || sender.isOp()) {
					if(args.length == 1) {
						sender.sendMessage(Msg.color(prefix + lang.getString("messages.reload_usage")));
						return true;
					}
					
					if(args[1].equalsIgnoreCase("config")) {
						long RELOAD = System.currentTimeMillis();
						spc.reload("config");
						String message = lang.getString("messages.reload_config");
						message = message.replace("<time>", (System.currentTimeMillis() - RELOAD) + "");
						sender.sendMessage(Msg.color(prefix + message));
						return true;
					}
					
					if(args[1].equalsIgnoreCase("data")) {
						long RELOAD = System.currentTimeMillis();
						spc.reload("data");
						String message = lang.getString("messages.reload_data");
						message = message.replace("<time>", (System.currentTimeMillis() - RELOAD) + "");
						sender.sendMessage(Msg.color(prefix + message));
						return true;
					}
					
					if(args[1].equalsIgnoreCase("lang")) {
						long RELOAD = System.currentTimeMillis();
						spc.reload("lang");
						String message = lang.getString("messages.reload_lang");
						message = message.replace("<time>", (System.currentTimeMillis() - RELOAD) + "");
						sender.sendMessage(Msg.color(prefix + message));
						return true;
					}
					
					sender.sendMessage(Msg.color(prefix + lang.getString("messages.not_file")));
					return false;
					
				} else {
					String message = lang.getString("messages.not_perm");
					message = message.replace("<permission>", "spcore.cmd.list");
					sender.sendMessage(Msg.color(prefix + message));
				}
				return true;
			}
			
			sender.sendMessage(Msg.color(prefix + lang.getString("messages.not_command")));
			return false;
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
