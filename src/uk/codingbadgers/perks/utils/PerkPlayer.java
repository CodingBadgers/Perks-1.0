package uk.codingbadgers.perks.utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitTask;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.bukkit.PermissionsEx;
import uk.codingbadgers.perks.config.DatabaseManager;
import uk.codingbadgers.perks.config.PerkConfig;

public class PerkPlayer {

	private Player m_player = null;					//!< store the bukkit player
	
	private class Hunger {
		public float m_hungerRate = PerkConfig.hungerRate;				//!< means hunger goes down at 1/4 the normal rate <- Loads from config
		public float m_hungerCounter = PerkConfig.hungerCounter;			//!< stores the last hunger counter, if this is equal to 1.0 (100%) let a hunger event work as normal
	}
	
	private class TP {
		public PerkPlayerArray m_tpRequest = new PerkPlayerArray();			// !< stores the teleport requests
		public ArrayList<Long> m_tpRequestTime = new ArrayList<Long>();		// !< stores the request time
		public boolean m_tpHere = false;									// !< If it is to tp the player to here
	}
	
	private class DeathTP {
		public Location m_location = null;			//!< stores the death location
		public boolean m_hasDied = false;			//!< stores whether the player has died
		public int taskId = -1;						//!< 
	}
	
	private class Vanish {
		public boolean vanished = false; 			// !< stores whether you are vanished
	}
	
	private class Afk {
		public boolean afk = false;					// !< stores whether you are afk
		public int kickTask = -1;					// !< Id of the task used to kick the player after a given length of time
		public Location lastPosition = null;		// !< 
		public Long lastPositionCheck = -1L;		// !<
		public Long lastChatCheck = -1L;			// !<
	}
	
	private class Inventory {
		public ItemStack[] inv = null;			// !< stores the players inventory
		public ItemStack[] armour;
	}
	
	private class Spectate {
		public boolean spectating = false;			// !< whether the player is spectating or not
		public ItemStack[] inv = null;			// !< the players inventory
		public PerkPlayer folowing = null;			// !< the player this player is folowing
		public PerkPlayer stalker = null;			// !< the player this player is folowing
		public Location startLocation = null;  		// !< stores the start location
	}
	
	private class Thor {
		public boolean thorEnabled = false; 		// !< stores whether the player has thor or not
		public Material thorHammer = null;			// !< stores what thor item the player is using, defaults to air
		public int ammount = 0;						// !< the ammount of strikes a player causes on each swing
	}
	
	private class Dynmap {
		public boolean hidden = false;				// !< stores whether the player is hidden from live map or not
	}
	
	private class Pvp {
		public BukkitTask teleportTask;				// !< stores the bukkit task id for the spawn teleport
		protected int counter;
	}
	
	private Hunger m_hunger = null;
	private TP m_tp = null;
	private Vanish m_vanish = null;
	private DeathTP m_deathTP = null;
	private Afk m_afk = null;
	private Inventory m_inv = null;
	private Spectate m_spec = null;
	private Thor m_thor = null;
	private Dynmap m_map = null;
	private Pvp m_pvp = null;
	
	
	public PerkPlayer(Player player) {
		m_player = player;

		m_hunger = new Hunger();
		m_tp = new TP();
		m_deathTP = new DeathTP();
		m_vanish = new Vanish();
		m_afk = new Afk();
		m_inv = new Inventory();
		m_spec = new Spectate();
		m_thor = new Thor();
		m_map = new Dynmap();
		m_pvp = new Pvp();
	}
	
	// called when a player is kicked or leaves...
	// all cleanups should be done in here
	public void remove() {
		m_player.setAllowFlight(false);
		cancelTeleports("you have logged out");
		
		if (m_deathTP.taskId != -1) {
			Bukkit.getScheduler().cancelTask(m_deathTP.taskId);
			clearTaskIds();
		}
	}

	// returns the bukkit player
	public Player getPlayer() {
		return m_player;
	}

	// checks whether a player has permission to do something or not
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
		PerkUtils.OutputToPlayer(this, "Type '/tpa' to accept there request");
		
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
		PerkUtils.OutputToPlayer(this, "Type '/tpa' to accept there request");
		
