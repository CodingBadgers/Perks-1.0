package me.wisbycraft.perks.utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

import me.wisbycraft.perks.config.DatabaseManager;
import me.wisbycraft.perks.config.PerkConfig;
import me.wisbycraft.perks.donator.PerkMagicCarpet;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.getspout.spoutapi.player.SpoutPlayer;


import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class PerkPlayer {

	private Player m_player = null;					//!< store the bukkit player
	private SpoutPlayer m_spoutPlayer = null;		//!< store the spout player (spout has more commands so if we can use them why not :P)

	private class Flying {
		public boolean m_flying = false;				//!< is the player flying?
		public PerkMagicCarpet m_magicCarpet = null;	//!< A players magic carpet object (Non spout only)
		public boolean m_forceCarpet = false;			//!< Force magic carpet even when using spout
	}
	
	private class Hunger {
		public float m_hungerRate = PerkConfig.hungerRate;				//!< means hunger goes down at 1/4 the normal rate <- Loads from config
		public float m_hungerCounter  =PerkConfig.hungerCounter;			//!< stores the last hunger counter, if this is equal to 1.0 (100%) let a hunger event work as normal
	}
	
	private class TP {
		public PerkPlayerArray m_tpRequest = new PerkPlayerArray();			// !< stores the teleport requests
		public ArrayList<Long> m_tpRequestTime = new ArrayList<Long>();		// !< stores the request time
		public boolean m_tpHere = false;									// !< If it is to tp the player to here
	}
	
	private class DeathTP {
		public Location m_location = null;			// !< stores the death location
		public boolean m_hasDied = false;			// !< stores whether the player has died
	}
	
	private class Vanish {
		public boolean vanished = false; 			// !< stores whether you are vanished
	}
	
	private class Afk {
		public boolean afk = false;					// !< stores whether you are afk
	}
	
	private class PlayerKit {
		private ArrayList<PerkKit> usedKit = new ArrayList<PerkKit>();  // !< stores what kit you used
		private ArrayList<Long> usedTime = new ArrayList<Long>(); 		// !< store the remaining time
	}
	
	private class Inventory {
		public PlayerInventory inv = null;			// !< stores the players inventory
	}
	
	private class Spectate {
		public boolean spectating = false;			// !< whether the player is spectating or not
		public PlayerInventory inv = null;			// !< the players inventory
		public PerkPlayer folowing = null;			// !< the player this player is folowing
		public PerkPlayer stalker = null;			// !< the player this player is folowing
		public Location startLocation = null;  		// !< stores the start location
	}
	
	private class Thor {
		public boolean thorEnabled = false; 		// !< stores whether the player has thor or not
	}
	
	private Flying m_fly = null;
	private Hunger m_hunger = null;
	private TP m_tp = null;
	private Vanish m_vanish = null;
	private DeathTP m_deathTP = null;
	private Afk m_afk = null;
	private PlayerKit m_kits = null;
	private Inventory m_inv = null;
	private Spectate m_spec = null;
	private Thor m_thor = null;
	
	public PerkPlayer(Player player) {
		m_player = player;
		
		if (PerkUtils.spoutEnabled) {
			m_spoutPlayer = (SpoutPlayer) player;
		}
		
		m_fly = new Flying();
		m_hunger = new Hunger();
		m_tp = new TP();
		m_deathTP = new DeathTP();
		m_vanish = new Vanish();
		m_afk = new Afk();
		m_kits = new PlayerKit();
		m_inv = new Inventory();
		m_spec = new Spectate();
		m_thor = new Thor();

		// if the player isnt using spout make a magic carpet
		if (!PerkUtils.spoutEnabled || !m_spoutPlayer.isSpoutCraftEnabled()) {
			m_fly.m_magicCarpet = new PerkMagicCarpet();
		}
	}
	
	// called when a player is kicked or leaves...
	// all cleanups should be done in here
	public void remove() {
		if (m_fly.m_magicCarpet != null) {
			m_fly.m_magicCarpet.destroy();
		}
	}

	// returns the bukkit player
	public Player getPlayer() {
		return m_player;
	}

	// returns the spout player
	public SpoutPlayer getSpoutPlayer() {
		return m_spoutPlayer;
	}

	public void setFlying(boolean flying, boolean forceCarpet) {
		
		// output a message to the user
		if (flying) {
			PerkUtils.OutputToPlayer(this, "Fly mode is now enabled");
		} else {
			PerkUtils.OutputToPlayer(this, "Fly mode is now disabled");
			m_fly.m_magicCarpet.destroy();
		}
		
		if (flying)
			m_fly.m_magicCarpet.create(m_player);
		
		// new bukkit stuff, is creative and works. WIN!
		m_player.setAllowFlight(flying);
		m_player.setFlying(flying);

		// store whether we're flying or not
		m_fly.m_flying = flying;
		m_fly.m_forceCarpet = forceCarpet;
	}

	public boolean isFlying() {
		return m_fly.m_flying;
	}

	public boolean getForceCarpet() {
		return m_fly.m_forceCarpet;
	}

	public void setForceCarpet(boolean forceCarpet) {
		m_fly.m_forceCarpet = forceCarpet;
	}

	public PerkMagicCarpet getMagicCarpet() {
		return m_fly.m_magicCarpet;
	}

	// checks whether a player has permission to do something or not
	// tryed to use vault, but failed
	public boolean hasPermission(String permission, boolean reportError) {
		PermissionManager pex = PermissionsEx.getPermissionManager();
		
		if (pex.has(m_player, permission) || m_player.isOp())
			return true;

		if (reportError)
			PerkUtils.OutputToPlayer(this, "You do not have permission to use that command");
		
		return false;
	}

	// checks to see if a hunger event should be cancelled or not
	public boolean shouldLowerHunger() {
		
		// increase the hunger counter
		m_hunger.m_hungerCounter += m_hunger.m_hungerRate;
		
		// if the counter is at 100%, reset the counter and let the hunger event fire
		if (m_hunger.m_hungerCounter == 1.0f) {
			m_hunger.m_hungerCounter = 0.0f;
			return true;
		}
		
		// cancel the hunger event
		return false;
	}

	public void sendTpRequest(PerkPlayer player) {		
		
		boolean newRequest = false;
		Calendar cal = Calendar.getInstance();
		
		if (m_tp.m_tpRequest.getPlayer(player.getPlayer()) == null) {
			m_tp.m_tpRequest.add(player);
			m_tp.m_tpRequestTime.add(cal.getTimeInMillis());
			m_tp.m_tpHere = false;
			newRequest = true;
		}
		
		// if were requesting to a player we've requested to before check times
		if (!newRequest) {
			for (int i = 0; i < m_tp.m_tpRequest.size(); ++i) {
				if (m_tp.m_tpRequest.get(i) == player) {
					// see if 30 seconds have passed, if not tell them to fuck off.
					Long timeSinceLastReguest = cal.getTimeInMillis() - m_tp.m_tpRequestTime.get(i);
					if(timeSinceLastReguest < 30000) {
						PerkUtils.OutputToPlayer(player, "Please wait another " + Float.valueOf((new DecimalFormat("##.##").format(30-(timeSinceLastReguest/1000))))  + " seconds before sending another tp request to " + m_player.getName());
						return;
					}
					else
					{
						// update the time so that they have to wait another 30 seconds.
						m_tp.m_tpRequestTime.set(i, cal.getTimeInMillis());
					}
				}
			}
		}
				
		PerkUtils.OutputToPlayer(this, player.getPlayer().getName() + " have sent you a tp request");
		PerkUtils.OutputToPlayer(this, "Type '/tpa " + player.getPlayer().getName() + "' to accept there request");
		
		PerkUtils.OutputToPlayer(player, "Your tp request has been sent to " + m_player.getName());
	}

	public void acceptTpRequest(PerkPlayer player) {
		
		// if they havn't specified a player, get the last one from the array
		if (player == null) {
			
			if (m_tp.m_tpRequest.size() == 0) {
				PerkUtils.OutputToPlayer(this, "You have not recieved a tp request from any players");
				return;
			}			
			
			player = m_tp.m_tpRequest.get(m_tp.m_tpRequest.size() - 1);
		} 
		else if (m_tp.m_tpRequest.getPlayer(player.getPlayer()) == null) {
			PerkUtils.OutputToPlayer(this, "You have not recieved a tp request from that player");
			return;
		}
		
		PerkUtils.OutputToPlayer(player, m_player.getName() + " has accepted your tp request");
		
		if (m_tp.m_tpHere)
			m_player.teleport(player.getPlayer());
		else
			player.getPlayer().teleport(m_player);
		
		m_tp.m_tpRequest.removePlayer(player.getPlayer());
		
	}

	public void declineTpRequest(PerkPlayer player) {
		
		// if they havn't specified a player, get the last one from the array
		if (player == null) {
			
			if (m_tp.m_tpRequest.size() == 0) {
				PerkUtils.OutputToPlayer(this, "You have not recieved a tp request from any players");
				return;
			}			
			
			player = m_tp.m_tpRequest.get(m_tp.m_tpRequest.size() - 1);
		}
		else if (m_tp.m_tpRequest.getPlayer(player.getPlayer()) == null) {
			PerkUtils.OutputToPlayer(this, "You have not recieved a tp request from that player");
			return;
		}
		
		PerkUtils.OutputToPlayer(player, m_player.getName() + " has declined your tp request");
		
		m_tp.m_tpRequest.removePlayer(player.getPlayer());
		
	}

	public void sendTpHereRequest(PerkPlayer player) {
		
		boolean newRequest = false;
		Calendar cal = Calendar.getInstance();
		
		if (m_tp.m_tpRequest.getPlayer(player.getPlayer()) == null) {
			m_tp.m_tpRequest.add(player);
			m_tp.m_tpRequestTime.add(cal.getTimeInMillis());
			m_tp.m_tpHere = true;
			newRequest = true;
		}
		
		// if were requesting to a player we've requested to before check times
		if (!newRequest) {
			for (int i = 0; i < m_tp.m_tpRequest.size(); ++i) {
				if (m_tp.m_tpRequest.get(i) == player) {
					// see if 30 seconds have passed, if not tell them to fuck off.
					Long timeSinceLastReguest = cal.getTimeInMillis() - m_tp.m_tpRequestTime.get(i);
					if(timeSinceLastReguest < 30000) {
						PerkUtils.OutputToPlayer(player, "Please wait another " + Float.valueOf((new DecimalFormat("##.##").format(30-(timeSinceLastReguest/1000))))  + " seconds before sending another tp request to " + m_player.getName());
						return;
					}
					else
					{
						// update the time so that they have to wait another 30 seconds.
						m_tp.m_tpRequestTime.set(i, cal.getTimeInMillis());
					}
				}
			}
		}
				
		PerkUtils.OutputToPlayer(this, player.getPlayer().getName() + " have sent you a tp here request");
		PerkUtils.OutputToPlayer(this, "Type '/tpa " + player.getPlayer().getName() + "' to accept there request");
		
		PerkUtils.OutputToPlayer(player, "Your tp here request has been sent to " + m_player.getName());
		
	}

	public void addDeathLocation(Location deathLocation) {
		
		m_deathTP.m_location = deathLocation;		
		m_deathTP.m_hasDied = true;
		
		PerkUtils.OutputToPlayer(this, "Use /death to be teleported back to the point where you died");
		
	}
	
	public boolean canDeathTP() {
		
		if (!m_deathTP.m_hasDied) {
			PerkUtils.OutputToPlayer(this, "You havn't died recently");
			return false;
		}
				
		return true;
	}
	
	public Location getDeathLocation() {
		return m_deathTP.m_location;
	}
	
	public void resetDeath() {
		m_deathTP.m_hasDied = false;
	}
	
	public void setBuildLocation(Location loc, boolean announce) {
		
		if (loc == null)
			loc = m_player.getLocation();
		
		if (announce)
			PerkUtils.OutputToPlayer(this, "Build location set");
		
		DatabaseManager.setBuildLocation(m_player, loc);
	}
		
	public void setHomeLocation(Location loc, boolean announce) {
		
		if (loc == null)
			loc = m_player.getLocation();
		
		
		if (announce) {
			PerkUtils.OutputToPlayer(this, "Added new home in world '" + loc.getWorld().getName() + "'");
		}
		
		DatabaseManager.setHomeLocation(m_player, loc);
	}
	
	public void hidePlayer(boolean broadcast) {
		
		Player[] players = PerkUtils.server().getOnlinePlayers();
		
		for (int i = 0; i < players.length; ++i) {
			
			// dont hide yourself from yourself
			if (players[i] == m_player)
				continue;
			
			//only hide to players who dont have the permissions to see everyone
			if (PerkUtils.getPlayer(players[i]).hasPermission("perks.vanish.show", false))
				continue;
			
			players[i].hidePlayer(m_player);
		} 
		
		if (PerkUtils.dynmapapi != null)
			PerkUtils.dynmapapi.setPlayerVisiblity(m_player, false);
		
		if (broadcast)
			PerkUtils.server().broadcastMessage(ChatColor.YELLOW + m_player.getName() + " left the game.");
		
		PerkUtils.OutputToPlayer(this, "You're the invisible man...");
		PerkUtils.OutputToPlayer(this, "Incredible how you can...");
		PerkUtils.OutputToPlayer(this, "See right through you!");
		
		setHidden(true);
	}
	
	public void showPlayer(boolean broadcast) {
		
		Player[] players = PerkUtils.server().getOnlinePlayers();
		
		for (int i = 0; i < players.length; ++i) {
			if (players[i] == m_player)
				continue;
			
			players[i].showPlayer(m_player);
		}
		
		if (PerkUtils.dynmapapi != null)
			PerkUtils.dynmapapi.setPlayerVisiblity(m_player, true);
		
		if (broadcast)
			PerkUtils.server().broadcastMessage(ChatColor.YELLOW + m_player.getName() + " joined the game.");
		
		PerkUtils.OutputToPlayer(this, "You are visible again...");
		
		setHidden(false);
	}
	
	public boolean isHidden() {
		return m_vanish.vanished;
	}
	
	public void setHidden(boolean hidden) {
		m_vanish.vanished = hidden;
	}
	
	public void teleport(Location loc) {
		m_player.teleport(loc);
	}
	
	public void teleportHere(PerkPlayer player) {
		player.getPlayer().teleport(m_player.getPlayer());
	}
	
	public void clearInv() {
		m_inv.inv = m_player.getPlayer().getInventory();
		m_player.getInventory().clear();
	}
	
	public void colectInv() {
		m_player.getInventory().clear();
		
		for (int i = 0; i<m_player.getInventory().getSize(); i++) {
			m_player.getInventory().addItem(m_inv.inv.getItem(i));
		}
	}
	
	public boolean isAfk() {
		return m_afk.afk;
	}
	
	public void setAfk(boolean afk)  {
		m_afk.afk = afk;
	}

	public void usedKit(PerkKit kit) {
		m_kits.usedKit.add(kit);
		Calendar cal = Calendar.getInstance();
		
		Long time = cal.getTimeInMillis();
		m_kits.usedTime.add(time);
		
		DatabaseManager.addKit(this, kit, time);
		
	}
	
	public void usedKit(PerkKit kit, Long time) {
		m_kits.usedKit.add(kit);
		m_kits.usedTime.add(time);
	}
	
	public boolean canUseKit(PerkKit requestedKit) {
		
		for (int i = 0; i < m_kits.usedKit.size(); ++i) {
			
			if (requestedKit.getName().equalsIgnoreCase(m_kits.usedKit.get(i).getName())) {
				
				Calendar cal = Calendar.getInstance();
				Long timeDifference = (long) ((cal.getTimeInMillis() - m_kits.usedTime.get(i)) * 0.001);
					
				if (timeDifference > m_kits.usedKit.get(i).getTimeout()) {
					
					m_kits.usedKit.remove(i);
					m_kits.usedTime.remove(i);
					
					DatabaseManager.deleteKit(this, requestedKit);
					
					return true;
				}
				
				PerkUtils.OutputToPlayer(this, "You can't use kit '" + requestedKit.getName() + "' for another " + ((m_kits.usedKit.get(i).getTimeout() - timeDifference) + 1) + " seconds");
				
				return false;
			}
			
		}
		
		// if we are here then we have never used the requested kit before
		return true;
	}
	
	public boolean isSpectating() {
		return m_spec.spectating;
	}
	
	public PlayerInventory getInventory() {
		return m_spec.inv;
	}
	
	public PerkPlayer getFolowing() {
		return m_spec.folowing;
	}
	
	public void setSpectating(boolean spectate) {
		m_spec.spectating = spectate;
	}
	
	public void setSpectatingInventory() {
		m_spec.inv = m_player.getInventory();
	}
	
	public void setSpectatingInventory(PlayerInventory inv) {
		m_spec.inv = inv;
	}
	
	public void setSpecatingPlayer(PerkPlayer player) {
		if (m_spec.folowing != null)
			m_spec.folowing.setStalker(null);
		m_spec.folowing = player;
	}
	
	public void setStalker(PerkPlayer player) {
		m_spec.stalker = player;
	}

	public Location getStartLocation() {
		return m_spec.startLocation;
	}
	
	public void setStartLocation(Location loc) {
		m_spec.startLocation = loc;
	}
	
	public void setStartLocation() {
		m_spec.startLocation = m_player.getLocation();
	}

	public PerkPlayer getSpecatingPlayer() {
		return m_spec.stalker;
	}
	
	public boolean isThorEnabled() {
		return m_thor.thorEnabled;
	}
	
	public void setThor (boolean thor) {
		m_thor.thorEnabled = thor;
	}
	
	public boolean isBlacklisted() {
		return PerkUtils.blacklist.contains(m_player);
	}
	
	public boolean isForceCarpet(){
		return m_fly.m_forceCarpet;
	}
}