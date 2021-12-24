package org.benja.coder.spcore.scoreboard;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.benja.coder.spcore.color.Msg;
import org.benja.coder.spcore.utility.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

/**
 * 
 * @author crisdev333
 *
 */
public class ScoreboardBuilder {
	
	private static final HashMap<UUID, ScoreboardBuilder> players = new HashMap<>();
	private Player player;
	
	public static boolean hasScore(Player player) {
		return players.containsKey(player.getUniqueId());
	}
	
	public static ScoreboardBuilder createScore(Player player) {
		return new ScoreboardBuilder(player);
	}
	
	public static ScoreboardBuilder getPlayer(Player player) {
		return players.get(player.getUniqueId());
	}
	
	public static ScoreboardBuilder removeScore(Player player) {
		return players.remove(player.getUniqueId());
	}
	
	private Scoreboard scoreboard;
	private Objective sidebar;
	
	@SuppressWarnings("deprecation")
	public ScoreboardBuilder(Player player) {
		this.player = player;
		scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		sidebar = scoreboard.registerNewObjective("sidebar", "dummy");
		sidebar.setDisplaySlot(DisplaySlot.SIDEBAR);
		for(int i = 1 ; i <= 15 ; i++) {
			Team team = scoreboard.registerNewTeam("SLOT_" + i);
			team.addEntry(genEntry(i));
		}
		player.setScoreboard(scoreboard);
		players.put(player.getUniqueId(), this);
	}
	
	public void setTitle(String title) {
		title = Msg.color(title);
		sidebar.setDisplayName(title.length() > 32 ? title.substring(0, 32) : title);
	}
	
	public void setSlot(int slot, String text) {
		Team team = scoreboard.getTeam("SLOT_" + slot);
		String entry = genEntry(slot);
		if(!(scoreboard.getEntries().contains(entry))) {
			sidebar.getScore(entry).setScore(slot);
		}
		
		text = Msg.color(text);
		text = StringUtil.setPlaceholders(text, this.player);
		String prefix = getFirstSplit(text);
		String suffix = getFirstSplit(ChatColor.getLastColors(prefix) + getSecondSplit(text));
		team.setPrefix(prefix);
		team.setSuffix(suffix);
	}
	
	public void removeSlot(int slot) {
		String entry = genEntry(slot);
		if(scoreboard.getEntries().contains(entry)) {
			scoreboard.resetScores(entry);
		}
	}
	
	public void setSlotsFromList(List<String> list) {
		while(list.size() > 15) {
			list.remove(list.size() - 1);
		}
		
		int slot = list.size();
		if(slot < 15) {
			for(int i = (slot + 1) ; i <= 15 ; i++) {
				removeSlot(i);
			}
		}
	}
	
	private String genEntry(int slot) {
		return ChatColor.values()[slot].toString();
	}
	
	private String getFirstSplit(String str) {
		return str.length() > 16 ? str.substring(0, 16) : str;
	}
	
	private String getSecondSplit(String str) {
		if(str.length() > 32) {
			str = str.substring(0, 32);
		}
		return str.length() > 16 ? str.substring(16) : " ";
	}
}

