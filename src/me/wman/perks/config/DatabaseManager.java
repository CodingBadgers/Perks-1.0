package me.wman.perks.config;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import me.wman.perks.donator.PerkKits;
import me.wman.perks.utils.PerkKit;
import me.wman.perks.utils.PerkPlayer;
import me.wman.perks.utils.PerkUtils;
import n3wton.me.BukkitDatabaseManager.BukkitDatabaseManager;
import n3wton.me.BukkitDatabaseManager.BukkitDatabaseManager.DatabaseType;
import n3wton.me.BukkitDatabaseManager.Database.BukkitDatabase;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;


public class DatabaseManager {
	
	static private BukkitDatabase m_homedb = null;
	static private BukkitDatabase m_builddb = null;
	static private BukkitDatabase m_vanishdb = null;
	static private BukkitDatabase m_kitdb = null;
	static private BukkitDatabase m_flydb = null;
	
	public static class tpLocation {	
		String playername;
		Location loc;
	}
		
	static public ArrayList<tpLocation> homes = new ArrayList<tpLocation>();
	static public ArrayList<tpLocation> builds = new ArrayList<tpLocation>();
	
	public static void loadDatabases() {
		
		// create an the database
		m_homedb = BukkitDatabaseManager.CreateDatabase("Homes", PerkUtils.plugin, DatabaseType.SQLite);
		
		// see if a table called properties exist
		if (!m_homedb.TableExists("homes")) {
			
			// the table doesn't exist, so make one.
			
			PerkUtils.DebugConsole("Could not find perk homes table, now creating one.");
			String query = "CREATE TABLE homes (" +
					"player VARCHAR(64)," +
					"world VARCHAR(128)," +
					"x INT," +
					"y INT," +
					"z INT," +
					"yaw INT," +
					"pitch INT" +
					");";
			
			// to create a table we pass an SQL query.
			m_homedb.Query(query, true);
		}
		
		// load all properties
		
		// select every property from the table
		String query = "SELECT * FROM homes";
		ResultSet result = m_homedb.QueryResult(query);
		
		try {
			// while we have another result, read in the data
			while (result.next()) {
	            String worldName = result.getString("world");
	            String playerName = result.getString("player");

	            int x = result.getInt("x");
	            int y = result.getInt("y");
	            int z = result.getInt("z");
	            int pitch = result.getInt("pitch");
	            int yaw = result.getInt("yaw");
	            
	            World world = PerkUtils.plugin.getServer().getWorld(worldName);
	            Location loc = new Location(world, x, y, z, yaw, pitch);
	                        
	            tpLocation newHome = new tpLocation();
	            newHome.playername = playerName;
	            newHome.loc = loc;
	            homes.add(newHome);
	            
	        }
		} catch (SQLException e) {
			e.printStackTrace();
			return;
		}
		
		m_homedb.FreeResult(result);
				
		// create an SQList object
		m_builddb = BukkitDatabaseManager.CreateDatabase("Build", PerkUtils.plugin, DatabaseType.SQLite);
		
		// see if a table called properties exist
		if (!m_builddb.TableExists("build")) {
			
			// the table doesn't exist, so make one.
			
			PerkUtils.DebugConsole("Could not find perk homes builds, now creating one.");
			query = "CREATE TABLE build (" +
					"player VARCHAR(64)," +
					"world VARCHAR(128)," +
					"x INT," +
					"y INT," +
					"z INT," +
					"yaw INT," +
					"pitch INT" +
					");";
			
			// to create a table we pass an SQL query.
			m_builddb.Query(query, true);
		}
		
		// select every property from the table
		query = "SELECT * FROM build";
		result = m_builddb.QueryResult(query);
		
		try {
			// while we have another result, read in the data
			while (result.next()) {
	            String worldName = result.getString("world");
	            String playerName = result.getString("player");

	            int x = result.getInt("x");
	            int y = result.getInt("y");
	            int z = result.getInt("z");
	            int pitch = result.getInt("pitch");
	            int yaw = result.getInt("yaw");
	            
	            World world = PerkUtils.plugin.getServer().getWorld(worldName);
	            Location loc = new Location(world, x, y, z, yaw, pitch);

	            tpLocation newHome = new tpLocation();
	            newHome.playername = playerName;
	            newHome.loc = loc;
	            builds.add(newHome);
	            
	        }
		} catch (SQLException e) {
			e.printStackTrace();
			return;
		}		
		
		m_builddb.FreeResult(result);
		
		// create an SQList object
		m_vanishdb = BukkitDatabaseManager.CreateDatabase("vanish", PerkUtils.plugin, DatabaseType.SQLite);
		
		// see if a table called properties exist
		if (!m_vanishdb.TableExists("vanish")) {
			
			// the table doesn't exist, so make one.
			
			PerkUtils.DebugConsole("Could not find perk vanish databse, now creating one.");
			query = "CREATE TABLE vanish (" +
					"player VARCHAR(64)" +
					");";
			
			// to create a table we pass an SQL query.
			m_vanishdb.Query(query, true);
		}

		// create an SQList object
		m_kitdb = BukkitDatabaseManager.CreateDatabase("kit", PerkUtils.plugin, DatabaseType.SQLite);
		
		// see if a table called properties exist
		if (!m_kitdb.TableExists("kit")) {
			
			// the table doesn't exist, so make one.
			
			PerkUtils.DebugConsole("Could not find perk kit databse, now creating one.");
			query = "CREATE TABLE kit (" +
					"player VARCHAR(64)," +
					"kitname VARCHAR(64)," +
					"time LONG" +
					");";
			
			// to create a table we pass an SQL query.
			m_kitdb.Query(query, true);
		}
		
		// select every property from the table
		query = "SELECT * FROM kit";
		result = m_kitdb.QueryResult(query);
		
		try {
			// while we have another result, read in the data
			while (result.next()) {
	            String playerName = result.getString("player");
	            String kitName = result.getString("kitname");
	            Long time = result.getLong("time");
	            
	            PerkPlayer player = PerkUtils.getPlayer(playerName);
	            PerkKits.load(player, kitName, time);
	        }
		} catch (SQLException e) {
			e.printStackTrace();
			return;
		}		
		
		m_kitdb.FreeResult(result);
		
		m_flydb = BukkitDatabaseManager.CreateDatabase("Fly", PerkUtils.plugin, DatabaseType.SQLite);
		
		if (!m_flydb.TableExists("flying")) {
			
			PerkUtils.DebugConsole("Could not find flying table, creating one now");
			
			query = "CREATE TABLE flying (name VARCHAR(64));";
			
			m_flydb.Query(query, true);
		}
		
		// no need to store in array, will just read when needed
	}
	
