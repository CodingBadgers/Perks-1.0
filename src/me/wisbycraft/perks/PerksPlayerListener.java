package me.wisbycraft.perks;

import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PerksPlayerListener extends PlayerListener {
	
	@SuppressWarnings("unused")
	private Perks m_plugin = null; // were gonna need it at some point... and the warning was annoying me
	
	private PerkPlayerArray perkPlayers = new PerkPlayerArray();
	
	public PerksPlayerListener(Perks plugin) {
		m_plugin = plugin;		
	}
	
    @Override
    public void onPlayerJoin(PlayerJoinEvent event) {
    	PerkPlayer player = new PerkPlayer(event.getPlayer());
    	perkPlayers.add(player);
    	
    	System.out.println("[Perks] Added player to perks array");
    }

    @Override
    public void onPlayerQuit(PlayerQuitEvent event) {
    	perkPlayers.removePlayer(event.getPlayer());
    	
    	System.out.println("[Perks] Removed player from perks array");
    }

    @Override
    public void onPlayerKick(PlayerKickEvent event) {
    	perkPlayers.removePlayer(event.getPlayer());
    	
    	System.out.println("[Perks] Removed player from perks array");
    }
    
    @Override
    public void onPlayerMove(PlayerMoveEvent event){
    	
    	PerkPlayer player = perkPlayers.getPlayer(event.getPlayer());
    	
    	if (player == null)
    		return;
    	    	
    	PerkFlying.fly(player);
    		
    }
    
    @Override
    public void onPlayerTeleport(PlayerTeleportEvent event) {

    }
    
    

}