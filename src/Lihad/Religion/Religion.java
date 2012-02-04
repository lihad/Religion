package Lihad.Religion;

import java.io.File;

import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

import Lihad.Religion.Command.CommandRunner;
import Lihad.Religion.Config.BeyondConfig;
import Lihad.Religion.Config.BeyondConfigReader;
import Lihad.Religion.Config.BeyondConfigWriter;
import Lihad.Religion.Information.BeyondInfo;
import Lihad.Religion.Information.BeyondInfoReader;
import Lihad.Religion.Information.BeyondInfoWriter;
import Lihad.Religion.Listeners.BeyondBlockListener;
import Lihad.Religion.Listeners.BeyondEntityListener;
import Lihad.Religion.Listeners.BeyondPlayerListener;
import Lihad.Religion.Listeners.BeyondPluginListener;
import Lihad.Religion.Util.BeyondTimerTask;
import Lihad.Religion.Util.UpdateTimer;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

@SuppressWarnings("deprecation")
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
	 * TODO: I would like to see more general 'player.sendMessage(".."); things.  Just as event flares
	 * Use Util.towerBroadcast(name, msg), Util.religionBroadcast(name, msg)
	 * TODO: Need to add a class/package/something for ability handling.  <- Really the next big step.
	 * 
	 * 
	 * TODO: Make it so people can't make a /home or sleep in a bed within the aoe of an opposing tower
	 * TODO: '/rr who' doesn't work for some reason
	 * 
	 * 
	 */
	
	
	
    public static Configuration configuration;
    public static Configuration information;
	public static PermissionHandler handler;
	public static CommandExecutor cmd;
    public static UpdateTimer timer;
    public static BeyondTimerTask task;

	
	public static BeyondConfigWriter configwrite;
	public static BeyondConfigReader configread;
	public static BeyondConfig config;
	
	public static BeyondInfoWriter infowrite;
	public static BeyondInfoReader inforead;
	public static BeyondInfo info;
	
	private final BeyondPluginListener pluginListener = new BeyondPluginListener(this);
	private final BeyondBlockListener blockListener = new BeyondBlockListener(this);
	private final BeyondPlayerListener playerListener = new BeyondPlayerListener(this);
	private final BeyondEntityListener entityListener = new BeyondEntityListener(this);


	
	public static File infoFile = new File("plugins/Religion/information.yml");


	@Override
	public void onDisable() {
		UpdateTimer.timer.cancel();
		configuration.save();
		information.save();
	}

	@Override
	public void onEnable() {
		System.out.println("-----------------------------------------");
		
		//ConfigManager
		configuration = getConfiguration();
		configuration.load();
		
		//InfoManager
		information = new Configuration(infoFile);
		information.load();
		
		//ClassManager
		configwrite = new BeyondConfigWriter(this);
		configread = new BeyondConfigReader(this);
		config = new BeyondConfig(this);
		infowrite = new BeyondInfoWriter(this);
		inforead = new BeyondInfoReader(this);
		info = new BeyondInfo(this);

		//PermsManager
		setupPermissions();
		
		//PluginManager
		PluginManager pm = getServer().getPluginManager();
        pm.registerEvent(Event.Type.SIGN_CHANGE, this.blockListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.BLOCK_BREAK, this.blockListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.BLOCK_DAMAGE, this.blockListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.PLUGIN_ENABLE, this.pluginListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_MOVE, this.playerListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.PLAYER_INTERACT, this.playerListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.ENTITY_DAMAGE, this.entityListener, Priority.Normal, this);
        pm.registerEvent(Event.Type.ENTITY_EXPLODE, this.entityListener, Priority.Normal, this);



		//TimerManager
		task = new BeyondTimerTask();
		timer = new UpdateTimer(this);
        
		//CommandManager
		cmd = new CommandRunner(this);
		getCommand("rr").setExecutor(cmd);
 
		
		
		System.out.println("[Religion] Has launched successfully.");
		System.out.println("-----------------------------------------");
	}
	
	public void setupPermissions() {
		Plugin permissionsPlugin = this.getServer().getPluginManager().getPlugin("Permissions");
		
		if (permissionsPlugin != null) {
			handler = ((Permissions) permissionsPlugin).getHandler();

		} else {
			System.out.println("[Religion] Permissions has failed to connect");
		}
	}

}
