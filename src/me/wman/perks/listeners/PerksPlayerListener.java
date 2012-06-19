package me.wman.perks.listeners;

import me.wman.perks.admin.PerkSpectate;
import me.wman.perks.admin.PerkThor;
import me.wman.perks.admin.PerkVanish;
import me.wman.perks.donator.PerkJoining;
import me.wman.perks.donator.PerkCapes;
import me.wman.perks.donator.PerkColors;
import me.wman.perks.donator.PerkList;
import me.wman.perks.donator.PerkPlugins;
import me.wman.perks.utils.PerkPlayer;
import me.wman.perks.utils.PerkUrlShortener;
import me.wman.perks.utils.PerkUtils;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.bukkit.PermissionsEx;

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

	// Not tested yet, need to max out a server to try though :P
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
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerQuit(PlayerQuitEvent event) {
		
		PerkPlayer player = PerkUtils.getPlayer(event.getPlayer());
		if (player == null)
			return;
		
		if (player.isVanished()) {
			event.setQuitMessage(null);
		}
		
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
			event.setCancelled(true);
			return;
		}
		
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerTeleport(PlayerTeleportEvent event) {

		PerkPlayer player = PerkUtils.getPlayer(event.getPlayer());

		if (player == null)
			return;

		if (player.isAfk()) {
			event.setTo(event.getFrom());
			return;
		}
		
		if (player.hasPermission("perks.capes", false)) {
			PerkCapes.setCape(player.getPlayer());
		}
				
		PerkColors.addColor(player.getPlayer());
				
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
			
			if (bukkitPlayer.isFlying()) {
				bukkitPlayer.setFlying(false);
			}
		}
				
		// make sure the location wont kill them...
		if (event.getCause() == PlayerTeleportEvent.TeleportCause.COMMAND || event.getCause() == PlayerTeleportEvent.TeleportCause.PLUGIN) {
			
			if (player.getPlayer().getGameMode() == GameMode.CREATIVE)
				return;
			
			Location safe = new Location(to.getWorld(), to.getX(), to.getY(), to.getZ(), to.getYaw(), to.getPitch());
			Location safePlusOne = new Location(to.getWorld(), to.getX(), to.getY() + 1, to.getZ());
			
			
			while (IsSolidBlock(safe.getBlock()) || IsSolidBlock(safePlusOne.getBlock())) {
				safe = safe.add(0.0, 1.0, 0.0);
				safePlusOne = safePlusOne.add(0.0, 1.0, 0.0);
			}
			
			Location below = new Location(safe.getWorld(), safe.getX(), safe.getY(), safe.getZ());

			while (!IsSolidBlock(below.getBlock())) {
				below = below.subtract(0.0, 1.0, 0.0);
			}
			
			if (below.getBlock().getType() == Material.LAVA || below.getBlock().getType() == Material.STATIONARY_LAVA || below.getBlock().getType() == Material.FIRE) {
				PerkUtils.OutputToPlayer(player, "The location you wanted to teleport to will hurt you");
				PerkUtils.OutputToPlayer(player, "You will fall into lava or fire, teleport cancelled");
				event.setCancelled(true);
			} else if (safe.getY() != to.getY()) {
				PerkUtils.OutputToPlayer(player, "The location you wanted to teleport to is obstructed");
				PerkUtils.OutputToPlayer(player, "We have teleported you to a safe location");
			}
			
			event.setTo(safe);
		}
		
		// stop the use of ender pearls, there just annoying
		if (event.getCause() == TeleportCause.ENDER_PEARL) {
			event.setCancelled(true);
		}
		
		// check for creative world.
		if (event.getTo().getWorld().getName().equalsIgnoreCase("world_creative")) {
			player.getPlayer().setGameMode(GameMode.CREATIVE);
		}		

	}
	
	private boolean IsSolidBlock(Block block) {
		
		if (block.getType() == Material.AIR)
			return false;
		
		if (block.getType() == Material.WATER)
			return false;
		
		if (block.getType() == Material.STATIONARY_WATER)
			return false;
		
		if (block.getType() == Material.PORTAL)
			return false;
		
		if (block.getType() == Material.SIGN)
			return false;
		
		if (block.getType() == Material.SIGN_POST)
			return false;
		
		if (block.getType() == Material.WALL_SIGN)
			return false;
		
		if (block.getType() == Material.SNOW)
			return false;
		
		if (block.getType() == Material.LONG_GRASS)
			return false;
		
		if (block.getType() == Material.RED_ROSE)
			return false;
		
		if (block.getType() == Material.YELLOW_FLOWER)
			return false;
		
		if (block.getType() == Material.RED_MUSHROOM)
			return false;
		
		if (block.getType() == Material.BROWN_MUSHROOM)
			return false;
		
		if (block.getType() == Material.WEB)
			return false;
		
		if (block.getType() == Material.VINE)
			return false;
		
		if (block.getType() == Material.LADDER)
			return false;
		
		if (block.getType() == Material.RAILS)
			return false;
		
		if (block.getType() == Material.POWERED_RAIL)
			return false;
		
		if (block.getType() == Material.DETECTOR_RAIL)
			return false;
		
		if (block.getType() == Material.DEAD_BUSH)
			return false;
		
		if (block.getType() == Material.LEVER)
			return false;
		
		if (block.getType() == Material.STONE_BUTTON)
			return false;
		
		if (block.getType() == Material.STONE_PLATE)
			return false;
		
		if (block.getType() == Material.WOOD_PLATE)
			return false;
				
		return true;
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
			event.setMessage(PerkUrlShortener.parseMessage(msg));
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
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerChangeGamemode(PlayerGameModeChangeEvent event) {
		PerkPlayer player = PerkUtils.getPlayer(event.getPlayer());
		
		if (event.getNewGameMode() != GameMode.SURVIVAL)
			return;
		
		player.setFlying(false);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void commandPreprocess(final PlayerCommandPreprocessEvent event) {
		
		final String command = event.getMessage().split(" ")[0].substring(1).toLowerCase();
		if (command.equals("plugins") || command.equals("pl")) {
			
			PerkPlayer player = PerkUtils.getPlayer(event.getPlayer());
			if (player == null)
				return;
			
			// handles plugins cmd
			if (!PerkPlugins.onCommand(player, command))		
				return;
			
			event.setCancelled(true);
		}
		
	}
}