	public static void AddHome(Player player, Location loc) {
		
		String query = "INSERT INTO 'homes' " +
				"('player','world','x','y','z','yaw','pitch') VALUES (" + 
				"'" + player.getName() + "'," +
				"'" + loc.getWorld().getName() + "'," +
				"'" + loc.getX() + "'," +
				"'" + loc.getY() + "'," +
				"'" + loc.getZ() + "'," +
				"'" + loc.getYaw() + "'," +
				"'" + loc.getPitch() + 
				"');";
		m_homedb.Query(query);
		
	}

	public static void AddBuild(Player player, Location loc) {
		
		String query = "INSERT INTO 'build' " +
				"('player','world','x','y','z','yaw','pitch') VALUES (" + 
				"'" + player.getName() + "'," +
				"'" + loc.getWorld().getName() + "'," +
				"'" + loc.getX() + "'," +
				"'" + loc.getY() + "'," +
				"'" + loc.getZ() + "'," +
				"'" + loc.getYaw() + "'," +
				"'" + loc.getPitch() + 
				"');";
		m_builddb.Query(query);
		
	}
	
	public static void UpdateHome(Player player, Location loc) {
		
		String query = "UPDATE homes SET " +
				"x = '" + loc.getX() +"', " +
				"y = '" + loc.getY() +"', " +
				"z = '" + loc.getZ() +"', " +
				"yaw = '" + loc.getYaw() +"', " +
				"pitch = '" + loc.getPitch() +"' " +
				"WHERE player = '" + player.getName() + 
				"' AND world = '" + loc.getWorld().getName() + "';";
		m_homedb.Query(query);
		
	}

	public static void UpdateBuild(Player player, Location loc) {
		
		String query = "UPDATE build SET " +
				"world = '" + loc.getWorld().getName() +"', " +
				"x = '" + loc.getX() +"', " +
				"y = '" + loc.getY() +"', " +
				"z = '" + loc.getZ() +"', " +
				"yaw = '" + loc.getYaw() +"', " +
				"pitch = '" + loc.getPitch() +"' " +
				"WHERE player = '" + player.getName() + "';";
		m_builddb.Query(query);			
		
	}
	
	public static void setBuildLocation(Player player, Location loc) {
		
		for (int i = 0; i < builds.size(); ++i) {
			tpLocation b = builds.get(i);
			if (b.playername.equalsIgnoreCase(player.getName())) {
				DatabaseManager.UpdateBuild(player, loc);
				b.loc = loc;
				return;
			}
		}
		
		DatabaseManager.AddBuild(player, loc);
		
		tpLocation neBuild = new tpLocation();
		neBuild.playername = player.getName();
		neBuild.loc = loc;
		builds.add(neBuild);
		
	}
	
