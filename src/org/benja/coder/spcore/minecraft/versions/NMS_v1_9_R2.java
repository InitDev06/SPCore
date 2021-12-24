package org.benja.coder.spcore.minecraft.versions;

import org.benja.coder.spcore.minecraft.NMS;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_9_R2.IChatBaseComponent;
import net.minecraft.server.v1_9_R2.Packet;
import net.minecraft.server.v1_9_R2.PacketPlayOutChat;
import net.minecraft.server.v1_9_R2.PacketPlayOutTitle;
import net.minecraft.server.v1_9_R2.PlayerConnection;

public class NMS_v1_9_R2 implements NMS {

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

	@SuppressWarnings("rawtypes")
	@Override
	public void sendActionBar(Player player, String text) {
		IChatBaseComponent icbc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + text + "\"}");
	    PacketPlayOutChat bar = new PacketPlayOutChat(icbc, (byte)2);
	    (((CraftPlayer)player).getHandle()).playerConnection.sendPacket((Packet)bar);
	}
}
