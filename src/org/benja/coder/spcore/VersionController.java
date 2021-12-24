package org.benja.coder.spcore;

import org.benja.coder.spcore.color.Msg;
import org.benja.coder.spcore.minecraft.NMS;
import org.benja.coder.spcore.minecraft.versions.NMS_v1_10_R1;
import org.benja.coder.spcore.minecraft.versions.NMS_v1_11_R1;
import org.benja.coder.spcore.minecraft.versions.NMS_v1_12_R1;
import org.benja.coder.spcore.minecraft.versions.NMS_v1_13_R2;
import org.benja.coder.spcore.minecraft.versions.NMS_v1_14_R1;
import org.benja.coder.spcore.minecraft.versions.NMS_v1_15_R1;
import org.benja.coder.spcore.minecraft.versions.NMS_v1_16_R3;
import org.benja.coder.spcore.minecraft.versions.NMS_v1_17_R1;
import org.benja.coder.spcore.minecraft.versions.NMS_v1_8_R3;
import org.benja.coder.spcore.minecraft.versions.NMS_v1_9_R2;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;

public class VersionController {

	private final SPCore spc;
	private final ConsoleCommandSender log;
	private String version;
	private NMS nms;
	
	public VersionController(SPCore spc) {
		this.spc = spc;
		this.log = Bukkit.getConsoleSender();
		setupVersion();
	}
	
	private void setupVersion() {
		try {
			version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
			
			switch(version) {
				case "v1_8_R1":
				case "v1_8_R2":	
					log.sendMessage(Msg.color("&c  You're using a older version of 1.8, please update to the release 1.8.8."));
					Bukkit.getScheduler().cancelTasks(spc);
					Bukkit.getPluginManager().disablePlugin(spc);
					return;
				case "v1_8_R3":
					nms = new NMS_v1_8_R3();
					log.sendMessage(Msg.color("&e  Using Minecraft 1.8.8"));
					return;
				case "v1_9_R1":	
					log.sendMessage(Msg.color("&c  You're using a older version of 1.9, please update to the release 1.9.4."));
					Bukkit.getScheduler().cancelTasks(spc);
					Bukkit.getPluginManager().disablePlugin(spc);
					return;
				case "v1_9_R2":
					nms = new NMS_v1_9_R2();
					log.sendMessage(Msg.color("&e  Using Minecraft 1.9.4"));
					return;
				case "v1_10_R1":
					nms = new NMS_v1_10_R1();
					log.sendMessage(Msg.color("&e  Using Minecraft 1.10.2"));
					return;
				case "v1_11_R1":
					nms = new NMS_v1_11_R1();
					log.sendMessage(Msg.color("&e  Using Minecraft 1.11.2"));
					return;
				case "v1_12_R1":
					nms = new NMS_v1_12_R1();
					log.sendMessage(Msg.color("&e  Using Minecraft 1.12.2"));
					return;
				case "v1_13_R1":	
					log.sendMessage(Msg.color("&c  You're using a older version of 1.13, please update to the release 1.13.2."));
					Bukkit.getScheduler().cancelTasks(spc);
					Bukkit.getPluginManager().disablePlugin(spc);
					return;
				case "v1_13_R2":
					nms = new NMS_v1_13_R2();
					log.sendMessage(Msg.color("&e  Using Minecraft 1.13.2"));
					return;
				case "v1_14_R1":
					nms = new NMS_v1_14_R1();
					log.sendMessage(Msg.color("&e  Using Minecraft 1.14.4"));
					return;
				case "v1_15_R1":
					nms = new NMS_v1_15_R1();
					log.sendMessage(Msg.color("&e  Using Minecraft 1.15.2"));
					return;
				case "v1_16_R1":
				case "v1_16_R2":	
					log.sendMessage(Msg.color("&c  You're using a older version of 1.16, please update to the release 1.16.5."));
					Bukkit.getScheduler().cancelTasks(spc);
					Bukkit.getPluginManager().disablePlugin(spc);
					return;
				case "v1_16_R3":
					nms = new NMS_v1_16_R3();
					log.sendMessage(Msg.color("&e  Using Minecraft 1.16.5"));
					return;
				case "v1_17_R1":	
					nms = new NMS_v1_17_R1();
					log.sendMessage(Msg.color("&e  Using Minecraft 1.17.1"));
					return;
			}
			log.sendMessage(Msg.color("&c  Your server version is not compatible with the plugin."));
			Bukkit.getScheduler().cancelTasks(spc);
			Bukkit.getPluginManager().disablePlugin(spc);
		} catch(ArrayIndexOutOfBoundsException ex) {
			log.sendMessage(Msg.color("&c  An occurred a error while checking your server version."));
			Bukkit.getScheduler().cancelTasks(spc);
			Bukkit.getPluginManager().disablePlugin(spc);
			return;
		}
	}
	
	public NMS get() {
		return nms;
	}
}
