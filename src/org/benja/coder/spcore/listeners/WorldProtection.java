package org.benja.coder.spcore.listeners;

import java.util.Optional;

import org.benja.coder.spcore.SPCore;
import org.benja.coder.spcore.color.Msg;
import org.benja.coder.spcore.xseries.XSound;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public class WorldProtection implements Listener {

	private final SPCore spc;
	private final Sound sound;
	private final int vol;
	private final int pitch;
	
	public WorldProtection(SPCore spc) {
		Optional<XSound> xs = XSound.matchXSound(spc.getConfig().getString("options.sounds.value_world_event"));
		if(xs.isPresent()) {
			this.sound = xs.get().parseSound();
		} else {
			this.sound = XSound.ENTITY_VILLAGER_DEATH.parseSound();
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
	public void onPlaceBlock(BlockPlaceEvent ev) {
		FileConfiguration config = spc.getConfig();
		FileConfiguration data = spc.getData();
		FileConfiguration lang = spc.getLang();
		Player player = ev.getPlayer();
		if(data.getBoolean("build") == false) {
			if(player.hasPermission("spcore.bypass.world.build") || player.isOp()) {
				return;
			} else {
				ev.setCancelled(true);
				if(config.getBoolean("options.sounds.reproduce_sounds")) {
					execute(player);
				}
				player.sendMessage(Msg.color(lang.getString("messages.not_place")));
			}
		}
	}
	
	@EventHandler
	public void onBreakBlock(BlockBreakEvent ev) {
		FileConfiguration config = spc.getConfig();
		FileConfiguration data = spc.getData();
		FileConfiguration lang = spc.getLang();
		Player player = ev.getPlayer();
		if(data.getBoolean("build") == false) {
			if(player.hasPermission("spcore.bypass.world.build") || player.isOp()) {
				return;
			} else {
				ev.setCancelled(true);
				if(config.getBoolean("options.sounds.reproduce_sounds")) {
					execute(player);
				}
				player.sendMessage(Msg.color(lang.getString("messages.not_break")));
			}
		}
	}
	
	@SuppressWarnings("incomplete-switch")
	@EventHandler
	public void onDamage(EntityDamageEvent ev) {
		FileConfiguration data = spc.getData();
		Player player = (Player) ev.getEntity();
		DamageCause cause = ev.getCause();
		if(data.getBoolean("interact") == false) {
			if(player.hasPermission("spcore.bypass.world.damage") || player.isOp()) {
				return;
			} else {
				switch(cause) {
					case BLOCK_EXPLOSION:
						ev.setCancelled(true);
						break;
					case LAVA:
						ev.setCancelled(true);
						break;
					case WITHER:
						ev.setCancelled(true);
						break;
					case SUICIDE:
						ev.setCancelled(true);
						break;
					case THORNS:
						ev.setCancelled(true);
						break;
					case SUFFOCATION:
						ev.setCancelled(true);
						break;
					case FALL:
						ev.setCancelled(true);
						break;
					case FALLING_BLOCK:
						ev.setCancelled(true);
						break;
					case POISON:
						ev.setCancelled(true);
						break;
					case PROJECTILE:
						ev.setCancelled(true);
						break;
					case FIRE:
						ev.setCancelled(true);
						break;
					case ENTITY_ATTACK:
						ev.setCancelled(true);
						break;
					case ENTITY_EXPLOSION:
						ev.setCancelled(true);
						break;
				}
			}
		}
	}
	
	@EventHandler
	public void onEntites(EntitySpawnEvent ev) {
		FileConfiguration data = spc.getData();
		if(data.getBoolean("entities") == false) {
			ev.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onThunder(WeatherChangeEvent ev) {
		FileConfiguration data = spc.getData();
		if(data.getBoolean("thunder") == false) {
			ev.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onHunger(FoodLevelChangeEvent ev) {
		FileConfiguration data = spc.getData();
		Player player = (Player) ev.getEntity();
		if(data.getBoolean("hunger") == false) {
			if(player.hasPermission("spcore.bypass.world.hunger") || player.isOp()) {
				return;
			} else {
				ev.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onVoid(EntityDamageEvent ev) {
		FileConfiguration data = spc.getData();
		Player player = (Player) ev.getEntity();
		if(data.getBoolean("void") == false) {
			if(player.hasPermission("spcore.bypass.world.void") || player.isOp()) {
				return;
			} else {
				Double x = Double.valueOf(data.getInt("lobby.x"));
				Double y = Double.valueOf(data.getInt("lobby.y"));
				Double z = Double.valueOf(data.getInt("lobby.z"));
				
				Float yaw = Float.valueOf(data.getInt("lobby.yaw"));
				Float pitch = Float.valueOf(data.getInt("lobby.pitch"));
				
				World world = Bukkit.getServer().getWorld(data.getString("lobby.world"));
				
				Location location = new Location(world, x, y, z, yaw, pitch);
				location.subtract(0.5D, 0.0D, 0.5D);
				if(ev.getCause() == EntityDamageEvent.DamageCause.VOID) {
					player.setFallDistance(0.0F);
					
					if(world == null) return;
					
					Bukkit.getScheduler().scheduleSyncDelayedTask(spc, () -> player.teleport(location), 3L);
					ev.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent ev) {
		ev.setDeathMessage(null);
	}
	
	private void execute(Player player) {
		player.playSound(player.getLocation(), getSound(), getVol(), getPitch());
	}
}