		PerkUtils.OutputToPlayer(player, "Your tp here request has been sent to " + m_player.getName());
		
	}

	public void addDeathLocation(final Location deathLocation) {

		if (PerkConfig.isPvpServer()) {
			PerkUtils.OutputToPlayer(this, "You cannot death teleport for " + PerkConfig.deathTpDelay + " seconds");
			
			if (m_deathTP.taskId != -1) {
				Bukkit.getScheduler().cancelTask(m_deathTP.taskId);
				clearTaskIds();
			}
			
			m_deathTP.taskId = Bukkit.getScheduler().scheduleSyncDelayedTask(PerkUtils.plugin, new Runnable() {
	
				public void run() {
	
					m_deathTP.m_location = deathLocation;		
					m_deathTP.m_hasDied = true;
					
					PerkUtils.OutputToPlayer(m_player, "Use /death to be teleported back to the point where you died");
					clearTaskIds();
				}
				
			}, PerkConfig.deathTpDelay*20);
		} else {
			m_deathTP.m_location = deathLocation;		
			m_deathTP.m_hasDied = true;
			clearTaskIds();
			
			PerkUtils.OutputToPlayer(m_player, "Use /death to be teleported back to the point where you died");
		}		
	}
	
	public boolean canDeathTP() {
		
		if (m_deathTP.taskId != -1 && PerkConfig.isPvpServer()) {
			PerkUtils.OutputToPlayer(this, "You cannot death teleport yet, please wait");
			return false;
		}
		
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
		m_deathTP.m_location = null;
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

	public void clearTaskIds() {
		m_deathTP.taskId = -1;
	}
	
	public void cancelTeleports(String reason) {
		if (m_pvp.teleportTask == null)
			return;
		
		PerkUtils.OutputToPlayer(this, "Teleport cancelled, " + reason);
		
		m_pvp.teleportTask.cancel();
		m_pvp.teleportTask = null;
	}
	
	public void gotoHome() {
		if (PerkConfig.isPvpServer()) {
			
			// they are already trying to teleport home
			if (m_pvp.teleportTask != null) {
				PerkUtils.OutputToPlayer(this, "You are already trying to teleport, please wait");
				return;
			}
			
			final Location home = DatabaseManager.getHome(m_player);
			
			if (home == null) {
				PerkUtils.OutputToPlayer(m_player, "You don't have a home in '" + m_player.getWorld().getName() + "'");
				PerkUtils.OutputToPlayer(m_player, "Use /sethome to set your home location");
			}
			
			PerkUtils.OutputToPlayer(this, "Waiting for teleport, stand still for " + PerkConfig.homeWarmUpTime + " seconds");
			
			m_pvp.teleportTask = startTimer(new Runnable() {
				public void run() {
					m_player.teleport(home);
					PerkUtils.OutputToPlayer(m_player, "You have been teleported to your home in '" + m_player.getWorld().getName() + "'");
				}
			}, PerkConfig.homeWarmUpTime);
		} else {
			final Location home = DatabaseManager.getHome(m_player);
			
			if (home == null) {
				PerkUtils.OutputToPlayer(m_player, "You don't have a home in '" + m_player.getWorld().getName() + "'");
				PerkUtils.OutputToPlayer(m_player, "Use /sethome to set your home location");
			}
			
			m_player.teleport(home);
			PerkUtils.OutputToPlayer(m_player, "You have been teleported to your home in '" + m_player.getWorld().getName() + "'");
		}
		
	}
	
	public void gotoHome(final World world) {
		if (PerkConfig.isPvpServer()) {
			
			// they are already trying to teleport home
			if (m_pvp.teleportTask != null) {
				PerkUtils.OutputToPlayer(this, "You are already trying to teleport, please wait");
				return;
			}
			
			final Location home = DatabaseManager.getHome(m_player, world);
			
			if (home == null) {
				PerkUtils.OutputToPlayer(m_player, "You don't have a home in '" + world.getName() + "'");
				PerkUtils.OutputToPlayer(m_player, "Use /sethome to set your home location");
			}
			
			PerkUtils.OutputToPlayer(this, "Waiting for teleport, stand still for " + PerkConfig.homeWarmUpTime + " seconds");
			
			m_pvp.teleportTask = startTimer(new Runnable() {
				public void run() {
					m_player.teleport(home);
					PerkUtils.OutputToPlayer(m_player, "You have been teleported to your home in '" + world.getName() + "'");
				}
			}, PerkConfig.homeWarmUpTime);
		} else {
			final Location home = DatabaseManager.getHome(m_player, world);
			
			if (home == null) {
				PerkUtils.OutputToPlayer(m_player, "You don't have a home in '" + world.getName() + "'");
				PerkUtils.OutputToPlayer(m_player, "Use /sethome to set your home location");
			}
			
			m_player.teleport(home);
			PerkUtils.OutputToPlayer(m_player, "You have been teleported to your home in '" + world.getName() + "'");
		}
	}

	public void gotoBuild() {
		if (PerkConfig.isPvpServer()) {
			
			// they are already trying to teleport home
			if (m_pvp.teleportTask != null) {
				PerkUtils.OutputToPlayer(this, "You are already trying to teleport, please wait");
				return;
			}
			
			final Location build = DatabaseManager.getBuild(m_player);
			
			if (build == null) {
				PerkUtils.OutputToPlayer(m_player, "You don't have a build location set");
				PerkUtils.OutputToPlayer(m_player, "Use /setbuild to set your home location");
				return;
			}
			
			PerkUtils.OutputToPlayer(this, "Waiting for teleport, stand still for " + PerkConfig.homeWarmUpTime + " seconds");
			
			m_pvp.teleportTask = startTimer (new Runnable() {
				public void run() {
					m_player.teleport(build);
					PerkUtils.OutputToPlayer(m_player, "You have been teleported to your build location");
				}
			}, PerkConfig.buildWarmUpTime);
		} else {
			final Location build = DatabaseManager.getBuild(m_player);
			
			if (build == null) {
				PerkUtils.OutputToPlayer(m_player, "You don't have a build location set");
				PerkUtils.OutputToPlayer(m_player, "Use /setbuild to set your home location");
				return;
			}
			
			m_player.teleport(build);
			PerkUtils.OutputToPlayer(m_player, "You have been teleported to your build location");
		}
		
	}

	public void gotoSpawn(final PerkWorldSpawn spawn) {
		if (PerkConfig.isPvpServer()) {
			
			// they are already trying to teleport home
			if (m_pvp.teleportTask != null) {
				PerkUtils.OutputToPlayer(this, "You are already trying to teleport, please wait");
				return;
			}
			
			PerkUtils.OutputToPlayer(this, "Waiting for teleport, stand still for " + PerkConfig.homeWarmUpTime + " seconds");
			
			m_pvp.teleportTask = startTimer(new Runnable() {
				public void run() {
					m_player.teleport(spawn.getSpawn());
					PerkUtils.OutputToPlayer(m_player, "You have been teleported to the spawn of " + spawn.getWorld().getName());
				}
				
			}, PerkConfig.spawnWarmUpTime);
		} else {
			m_player.teleport(spawn.getSpawn());
			PerkUtils.OutputToPlayer(m_player, "Teleported to spawn of " + spawn.getWorld().getName());
		}
		
	}
	
	public BukkitTask startTimer(final Runnable runnable, long time) {
		m_pvp.counter = (int)time;
		
		BukkitTask task = Bukkit.getScheduler().runTaskTimer(PerkUtils.plugin, new Runnable() {

			public void run() {
				m_pvp.counter --;
				
				if (m_pvp.counter > 0) {
					m_player.sendMessage(ChatColor.AQUA + String.valueOf(m_pvp.counter));
				} else {
					runnable.run();
					cancelTimer();
				}
				
			}
		
		}, 20, 20);
		
		return task;
	}
	
	private void cancelTimer() {
		m_pvp.teleportTask.cancel();
		m_pvp.teleportTask = null;
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

		if (broadcast)
			PerkUtils.server().broadcastMessage(ChatColor.YELLOW + m_player.getName() + " joined the game.");
		
		PerkUtils.OutputToPlayer(this, "You are visible again...");
		
		setHidden(false);
	}
	
	public boolean isVanished() {
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
	
	public void clearInv(boolean all) {
        m_inv.inv = m_player.getPlayer().getInventory().getContents();
        m_inv.armour = m_player.getPlayer().getInventory().getArmorContents();
        
        for (int i = (all ? 0 : 9); i < 36; i++) {
            m_player.getInventory().setItem(i, null);
        }
        
        if (all) {
            for (int i = 36; i <= 39; i++) {
                m_player.getInventory().setItem(i, null);
            }
        }
                
    }

	
	public void colectInv() {
		clearInv(true);
		
		m_player.getInventory().setContents(m_inv.inv);
		m_player.getInventory().setArmorContents(m_inv.armour);
	}
	
	public boolean isAfk() {
		return m_afk.afk;
	}
	
	public void setAfk(boolean afk)  {
		m_player.setMetadata("perks_afk", new FixedMetadataValue(PerkUtils.plugin, afk));
		m_afk.afk = afk;
		
		if (afk)
		{
            // if a players y coord is greater than the highest block at there location, lower them
            double y = getPlayer().getWorld().getHighestBlockAt(getPlayer().getLocation()).getLocation().getY() + 1;
            if (getPlayer().getLocation().getY() > y) {
                    teleport(new Location(getPlayer().getLocation().getWorld(),
                                    getPlayer().getLocation().getX(), 
                                    y, 
                                    getPlayer().getLocation().getZ()));
            }
			
			final int noofMinutes = PerkConfig.afkKickTime;
			final int noofSeconds = 60 * noofMinutes;
			
			final Player player = getPlayer();
			m_afk.kickTask = Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(PerkUtils.plugin, new Runnable() {
				public void run() {
					player.kickPlayer("You have been AFK for " + noofMinutes + " minutes.");
				}
			}, 20L * noofSeconds);
		}
		else
		{
			Bukkit.getServer().getScheduler().cancelTask(m_afk.kickTask);
			m_afk.lastChatCheck = -1L;
			m_afk.lastPositionCheck = -1L;
			checkAFK();
		}
		
	}
	
	public void playerSpoke() {
		m_afk.lastChatCheck = System.currentTimeMillis();
	}
	
	public void checkAFK() {
		
		if (this.hasPermission("perks.afk.check.ignore", false)) {
			return;
		}
		
		if (m_afk.afk) {
			return;
		}
		
		if (m_afk.lastChatCheck == -1L) {
			m_afk.lastChatCheck = System.currentTimeMillis();
		}
		
		if (m_afk.lastPosition == null || m_afk.lastPositionCheck == -1L) {
			m_afk.lastPosition = getPlayer().getLocation();
			m_afk.lastPositionCheck = System.currentTimeMillis();
			return;
		}
		
		boolean playerMoved = true;
		boolean playerSpoke = true;
		
		final Long afkCheckTime = 1L * 60L * 1000L;
		if (System.currentTimeMillis() - m_afk.lastPositionCheck >= afkCheckTime) {
			
			final int oldX = m_afk.lastPosition.getBlockX();
			final int oldY = m_afk.lastPosition.getBlockY();
			final int oldZ = m_afk.lastPosition.getBlockZ();
			final float oldPitch = m_afk.lastPosition.getPitch();
			final float oldYaw = m_afk.lastPosition.getYaw();
			
			final Location newLocation = getPlayer().getLocation();
			final int newX = newLocation.getBlockX();
			final int newY = newLocation.getBlockY();
			final int newZ = newLocation.getBlockZ();
			final float newPitch = newLocation.getPitch();
			final float newYaw = newLocation.getYaw();
			
			if (
				oldX == newX &&
				oldY == newY &&
				oldZ == newZ &&
				oldPitch == newPitch &&
				oldYaw == newYaw 
				) 
			{
				playerMoved = false;
			}
		}
		
		if (System.currentTimeMillis() - m_afk.lastChatCheck >= afkCheckTime) {
			playerSpoke = false;
		}
		
		if (!playerMoved && !playerSpoke) {
			setAfk(true);
			PerkUtils.OutputToPlayer(getPlayer(), "You are afk.");
            PerkUtils.OutputToAllExcluding(getPlayer().getName() + " is afk", getPlayer());
		}
		
	}
	
	// Spectating 
	public boolean isSpectating() {
		return m_spec.spectating;
	}
	
	public ItemStack[] getInventory() {
		return m_spec.inv;
	}
	
	public PerkPlayer getFolowing() {
		return m_spec.folowing;
	}
	
	public void setSpectating(boolean spectate) {
		m_spec.spectating = spectate;
	}
	
	public void setSpectatingInventory() {
		m_spec.inv = m_player.getInventory().getContents();
	}
	
	public void setSpectatingInventory(ItemStack[] inv) {
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
	
	public void setThorHammer(Material item) {
		m_thor.thorHammer = item;
	}
	
	public Material getThorHammer() {
		if (m_thor.thorHammer == null)
			m_thor.thorHammer = Material.AIR;
		
		return m_thor.thorHammer;
	}
	
	public void setThorAmmount(int ammount) {
		m_thor.ammount = ammount;
	}
	
	public int getThorAmmount() {
		return m_thor.ammount;
	}
	
	public boolean isDynmapHidden() {
		return m_map.hidden;
	}
	
	@Override
	public String toString() {
		return m_player.getDisplayName();
	}
}