package me.drmayx.voxelmoneymaker.voxelversemoneymaker.utils;

import net.minecraft.server.v1_14_R1.ChatMessageType;
import net.minecraft.server.v1_14_R1.IChatBaseComponent;
import net.minecraft.server.v1_14_R1.PacketPlayOutChat;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class ActionBarUtils {

    public static void sendActionBarMessage(Player p, String message) {
        IChatBaseComponent icbc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + CC(message) + "\"}");
        PacketPlayOutChat bar = new PacketPlayOutChat(icbc, ChatMessageType.GAME_INFO);
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(bar);
    }

    private static String CC(String s) {
        // return an empty string if given string is null
        if(s == null) {
            return "";
        }
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
