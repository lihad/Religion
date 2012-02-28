package Lihad.Religion.Listeners;

import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.ServerListener;

import com.nijikokun.bukkit.Permissions.Permissions;

import de.diddiz.LogBlock.LogBlock;

import Lihad.Religion.Religion;


public class BeyondPluginListener extends ServerListener {
	public static Religion plugin;
    public BeyondPluginListener(Religion instance) {
        plugin = instance;
    }
    public void onPluginEnable(PluginEnableEvent event){
    	if(event.getPlugin().getDescription().getName().equals("Permissions"))
    		plugin.setupPermissions();
    	else if(event.getPlugin().getDescription().getName().equals("LogBlock"))
    		plugin.setupLogBlock();
    }
    
    public void onPluginDisable(PluginDisableEvent event){
    	if(event.getPlugin().getDescription().getName().equals("Permissions"))
    		plugin.setupPermissions();
    	else if(event.getPlugin().getDescription().getName().equals("LogBlock"))
    		plugin.setupLogBlock();
    }
}
