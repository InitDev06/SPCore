package org.benja.coder.spcore;

import java.io.File;

import org.benja.coder.spcore.color.Msg;
import org.benja.coder.spcore.commands.FlightCommand;
import org.benja.coder.spcore.commands.GamemodeManager;
import org.benja.coder.spcore.commands.LobbyCommand;
import org.benja.coder.spcore.commands.MainCommand;
import org.benja.coder.spcore.commands.NickerCommand;
import org.benja.coder.spcore.commands.ScoreboardCommand;
import org.benja.coder.spcore.commands.SetupCommand;
import org.benja.coder.spcore.commands.TabComplete;
import org.benja.coder.spcore.commands.TeleportCommand;
import org.benja.coder.spcore.commands.VanishCommand;
import org.benja.coder.spcore.listeners.GroupsChatListener;
import org.benja.coder.spcore.listeners.GroupsJoinListener;
import org.benja.coder.spcore.listeners.GroupsQuitListener;
import org.benja.coder.spcore.listeners.PlayerJoinListener;
import org.benja.coder.spcore.listeners.PlayerListener;
import org.benja.coder.spcore.listeners.PlayerMessages;
import org.benja.coder.spcore.listeners.PlayerMotd;
import org.benja.coder.spcore.listeners.WorldProtection;
import org.benja.coder.spcore.scoreboard.ScoreboardBuilder;
import org.benja.coder.spcore.utility.CooldownCMD;
import org.benja.coder.spcore.utility.CooldownChat;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.clip.placeholderapi.PlaceholderAPI;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;

public class SPCore extends JavaPlugin implements Listener {

	PluginDescriptionFile pdffile = getDescription();
	private final String DEVELOPER_NAME = "iAlqjDV";
	private final String VERSION_ID = pdffile.getVersion();
	private final ConsoleCommandSender log = Bukkit.getConsoleSender();
	private CooldownCMD cooldownCMD;
	private CooldownChat cooldownChat;
	private FileConfiguration config;
	private FileConfiguration data;
	private FileConfiguration lang;
	private VersionController vc;
	
	public Chat chat = null;
	public Permission permission = null;
	
	public String getDevName() {
		return DEVELOPER_NAME;
	}
	
	public String getVersionOnRunning() {
		return VERSION_ID;
	}
	
	@Override
	public void onEnable() {
		long START = System.currentTimeMillis();
		
		log.sendMessage("");
		log.sendMessage(Msg.color("&6      SPCore"));
		log.sendMessage("");
		log.sendMessage(Msg.color("&f  Author: &b" + DEVELOPER_NAME));
		log.sendMessage(Msg.color("&f  Version: &b" + VERSION_ID));
		log.sendMessage(Msg.color(""));
				
		try {
			Class.forName("org.spigotmc.SpigotConfig");
		} catch(ClassNotFoundException ex) {
			ex.printStackTrace();
		}
		
		create("config");
		create("data");
		create("lang");
		config = getFile("config");
		data = getFile("data");
		lang = getFile("lang");
		setupCommands();
		setupListeners();
		cooldownCMD = new CooldownCMD(this);
		cooldownChat = new CooldownChat(this);
		if(getServer().getPluginManager().getPlugin("Vault") != null) {
			setupChat();
			setupPermission();
			log.sendMessage(Msg.color("&a  Vault hooked correctly"));
		} else {
			log.sendMessage(Msg.color("&a  Vault not hooked"));
		}
		if(getConfig().getBoolean("options.scoreboard.refresh")) {
			new BukkitRunnable() {		
				@Override
				public void run() {
					for(Player player : Bukkit.getOnlinePlayers()) {
						updateScoreboard(player);
					}
				}
			}.runTaskTimer(this, getConfig().getLong("options.scoreboard.delay"), getConfig().getLong("options.scoreboard.period"));
		} else {
			for(Player player : Bukkit.getOnlinePlayers()) {
				updateScoreboard(player);
			}
		}
		vc = new VersionController(this);
		
		log.sendMessage(Msg.color("&a  Enabled in &e" + (System.currentTimeMillis() - START) + "ms&a."));
		log.sendMessage("");
	}
	
