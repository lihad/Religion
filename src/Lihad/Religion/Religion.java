package Lihad.Religion;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import ru.tehkode.permissions.PermissionBackend;
import ru.tehkode.permissions.PermissionManager;
import ru.tehkode.permissions.bukkit.PermissionsEx;

import Lihad.Religion.Abilities.Personal;
import Lihad.Religion.Abilities.ReverseEngineering;
import Lihad.Religion.Abilities.SpellAoE;
import Lihad.Religion.Abilities.TowerAoE;
import Lihad.Religion.Bosses.Ahkmed;
import Lihad.Religion.Bosses.Bosses;
import Lihad.Religion.Bosses.BukkitScheduleBossManager;
import Lihad.Religion.Command.CommandRunner;
import Lihad.Religion.Config.BeyondConfig;
import Lihad.Religion.Config.BeyondConfigReader;
import Lihad.Religion.Config.BeyondConfigWriter;
import Lihad.Religion.Information.BeyondInfo;
import Lihad.Religion.Information.BeyondInfoReader;
import Lihad.Religion.Information.BeyondInfoWriter;
import Lihad.Religion.Listeners.BeyondBlockListener;
import Lihad.Religion.Listeners.BeyondEntityListener;
import Lihad.Religion.Listeners.BeyondInventoryListener;
import Lihad.Religion.Listeners.BeyondPlayerListener;
import Lihad.Religion.Listeners.BeyondPluginListener;
import Lihad.Religion.Recipe.Recipes;
import Lihad.Religion.Trades.TradesDriver;
import Lihad.Religion.Util.BeyondTimerTask;
import Lihad.Religion.Util.BukkitSchedulePlayerMove;
import Lihad.Religion.Util.Notification;
import Lihad.Religion.Util.UpdateTimer;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

import de.diddiz.LogBlock.LogBlock;

public class Religion extends JavaPlugin {
	
	
	/**
	 * @author Kyle, Joren
	 * 
	 * 
	 * ------------------------------READ THIS READ THIS!!!!------------------------------------------
	 * 
	 * To run this plugin, you will need the config.yml and information.yml, get those here:
	 *         -------          http://dl.dropbox.com/u/31548940/Religion.zip
	 * 
	 * ------------------------------GENERAL NEED TO SHTUFF ------------------------------------------
	 * TODO: Make it so people can't make a /home within the aoe of an opposing tower (requires MultiHome support)
	 * TODO: Make hooks for dynmap to show radius of towers (requires Dynmap support)
	 * 
	 * 
	 */
	
	/** Name of the plugin, used in output messages */
	protected static String name = "Religion";
	/** Header used for console and player output messages */
	protected static String header = "[" + name + "] ";
	
	public static List<String> OVERT = new LinkedList<String>();
	
	public static java.util.logging.Logger log = java.util.logging.Logger.getLogger("Minecraft");
	
    public static FileConfiguration configuration;
    public static YamlConfiguration information;
	public static PermissionHandler handler;
	public static PermissionManager ex;
	public static LogBlock logBlock;
	public static CommandExecutor cmd;
    public static UpdateTimer timer;
    public static BeyondTimerTask task;
    public static BukkitSchedulePlayerMove playerMoveTask;
    public static BukkitScheduleBossManager bossManagerTask;
    public static ReverseEngineering reverse;
    public static Recipes recipes;
    public static Notification notify;

    public static TradesDriver trades;
    
	public static BeyondConfigWriter configwrite;
	public static BeyondConfigReader configread;
	public static BeyondConfig config;
	
	public static BeyondInfoWriter infowrite;
	public static BeyondInfoReader inforead;
	public static BeyondInfo info;
	
	public static SpellAoE spell;
	public static TowerAoE tower;
	public static Personal personal;
	
	public static Bosses bosses;

	
	private final BeyondPluginListener pluginListener = new BeyondPluginListener(this);
	private final BeyondBlockListener blockListener = new BeyondBlockListener(this);
	private final BeyondPlayerListener playerListener = new BeyondPlayerListener(this);
	private final BeyondEntityListener entityListener = new BeyondEntityListener(this);
	private final BeyondInventoryListener inventoryListener = new BeyondInventoryListener(this);


	
	public static File infoFile = new File("plugins/Religion/information.yml");

	public void printResults(double timer, double count, String name)
	{
		double avg = 0;
		if (count != 0)
			avg = timer/count;
		info(name + " took " + timer + "ns " + " for a total of " + count + "runs - average: " + avg + "ns/run");
		return;
	}

