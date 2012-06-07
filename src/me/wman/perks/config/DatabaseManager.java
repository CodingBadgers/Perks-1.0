package me.wman.perks.config;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import me.wman.perks.donator.PerkKits;
import me.wman.perks.utils.PerkKit;
import me.wman.perks.utils.PerkPlayer;
import me.wman.perks.utils.PerkUtils;
import me.wman.perks.utils.PerkWorldSpawn;
import n3wton.me.BukkitDatabaseManager.BukkitDatabaseManager;
import n3wton.me.BukkitDatabaseManager.BukkitDatabaseManager.DatabaseType;
import n3wton.me.BukkitDatabaseManager.Database.BukkitDatabase;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;


public class DatabaseManager {
	
	static private BukkitDatabase m_perksDB = null;
	
	public static class tpLocation {	
		String playername;
		Location loc;
	}
		
	static public ArrayList<tpLocation> homes = new ArrayList<tpLocation>();
	static public ArrayList<tpLocation> builds = new ArrayList<tpLocation>();
	static public ArrayList<PerkWorldSpawn> spawns = new ArrayList<PerkWorldSpawn>();
	
	public static void loadDatabases() {
				
		// create an the database
		m_perksDB = BukkitDatabaseManager.CreateDatabase("Perks", PerkUtils.plugin, DatabaseType.SQLite);
		
		// see if a table called properties exist
		if (!m_perksDB.TableExists("homes")) {
			
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
			m_perksDB.Query(query, true);
		}
		
		// load all properties
		
		// select every property from the table
		String query = "SELECT * FROM homes";
		ResultSet result = m_perksDB.QueryResult(query);
		
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
		
		m_perksDB.FreeResult(result);
				
		// see if a table called properties exist
		if (!m_perksDB.TableExists("build")) {
			
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
			m_perksDB.Query(query, true);
		}
		
		// select every property from the table
		query = "SELECT * FROM build";
		result = m_perksDB.QueryResult(query);
		
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
		
		m_perksDB.FreeResult(result);
		
		// see if a table called properties exist
		if (!m_perksDB.TableExists("vanish")) {
			
			// the table doesn't exist, so make one.
			
			PerkUtils.DebugConsole("Could not find perk vanish databse, now creating one.");
			query = "CREATE TABLE vanish (" +
					"player VARCHAR(64)" +
					");";
			
			// to create a table we pass an SQL query.
			m_perksDB.Query(query, true);
		}


		// see if a table called properties exist
		if (!m_perksDB.TableExists("kit")) {
			
			// the table doesn't exist, so make one.
			
			PerkUtils.DebugConsole("Could not find perk kit databse, now creating one.");
			query = "CREATE TABLE kit (" +
					"player VARCHAR(64)," +
					"kitname VARCHAR(64)," +
					"time LONG" +
					");";
			
			// to create a table we pass an SQL query.
			m_perksDB.Query(query, true);
		}
		
		// select every property from the table
		query = "SELECT * FROM kit";
		result = m_perksDB.QueryResult(query);
		
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
		
		m_perksDB.FreeResult(result);
		
		if (!m_perksDB.TableExists("flying")) {
			
			PerkUtils.DebugConsole("Could not find flying table, creating one now");
			
			query = "CREATE TABLE flying (name VARCHAR(64));";
			
			m_perksDB.Query(query, true);
		}
		
		// no need to store in array, will just read when needed
		
		if (!m_perksDB.TableExists("spawn")) {
			
			PerkUtils.DebugConsole("Could not find spawn table, creating one now");
			
			query = "CREATE TABLE spawn (" +
						"world VARCHAR(64)," +
						"x INT," +
						"y INT," +
						"z INT," +
						"yaw FLOAT," +
						"pitch FLOAT" +
						");";
			m_perksDB.Query(query, true);
		}
		
		query = "SELECT * FROM spawn";
		result = m_perksDB.QueryResult(query);
		
		try {
			if (result != null) {
				while(result.next()) {
					World world = PerkUtils.server().getWorld(result.getString("world"));
					Location loc = new Location (world, 
													result.getInt("x"),
													result.getInt("y"),
													result.getInt("z"),
													result.getFloat("yaw"),
													result.getFloat("pitch"));
					PerkWorldSpawn spawn = new PerkWorldSpawn(world, loc);
					spawns.add(spawn);
				}
				PerkUtils.DebugConsole("Loaded " + spawns.size() + " spawns");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		m_perksDB.FreeResult(result);
		
		UpgradeDatabases();
	}
	
	private static void UpgradeDatabases() {
		
		File buildFile = FindDatabase("Build.sqlite");
		if (buildFile != null) {
			
			BukkitDatabase tempDb = BukkitDatabaseManager.CreateDatabase("Build", PerkUtils.plugin, DatabaseType.SQLite);
			
			// select every property from the table
			String query = "SELECT * FROM build";
			ResultSet result = tempDb.QueryResult(query);
			
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
		            if (world == null)
		            	continue;
		            
		            Location loc = new Location(world, x, y, z, yaw, pitch);

		            tpLocation newHome = new tpLocation();
		            newHome.playername = playerName;
		            newHome.loc = loc;
		            builds.add(newHome);
		            AddBuild(playerName, loc);
		        }
			} catch (SQLException e) {
				e.printStackTrace();
				return;
			}		
			tempDb.FreeResult(result);
			tempDb.End();
			
			buildFile.renameTo(new File(PerkUtils.plugin.getDataFolder().getAbsolutePath() + "\\Build.sqlite.upgradded"));
		}
		
		File homeFile = FindDatabase("Homes.sqlite");
		if (homeFile != null) {
			
			BukkitDatabase tempDb = BukkitDatabaseManager.CreateDatabase("Homes", PerkUtils.plugin, DatabaseType.SQLite);
			
			// select every property from the table
			String query = "SELECT * FROM homes";
			ResultSet result = tempDb.QueryResult(query);
			
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
		            if (world == null)
		            	continue;
		            
		            Location loc = new Location(world, x, y, z, yaw, pitch);
		                        
		            tpLocation newHome = new tpLocation();
		            newHome.playername = playerName;
		            newHome.loc = loc;
		            homes.add(newHome);
		            AddHome(playerName, loc);
		        }
			} catch (SQLException e) {
				e.printStackTrace();
				return;
			}		
			tempDb.FreeResult(result);
			tempDb.End();
			
