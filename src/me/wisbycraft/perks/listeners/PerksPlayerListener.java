package me.wisbycraft.perks.listeners;

import java.util.StringTokenizer;

import me.wisbycraft.perks.admin.PerkSpectate;
import me.wisbycraft.perks.admin.PerkThor;
import me.wisbycraft.perks.admin.PerkVanish;
import me.wisbycraft.perks.donator.PerkCapes;
import me.wisbycraft.perks.donator.PerkColors;
//import me.wisbycraft.perks.donator.PerkJoining;
import me.wisbycraft.perks.donator.PerkList;
import me.wisbycraft.perks.utils.PerkPlayer;
import me.wisbycraft.perks.utils.PerkUrlShortener;
import me.wisbycraft.perks.utils.PerkUtils;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
//import org.bukkit.event.player.PlayerLoginEvent;
//import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

//import ru.tehkode.permissions.PermissionManager;
//import ru.tehkode.permissions.bukkit.PermissionsEx;

public class PerksPlayerListener implements Listener {

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerJoin(PlayerJoinEvent event) {
		PerkPlayer player = new PerkPlayer(event.getPlayer());
		PerkUtils.perkPlayers.add(player);
		
		if (player.hasPermission("perks.capes", false)) {
			PerkCapes.setCape(player.getPlayer());
		}
		
		PerkVanish.vanishJoin(player, event);
		
		if (player.isVanished()) {
			event.setJoinMessage(null);
		}
		
		PerkColors.addColor(player.getPlayer());
		
		if (event.getPlayer() instanceof Player) {
			PerkList.showOnlineList(player);
		}
		
		player.dynmapHide();
	}	

