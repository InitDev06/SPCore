package org.benja.coder.spcore.listeners;

import org.benja.coder.spcore.SPCore;
import org.benja.coder.spcore.color.Msg;
import org.benja.coder.spcore.utility.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.FireworkEffect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionEffectType;

public class PlayerJoinListener implements Listener {

	private final SPCore spc;
	
	public PlayerJoinListener(SPCore spc) {
		this.spc = spc;
	}
	
	@EventHandler
	public void onTeleport(PlayerJoinEvent ev) {
		Player player = ev.getPlayer();
		FileConfiguration data = spc.getData();
		Double x = Double.valueOf(data.getInt("lobby.x"));
		Double y = Double.valueOf(data.getInt("lobby.y"));
		Double z = Double.valueOf(data.getInt("lobby.z"));
		
		Float yaw = Float.valueOf(data.getInt("lobby.yaw"));
		Float pitch = Float.valueOf(data.getInt("lobby.pitch"));
		
		World world = Bukkit.getServer().getWorld(data.getString("lobby.world"));
		
		if(world != null) {
			Location location = new Location(world, x, y, z, yaw, pitch);
			location.add(x > 0 ? 0.5 : -0.5, 0.0, z > 0 ? 0.5 : -0.5);
			player.teleport(location);
		}
	}
	
	@EventHandler
	public void onJoinTitle(PlayerJoinEvent ev) {
		Player player = ev.getPlayer();
		FileConfiguration config = spc.getConfig();
		if(config.getBoolean("options.titles.show_titles")) {
			String title = config.getString("options.titles.title_joined");
			String subtitle = config.getString("options.titles.subtitle_joined");
			
			title = Msg.color(title);
			title = StringUtil.setPlaceholders(title, player);
			subtitle = Msg.color(subtitle);
			subtitle = StringUtil.setPlaceholders(subtitle, player);
			
			int fadeIn = config.getInt("options.titles.fadeIn");
			int showTime = config.getInt("options.titles.showTime");
			int fadeOut = config.getInt("options.titles.fadeOut");
			
			spc.getVc().get().sendTitle(player, title, subtitle, fadeIn, showTime, fadeOut);
		}
	}
	
	@EventHandler
	public void onJoinActionBar(PlayerJoinEvent ev) {
		Player player = ev.getPlayer();
		FileConfiguration config = spc.getConfig();
		if(config.getBoolean("options.actionbar")) {
			String actionbar = config.getString("options.actionbar_message");
			actionbar = StringUtil.setPlaceholders(actionbar, player);
			spc.getVc().get().sendActionBar(player, Msg.color(actionbar));
		}
	}
	
	@EventHandler
	public void onJoinSettings(PlayerJoinEvent ev) {
		Player player = ev.getPlayer();
		FileConfiguration config = spc.getConfig();
		if(config.getBoolean("options.give_speed")) {
			if(!(player.hasPotionEffect(PotionEffectType.SPEED))) {
				player.addPotionEffect(PotionEffectType.SPEED.createEffect(1000000, 1));
			}
		}
		
		player.setGameMode(GameMode.SURVIVAL);
		player.setHealth(20);
		player.setFoodLevel(20);
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onJoinEffect(PlayerJoinEvent ev) {
		Player player = ev.getPlayer();
		FileConfiguration config = spc.getConfig();
		String type = config.getString("options.effects.type");
		int power = config.getInt("options.effects.power");
		if(config.getBoolean("options.effects")) {
			if(player.hasPermission("spcore.event.join.effect") || player.isOp()) {
				player.playEffect(player.getLocation(), Effect.valueOf(type), power);
			}
		}
	}
	
	@EventHandler
	public void onJoinFirework(PlayerJoinEvent ev) {
		Player player = ev.getPlayer();
		FileConfiguration config = spc.getConfig();
		String type = config.getString("options.fireworks.type");
		int power = config.getInt("options.fireworks.power");
		if(config.getBoolean("options.show_fireworks")) {
			if(player.hasPermission("spcore.event.join.firework") || player.isOp()) {
				Firework firework = (Firework) player.getLocation().getWorld().spawn(player.getLocation(), Firework.class);
				FireworkMeta fireworkMeta = firework.getFireworkMeta();
				fireworkMeta.addEffect(FireworkEffect.builder()
						.flicker(false)
						.trail(true)
						.with(FireworkEffect.Type.valueOf(type))
						.withColor(Color.YELLOW)
						.withColor(Color.BLUE)
						.build());
				
				fireworkMeta.setPower(power);
				firework.setFireworkMeta(fireworkMeta);
			}
		}
	}
}
