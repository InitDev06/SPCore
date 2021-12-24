package org.benja.coder.spcore.minecraft.versions;

import org.benja.coder.spcore.minecraft.NMS;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.minecraft.server.v1_16_R3.IChatBaseComponent;
import net.minecraft.server.v1_16_R3.Packet;
import net.minecraft.server.v1_16_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_16_R3.PlayerConnection;

public class NMS_v1_16_R3 implements NMS {

	@SuppressWarnings("rawtypes")
	@Override
	public void sendTitle(Player player, String title, String subtitle, int fadeIn, int showTime, int fadeOut) {
		PlayerConnection pConn = (((CraftPlayer)player).getHandle()).playerConnection;
        PacketPlayOutTitle pTitleInfo = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TIMES, null, fadeIn, showTime, fadeOut);
        pConn.sendPacket((Packet)pTitleInfo);
        if (subtitle != null)
        {
            IChatBaseComponent iComp = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + subtitle + "\"}");
            PacketPlayOutTitle pSubtitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, iComp);
            pConn.sendPacket((Packet)pSubtitle);
        }
        if (title != null)
        {
            IChatBaseComponent iComp = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + title + "\"}");
            PacketPlayOutTitle pTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, iComp);
            pConn.sendPacket((Packet)pTitle);
        }
	}

	@Override
	public void sendActionBar(Player player, String text) {
		player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new ComponentBuilder(text).create());
	}
}