	public static void gotoBuild(Player player) {
		
		for (int i = 0; i < builds.size(); ++i) {
			tpLocation b = builds.get(i);
			if (b.playername.equalsIgnoreCase(player.getName())) {
				if (player.teleport(b.loc)) {
					PerkUtils.OutputToPlayer(player, "You have been teleported to your build location");
				}
				return;
			}
		}
		
		PerkUtils.OutputToPlayer(player, "You don't have a build location");
		PerkUtils.OutputToPlayer(player, "Use /setbuild to set your build location");
		
	}
	
	
	public static void setHomeLocation(Player player, Location loc) {
		
		for (int i = 0; i < homes.size(); ++i) {
			tpLocation b = homes.get(i);
			if (b.playername.equalsIgnoreCase(player.getName()) 
					&& player.getLocation().getWorld() == b.loc.getWorld()) {
				DatabaseManager.UpdateHome(player, loc);
				b.loc = loc;
				return;
			}
		}
		
		DatabaseManager.AddHome(player, loc);
		
		tpLocation newHome = new tpLocation();
		newHome.playername = player.getName();
		newHome.loc = loc;
		homes.add(newHome);
		
	}
	
	public static void gotoHome(Player player, World world) {
		
		for (int i = 0; i < homes.size(); ++i) {
			tpLocation h = homes.get(i);
			if (h.playername.equalsIgnoreCase(player.getName()) 
					&& world == h.loc.getWorld()) {
				if (player.teleport(h.loc)) {
					PerkUtils.OutputToPlayer(player, "You have been teleported to your home location");
				}
				return;
			}
		}
		
		PerkUtils.OutputToPlayer(player, "You don't have a home in '" + world.getName() + "'");
		PerkUtils.OutputToPlayer(player, "Use /sethome to set your home location");
		
	}
	
	public static void gotoHome(Player player) {
		gotoHome(player, player.getLocation().getWorld());
	}
	
	public static void addVanishPlayer(PerkPlayer player) {
		
		String query = "INSERT INTO 'vanish' " +
				"('player') VALUES (" + 
				"'" + player.getPlayer().getName() +
				"');";
		m_vanishdb.Query(query, true);
		
	}
	
	public static void removeVanishPlayer(PerkPlayer player) {
		
		String query = "DELETE FROM 'vanish' " +
				"WHERE player=" + 
				"'" + player.getPlayer().getName() +
				"';";
		m_vanishdb.Query(query, true);
		
	}
	
	public static boolean isVanished(PerkPlayer player) {
		
		if (!player.hasPermission("perks.vanish", false))
			return false;
		
		String query = "SELECT * FROM vanish WHERE player='" + player.getPlayer().getName() + "'";
		ResultSet result = m_vanishdb.QueryResult(query);
		
		boolean vanished;
		try {
			vanished = result.next();
		} catch (SQLException e) {
			vanished = false;
		}
		
		m_vanishdb.FreeResult(result);
		return vanished;
	}
	
	public static void addKit(PerkPlayer player, PerkKit kit, Long time) {
		
		String query = "INSERT INTO 'kit' " +
				"('player','kitname','time') VALUES (" + 
				"'" + player.getPlayer().getName() + "'," +
				"'" + kit.getName() + "'," +
				"'" + time + 
				"');";
		
		m_kitdb.Query(query, true);
		
	}
	
	public static void deleteKit(PerkPlayer player, PerkKit kit) {
		
		String query = "DELETE FROM 'kit' " +
				"WHERE player=" + 
				"'" + player.getPlayer().getName() +
				"' AND kitname=" +
				"'" + kit.getName() +
				"';";
		
		m_kitdb.Query(query, true);
		
	}
	
	public static void loadKit(PerkPlayer player) {
		
		String query = "SELECT * FROM kit WHERE player = '" + player.getPlayer().getName() + "'";
		ResultSet result = m_kitdb.QueryResult(query);
		
		try {
			// while we have another result, read in the data
			while (result.next()) {
	            String kitName = result.getString("kitname");
	            Long time = result.getLong("time");
	            
	            PerkKits.load(player, kitName, time);
	        }
		} catch (SQLException e) {
			e.printStackTrace();
			return;
		}		
		
		m_kitdb.FreeResult(result);
	}
	
	public static boolean isFlying(PerkPlayer player) {
		
		if (m_flydb == null)
			return false;
		
		if (!player.hasPermission("perks.fly", false)) 
			return false;
		
		String query = "SELECT * FROM flying WHERE name ='" + player.getPlayer().getName() + "'";
		ResultSet result = m_flydb.QueryResult(query);

		boolean flying;
		try {
			flying = result.next();
		} catch (SQLException e) {
			flying = false;
		}
		
		m_flydb.FreeResult(result);
		return flying;
	}
	
	public static void setFlying(PerkPlayer player, boolean flying) {
		
		if (m_flydb == null)
			return;
		
		String query;
		
		// if they are to be set flying insert them into the table
		if (flying) {
			query = "INSERT INTO flying (name) VALUES (" +
					"'" + player.getPlayer().getName() + "');";
		// if not remove them from it
		} else {
			query = "DELETE FROM flying WHERE " +
					"name='" + player.getPlayer().getName() + "';";
		}
		
		m_flydb.Query(query, true);
	}

}