			homeFile.renameTo(new File(PerkUtils.plugin.getDataFolder().getAbsolutePath() + "\\Homes.sqlite.upgradded"));
		}
		
	}

	private static File FindDatabase(String dbName) {
		
		File path = PerkUtils.plugin.getDataFolder();
		
		if (!path.exists())
			return null;
		
		for (File file : path.listFiles()) {
			if (file.getName().equals(dbName))
				return file;
		}
		
		return null;
	}

	public static void AddHome(String player, Location loc) {
		
		String query = "INSERT INTO 'homes' " +
				"('player','world','x','y','z','yaw','pitch') VALUES (" + 
				"'" + player + "'," +
				"'" + loc.getWorld().getName() + "'," +
				"'" + loc.getX() + "'," +
				"'" + loc.getY() + "'," +
				"'" + loc.getZ() + "'," +
				"'" + loc.getYaw() + "'," +
				"'" + loc.getPitch() + 
				"');";
		m_perksDB.Query(query);
		
	}

	public static void AddBuild(String player, Location loc) {
		
		String query = "INSERT INTO 'build' " +
				"('player','world','x','y','z','yaw','pitch') VALUES (" + 
				"'" + player + "'," +
				"'" + loc.getWorld().getName() + "'," +
				"'" + loc.getX() + "'," +
				"'" + loc.getY() + "'," +
				"'" + loc.getZ() + "'," +
				"'" + loc.getYaw() + "'," +
				"'" + loc.getPitch() + 
				"');";
		m_perksDB.Query(query);
		
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
		m_perksDB.Query(query);
		
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
		m_perksDB.Query(query);			
		
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
		
		DatabaseManager.AddBuild(player.getName(), loc);
		
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
		
		DatabaseManager.AddHome(player.getName(), loc);
		
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
		m_perksDB.Query(query, true);
		
	}
	
	public static void removeVanishPlayer(PerkPlayer player) {
		
		String query = "DELETE FROM 'vanish' " +
				"WHERE player=" + 
				"'" + player.getPlayer().getName() +
				"';";
		m_perksDB.Query(query, true);
		
	}
	
	public static boolean isVanished(PerkPlayer player) {
		
		if (!player.hasPermission("perks.vanish", false))
			return false;
		
		String query = "SELECT * FROM vanish WHERE player='" + player.getPlayer().getName() + "'";
		ResultSet result = m_perksDB.QueryResult(query);
		
		boolean vanished;
		try {
			vanished = result.next();
		} catch (SQLException e) {
			vanished = false;
		}
		
		m_perksDB.FreeResult(result);
		return vanished;
	}
	
	public static void addKit(PerkPlayer player, PerkKit kit, Long time) {
		
		String query = "INSERT INTO 'kit' " +
				"('player','kitname','time') VALUES (" + 
				"'" + player.getPlayer().getName() + "'," +
				"'" + kit.getName() + "'," +
				"'" + time + 
				"');";
		
		m_perksDB.Query(query, true);
		
	}
	
	public static void deleteKit(PerkPlayer player, PerkKit kit) {
		
		String query = "DELETE FROM 'kit' " +
				"WHERE player=" + 
				"'" + player.getPlayer().getName() +
				"' AND kitname=" +
				"'" + kit.getName() +
				"';";
		
		m_perksDB.Query(query, true);
		
	}
	
	public static void loadKit(PerkPlayer player) {
		
		String query = "SELECT * FROM kit WHERE player = '" + player.getPlayer().getName() + "'";
		ResultSet result = m_perksDB.QueryResult(query);
		
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
		
		m_perksDB.FreeResult(result);
	}
	
	public static boolean isFlying(PerkPlayer player) {
		
		if (m_perksDB == null)
			return false;
		
		if (!player.hasPermission("perks.fly", false)) 
			return false;
		
		String query = "SELECT * FROM flying WHERE name ='" + player.getPlayer().getName() + "'";
		ResultSet result = m_perksDB.QueryResult(query);

		boolean flying;
		try {
			flying = result.next();
		} catch (SQLException e) {
			flying = false;
		}
		
		m_perksDB.FreeResult(result);
		return flying;
	}
	
	public static void setFlying(PerkPlayer player, boolean flying) {
		
		if (m_perksDB == null)
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
		
		m_perksDB.Query(query, true);
	}
	
	public static void setSpawn(PerkWorldSpawn spawn) {
		
		String query = "INSERT INTO spawn (" +
						"world, x, y, z, yaw, pitch) VALUES (" +
						"'" + spawn.getSpawn().getWorld().getName() + "'," +
						"'" + spawn.getSpawn().getX() + "'," +
						"'" + spawn.getSpawn().getY() + "'," +
						"'" + spawn.getSpawn().getZ() + "'," +
						"'" + spawn.getSpawn().getYaw() + "'," +
						"'" + spawn.getSpawn().getPitch() + "'" +
						");";
		
		for (int i = 0; i < spawns.size(); ++i) {
			if (spawns.get(i).getWorld().getName().equalsIgnoreCase(spawn.getWorld().getName())) {
				query = "UPDATE spawn SET " +
						"x='" + spawn.getSpawn().getX() + "', " +
						"y='" + spawn.getSpawn().getY() + "', " + 
						"z='" + spawn.getSpawn().getZ() + "', " +
						"yaw='" + spawn.getSpawn().getYaw() + "', " +
						"pitch='" + spawn.getSpawn().getPitch() + "' " +
						"WHERE world='" + spawn.getWorld().getName() + "'" +
						";";
				spawns.remove(i);
				break;
			}
		}
		
		m_perksDB.Query(query, true);
		spawns.add(spawn);
		PerkUtils.DebugConsole("Adding spawn for " + spawn.getWorld().getName());
	}
	
	public static PerkWorldSpawn getSpawn(World world) {
		Iterator<PerkWorldSpawn> itr = spawns.iterator();
		while(itr.hasNext()) {
			PerkWorldSpawn current = itr.next();
			if (current.getWorld().equals(world))
				return current;
		}
		return null;
	}

}
