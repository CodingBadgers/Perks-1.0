package uk.codingbadgers.perks.config;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import uk.codingbadgers.perks.utils.PerkPlayer;
import uk.codingbadgers.perks.utils.PerkUtils;
import uk.codingbadgers.perks.utils.PerkWorldSpawn;
import uk.thecodingbadgers.bDatabaseManager.Database.BukkitDatabase;
import uk.thecodingbadgers.bDatabaseManager.bDatabaseManager;
import uk.thecodingbadgers.bDatabaseManager.bDatabaseManager.DatabaseType;


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
		m_perksDB = bDatabaseManager.createDatabase(PerkConfig.DATABASE.name, PerkUtils.plugin, DatabaseType.SQL);
		
		if (!m_perksDB.login(PerkConfig.DATABASE.ip, PerkConfig.DATABASE.user, PerkConfig.DATABASE.password, PerkConfig.DATABASE.port))
			return;
		
		// see if a table called properties exist
		if (!m_perksDB.tableExists("perks_homes")) {
			
			// the table doesn't exist, so make one.
			
			PerkUtils.DebugConsole("Could not find perk homes table, now creating one.");
			String query = "CREATE TABLE perks_homes (" +
					"player VARCHAR(64)," +
					"world VARCHAR(128)," +
					"x INT," +
					"y INT," +
					"z INT," +
					"yaw INT," +
					"pitch INT" +
					");";
			
			// to create a table we pass an SQL query.
			m_perksDB.query(query, true);
		}
		
		// load all properties
		
		// select every property from the table
		String query = "SELECT * FROM perks_homes";
		ResultSet result = m_perksDB.queryResult(query);
		
		if (result != null) {
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
			
			m_perksDB.freeResult(result);
		}
		
		// see if a table called properties exist
		if (!m_perksDB.tableExists("perks_build")) {
			
			// the table doesn't exist, so make one.
			
			PerkUtils.DebugConsole("Could not find perk homes builds, now creating one.");
			query = "CREATE TABLE perks_build (" +
					"player VARCHAR(64)," +
					"world VARCHAR(128)," +
					"x INT," +
					"y INT," +
					"z INT," +
					"yaw INT," +
					"pitch INT" +
					");";
			
			// to create a table we pass an SQL query.
			m_perksDB.query(query, true);
		}
		
		// select every property from the table
		query = "SELECT * FROM perks_build";
		result = m_perksDB.queryResult(query);
		
		if (result != null) {
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
			
			m_perksDB.freeResult(result);
		}
			
		// see if a table called properties exist
		if (!m_perksDB.tableExists("perks_vanish")) {
			
			// the table doesn't exist, so make one.
			
			PerkUtils.DebugConsole("Could not find perk vanish databse, now creating one.");
			query = "CREATE TABLE perks_vanish (" +
					"player VARCHAR(64)" +
					");";
			
			// to create a table we pass an SQL query.
			m_perksDB.query(query, true);
		}


		// see if a table called properties exist
		if (!m_perksDB.tableExists("perks_kit")) {
			
			// the table doesn't exist, so make one.
			
			PerkUtils.DebugConsole("Could not find perk kit databse, now creating one.");
			query = "CREATE TABLE perks_kit (" +
					"player VARCHAR(64)," +
					"kitname VARCHAR(64)," +
					"time LONG" +
					");";
			
			// to create a table we pass an SQL query.
			m_perksDB.query(query, true);
		}
			
		if (!m_perksDB.tableExists("perks_flying")) {
			
			PerkUtils.DebugConsole("Could not find flying table, creating one now");
			
			query = "CREATE TABLE perks_flying (name VARCHAR(64));";
			
			m_perksDB.query(query, true);
		}

		if (!m_perksDB.tableExists("perks_spawn")) {
			
			PerkUtils.DebugConsole("Could not find spawn table, creating one now");
			
			query = "CREATE TABLE perks_spawn (" +
						"world VARCHAR(64)," +
						"x INT," +
						"y INT," +
						"z INT," +
						"yaw FLOAT," +
						"pitch FLOAT" +
						");";
			m_perksDB.query(query, true);
		}
		
		query = "SELECT * FROM perks_spawn";
		result = m_perksDB.queryResult(query);
		
		if (result != null) {
			try {
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
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			m_perksDB.freeResult(result);
		}
		
		UpgradeDatabases();
	}
	
	private static void UpgradeDatabases() {
		
		File buildFile = FindDatabase("Build.sqlite");
		if (buildFile != null) {
			
			BukkitDatabase tempDb = bDatabaseManager.createDatabase("Build", PerkUtils.plugin, DatabaseType.SQLite);
			
			// select every property from the table
			String query = "SELECT * FROM build";
			ResultSet result = tempDb.queryResult(query);
			
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
			tempDb.freeResult(result);
			tempDb.freeDatabase();
			
			buildFile.renameTo(new File(PerkUtils.plugin.getDataFolder().getAbsolutePath() + "\\Build.sqlite.upgradded"));
		}
		
		File homeFile = FindDatabase("Homes.sqlite");
		if (homeFile != null) {
			
			BukkitDatabase tempDb = bDatabaseManager.createDatabase("Homes", PerkUtils.plugin, DatabaseType.SQLite);
			
			// select every property from the table
			String query = "SELECT * FROM homes";
			ResultSet result = tempDb.queryResult(query);
			
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
			tempDb.freeResult(result);
			tempDb.freeDatabase();
			
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
		
		String query = "INSERT INTO `perks_homes` " +
				"(`player`,`world`,`x`,`y`,`z`,`yaw`,`pitch`) VALUES (" + 
				"'" + player + "'," +
				"'" + loc.getWorld().getName() + "'," +
				"'" + loc.getX() + "'," +
				"'" + loc.getY() + "'," +
				"'" + loc.getZ() + "'," +
				"'" + loc.getYaw() + "'," +
				"'" + loc.getPitch() + 
				"');";
		m_perksDB.query(query);
		
	}

	public static void AddBuild(String player, Location loc) {
		
		String query = "INSERT INTO `perks_build` " +
				"(`player`,`world`,`x`,`y`,`z`,`yaw`,`pitch`) VALUES (" + 
				"'" + player + "'," +
				"'" + loc.getWorld().getName() + "'," +
				"'" + loc.getX() + "'," +
				"'" + loc.getY() + "'," +
				"'" + loc.getZ() + "'," +
				"'" + loc.getYaw() + "'," +
				"'" + loc.getPitch() + 
				"');";
		m_perksDB.query(query);
		
	}
	
	public static void UpdateHome(Player player, Location loc) {
		
		String query = "UPDATE `perks_homes` SET " +
				"x = '" + loc.getX() +"', " +
				"y = '" + loc.getY() +"', " +
				"z = '" + loc.getZ() +"', " +
				"yaw = '" + loc.getYaw() +"', " +
				"pitch = '" + loc.getPitch() +"' " +
				"WHERE player = '" + player.getName() + 
				"' AND world = '" + loc.getWorld().getName() + "';";
		m_perksDB.query(query);
		
	}

	public static void UpdateBuild(Player player, Location loc) {
		
		String query = "UPDATE `perks_build` SET " +
				"world = '" + loc.getWorld().getName() +"', " +
				"x = '" + loc.getX() +"', " +
				"y = '" + loc.getY() +"', " +
				"z = '" + loc.getZ() +"', " +
				"yaw = '" + loc.getYaw() +"', " +
				"pitch = '" + loc.getPitch() +"' " +
				"WHERE player = '" + player.getName() + "';";
		m_perksDB.query(query);			
		
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
	
	public static Location getBuild(Player player) {
		
		for (int i = 0; i < builds.size(); ++i) {
			tpLocation h = builds.get(i);
			if (h.playername.equalsIgnoreCase(player.getName())) {
				return h.loc;
			}
		}
		return null;
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
	
	public static Location getHome(Player player, World world) {
		
		for (int i = 0; i < homes.size(); ++i) {
			tpLocation h = homes.get(i);
			if (h.playername.equalsIgnoreCase(player.getName()) 
					&& world == h.loc.getWorld()) {
				return h.loc;
			}
		}
		return null;
	}
	
	public static Location getHome(Player player) {
		return getHome(player, player.getLocation().getWorld());
	}
	
	public static void addVanishPlayer(PerkPlayer player) {
		
		String query = "INSERT INTO `perks_vanish` " +
				"(`player`) VALUES (" + 
				"'" + player.getPlayer().getName() +
				"');";
		m_perksDB.query(query, true);
		
	}
	
	public static void removeVanishPlayer(PerkPlayer player) {
		
		String query = "DELETE FROM `perks_vanish` " +
				"WHERE player=" + 
				"'" + player.getPlayer().getName() +
				"';";
		m_perksDB.query(query, true);
		
	}
	
	public static boolean isVanished(PerkPlayer player) {
		
		if (!player.hasPermission("perks.vanish", false))
			return false;
		
		String query = "SELECT * FROM `perks_vanish` WHERE player='" + player.getPlayer().getName() + "'";
		ResultSet result = m_perksDB.queryResult(query);
		
		if (result == null)
			return false;
		
		boolean vanished;
		try {
			vanished = result.next();
		} catch (SQLException e) {
			vanished = false;
		}
		
		m_perksDB.freeResult(result);
		return vanished;
	}
	
	public static boolean isFlying(PerkPlayer player) {
		
		if (m_perksDB == null)
			return false;
		
		if (!player.hasPermission("perks.fly", false)) 
			return false;
		
		String query = "SELECT * FROM `perks_flying` WHERE name ='" + player.getPlayer().getName() + "'";
		ResultSet result = m_perksDB.queryResult(query);
		
		if (result == null)
			return false;

		boolean flying;
		try {
			flying = result.next();
		} catch (SQLException e) {
			flying = false;
		}
		
		m_perksDB.freeResult(result);
		return flying;
	}
	
	public static void setFlying(PerkPlayer player, boolean flying) {
		
		if (m_perksDB == null)
			return;
		
		String query;
		
		// if they are to be set flying insert them into the table
		if (flying) {
			query = "INSERT INTO `perks_flying` (name) VALUES (" +
					"'" + player.getPlayer().getName() + "');";
		// if not remove them from it
		} else {
			query = "DELETE FROM `perks_flying` WHERE " +
					"name='" + player.getPlayer().getName() + "';";
		}
		
		m_perksDB.query(query, true);
	}
	
	public static void setSpawn(PerkWorldSpawn spawn) {
		
		String query = "INSERT INTO `perks_spawn` (" +
						"`world`, `x`, `y`, `z`, `yaw`, `pitch`) VALUES (" +
						"'" + spawn.getSpawn().getWorld().getName() + "'," +
						"'" + spawn.getSpawn().getX() + "'," +
						"'" + spawn.getSpawn().getY() + "'," +
						"'" + spawn.getSpawn().getZ() + "'," +
						"'" + spawn.getSpawn().getYaw() + "'," +
						"'" + spawn.getSpawn().getPitch() + "'" +
						");";
		
		for (int i = 0; i < spawns.size(); ++i) {
			if (spawns.get(i).getWorld().getName().equalsIgnoreCase(spawn.getWorld().getName())) {
				query = "UPDATE `perks_spawn` SET " +
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
		
		m_perksDB.query(query, true);
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

	public static void Stop() {
		m_perksDB.freeDatabase();
	}

}