	/* Not tested yet, need to max out a server to try though :P
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerLogin(PlayerLoginEvent event) {
		PermissionManager pex = PermissionsEx.getPermissionManager();
		
		if (PerkUtils.server().getOnlinePlayers().length != PerkUtils.server().getMaxPlayers())
			return;
		
		if (!pex.has(event.getPlayer(), "perks.forcejoin")) {
			event.disallow(Result.KICK_FULL, "Sorry the server is full");
			return;
		}

		if (PerkJoining.kickPlayer()){
			event.allow();
		} else {
			event.disallow(Result.KICK_FULL, "Sorry the server is full");
		}
	}
	*/
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerQuit(PlayerQuitEvent event) {
		
		PerkPlayer player = PerkUtils.getPlayer(event.getPlayer());
		if (player == null)
			return;
		
		if (player.isVanished()) {
			event.setQuitMessage(null);
		}
		
		player.showPlayer(false);
		PerkUtils.perkPlayers.removePlayer(player.getPlayer());
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerKick(PlayerKickEvent event) {
		PerkPlayer player = PerkUtils.getPlayer(event.getPlayer());
		
		if (player == null) 
			return;
		
		if (player.isVanished()) {
			event.setLeaveMessage(null);
		}
		
		player.showPlayer(false);
			
		PerkUtils.perkPlayers.removePlayer(event.getPlayer());
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerMove(PlayerMoveEvent event) {

		PerkPlayer player = PerkUtils.getPlayer(event.getPlayer());
		if (player == null)
			return;

		/* Handle afk moving */
		if (player.isAfk()) {
			event.setTo(event.getFrom());
			return;
		}
		
		/* handle spectate */		
		if (PerkSpectate.isBeingFolowed(player)) {
			PerkPlayer stalker = player.getSpecatingPlayer();
			stalker.teleport(player.getPlayer().getLocation());
			return;
		}

		// stop the stalker moving
		if (player.isSpectating()) {
			event.setTo(event.getFrom());
			return;
		}
		
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerTeleport(PlayerTeleportEvent event) {

		PerkPlayer player = PerkUtils.getPlayer(event.getPlayer());

		if (player == null)
			return;

		Location from = event.getFrom();
		Location to = event.getTo();

		// stop people changing worlds without permission
		if (from.getWorld() != to.getWorld()) {
			String permission = "perks.changeworld." + to.getWorld().getName();
			if (!player.hasPermission(permission, false)) {
				PerkUtils.OutputToPlayer(player, "You don't have permission to enter that world");
				event.setCancelled(true);
				return;
			}

			Player bukkitPlayer = player.getPlayer();

			// if in creative and DOES NOT have permissions to keep inventory,
			// wipe inventory
			if (bukkitPlayer.getGameMode() == GameMode.CREATIVE) {
				if (!player.hasPermission("perks.keepinventonworldchange", false)) {
					bukkitPlayer.getInventory().clear();
					bukkitPlayer.setGameMode(GameMode.SURVIVAL);
				}
			}
		}
		
		if (player.hasPermission("perks.capes", false)) {
			PerkCapes.setCape(player.getPlayer());
		}
		
		PerkColors.addColor(player.getPlayer());

	}
	
	// returns a PerkPlayer from a given Bukkit Player
	public PerkPlayer findPlayer(Player player) {
		return PerkUtils.getPlayer(player);
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerChat(PlayerChatEvent event) {
		PerkPlayer player = PerkUtils.getPlayer(event.getPlayer());
		
		if (player.isAfk()) {	
			event.setCancelled(true);
		}
		
		/* URL Shortener */
		if (player.hasPermission("perks.chat.shorten", false)) {
			
			String msg = event.getMessage();
			// if it uses http
			if(msg.indexOf("http://") != -1){
				StringBuilder result = new StringBuilder(msg.length());
					for(StringTokenizer tokenizer = new StringTokenizer(msg, " ", true); tokenizer.hasMoreTokens();)
					{
						String token = tokenizer.nextToken();
						if(token.startsWith("http://")) {
							try	{
								// shorten the url and add it into the message
								result.append(PerkUrlShortener.tinyUrl(token));
							} catch(Exception e) {
								result.append(token);
								e.printStackTrace();
							}
						} else {
						result.append(token);
						}
				 
					}
				event.setMessage(result.toString());
			}
			
			// if it uses https
			if(msg.indexOf("https://") != -1){
				StringBuilder result = new StringBuilder(msg.length());
					for(StringTokenizer tokenizer = new StringTokenizer(msg, " ", true); tokenizer.hasMoreTokens();){
						String token = tokenizer.nextToken();
						if(token.startsWith("https://")) {
							try	{
								// shorten the url and add it into the message
								result.append(PerkUrlShortener.tinyUrl(token));
							} catch(Exception e) {
								result.append(token);
								e.printStackTrace();
							}
						} else {
						result.append(token);
						}
				 
					}
				event.setMessage(result.toString());
			}
			
			// if it uses www.
			if(msg.indexOf("www.") != -1){
				StringBuilder result = new StringBuilder(msg.length());
					for(StringTokenizer tokenizer = new StringTokenizer(msg, " ", true); tokenizer.hasMoreTokens();){
						String token = tokenizer.nextToken();
						if(token.startsWith("www.")) {
							try	{
								// shorten the url and add it into the message
								result.append(PerkUrlShortener.tinyUrl("http://" + token));
							} catch(Exception e) {
								result.append(token);
								e.printStackTrace();
							}
						} else {
							result.append(token);
						}
							 
					}
				event.setMessage(result.toString());
			}
					
		}
	}
	
	@EventHandler (priority = EventPriority.NORMAL)
	public void onPlayerInteraction(PlayerInteractEvent event) {
		PerkPlayer player = PerkUtils.getPlayer(event.getPlayer());
		PerkThor.onPlayerInteract(player, event);		
		
		if (player != null && (player.isFlying() || player.isVanished())) {
			
			Player p = player.getPlayer();			
			if (p.getItemInHand().getType() == Material.LAVA_BUCKET || 
				p.getItemInHand().getType() == Material.FLINT_AND_STEEL ||
				p.getItemInHand().getType().getId() == 385) {	
				PerkUtils.OutputToPlayer(player, player.isFlying() ? "You can't start fires whilst flying" : "You can't start fires whilst vanished");
				event.setCancelled(true);	
				return;
			}
		}
	}
	
	@EventHandler (priority = EventPriority.NORMAL)
	public void onPlayerPickUpItem(PlayerPickupItemEvent event) {
		PerkPlayer player = PerkUtils.getPlayer(event.getPlayer());
		
		if (player.isVanished())
			event.setCancelled(true);
		
	}
}