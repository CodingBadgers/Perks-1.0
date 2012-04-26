package me.wisbycraft.perks.listeners;

import me.wisbycraft.perks.utils.PerkPlayer;
import me.wisbycraft.perks.utils.PerkUtils;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.dthielke.herochat.ChannelChatEvent;

public class PerksHeroChatListener implements Listener {

	@EventHandler (priority = EventPriority.NORMAL)
	public void onPlayerChat(ChannelChatEvent event) {
		PerkPlayer player = PerkUtils.getPlayer(event.getBukkitEvent().getPlayer());
		
		if (player.isVanished() 
				&& !event.getChannel().getName().equalsIgnoreCase("Staff")) {
			event.getBukkitEvent().setCancelled(true);
			PerkUtils.OutputToPlayer(player, "You cannot talk whist vanished");
		}
	}
}
