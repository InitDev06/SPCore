package org.benja.coder.spcore.utility;

import java.util.HashMap;

import org.benja.coder.spcore.SPCore;

/**
 * 
 * @author vomarek
 *
 */
public class CooldownChat {

	private final SPCore spc;
	private final HashMap<String, Integer> cooldowns;
	
	public CooldownChat(SPCore spc) {
		this.spc = spc;
		this.cooldowns = new HashMap<>();
	}
	
	public void setCooldown(final String uuid) {
		cooldowns.put(uuid, (int)System.currentTimeMillis() / 1000);
	}
	
	public boolean hasCooldown(final String uuid) {
		if(!(cooldowns.containsKey(uuid))) return false;
		
		if(cooldowns.containsKey(uuid)) {
			if(((int) System.currentTimeMillis() / 1000) - cooldowns.get(uuid) < spc.getConfig().getInt("options.chat.cooldown")) {
				return true;
			}
		}
		return false;
	}
	
	public void removeCooldown(final String uuid) {
		cooldowns.remove(uuid);
	}
	
	public Integer getCooldown(final String uuid) {
		if(!(cooldowns.containsKey(uuid))) return null;
		return spc.getConfig().getInt("options.chat.cooldown") - (((int) System.currentTimeMillis() / 1000) - cooldowns.get(uuid));
	}
}

