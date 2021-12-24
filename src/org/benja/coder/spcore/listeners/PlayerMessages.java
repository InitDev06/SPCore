package org.benja.coder.spcore.listeners;

import org.benja.coder.spcore.SPCore;
import org.benja.coder.spcore.color.Msg;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class PlayerMessages implements Listener {

	private final SPCore spc;
	
	public PlayerMessages(SPCore spc) {
		this.spc = spc;
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onJoinNetworks(PlayerJoinEvent ev) {
		Player player = ev.getPlayer();
		FileConfiguration config = spc.getConfig();
		if(config.getBoolean("options.social_networks")) {
			String name = config.getString("options.networks.store.name");
			String url = config.getString("options.networks.store.link");
			String desc = config.getString("options.networks.store.description");
			
			name = Msg.color(name);
			desc = Msg.color(desc);
			desc = PlaceholderAPI.setPlaceholders(player, desc);
			
			String name1 = config.getString("options.networks.forum.name");
			String url1 = config.getString("options.networks.forum.link");
			String desc1 = config.getString("options.networks.forum.description");
			
			name1 = Msg.color(name1);
			desc1 = Msg.color(desc1);
			desc1 = PlaceholderAPI.setPlaceholders(player, desc1);
			
			String name2 = config.getString("options.networks.youtube.name");
			String url2 = config.getString("options.networks.youtube.link");
			String desc2 = config.getString("options.networks.youtube.description");
			
			name2 = Msg.color(name2);
			desc2 = Msg.color(desc2);
			desc2 = PlaceholderAPI.setPlaceholders(player, desc2);
			
			String name3 = config.getString("options.networks.discord.name");
			String url3 = config.getString("options.networks.discord.link");
			String desc3 = config.getString("options.networks.discord.description");
			
			name3 = Msg.color(name3);
			desc3 = Msg.color(desc3);
			desc3 = PlaceholderAPI.setPlaceholders(player, desc3);
			
			TextComponent BUTTON = new TextComponent(name);
			BUTTON.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
			BUTTON.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(desc).create()));
			
			TextComponent BUTTON1 = new TextComponent(name1);
			BUTTON1.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url1));
			BUTTON1.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(desc1).create()));
			
			TextComponent BUTTON2 = new TextComponent(name2);
			BUTTON2.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url2));
			BUTTON2.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(desc2).create()));
			
			TextComponent BUTTON3 = new TextComponent(name3);
			BUTTON3.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url3));
			BUTTON3.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(desc3).create()));
			
			BUTTON.addExtra(BUTTON1);
			BUTTON.addExtra(BUTTON2);
			BUTTON.addExtra(BUTTON3);
			
			player.spigot().sendMessage(BUTTON);
		}
	}
}
