package me.wman.perks.listeners;

import me.n3wton.weblistener.events.WebChatJoinEvent;
import me.n3wton.weblistener.events.WebChatLeaveEvent;
import me.wman.perks.utils.PerkUtils;
import me.wman.perks.utils.PerkWebChatPlayer;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class PerksWebChatListener implements Listener{

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerJoin(WebChatJoinEvent event) {
		PerkUtils.webChatPlayers.add(new PerkWebChatPlayer(event.getName()));
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerLeave(WebChatLeaveEvent event) {
		PerkUtils.webChatPlayers.remove(new PerkWebChatPlayer(event.getName()));
	}
}
