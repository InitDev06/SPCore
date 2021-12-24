package org.benja.coder.spcore.commands;

import java.util.Optional;

import org.benja.coder.spcore.SPCore;
import org.benja.coder.spcore.color.Msg;
import org.benja.coder.spcore.xseries.XSound;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class GamemodeManager implements CommandExecutor {

	private final SPCore spc;
	private final Sound sound;
	private final Sound sound1;
	private final Sound sound2;
	private final int vol;
	private final int pitch;
	
	public GamemodeManager(SPCore spc) {
		Optional<XSound> xs = XSound.matchXSound(spc.getConfig().getString("options.sounds.value_perm"));
		Optional<XSound> xs1 = XSound.matchXSound(spc.getConfig().getString("options.sounds.value_gamemode"));
		Optional<XSound> xs2 = XSound.matchXSound(spc.getConfig().getString("options.sounds.value_cooldown"));
		if(xs.isPresent() || xs1.isPresent() || xs2.isPresent()) {
			this.sound = xs.get().parseSound();
			this.sound1 = xs1.get().parseSound();
			this.sound2 = xs2.get().parseSound();
		} else {
			this.sound = XSound.ENTITY_ITEM_BREAK.parseSound();
			this.sound1 = XSound.UI_BUTTON_CLICK.parseSound();
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
		if(sender instanceof Player){
            Player player = (Player) sender;
            if(player.hasPermission("spcore.cmd.gamemode") || player.isOp()) {
            	if(args.length == 0) {
            		player.sendMessage(Msg.color(lang.getString("messages.command.gamemode.usage")));
            		return true;
            	}
            	
            	if(args[0].equals("0") || args[0].equalsIgnoreCase("survival") || args[0].equalsIgnoreCase("supervivencia")) {
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
            		player.setGameMode(GameMode.SURVIVAL);
            		if(config.getBoolean("options.sounds.reproduce_sounds")) {
            			executeNext(player);
            		}
            		player.sendMessage(Msg.color(lang.getString("messages.command.gamemode.survival")));
            		return true;
            	}
            	
            	if(args[0].equals("1") || args[0].equalsIgnoreCase("creative") || args[0].equalsIgnoreCase("creativo")) {
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
            		player.setGameMode(GameMode.CREATIVE);
            		if(config.getBoolean("options.sounds.reproduce_sounds")) {
            			executeNext(player);
            		}
            		player.sendMessage(Msg.color(lang.getString("messages.command.gamemode.creative")));
            		return true;
            	}
            	
            	if(args[0].equals("2") || args[0].equalsIgnoreCase("adventure") || args[0].equalsIgnoreCase("aventura")) {
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
            		player.setGameMode(GameMode.ADVENTURE);
            		if(config.getBoolean("options.sounds.reproduce_sounds")) {
            			executeNext(player);
            		}
            		player.sendMessage(Msg.color(lang.getString("messages.command.gamemode.adventure")));
            		return true;
            	}
            	
            	if(args[0].equals("3") || args[0].equalsIgnoreCase("spectator") || args[0].equalsIgnoreCase("espectador")) {
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
            		player.setGameMode(GameMode.SPECTATOR);
            		if(config.getBoolean("options.sounds.reproduce_sounds")) {
            			executeNext(player);
            		}
            		player.sendMessage(Msg.color(lang.getString("messages.command.gamemode.spectator")));
            		return true;
            	}
            	
            	player.sendMessage(Msg.color(lang.getString("messages.command.gamemode.null_identifier")));
        		return false;
            } else {
            	String message = lang.getString("messages.not_perm");
    			message = message.replace("<permission>", "spcore.cmd.gamemode");
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
