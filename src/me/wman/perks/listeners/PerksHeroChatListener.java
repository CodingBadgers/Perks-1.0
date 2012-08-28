package me.wman.perks.listeners;

import me.wman.perks.utils.PerkPlayer;
import me.wman.perks.utils.PerkUtils;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.dthielke.herochat.ChannelChatEvent;
import com.dthielke.herochat.Chatter.Result;

public class PerksHeroChatListener implements Listener {

	@EventHandler (priority = EventPriority.NORMAL)
	public void onPlayerChat(ChannelChatEvent event) {
		PerkPlayer player = PerkUtils.getPlayer(event.getSender().getPlayer());
		
		if (player.isVanished() && !event.getChannel().getName().equalsIgnoreCase("Staff")) {
			event.setResult(Result.INVALID);
			PerkUtils.OutputToPlayer(player, "You cannot talk whist vanished");
		}
	}
}