	public void createScoreboard(Player player) {
		ScoreboardBuilder builder = ScoreboardBuilder.createScore(player);
		World world = player.getWorld();
		String list = "";
		if(getConfig().getBoolean("options.scoreboard.per_worlds")) {
			if(getConfig().getConfigurationSection("options.scoreboard.types." + world.getName()) != null) {
				builder.setTitle(Msg.color(getConfig().getString("options.scoreboard.types." + world.getName() + ".name")));
				list = getConfig().getString("options.scoreboard.types." + world.getName() + ".lines");
				String[] lines = list.split("\\n");
				for(int i = 0 ; i < lines.length ; i++) {
					String list_lines = lines[i];
					list_lines = Msg.color(list_lines);
					list_lines = PlaceholderAPI.setPlaceholders(player, list_lines);
					builder.setSlot(lines.length - i, list_lines);
				}
			} else {
				list = getConfig().getString("options.scoreboard.default");
				builder.setTitle(Msg.color(getConfig().getString("options.scoreboard.default.name")));
				String[] lines = list.split("\\n");
				for(int i = 0 ; i < lines.length ; i++) {
					String list_lines = lines[i];
					list_lines = Msg.color(list_lines);
					list_lines = PlaceholderAPI.setPlaceholders(player, list_lines);
					builder.setSlot(lines.length - i, list_lines);
				}
			}
		} else {
			list = getConfig().getString("options.scoreboard.default");
			builder.setTitle(Msg.color(getConfig().getString("options.scoreboard.default.name")));
			String[] lines = list.split("\\n");
			for(int i = 0 ; i < lines.length ; i++) {
				String list_lines = lines[i];
				list_lines = Msg.color(list_lines);
				list_lines = PlaceholderAPI.setPlaceholders(player, list_lines);
				builder.setSlot(lines.length - i, list_lines);
			}
		}
	}
	
	public VersionController getVc() {
		return vc;
	}
	
	public CooldownCMD getCooldownsCMD() {
		return cooldownCMD;
	}
	
	public CooldownChat getCooldownsChat() {
		return cooldownChat;
	}
	
	public FileConfiguration getConfig() {
		return config;
	}
	
	public FileConfiguration getLang() {
		return lang;
	}
	
	public FileConfiguration getData() {
		return data;
	}
	