	@Override
	public void onDisable() {
		UpdateTimer.timer.cancel();
		this.saveConfig();
		try {
			saveInfoFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onEnable() {
		System.out.println("-----------------------------------------");
		
		//LogManager
		log = this.getLogger();
		
		//ConfigManager
		configuration = this.getConfig();
		
		//InfoManager
		information = new YamlConfiguration();
		try {
			information.load(infoFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}
		
		//ClassManager
		configwrite = new BeyondConfigWriter(this);
		configread = new BeyondConfigReader(this);
		config = new BeyondConfig(this);
		infowrite = new BeyondInfoWriter(this);
		inforead = new BeyondInfoReader(this);
		info = new BeyondInfo(this);
		spell = new SpellAoE(this);
		tower = new TowerAoE(this);
		personal = new Personal(this);
		trades = new TradesDriver(this);
		reverse = new ReverseEngineering(this);
		recipes = new Recipes(this);
		bosses = new Bosses(this);

		//PermsManager
		
		setupPermissions();
		
		//PluginManager
		PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(blockListener, this);
        pm.registerEvents(pluginListener, this);
        pm.registerEvents(playerListener, this);
        pm.registerEvents(entityListener, this);
        pm.registerEvents(inventoryListener, this);

        //pm.registerEvent(Event.Type.ENTITY_DEATH, this.entityListener, Priority.Normal, this);

		//TimerManager
		task = new BeyondTimerTask();
		timer = new UpdateTimer(this);
        
		//BukkitSchedulerManager
		playerMoveTask = new BukkitSchedulePlayerMove(this);
		getServer().getScheduler().scheduleAsyncRepeatingTask(this,Religion.playerMoveTask, 0, 10L);
		
		//CommandManager
		cmd = new CommandRunner(this);
		getCommand("rr").setExecutor(cmd);
		getCommand("overt").setExecutor(cmd);
		
		//BossInitiallizer
		bossManagerTask = new BukkitScheduleBossManager();
		getServer().getScheduler().scheduleAsyncRepeatingTask(this,Religion.bossManagerTask, 0, 100L);
		
		//NotificationInit
		notify = new Notification(this);
		//getServer().getScheduler().scheduleAsyncRepeatingTask(this,Religion.notify, 0,37200L);
		

		//bosses.bossInit();
		
		//RecipeManager
		for(int i = 0;i<recipes.REVERSE_ENGINEERING_RECIPES.size();i++){
			this.getServer().addRecipe(recipes.REVERSE_ENGINEERING_RECIPES.get(i));
		}
		for(int i = 0;i<recipes.WEAPON_UPGRADE_RECIPES.size();i++){
			this.getServer().addRecipe(recipes.WEAPON_UPGRADE_RECIPES.get(i));
		}		

		System.out.println("[Religion] Has launched successfully.");
		System.out.println("-----------------------------------------");
	}
	
	public void setupPermissions() {
		Plugin permissionsPlugin = this.getServer().getPluginManager().getPlugin("Permissions");
		
		if (permissionsPlugin != null) {
			info("Succesfully connected to Permissions!");
			handler = ((Permissions) permissionsPlugin).getHandler();
			
		} else {
			handler = null;
			warning("Disconnected from Permissions...what could possibly go wrong?");
		}
	}
	public void setupPermissionsEx() {
		Plugin permissionsPlugin = this.getServer().getPluginManager().getPlugin("PermissionsEx");
		
		if (permissionsPlugin != null) {
			info("Succesfully connected to Permissions!");
			ex = PermissionsEx.getPermissionManager();
			
		} else {
			ex = null;
			warning("Disconnected from PermissionsEx...what could possibly go wrong?");
		}
	}
	public void setPlayerSuffix(Player player){
		if(BeyondInfo.hasPlayer(player))ex.getUser(player).setSuffix(ChatColor.WHITE.toString()+"["+ChatColor.LIGHT_PURPLE.toString()+BeyondInfo.getReligion(BeyondInfo.getTowerNamePlayerString(player.getName())).charAt(0)+ChatColor.WHITE.toString()+"]", null);
		else ex.getUser(player).setSuffix("", null);
	}
	public void setupLogBlock() {
		logBlock = (LogBlock)this.getServer().getPluginManager().getPlugin("LogBlock");
		
		if (logBlock != null) {
			info("Successfully connected to LogBlock!");
		} else {
			warning("Disconnected from LogBlock; towers will be denied until it is re-enabled.");
		}
	}
	public void notification(String message){
		getServer().broadcastMessage(ChatColor.LIGHT_PURPLE.toString() +"[Religion Notification] " + ChatColor.AQUA.toString() + message);
	}
	/**
	 * Logs an informative message to the console, prefaced with this plugin's header
	 * @param message: String
	 */
	public static void info(String message)
	{ 
		log.info(header + ChatColor.WHITE + message);
	}

	/**
	 * Logs a severe error message to the console, prefaced with this plugin's header
	 * Used to log severe problems that have prevented normal execution of the plugin
	 * @param message: String
	 */
	public static void severe(String message)
	{
		log.severe(header + ChatColor.RED + message);
	}

	/**
	 * Logs a warning message to the console, prefaced with this plugin's header
	 * Used to log problems that could interfere with the plugin's ability to meet admin expectations
	 * @param message: String
	 */
	public static void warning(String message)
	{
		log.warning(header + ChatColor.YELLOW + message);
	}

	/**
	 * Logs a message to the console, prefaced with this plugin's header
	 * @param level: Logging level under which to send the message
	 * @param message: String
	 */
	public static void log(java.util.logging.Level level, String message)
	{
		log.log(level, header + message);
	}
	public static void saveInfoFile() throws IOException{
		information.save(infoFile);
	}
}
