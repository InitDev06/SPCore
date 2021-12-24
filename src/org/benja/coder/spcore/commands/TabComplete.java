package org.benja.coder.spcore.commands;

import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class TabComplete implements TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String la, String[] args) {
		if(cmd.getName().equalsIgnoreCase("spcore")) {
			if(args.length == 1) {
				return Arrays.asList("list", "reload");
			}
		}
		
		if(cmd.getName().equalsIgnoreCase("gamemode")) {
			if(args.length == 1) {
				return Arrays.asList("survival", "creative", "adventure", "spectator");
			}
		}
		return null;
	}
}