	public void reload(String name) {
		if(name.equals("config")) {
			config = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "config.yml"));
		} else if(name.equals("data")) {
			data = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "data.yml"));
		} else if(name.equals("lang")) {
			lang = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "lang_en.yml"));
		} else {
			getLogger().warning("This file doesn't exists.");
		}
	}

	private void create(String name) {
		File file;
		if(name.equals("config")) {
			file = new File(getDataFolder(), "config.yml");
			if(!(file.exists())) {
				saveResource("config.yml", false);
			}
		} else if(name.equals("data")) {
			file = new File(getDataFolder(), "data.yml");
			if(!(file.exists())) {
				saveResource("data.yml", false);
			}
		} else if(name.equals("lang")) {
			file = new File(getDataFolder(), "lang_en.yml");
			if(!(file.exists())) {
				saveResource("lang_en.yml", false);
			}
		} else {
			getLogger().warning("This file doesn't exists.");
		}
	}
	
	private FileConfiguration getFile(String name) {
		if(name.equals("config")) {
			return YamlConfiguration.loadConfiguration(new File(getDataFolder(), "config.yml"));
		} else if(name.equals("data")) {
			return YamlConfiguration.loadConfiguration(new File(getDataFolder(), "data.yml"));
		} else if(name.equals("lang")) {
			return YamlConfiguration.loadConfiguration(new File(getDataFolder(), "lang_en.yml"));
		} else {
			getLogger().warning("This file doesn't exists.");
		}
		return null;
	}
	
	private void setupCommands() {
		getCommand("spcore").setExecutor(new MainCommand(this));
		getCommand("flight").setExecutor(new FlightCommand(this));
		getCommand("scoreboard").setExecutor(new ScoreboardCommand(this));
		getCommand("setlobby").setExecutor(new SetupCommand(this));
		getCommand("vanish").setExecutor(new VanishCommand(this));
		getCommand("teleport").setExecutor(new TeleportCommand(this));
		getCommand("nick").setExecutor(new NickerCommand(this));
		getCommand("gamemode").setExecutor(new GamemodeManager(this));
		
		getCommand("spcore").setTabCompleter(new TabComplete());
		getCommand("gamemode").setTabCompleter(new TabComplete());
	}
	
	private void setupListeners() {
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new FlightCommand(this), this);
		pm.registerEvents(new GroupsJoinListener(this), this);
		pm.registerEvents(new PlayerJoinListener(this), this);
		pm.registerEvents(new GroupsChatListener(this), this);
		pm.registerEvents(new PlayerListener(this), this);
		pm.registerEvents(new PlayerMotd(this), this);
		pm.registerEvents(new PlayerMessages(this), this);
		pm.registerEvents(new WorldProtection(this), this);
		pm.registerEvents(new VanishCommand(this), this);
		pm.registerEvents(new GroupsQuitListener(this), this);
		pm.registerEvents(this, this);
	}
	
	private boolean setupChat() {
		RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
	    chat = (Chat)rsp.getProvider();
	    return (chat != null);
	}
	
	private boolean setupPermission() {
		RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
	    permission = (Permission)rsp.getProvider();
	    return (permission != null);
	}
	
	private void updateScoreboard(Player player) {
		if(ScoreboardBuilder.hasScore(player)) {
			ScoreboardBuilder builder = ScoreboardBuilder.getPlayer(player);
			World world = player.getWorld();
			String list = "";
			if(getConfig().getBoolean("options.scoreboard.per_worlds")) {
				if(getConfig().getConfigurationSection("options.scoreboard.types." + world.getName()) != null) {
					builder.setTitle(Msg.color(getConfig().getString("options.scoreboard.types." + world.getName() + ".name")));
					list = getConfig().getString("options.scoreboard.types." + world.getName() + ".lines");
					String[] lines = list.split("\\n");
					for(int i = 0 ; i < lines.length ; i++) {
						String list_lines = lines[i];
						list_lines = Msg.color(list_lines);
						list_lines = PlaceholderAPI.setPlaceholders(player, list_lines);
						builder.setSlot(lines.length - i, list_lines);
					}
				} else {
					list = getConfig().getString("options.scoreboard.default.lines");
					builder.setTitle(Msg.color(getConfig().getString("options.scoreboard.default.name")));
					String[] lines = list.split("\\n");
					for(int i = 0 ; i < lines.length ; i++) {
						String list_lines = lines[i];
						list_lines = Msg.color(list_lines);
						list_lines = PlaceholderAPI.setPlaceholders(player, list_lines);
						builder.setSlot(lines.length - i, list_lines);
					}
				}
			} else {
				list = getConfig().getString("options.scoreboard.default.lines");
				builder.setTitle(Msg.color(getConfig().getString("options.scoreboard.default.name")));
				String[] lines = list.split("\\n");
				for(int i = 0 ; i < lines.length ; i++) {
					String list_lines = lines[i];
					list_lines = Msg.color(list_lines);
					list_lines = PlaceholderAPI.setPlaceholders(player, list_lines);
					builder.setSlot(lines.length - i, list_lines);
				}
			}
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent ev) {
		Player player = ev.getPlayer();
		if(getConfig().getBoolean("options.scoreboard.show_to_join")) {
			createScoreboard(player);
		}
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent ev) {
		Player player = ev.getPlayer();
		if(ScoreboardBuilder.hasScore(player)) {
			ScoreboardBuilder.removeScore(player);
			player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
		}
	}
	
	@EventHandler
	public void onChangeWorld(PlayerChangedWorldEvent ev) {
		Player player = ev.getPlayer();
		if(ScoreboardBuilder.hasScore(player)) {
			ScoreboardBuilder.removeScore(player);
			player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
			createScoreboard(player);
		}
	}
}
